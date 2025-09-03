package lk.upalisupermarket.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
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

import lk.upalisupermarket.dao.QuotationDao;
import lk.upalisupermarket.dao.QuotationRequestDao;
import lk.upalisupermarket.dao.QuotationRequestStatusDao;
import lk.upalisupermarket.dao.QuotationStatusDao;
import lk.upalisupermarket.dao.UserDao;
import lk.upalisupermarket.entity.Privilege;
import lk.upalisupermarket.entity.Quotation;
import lk.upalisupermarket.entity.QuotationHasItem;
import lk.upalisupermarket.entity.QuotationRequest;
import lk.upalisupermarket.entity.QuotationRequestHasItem;
import lk.upalisupermarket.entity.User;

@RestController
public class QuotationController {

  @Autowired
  private UserPrivilegeController userPrivilegeController;// create an instance of UserPrivilegeContoller to access
                                                          // the data for secuirity
  // privileged user kenek da kiyl blnna

  @Autowired
  private QuotationDao quotationDao;

  @Autowired
  private UserDao userDao;

  @Autowired
  private QuotationStatusDao quotationStatusDao;

  @Autowired
  private QuotationRequestDao quotationRequestDao;

  @Autowired
  private QuotationRequestStatusDao quotationRequestStatusDao;

  // mapping to return quotation ui(URL-->"/quotation")
  @RequestMapping(value = "/quotation")
  public ModelAndView loadSupplierPage() {

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();// get authentication object
    User logUser = userDao.getByUserName(auth.getName());
    ModelAndView quotationPage = new ModelAndView();

    quotationPage.setViewName("quotation.html");
    quotationPage.addObject("title", "Upali Supermarket-Quotation Management");// set the title for page
    quotationPage.addObject("loggedUserName", auth.getName());// get username of the logged user

    quotationPage.addObject("loggedUserPhoto", logUser.getUser_photo());// get userphoto of the logged user
    return quotationPage;
  }

  // mapping to return all quotation data(URL-->"quotation/findall")
  @GetMapping(value = "/quotation/findall", produces = "application/json")
  public List<Quotation> findAllData() {

    // Check user authentication and authorization

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    Privilege usrPrivilege = userPrivilegeController.getPrivilegeByUserModule(auth.getName(),
        "Quotation");// Module name should be checked with the database module table

    if (usrPrivilege.getSelect_privilege()) {
      // returning the values in the supplier table
      return quotationDao.findAll(Sort.by(Direction.DESC, "id"));// sort decending from quotationid
    } else {
      return new ArrayList<>();// returns an empty array since the user has no privilege to see the data
    }

  }

  // insert
  @PostMapping(value = "/quotation/insert") // quotation is the frontend object(no primary key since its now
  // getting inserted)
  public String saveQuotationData(@RequestBody Quotation quotation) { // Type should be Quotation and object sent from
    // frontend is quotation

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();// get authentication object
    User loggedUser = userDao.getByUserName(auth.getName());// logged user object

    Privilege usrPrivilege = userPrivilegeController.getPrivilegeByUserModule(auth.getName(), "Quotation");// get
                                                                                                           // quotation
                                                                                                           // object

    // check authorization
    // Check whether the user has permission to insert

    if (usrPrivilege.getInsert_privilege()) {

      // check duplication

      try {
        // set auto added data
        quotation.setAdded_datetime(LocalDateTime.now());// set value for addeddate time
        quotation.setAdded_user_id(loggedUser.getId());// database eke load wela tiyn nama withri penanne
        quotation.setQuotation_no(quotationDao.generateNextQuotationNo());// auto generate next quotationRequest no

        // save operator
        // api association eke tiyn main side eka block
        // krnw(QuotationRequestHasItem.java eke quotationRequestid foreign key)
        // main side ek block krm ey natuw submit krnn ba. ek nawath api add krnn one
        // nis list ekaka tiyn eka loop eken read krl, one by one set krnw pod id eka
        for (QuotationHasItem quotationHasItem : quotation.getQuotationHasItemList()) {
          quotationHasItem.setQuotation_id(quotation);
        }

        quotationDao.save(quotation);
        // dependencies
        // to update quotation request status to recieved when quotation is added
        QuotationRequest quotationRequest = quotationRequestDao.getReferenceById(quotation.getQuotation_request_id().getId());
        quotationRequest.setQuotation_request_status_id(quotationRequestStatusDao.getReferenceById(2));
        for (QuotationRequestHasItem qrhi : quotationRequest.getQuotationRequestHasItemList()) {
          qrhi.setQuotation_request_id(quotationRequest);
        }
        quotationRequestDao.save(quotationRequest);

        return "OK";// frontend recieving ok

      } catch (Exception e) {

        return "Save Unsucessfull...!" + e.getMessage();

      }
    } else {
      return "Save UnsucessFull..!\nYou Have No Permission to Insert";
    }

  }

  // update
  @PutMapping(value = "/quotation/update")
  public String updateQuotation(@RequestBody Quotation quotation) {

    // check authentication and authorization
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    Privilege usrPrivilege = userPrivilegeController.getPrivilegeByUserModule(auth.getName(),
        "Quotation");

    if (usrPrivilege.getUpdate_privilege()) {
      // Check exist
      if (quotation.getId() == null) {
        return "Update Unsuccessfull ! Quotation  Doesn't Exist...";// check there is an id of the send
      }

      Quotation extById = quotationDao.getReferenceById(quotation.getId());// check the record is in the db or not
      if (extById == null) {
        return "Update Unsuccessfull ! Quotation  Doesn't Exist...";
      }

      // check duplication
      /*
       * frontenmd eken ewan username ekt adala username ekk backend table eke tiynwd
       * blnwa query ekkin
       * eka tiynwnm e tiyna eke id ekai dn update krl ewn eke id amanananm awul equal
       * nm awulk naa mokd same row ek ethok
       * differet row unot awul
       */

      // **Get logged-in user ID**
      User loggedUser = userDao.getByUserName(auth.getName()); // Retrieve user by username

      try {
        // set auto added data
        quotation.setUpdated_datetime(LocalDateTime.now());// set value for addeddate time
        quotation.setUpdated_user_id(loggedUser.getId());

        // api association eke tiyn main side eka block krnw(QuotationHasItem.java eke
        // quotationid foreign key)
        // main side ek block krm ey natuw submit krnn ba. ek nawath api add krnn one
        // nis list ekaka tiyn eka loop eken read krl, one by one set krnw
        // quotationRequest id eka
        for (QuotationHasItem quotationHasItem : quotation.getQuotationHasItemList()) {
          quotationHasItem.setQuotation_id(quotation);
        }
        // save operator
        quotationDao.save(quotation);
        // dependencies

        return "OK";// frontend recieving ok

      } catch (Exception e) {

        return "Update Unsucessfull...!" + e.getMessage();

      }

    } else {
      return "Update Unsucessfull...!\nYou Have No Permission to Update";
    }

  }

  // delete
  @DeleteMapping(value = "quotation/delete")
  public String deleteQuotationData(@RequestBody Quotation quotation) {// quotation is the frontend object(has a primary
                                                                       // key)
    // check authorization

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    Privilege usrPrivilege = userPrivilegeController.getPrivilegeByUserModule(auth.getName(),
        "Quotation");

    if (usrPrivilege.getDelete_privilege()) {

      // Check exist
      if (quotation.getId() == null) {
        return "Delete Unsuccessfull ! Quotation  Doesn't Exist...";// check there is an id of the send(id ekk nytuw
                                                                    // awoth)
      }

      Quotation extquotationById = quotationDao.getReferenceById(quotation.getId());// check the record is in the db or
      // not
      if (extquotationById == null) {
        return "Delete Unsuccessfull ! Quotation Doesn't Exist...";
      }

      try {
        // set auto added data
        extquotationById.setDeleted_datetime(LocalDateTime.now());// set value for addeddate time
        extquotationById.setDeleted_user_id(userDao.getByUserName(auth.getName()).getId());
        extquotationById.setQuotation_no(quotationDao.generateNextQuotationNo());// auto generate next quotation no

        // update operator
        extquotationById.setQuotation_status_id(quotationStatusDao.getReferenceById(3));// id=5 is Removed state in
                                                                                        // quotation Status Table

        // api association eke tiyn main side eka block krnw(quotationHasItem.java eke
        // quotationRequestid foreign key)
        // main side ek block krm ey natuw submit krnn ba. ek nawath api add krnn one
        // nis list ekaka tiyn eka loop eken read krl, one by one set krnw quotation id
        // eka
        for (QuotationHasItem quotationHasItem : quotation.getQuotationHasItemList()) {
          quotationHasItem.setQuotation_id(quotation);
        }
        quotationDao.save(extquotationById);
        // front end eken ewn eka hora object ekk unoth eken enne chnage wechcha dat
        // wenn puluwn e nisa tamai extid arn save ekrnne

        // quotationRequestDao.delete(quotation); to delete record from the database

        // dependencies

        return "OK";// frontend recieving ok

      } catch (Exception e) {

        return "Delete Unsucessfull...!" + e.getMessage();

      }
    } else {
      return "Delete Unsucessfull...!\nYou Have No Permission to Delete";
    }

  }

  // mapping to return quotation requests list based on the selected
  // supplier and required date(URL-->"item/qrlistbysupplier?supplierid?requireddate") to quotation request form
  @GetMapping(value = "/quotation/qlistbysupplierandreqdate/{supplierid}/{requiredate}", produces = "application/json")
  public List<Quotation> getQuotationListBYSupplierId(@PathVariable("supplierid") Integer supplierid,@PathVariable("requiredate") LocalDate requiredate) {

    // Check user authentication and authorization

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    Privilege usrPrivilege = userPrivilegeController.getPrivilegeByUserModule(auth.getName(),
        "Quotation");// Module name should be checked with the database module table

    if (usrPrivilege.getSelect_privilege()) {
      // returning the values in the item table
      return quotationDao.getQuotationListBySupplier(supplierid,requiredate);
    } else {
      return new ArrayList<>();// returns an empty array since the user has no privilege to see the data
    }

  }


      //mapping to return quantity based on quotationrequest id and itemid
    @GetMapping(value = "/qt/qtybyquotationanditemid/{qtid}/{itemid}", produces = "application/json")
     public BigDecimal getQtyBYQrItem(@PathVariable("qtid") Integer qtid,@PathVariable("itemid") Integer itemid) {
 
         // Check user authentication and authorization
 
         Authentication auth = SecurityContextHolder.getContext().getAuthentication();
         Privilege usrPrivilege = userPrivilegeController.getPrivilegeByUserModule(auth.getName(),
                 "Quotation");// Module name should be checked with the database module table
 
         if (usrPrivilege.getSelect_privilege()) {
             // returning the values in the item table
             return quotationDao.getQtybyQtionandItemId(qtid,itemid);
         } else {
             return BigDecimal.ZERO;// returns an empty array since the user has no privilege to see the data
         }
 
     }

     //get unit price for purchase inner form
    @GetMapping(value = "/quotation/unitpricebyitem/{qtid}/{itemid}", produces = "application/json")
     public BigDecimal getUnitPriceBYQItem(@PathVariable("qtid") Integer qtid,@PathVariable("itemid") Integer itemid) {
 
         // Check user authentication and authorization
 
         Authentication auth = SecurityContextHolder.getContext().getAuthentication();
         Privilege usrPrivilege = userPrivilegeController.getPrivilegeByUserModule(auth.getName(),
                 "Quotation");// Module name should be checked with the database module table
 
         if (usrPrivilege.getSelect_privilege()) {
             // returning the values in the item table
             return quotationDao.getUnitPricebyQItemId(qtid,itemid);
         } else {
             return BigDecimal.ZERO;// returns an empty array since the user has no privilege to see the data
         }
 
     }
   
}
