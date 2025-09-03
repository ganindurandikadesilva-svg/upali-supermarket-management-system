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



import lk.upalisupermarket.dao.QuotationRequestDao;
import lk.upalisupermarket.dao.QuotationRequestStatusDao;
import lk.upalisupermarket.dao.UserDao;
import lk.upalisupermarket.entity.Privilege;
import lk.upalisupermarket.entity.QuotationRequest;
import lk.upalisupermarket.entity.QuotationRequestHasItem;
import lk.upalisupermarket.entity.User;

@RestController // api implement krn mapping siyalla servlet container ekt add wenne me
// annotation eken
public class QuotationRequestController {

  @Autowired // create instance for quotationrequest dao
  private QuotationRequestDao quotationRequestDao;

  @Autowired
  private UserPrivilegeController userPrivilegeController;// create an instance of UserPrivilegeContoller to access
                                                          // the data for secuirity
  // privileged user kenek da kiyl blnna

  @Autowired // generate instance
  private QuotationRequestStatusDao quotationRequestStatusDao;

  @Autowired // generate instance
  private UserDao userDao;

  // mapping to return quotationrequest ui(URL-->"/quotationrequest")
  @RequestMapping(value = "/quotationrequest")
  public ModelAndView loadQuotationRequestage() {

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();// get authentication object
    User logUser=userDao.getByUserName(auth.getName());
    ModelAndView quotationRequestPage = new ModelAndView();

    quotationRequestPage.setViewName("quotationrequest.html");
    quotationRequestPage.addObject("title", "Upali Supermarket-Quotation Request Management");// set the title for page
    quotationRequestPage.addObject("loggedUserName", auth.getName());// get username of the logged user
    
    quotationRequestPage.addObject("loggedUserPhoto", logUser.getUser_photo());//get userphoto of the logged user
    return quotationRequestPage;
  }

  // mapping to return all purchase order data(URL-->"quotationrequest/findall")
  @GetMapping(value = "/quotationrequest/findall", produces = "application/json")
  public List<QuotationRequest> findAllData() {

    // Check user authentication and authorization

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    Privilege usrPrivilege = userPrivilegeController.getPrivilegeByUserModule(auth.getName(),
        "Quotation_Request");// Module name should be checked with the database Module table

    if (usrPrivilege.getSelect_privilege()) {
      // returning the values in the purchase_order table
      return quotationRequestDao.findAll(Sort.by(Direction.DESC, "id"));// purchase_order dao query for selected only the
                                                                     // nneded db fileds

    } else {
      return new ArrayList<>();// returns an empty array since the user has no privilege to see the data
    }

  }



  //insert
  @PostMapping(value = "/quotationrequest/insert") // quotationRequest is the frontend object(no primary key since its now getting inserted
  public String saveQuotationRequestData(@RequestBody QuotationRequest quotationRequest) { // Type should be QuotationRequest and object sent from
                                                                // frontend is quotationRequest

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();// get authentication object
    User loggedUser = userDao.getByUserName(auth.getName());// logged user object

    Privilege usrPrivilege = userPrivilegeController.getPrivilegeByUserModule(auth.getName(), "Quotation_Request");// get quotationRequest object

    // check authorization
    // Check whether the user has permission to insert

    if (usrPrivilege.getInsert_privilege()) {
      // check duplication

      try {
        // set auto added data
        quotationRequest.setAdded_datetime(LocalDateTime.now());// set value for addeddate time
        quotationRequest.setAdded_user_id(loggedUser.getId());// database eke load wela tiyn nama withri penanne
        quotationRequest.setQuotation_request_no(quotationRequestDao.generateNextQuotationRequestNo());// auto generate next quotationRequest no

      // save operator
      //api association eke tiyn main side eka block krnw(QuotationRequestHasItem.java eke quotationRequestid foreign key)
      //main side ek block krm ey natuw submit krnn ba. ek nawath api add krnn one nis list ekaka tiyn eka loop eken read krl,  one by one set krnw pod id eka
        for (QuotationRequestHasItem quotationRequestHasItem : quotationRequest.getQuotationRequestHasItemList()) {
          quotationRequestHasItem.setQuotation_request_id(quotationRequest);
        }
  
        quotationRequestDao.save(quotationRequest);
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
    @PutMapping(value = "/quotationrequest/update")
    public String updateQuotationRequest(@RequestBody QuotationRequest quotationRequest) {

        // check authentication and authorization
        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        Privilege usrPrivilege=userPrivilegeController.getPrivilegeByUserModule(auth.getName(), 
        "Quotation_Request");

        if (usrPrivilege.getUpdate_privilege()) {
            // Check exist
        if (quotationRequest.getId() == null) {
            return "Update Unsuccessfull ! Quotation Request Doesn't Exist...";// check there is an id of the send
        }

        QuotationRequest extById = quotationRequestDao.getReferenceById(quotationRequest.getId());// check the record is in the db or not
        if (extById == null) {
            return "Update Unsuccessfull ! Quotation Request Doesn't Exist...";
        }

        // check duplication
        /*   frontenmd eken ewan username ekt adala username ekk backend table eke tiynwd blnwa query ekkin 
        eka tiynwnm e tiyna eke id ekai dn update krl ewn eke id amanananm awul equal nm awulk naa mokd same row ek ethok
        differet row unot awul */

        // **Get logged-in user ID**
        User loggedUser =userDao.getByUserName(auth.getName()); // Retrieve user by username

        try {
            // set auto added data
            quotationRequest.setUpdated_datetime(LocalDateTime.now());// set value for addeddate time
            quotationRequest.setUpdated_user_id(loggedUser.getId());

              //api association eke tiyn main side eka block krnw(QuotationRequestHasItem.java eke quotationRequestid foreign key)
              //main side ek block krm ey natuw submit krnn ba. ek nawath api add krnn one nis list ekaka tiyn eka loop eken read krl,  one by one set krnw quotationRequest id eka
            for (QuotationRequestHasItem quotationRequestHasItem : quotationRequest.getQuotationRequestHasItemList()) {
              quotationRequestHasItem.setQuotation_request_id(quotationRequest);
            }
            // save operator
            quotationRequestDao.save(quotationRequest);
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
    @DeleteMapping(value = "/quotationrequest/delete")
    public String deleteQuotationRequestData(@RequestBody QuotationRequest quotationRequest) {// quotationRequest is the frontend object(has a primary key)
        // check authorization

        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        Privilege usrPrivilege=userPrivilegeController.getPrivilegeByUserModule(auth.getName(), 
        "Quotation_Request");

        if (usrPrivilege.getDelete_privilege()) {
            
        // Check exist
        if (quotationRequest.getId() == null) {
            return "Delete Unsuccessfull ! Quotation Request Doesn't Exist...";// check there is an id of the send(id ekk nytuw awoth)
        }

        QuotationRequest extquotationrequestById = quotationRequestDao.getReferenceById(quotationRequest.getId());// check the record is in the db or
                                                                                  // not
        if (extquotationrequestById == null) {
            return "Delete Unsuccessfull ! Quotation Request Doesn't Exist...";
        }

        try {
            // set auto added data
            extquotationrequestById.setDeleted_datetime(LocalDateTime.now());// set value for addeddate time
            extquotationrequestById.setDeleted_user_id(userDao.getByUserName(auth.getName()).getId());
            extquotationrequestById.setQuotation_request_no(quotationRequestDao.generateNextQuotationRequestNo());// auto generate next purchase order no

            // update operator
            extquotationrequestById.setQuotation_request_status_id(quotationRequestStatusDao.getReferenceById(4));// id=4 is Deleted state in Item Status Table
           
           
            //api association eke tiyn main side eka block krnw(QuotationRequestHasItem.java eke quotationRequestid foreign key)
          //main side ek block krm ey natuw submit krnn ba. ek nawath api add krnn one nis list ekaka tiyn eka loop eken read krl,  one by one set krnw qorequest id eka
            for (QuotationRequestHasItem quotationRequestHasItem : quotationRequest.getQuotationRequestHasItemList()) 
            {
            quotationRequestHasItem.setQuotation_request_id(quotationRequest);
            }
            quotationRequestDao.save(extquotationrequestById);
            //front end eken ewn eka hora object ekk unoth eken enne chnage  wechcha dat wenn puluwn e nisa tamai extid arn save ekrnne

            // quotationRequestDao.delete(quotationRequest); to delete record from the database

            // dependencies

            return "OK";// frontend recieving ok

        } catch (Exception e) {

            return "Delete Unsucessfull...!" + e.getMessage();

        }
        } else {
            return "Delete Unsucessfull...!\nYou Have No Permission to Delete" ;
        }

    }

    
        // mapping to return quotation requests list based on the selected supplier(URL-->"item/qrlistbysupplier?supplierid") to quotation request form
     @GetMapping(value = "/qr/qrlistbysupplier/{supplierid}", produces = "application/json")
     public List<QuotationRequest> getQrListBYSupplierId(@PathVariable("supplierid") Integer supplierid) {
 
         // Check user authentication and authorization
 
         Authentication auth = SecurityContextHolder.getContext().getAuthentication();
         Privilege usrPrivilege = userPrivilegeController.getPrivilegeByUserModule(auth.getName(),
                 "Quotation_Request");// Module name should be checked with the database module table
 
         if (usrPrivilege.getSelect_privilege()) {
             // returning the values in the item table
             return quotationRequestDao.getQrListBySupplier(supplierid);
         } else {
             return new ArrayList<>();// returns an empty array since the user has no privilege to see the data
         }
 
     }
   


     //mapping to return quantity based on quotationrequest id and itemid
    @GetMapping(value = "/qr/listbyqtyandqr/{qrid}/{itemid}", produces = "application/json")
     public BigDecimal getQtyBYQrItem(@PathVariable("qrid") Integer qrid,@PathVariable("itemid") Integer itemid) {
 
         // Check user authentication and authorization
 
         Authentication auth = SecurityContextHolder.getContext().getAuthentication();
         Privilege usrPrivilege = userPrivilegeController.getPrivilegeByUserModule(auth.getName(),
                 "Quotation_Request");// Module name should be checked with the database module table
 
         if (usrPrivilege.getSelect_privilege()) {
             // returning the values in the item table
             return quotationRequestDao.getQtybyQrandItemId(qrid,itemid);
         } else {
             return BigDecimal.ZERO;// returns an empty array since the user has no privilege to see the data
         }
 
     }
   

}
