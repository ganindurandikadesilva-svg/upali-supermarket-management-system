package lk.upalisupermarket.controller;


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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import lk.upalisupermarket.dao.GrnDao;
import lk.upalisupermarket.dao.GrnStatusDao;
import lk.upalisupermarket.dao.SupplierPaymentDao;
import lk.upalisupermarket.dao.UserDao;
import lk.upalisupermarket.entity.Grn;
import lk.upalisupermarket.entity.GrnHasItem;
import lk.upalisupermarket.entity.Privilege;
import lk.upalisupermarket.entity.SupplierPayment;
import lk.upalisupermarket.entity.SupplierPaymentHasGrn;
import lk.upalisupermarket.entity.User;

@RestController // api implement krn mapping siyalla servlet container ekt add wenne me
// annotation eken
public class SupplierPaymentController {

  @Autowired // create instance for grnSttaus dao
  private  GrnStatusDao grnStatusDao;

  @Autowired // create instance for supplierpayment dao
  private SupplierPaymentDao supplierPaymentDao;

  @Autowired // create instance for supplierpayment dao
  private GrnDao grnDao;



  @Autowired
  private UserPrivilegeController userPrivilegeController;// create an instance of UserPrivilegeContoller to access
                                                          // the data for secuirity
  // privileged user kenek da kiyl blnna


  @Autowired // generate instance
  private UserDao userDao;


    SupplierPaymentController(GrnStatusDao grnStatusDao) {
        this.grnStatusDao = grnStatusDao;
    }



  // mapping to return supplierpayment ui(URL-->"/supplierpayment")
  @RequestMapping(value = "/supplierpayment")
  public ModelAndView loadSupplierPaymentPage() {

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();// get authentication object
    User logUser=userDao.getByUserName(auth.getName());
    ModelAndView supplierPaymentPage = new ModelAndView();

    supplierPaymentPage.setViewName("supplierpayment.html");
    supplierPaymentPage.addObject("title", "Upali Supermarket-Supplier Payment Management");// set the title for page
    supplierPaymentPage.addObject("loggedUserName", auth.getName());// get username of the logged user

    supplierPaymentPage.addObject("loggedUserPhoto", logUser.getUser_photo());//get userphoto of the logged user
    return supplierPaymentPage;
  }


  // mapping to return all supplier payment data(URL-->"supplierpayment/findall")
  @GetMapping(value = "/supplierpayment/findall", produces = "application/json")
  public List<SupplierPayment> findAllData() {

    // Check user authentication and authorization

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    Privilege usrPrivilege = userPrivilegeController.getPrivilegeByUserModule(auth.getName(),
        "Supplier_Payment");// Module name should be checked with the database Module table

    if (usrPrivilege.getSelect_privilege()) {
      // returning the values in the Supplier_Payment table
      return supplierPaymentDao.findAll(Sort.by(Direction.DESC, "id"));// Supplier_Payment dao query for selected only the
                                                                     // nneded db fileds

    } else {
      return new ArrayList<>();// returns an empty array since the user has no privilege to see the data
    }

  }


  //maping to  insert supplier payment  data 
 @PostMapping(value = "/supplierpayment/insert") // supplierPayment is the frontend object eke parameter(no primary key since its nowgetting inserted)
  public String savePurchaseOrderData(@RequestBody SupplierPayment supplierPayment) { // Type should be SupplierPayment and object sent from frontend is supplierPayment

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();// get authentication object
    User loggedUser = userDao.getByUserName(auth.getName());// logged user object

    Privilege usrPrivilege = userPrivilegeController.getPrivilegeByUserModule(auth.getName(), "Supplier_Payment");//check privilege

    // check authorization
    // Check whether the user has permission to insert

    if (usrPrivilege.getInsert_privilege()) {
      // check duplication

      //frontend eken insert krn cheque no eke datbase eke tiynwd blnwa quey ekkin-->supplierPayment.getCheque_no() -->meken fronrend eken ewn cheque no ek gnn puluwn
      SupplierPayment existChequeNo=supplierPaymentDao.getByChequeNo(supplierPayment.getCheque_no());
      if (existChequeNo!=null) {
        return "Save Unsuccessfull !\n Entered Cheque No : " + supplierPayment.getCheque_no() + " Already Exists..!";
      }

      try {
        // set auto added data
        supplierPayment.setAdded_datetime(LocalDateTime.now());// set value for addeddate time
        supplierPayment.setAdded_user_id(loggedUser.getId());// database eke load wela tiyn nama withri penanne
        supplierPayment.setBill_no(supplierPaymentDao.generateNextSupplierPayBillNo());;// auto generate next bill no

     
      //api association eke tiyn main side eka block krnw(supplierpaymenthasgrn.java eke paymentid foreign key)
      //main side ek block krm ey natuw submit krnn ba. ek nawath api add krnn one nis list ekaka tiyn eka loop eken read krl,  one by one set krnw payment id eka
        for (SupplierPaymentHasGrn suppayhasgrn : supplierPayment.getSupplierPaymentHasGrnList()) {//This iterates through the list of SupplierPaymentHasGrn objects (which came from the inner form of the UI).
          suppayhasgrn.setSupplier_payments_id(supplierPayment);//This sets the supplier_payments_id (a foreign key) of each GRN payment record to the parent supplierPayment object.
        //eka nttm hibernate ta kiynn ba eka kage under da kiyl
       
        }
  
        //save operator
        supplierPaymentDao.save(supplierPayment);
        // dependencies
        //Grn grn=grnDao.getReferenceById();


      //supplier payment data save unata passe tama dependency handle krnne
       for (SupplierPaymentHasGrn suppayhasgrn : supplierPayment.getSupplierPaymentHasGrnList()) {//This iterates through the list of SupplierPaymentHasGrn objects (which came from the inner form of the UI).
       
          Grn grn=grnDao.getReferenceById(suppayhasgrn.getGrn_id().getId());//supplier payment ta adla grn id eka gnnwa
          grn.setPaid_amount(supplierPayment.getPaid_amount().add(grn.getPaid_amount()));//grn table eke dn tiyna paid amount ekt suppay krn amount ek add krnwa
          if (grn.getNet_amount().compareTo(grn.getPaid_amount()) == 0) {//set grn status to completed if net maoun=paid amount
            grn.setGrn_status_id(grnStatusDao.getReferenceById(2));//@ means Complted status
          }
           //innerform ekk nisa loop ekk dala save krnne
          for (GrnHasItem grhi : grn.getGrnHasItemList()) {
            grhi.setGrn_id(grn);
          }
         
          grnDao.save(grn);//save 
        
        }

        return "OK";// frontend recieving ok

      } catch (Exception e) {

        return "Save Unsucessfull...!" + e.getMessage();

      }
    } else {
      return "Save UnsucessFull..!\nYou Have No Permission to Insert";
    }

  }


   // update
    @PutMapping(value = "/supplierpayment/update")
    public String updateSupplierPayment(@RequestBody SupplierPayment supplierPayment) {

        // check authentication and authorization
        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        Privilege usrPrivilege=userPrivilegeController.getPrivilegeByUserModule(auth.getName(), 
        "Supplier_Payment");

        if (usrPrivilege.getUpdate_privilege()) {
            // Check exist
        if (supplierPayment.getId() == null) {
            return "Update Unsuccessfull ! Supplier Payment Doesn't Exist...";// check there is an id of the send
        }

        SupplierPayment extById = supplierPaymentDao.getReferenceById(supplierPayment.getId());// check the record is in the db or not
        if (extById == null) {
            return "Update Unsuccessfull ! Supplier Payment Doesn't Exist...";
        }

        // check duplication
        /*   frontenmd eken ewan username ekt adala username ekk backend table eke tiynwd blnwa query ekkin 
        eka tiynwnm e tiyna eke id ekai dn update krl ewn eke id amanananm awul equal nm awulk naa mokd same row ek ethok
        differet row unot awul */
        

        //checkno
        //user ewn cheque no ek db ek tiynwd blnw & e no ekt adla id eka frontend eken darta ewn kenage id ekat asamananm awul mokd ethokt dennek ne same cheque no tiyna
        SupplierPayment existChequeNo=supplierPaymentDao.getByChequeNo(supplierPayment.getCheque_no());
        if (existChequeNo!=null && existChequeNo.getId()!=supplierPayment.getId()) {
           return "Update Unsuccessfull !\n Entered Cheque No : " + supplierPayment.getCheque_no() + " Already Exists..!";
        }

        //transferid

        try {
        
         //api association eke tiyn main side eka block krnw(purchaseorderhasitem.java eke purchaseorderid foreign key)
         //main side ek block krm ey natuw submit krnn ba. ek nawath api add krnn one nis list ekaka tiyn eka loop eken read krl,  one by one set krnw pod id eka
        for (SupplierPaymentHasGrn suppayhasgrn : supplierPayment.getSupplierPaymentHasGrnList()) {//This iterates through the list of SupplierPaymentHasGrn objects (which came from the inner form of the UI).
          suppayhasgrn.setSupplier_payments_id(supplierPayment);//This sets the supplier_payments_id (a foreign key) of each GRN payment record to the parent supplierPayment object.
        //eka nttm hibernate ta kiynn ba eka kage under da kiyl
        }
  
            // save operator
            supplierPaymentDao.save(supplierPayment);
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
    @DeleteMapping(value = "supplierpayment/delete")
    public String deleteSupplierPayment(@RequestBody SupplierPayment supplierPayment) {// supplierPayment is the frontend object(has a primary key)
        // check authorization

        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        Privilege usrPrivilege=userPrivilegeController.getPrivilegeByUserModule(auth.getName(), 
        "Supplier_Payment");

        if (usrPrivilege.getDelete_privilege()) {
            
        // Check exist
        if (supplierPayment.getId() == null) {
            return "Delete Unsuccessfull ! Supplier Payment Doesn't Exist...";// check there is an id of the send(id ekk nytuw awoth)
        }

        SupplierPayment extsupplierpaymentById = supplierPaymentDao.getReferenceById(supplierPayment.getId());// check the record is in the db or not
        if (extsupplierpaymentById == null) {
            return "Delete Unsuccessfull ! Supplier Payment Doesn't Exist...";
        }

        try {
            // set auto added data
            extsupplierpaymentById.setBill_no(supplierPaymentDao.generateNextSupplierPayBillNo());// auto generate next purchase order no

            // update operator changing the status
            //     extpurchaseorderById.setPurchase_order_status_id(purchaseOrderStatusDao.getReferenceById(5));// id=5 is Deleted state in Item Status Table
  
         //api association eke tiyn main side eka block krnw(purchaseorderhasitem.java eke purchaseorderid foreign key)
         //main side ek block krm ey natuw submit krnn ba. ek nawath api add krnn one nis list ekaka tiyn eka loop eken read krl,  one by one set krnw pod id eka
        for (SupplierPaymentHasGrn suppayhasgrn : supplierPayment.getSupplierPaymentHasGrnList()) {//This iterates through the list of SupplierPaymentHasGrn objects (which came from the inner form of the UI).
          suppayhasgrn.setSupplier_payments_id(supplierPayment);//This sets the supplier_payments_id (a foreign key) of each GRN payment record to the parent supplierPayment object.
        //eka nttm hibernate ta kiynn ba eka kage under da kiyl
        }

            supplierPaymentDao.save(extsupplierpaymentById);
            //front end eken ewn eka hora object ekk unoth eken enne chnage  wechcha dat wenn puluwn e nisa tamai extid arn save ekrnne

            // supplierPaymentDao.delete(supplierPayment); to delete record from the database

            // dependencies

            return "OK";// frontend recieving ok

        } catch (Exception e) {

            return "Delete Unsucessfull...!" + e.getMessage();

        }
        } else {
            return "Delete Unsucessfull...!\nYou Have No Permission to Delete" ;
        }

    }




}
