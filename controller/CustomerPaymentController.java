package lk.upalisupermarket.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import lk.upalisupermarket.dao.CustomerDao;
import lk.upalisupermarket.dao.CustomerPaymentDao;
import lk.upalisupermarket.dao.InventoryDao;
import lk.upalisupermarket.dao.InvoiceDao;
import lk.upalisupermarket.dao.LoyaltyPointsDao;
import lk.upalisupermarket.dao.UserDao;
import lk.upalisupermarket.entity.Customer;
import lk.upalisupermarket.entity.CustomerPayment;
import lk.upalisupermarket.entity.Inventory;
import lk.upalisupermarket.entity.Invoice;
import lk.upalisupermarket.entity.InvoiceHasInventory;
import lk.upalisupermarket.entity.LoyaltyPoints;
import lk.upalisupermarket.entity.Privilege;
import lk.upalisupermarket.entity.User;

@RestController
public class CustomerPaymentController {

    @Autowired
    private UserDao userDao;

    @Autowired // generate instance
    private InventoryDao inventoryDao;

    @Autowired // create instance for invoice dao
    private InvoiceDao invoiceDao;

    @Autowired // create instance for customer dao
    private CustomerDao customerDao;

    @Autowired // create instance for purchaseorder dao
    private LoyaltyPointsDao loyaltyPointsDao;


    @Autowired
    private CustomerPaymentDao customerPaymentDao;

    @Autowired
    private UserPrivilegeController userPrivilegeController;// create an instance of UserPrivilegeContoller to access
                                                            // the data for secuirity
    // privileged user kenek da kiyl blnna

    // mapping to return customerpayment ui(URL-->"/customerpayment")
    @RequestMapping(value = "/customerpayment")
    public ModelAndView UI() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();// get authentication object
        User logUser = userDao.getByUserName(auth.getName());
        ModelAndView customerPaymentPage = new ModelAndView();

        customerPaymentPage.setViewName("customerpayment.html");
        customerPaymentPage.addObject("title", "Upali Supermarket-Cutomer Payment Management");// set the title for page
        customerPaymentPage.addObject("loggedUserName", auth.getName());// get username of the logged user

        customerPaymentPage.addObject("loggedUserPhoto", logUser.getUser_photo());// get userphoto of the logged user
        return customerPaymentPage;
    }

    @PostMapping(value = "/customerpayment/insert") // customerpayment is the frontend object(no primary key since its
                                                    // now
    // getting inserted)
    public String saveCustomerPaymentData(@RequestBody CustomerPayment customerpayment) { // Type should be
                                                                                          // customerpayment and object
                                                                                          // sent from frontend is
                                                                                          // customerpayment

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();// get authentication object
        User loggedUser = userDao.getByUserName(auth.getName());// logged user object

        Privilege usrPrivilege = userPrivilegeController.getPrivilegeByUserModule(auth.getName(), "Customer_Payment");// check
                                                                                                                      // privilege

        // check authorization
        // Check whether the user has permission to insert

        if (usrPrivilege.getInsert_privilege()) {
            // check duplication

            try {
                // set auto added data

                // save operator

                Invoice invoice = customerpayment.getInvoice_id();
                // set auto added data
                invoice.setAdded_datetime(LocalDateTime.now());// set value for addeddate time
                invoice.setAdded_user_id(loggedUser.getId());// database eke load wela tiyn nama withri penanne
                invoice.setInvoice_no(invoiceDao.generateNextInvoiceNo());// auto generate next invoice no

                // save operator
                // api association eke tiyn main side eka block krnw(purchaseorderhasitem.java
                // eke purchaseorderid foreign key)
                // main side ek block krm ey natuw submit krnn ba. ek nawath api add krnn one
                // nis list ekaka tiyn eka loop eken read krl, one by one set krnw pod id eka
                for (InvoiceHasInventory invhasinventory : invoice.getInvoiceHasInventoryList()) {
                    invhasinventory.setInvoice_id(invoice);
                }

                Invoice newiInvoice = invoiceDao.save(invoice);
                // dependencies
                // reducing the inventory
                for (InvoiceHasInventory invoiceHasInventory : invoice.getInvoiceHasInventoryList()) {
                    Inventory inventory = inventoryDao
                            .getReferenceById(invoiceHasInventory.getItem_inventory_id().getId());
                    inventory.setAvailable_qty(inventory.getAvailable_qty().subtract(invoiceHasInventory.getQty()));
                    inventoryDao.save(inventory);// save
                }

                customerpayment.setBillno(customerPaymentDao.generateNextBillNo());// auto generate next invoice no
                customerpayment.setAdded_datetime(LocalDateTime.now());
                customerpayment.setTotal_amount(newiInvoice.getNet_amount());
                customerpayment.setInvoice_id(newiInvoice);
                customerPaymentDao.save(customerpayment);

                //change loyalty values based on invoice amount
                // 1. Get customer
                Customer customer = invoice.getCustomer_id();

                if(customer!=null){
                    // 2. Get current loyalty points (null-safe)
                BigDecimal currentPoints = customer.getLoyalty_point() != null ? customer.getLoyalty_point() : BigDecimal.ZERO;

                // 3. Query the loyalty table
                LoyaltyPoints loyaltyPoints = loyaltyPointsDao.byLoyaltyPoints(currentPoints);

                // 4. If eligible, update points
                if (loyaltyPoints !=null ) {
                    //empty nattm
              
                    BigDecimal increase = loyaltyPoints.getPoint_increase_rate();

                   
                    BigDecimal newPoints = currentPoints.add(customerpayment.getTotal_amount().multiply(increase));
                    customer.setLoyalty_point(newPoints);
                }
                

                customerDao.save(customer); // Save updated customer


                    /* compareTo(BigDecimal.ZERO) returns:

                    0 → equals

                    < 0 → less than

                    > 0 → greater than

                    You use > 0 to check if points are already there

                    If 0, you don't multiply, you assign the increase directly */

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
