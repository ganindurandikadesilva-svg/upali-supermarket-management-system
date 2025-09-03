package lk.upalisupermarket.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


import lk.upalisupermarket.dao.PurchaseOrderDao;
import lk.upalisupermarket.dao.PurchaseOrderStatusDao;
import lk.upalisupermarket.dao.UserDao;
import lk.upalisupermarket.entity.Privilege;
import lk.upalisupermarket.entity.PurchaseOrder;
import lk.upalisupermarket.entity.PurchaseOrderHasItem;
import lk.upalisupermarket.entity.User;

@RestController // api implement krn mapping siyalla servlet container ekt add wenne me
// annotation eken
public class PurchaseOrderController {

  @Autowired // create instance for purchaseorder dao
  private PurchaseOrderDao purchaseOrderDao;


  @Autowired
  private UserPrivilegeController userPrivilegeController;// create an instance of UserPrivilegeContoller to access
                                                          // the data for secuirity
  // privileged user kenek da kiyl blnna

  @Autowired // generate instance
  private PurchaseOrderStatusDao purchaseOrderStatusDao;

  @Autowired // generate instance
  private UserDao userDao;

  // mapping to return purchaseorder ui(URL-->"/purchaseorder")
  @RequestMapping(value = "/purchaseorder")
  public ModelAndView loadPurchaseOrderPage() {

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();// get authentication object
    User logUser=userDao.getByUserName(auth.getName());
    ModelAndView purchaseOrderPage = new ModelAndView();

    purchaseOrderPage.setViewName("purchaseorder.html");
    purchaseOrderPage.addObject("title", "Upali Supermarket-Purchase Order Management");// set the title for page
    purchaseOrderPage.addObject("loggedUserName", auth.getName());// get username of the logged user

    purchaseOrderPage.addObject("loggedUserPhoto", logUser.getUser_photo());//get userphoto of the logged user
    return purchaseOrderPage;
  }

  // mapping to return all purchase order data(URL-->"purchaseorder/findall")
  @GetMapping(value = "/purchaseorder/findall", produces = "application/json")
  public List<PurchaseOrder> findAllData() {

    // Check user authentication and authorization

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    Privilege usrPrivilege = userPrivilegeController.getPrivilegeByUserModule(auth.getName(),
        "Purchase_order");// Module name should be checked with the database Module table

    if (usrPrivilege.getSelect_privilege()) {
      // returning the values in the purchase_order table
      return purchaseOrderDao.findAll(Sort.by(Direction.DESC, "id"));// purchase_order dao query for selected only the
                                                                     // nneded db fileds

    } else {
      return new ArrayList<>();// returns an empty array since the user has no privilege to see the data
    }

  }



  @PostMapping(value = "/purchaseorder/insert") // purchaseorder is the frontend object(no primary key since its now
   // getting inserted)
  public String savePurchaseOrderData(@RequestBody PurchaseOrder purchaseOrder) { // Type should be purchaseOrder and object sent from
                                                                // frontend is purchaseOrder

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();// get authentication object
    User loggedUser = userDao.getByUserName(auth.getName());// logged user object

    Privilege usrPrivilege = userPrivilegeController.getPrivilegeByUserModule(auth.getName(), "Purchase_order");//check privilege

    // check authorization
    // Check whether the user has permission to insert

    if (usrPrivilege.getInsert_privilege()) {
      // check duplication

      try {
        // set auto added data
        purchaseOrder.setAdded_datetime(LocalDateTime.now());// set value for addeddate time
        purchaseOrder.setAdded_user_id(loggedUser.getId());// database eke load wela tiyn nama withri penanne
        purchaseOrder.setPod_no(purchaseOrderDao.generateNextPurchaseOrderNo());// auto generate next purchaseorder no

      // save operator
      //api association eke tiyn main side eka block krnw(purchaseorderhasitem.java eke purchaseorderid foreign key)
      //main side ek block krm ey natuw submit krnn ba. ek nawath api add krnn one nis list ekaka tiyn eka loop eken read krl,  one by one set krnw pod id eka
        for (PurchaseOrderHasItem podhasitem : purchaseOrder.getPurchaseOrderHasItemList()) {
          podhasitem.setPurchase_order_id(purchaseOrder);
        }
  
        purchaseOrderDao.save(purchaseOrder);
        // dependencies
     

        return "OK";// frontend recieving ok

      } catch (Exception e) {

        return "Save Unsucessfull...!" + e.getMessage();

      }
    } else {
      return "Save UnsucessFull..!\nYou Have No Permission to Insert";
    }

  }


   // update
    @PutMapping(value = "/purchaseorder/update")
    public String updatePurchaseOrder(@RequestBody PurchaseOrder purchaseOrder) {

        // check authentication and authorization
        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        Privilege usrPrivilege=userPrivilegeController.getPrivilegeByUserModule(auth.getName(), 
        "Purchase_order");

        if (usrPrivilege.getUpdate_privilege()) {
            // Check exist
        if (purchaseOrder.getId() == null) {
            return "Update Unsuccessfull ! Purchase Order Doesn't Exist...";// check there is an id of the send
        }

        PurchaseOrder extById = purchaseOrderDao.getReferenceById(purchaseOrder.getId());// check the record is in the db or not
        if (extById == null) {
            return "Update Unsuccessfull ! Purchase Order Doesn't Exist...";
        }

        // check duplication
        /*   frontenmd eken ewan username ekt adala username ekk backend table eke tiynwd blnwa query ekkin 
        eka tiynwnm e tiyna eke id ekai dn update krl ewn eke id amanananm awul equal nm awulk naa mokd same row ek ethok
        differet row unot awul */

        // **Get logged-in user ID**
        User loggedUser =userDao.getByUserName(auth.getName()); // Retrieve user by username

        try {
            // set auto added data
            purchaseOrder.setUpdated_datetime(LocalDateTime.now());// set value for addeddate time
            purchaseOrder.setUpdated_user_id(loggedUser.getId());

         //api association eke tiyn main side eka block krnw(purchaseorderhasitem.java eke purchaseorderid foreign key)
         //main side ek block krm ey natuw submit krnn ba. ek nawath api add krnn one nis list ekaka tiyn eka loop eken read krl,  one by one set krnw pod id eka
        for (PurchaseOrderHasItem podhasitem : purchaseOrder.getPurchaseOrderHasItemList()) {
          podhasitem.setPurchase_order_id(purchaseOrder);
        }
  
            // save operator
            purchaseOrderDao.save(purchaseOrder);
            // dependencies

            return "OK";// frontend recieving ok

        } catch (Exception e) {

            return "Update Unsucessfull...!" + e.getMessage();

        }

        } else {
            return "Update Unsucessfull...!\nYou Have No Permission to Update" ;
        }

        
    }


      // delete
    @DeleteMapping(value = "purchaseorder/delete")
    public String deletePurchaseOrder(@RequestBody PurchaseOrder purchaseOrder) {// purchaseorder is the frontend object(has a primary key)
        // check authorization

        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        Privilege usrPrivilege=userPrivilegeController.getPrivilegeByUserModule(auth.getName(), 
        "Purchase_order");

        if (usrPrivilege.getDelete_privilege()) {
            
        // Check exist
        if (purchaseOrder.getId() == null) {
            return "Delete Unsuccessfull ! Purchase Order Doesn't Exist...";// check there is an id of the send(id ekk nytuw awoth)
        }

        PurchaseOrder extpurchaseorderById = purchaseOrderDao.getReferenceById(purchaseOrder.getId());// check the record is in the db or
                                                                                  // not
        if (extpurchaseorderById == null) {
            return "Delete Unsuccessfull ! Purchase Order Doesn't Exist...";
        }

        try {
            // set auto added data
            extpurchaseorderById.setDeleted_datetime(LocalDateTime.now());// set value for addeddate time
            extpurchaseorderById.setDeleted_user_id(userDao.getByUserName(auth.getName()).getId());
            extpurchaseorderById.setPod_no(purchaseOrderDao.generateNextPurchaseOrderNo());// auto generate next purchase order no

            // update operator
            extpurchaseorderById.setPurchase_order_status_id(purchaseOrderStatusDao.getReferenceById(5));// id=5 is Deleted state in purchase order Status Table

          
            //api association eke tiyn main side eka block krnw(purchaseorderhasitem.java eke purchaseorderid foreign key)
            //main side ek block krm ey natuw submit krnn ba. ek nawath api add krnn one nis list ekaka tiyn eka loop eken read krl,  one by one set krnw pod id eka
            for (PurchaseOrderHasItem podhasitem : purchaseOrder.getPurchaseOrderHasItemList()) {
              podhasitem.setPurchase_order_id(purchaseOrder);
            }


            purchaseOrderDao.save(extpurchaseorderById);
            //front end eken ewn eka hora object ekk unoth eken enne chnage  wechcha dat wenn puluwn e nisa tamai extid arn save ekrnne

            // purchaseOrderDao.delete(purchaseOrder); to delete record from the database

            // dependencies

            return "OK";// frontend recieving ok

        } catch (Exception e) {

            return "Delete Unsucessfull...!" + e.getMessage();

        }
        } else {
            return "Delete Unsucessfull...!\nYou Have No Permission to Delete" ;
        }

    }



         // mapping to return purchase order list based on the selected supplier(URL-->"pod/podlistbysupplier?supplierid") to quotation request form
     @GetMapping(value = "/pod/podlistbysupplier/{supplierid}", produces = "application/json")
     public List<PurchaseOrder> getPodListBYSupplierId(@PathVariable("supplierid") Integer supplierid) {
 
         // Check user authentication and authorization
 
         Authentication auth = SecurityContextHolder.getContext().getAuthentication();
         Privilege usrPrivilege = userPrivilegeController.getPrivilegeByUserModule(auth.getName(),
                 "Purchase_order");// Module name should be checked with the database module table
 
         if (usrPrivilege.getSelect_privilege()) {
             // returning the values in the item table
             return purchaseOrderDao.getPodListBySupplier(supplierid);
         } else {
             return new ArrayList<>();// returns an empty array since the user has no privilege to see the data
         }
 
     }
   
        //mapping to return quantity based on pod id and itemid
    @GetMapping(value = "/pod/qtybypodanditemid/{podid}/{itemid}", produces = "application/json")
     public BigDecimal getQtyBYQrItem(@PathVariable("podid") Integer podid,@PathVariable("itemid") Integer itemid) {
 
         // Check user authentication and authorization
 
         Authentication auth = SecurityContextHolder.getContext().getAuthentication();
         Privilege usrPrivilege = userPrivilegeController.getPrivilegeByUserModule(auth.getName(),
                 "Purchase_order");// Module name should be checked with the database module table
 
         if (usrPrivilege.getSelect_privilege()) {
             // returning the values in the item table
             return purchaseOrderDao.getQtybyPodandItemid(podid,itemid);
         } else {
             return BigDecimal.ZERO;// returns an empty array since the user has no privilege to see the data
         }
 
     }
   
            //mapping to return unit price based on pod id and itemid for grn form unit price
    @GetMapping(value = "/purchaseorder/unitpricebyitem/{podid}/{itemid}", produces = "application/json")
     public BigDecimal getUnitPBYPodItem(@PathVariable("podid") Integer podid,@PathVariable("itemid") Integer itemid) {
 
         // Check user authentication and authorization
 
         Authentication auth = SecurityContextHolder.getContext().getAuthentication();
         Privilege usrPrivilege = userPrivilegeController.getPrivilegeByUserModule(auth.getName(),
                 "Purchase_order");// Module name should be checked with the database module table
 
         if (usrPrivilege.getSelect_privilege()) {
             // returning the values in the item table
             return purchaseOrderDao.getUnitPbyPodandItemid(podid,itemid);
         } else {
             return BigDecimal.ZERO;// returns an empty array since the user has no privilege to see the data
         }
 
     }
   

}
