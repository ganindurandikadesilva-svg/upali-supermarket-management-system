package lk.upalisupermarket.controller;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import lk.upalisupermarket.dao.InventoryDao;
import lk.upalisupermarket.dao.InvoiceDao;

import lk.upalisupermarket.dao.UserDao;
import lk.upalisupermarket.entity.Inventory;
import lk.upalisupermarket.entity.Invoice;
import lk.upalisupermarket.entity.InvoiceHasInventory;
import lk.upalisupermarket.entity.Privilege;
import lk.upalisupermarket.entity.User;

@RestController // api implement krn mapping siyalla servlet container ekt add wenne me
// annotation eken
public class InvoiceController {

  @Autowired // create instance for purchaseorder dao
  private InvoiceDao invoiceDao;


  @Autowired
  private UserPrivilegeController userPrivilegeController;// create an instance of UserPrivilegeContoller to access
                                                          // the data for secuirity
  // privileged user kenek da kiyl blnna



  @Autowired // generate instance
  private UserDao userDao;

   @Autowired // generate instance
  private InventoryDao inventoryDao;

 

  // mapping to return invoice ui(URL-->"/invoice")
  @RequestMapping(value = "/invoice")
  public ModelAndView loadInvoicePage() {

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();// get authentication object
    User logUser=userDao.getByUserName(auth.getName());
    ModelAndView invoicePage = new ModelAndView();

    invoicePage.setViewName("invoice.html");
    invoicePage.addObject("title", "Upali Supermarket-Invoice Management");// set the title for page
    invoicePage.addObject("loggedUserName", auth.getName());// get username of the logged user

    invoicePage.addObject("loggedUserPhoto", logUser.getUser_photo());//get userphoto of the logged user
    return invoicePage;
  }

  // mapping to return all purchase order data(URL-->"invoice/findall")
  @GetMapping(value = "/invoice/findall", produces = "application/json")
  public List<Invoice> findAllData() {

    // Check user authentication and authorization

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    Privilege usrPrivilege = userPrivilegeController.getPrivilegeByUserModule(auth.getName(),
        "Invoice");// Module name should be checked with the database Module table

    if (usrPrivilege.getSelect_privilege()) {
      // returning the values in the purchase_order table
      return invoiceDao.findAll(Sort.by(Direction.DESC, "id"));// purchase_order dao query for selected only the
                                                                     // nneded db fileds

    } else {
      return new ArrayList<>();// returns an empty array since the user has no privilege to see the data
    }

  }



  @PostMapping(value = "/invoice/insert") // invoice is the frontend object(no primary key since its now
   // getting inserted)
  public String saveInvoiceData(@RequestBody Invoice invoice) { // Type should be invoice and object sent from
                                                                // frontend is invoice

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();// get authentication object
    User loggedUser = userDao.getByUserName(auth.getName());// logged user object

    Privilege usrPrivilege = userPrivilegeController.getPrivilegeByUserModule(auth.getName(), "Invoice");//check privilege

    // check authorization
    // Check whether the user has permission to insert

    if (usrPrivilege.getInsert_privilege()) {
      // check duplication

      try {
        // set auto added data
        invoice.setAdded_datetime(LocalDateTime.now());// set value for addeddate time
        invoice.setAdded_user_id(loggedUser.getId());// database eke load wela tiyn nama withri penanne
        invoice.setInvoice_no(invoiceDao.generateNextInvoiceNo());// auto generate next invoice no



      // save operator
      //api association eke tiyn main side eka block krnw(purchaseorderhasitem.java eke purchaseorderid foreign key)
      //main side ek block krm ey natuw submit krnn ba. ek nawath api add krnn one nis list ekaka tiyn eka loop eken read krl,  one by one set krnw pod id eka
        for (InvoiceHasInventory invhasinventory : invoice.getInvoiceHasInventoryList()) {
          invhasinventory.setInvoice_id(invoice);
        }
  
        invoiceDao.save(invoice);
        // dependencies
        //reducing the inventory 
        for (InvoiceHasInventory invoiceHasInventory : invoice.getInvoiceHasInventoryList()) {
          Inventory inventory=inventoryDao.getReferenceById(invoiceHasInventory.getItem_inventory_id().getId());
          inventory.setAvailable_qty(inventory.getAvailable_qty().subtract(invoiceHasInventory.getQty()));
          inventoryDao.save(inventory);//save 
        }

        return "OK";// frontend recieving ok

      } catch (Exception e) {

        return "Save Unsucessfull...!" + e.getMessage();

      }
    } else {
      return "Save UnsucessFull..!\nYou Have No Permission to Insert";
    }

  }


 

}
