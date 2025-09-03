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

import lk.upalisupermarket.dao.GrnDao;
import lk.upalisupermarket.dao.GrnStatusDao;
import lk.upalisupermarket.dao.InventoryDao;
import lk.upalisupermarket.dao.PurchaseOrderDao;
import lk.upalisupermarket.dao.PurchaseOrderStatusDao;
import lk.upalisupermarket.dao.UserDao;
import lk.upalisupermarket.entity.Grn;
import lk.upalisupermarket.entity.GrnHasItem;
import lk.upalisupermarket.entity.Inventory;
import lk.upalisupermarket.entity.Privilege;
import lk.upalisupermarket.entity.PurchaseOrder;
import lk.upalisupermarket.entity.PurchaseOrderHasItem;
import lk.upalisupermarket.entity.User;

@RestController // api implement krn mapping siyalla servlet container ekt add wenne me
// annotation eken
public class GrnController {

  @Autowired // create instance for grn dao
  private GrnDao grnDao;

  @Autowired
  private UserPrivilegeController userPrivilegeController;// create an instance of UserPrivilegeContoller to access
                                                          // the data for secuirity
  // privileged user kenek da kiyl blnna

  @Autowired // generate instance
  private GrnStatusDao grnStatusDao;

  @Autowired // generate instance
  private UserDao userDao;

  @Autowired // generate instance
  private InventoryDao inventoryDao;

  @Autowired // generate instance
  private PurchaseOrderDao purchaseOrderDao;

  @Autowired // generate instance
  private PurchaseOrderStatusDao purchaseOrderStatusDao;

  // mapping to return purchaseorder ui(URL-->"/grn")
  @RequestMapping(value = "/grn")
  public ModelAndView loadGrnPage() {

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();// get authentication object
    User logUser = userDao.getByUserName(auth.getName());

    ModelAndView grnPage = new ModelAndView();

    grnPage.setViewName("grn.html");
    grnPage.addObject("title", "Upali Supermarket-Grn Management");// set the title for page
    grnPage.addObject("loggedUserName", auth.getName());// get username of the logged user

    grnPage.addObject("loggedUserPhoto", logUser.getUser_photo());// get userphoto of the logged user
    return grnPage;
  }

  // mapping to return all purchase order data(URL-->"grn/findall")
  @GetMapping(value = "/grn/findall", produces = "application/json")
  public List<Grn> findAllData() {

    // Check user authentication and authorization

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    Privilege usrPrivilege = userPrivilegeController.getPrivilegeByUserModule(auth.getName(),
        "Grn");// Module name should be checked with the database Module table

    if (usrPrivilege.getSelect_privilege()) {
      // returning the values in the grn table
      return grnDao.findAll(Sort.by(Direction.DESC, "id"));// grn dao query for selected only the
                                                           // needed db fileds

    } else {
      return new ArrayList<>();// returns an empty array since the user has no privilege to see the data
    }

  }


  @PostMapping(value = "/grn/insert") // grn is the frontend object(no primary key since its now getting inserted)
  public String saveGrnData(@RequestBody Grn grn) { // Type should be grn and object sent from
                                                                // frontend is grn

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();// get authentication object
    User loggedUser = userDao.getByUserName(auth.getName());// logged user object

    Privilege usrPrivilege = userPrivilegeController.getPrivilegeByUserModule(auth.getName(), "Grn");// get purchaseorder object

    // check authorization
    // Check whether the user has permission to insert

    if (usrPrivilege.getInsert_privilege()) {
      // check duplication of batch no

      Grn existByBatchNo = grnDao.getByGrnNo(grn.getGrn_no());
      if (existByBatchNo != null) {
          return "Save Unsuccessfull !\n Entered Batch No : " + grn.getGrn_no() + " Already Exists..!";
      }

      try {
        // set auto added data
        grn.setAdded_datetime(LocalDateTime.now());// set value for addeddate time
        grn.setAdded_user_id(loggedUser.getId());// database eke load wela tiyn nama withri penanne
        grn.setGrn_no(grnDao.generateNextGrnNo());// auto generate next grn no
        grn.setPaid_amount(BigDecimal.ZERO);//set paid amount to zero 

      // save operator
      //api association eke tiyn main side eka block krnw(grnhasitem.java eke grnid foreign key)
      //main side ek block krm ey natuw submit krnn ba. ek nawath api add krnn one nis list ekaka tiyn eka loop eken read krl,  one by one set krnw grn id eka
        for (GrnHasItem grnHasItem : grn.getGrnHasItemList()) {//me code eken thiyna item list එක (grn.getGrnHasItemList()-->This returns a List of GrnHasItem objects)loop krnwa, eka item ekakata parent GRN eka set කරනවා.
          grnHasItem.setGrn_id(grn);//Hibernate ekata kiyanna one meyalā kohen āwada kiyala.Næthan foreign key (grn_id) null wela database eke error enna puluwan.
        }
        grnDao.save(grn);

        // dependencies

        //update purchase order status  to Recieved after grn added -->innerform nisa loop eklk liynna one
        PurchaseOrder purchaseOrder=purchaseOrderDao.getReferenceById(grn.getPurchase_order_id().getId());//grn ekt adla purchase order ek id eken select krnwa
        purchaseOrder.setPurchase_order_status_id(purchaseOrderStatusDao.getReferenceById(2));//purchase order status ek "recieved"(id=2) krnwa
          for (PurchaseOrderHasItem pohi : purchaseOrder.getPurchaseOrderHasItemList()) {
          pohi.setPurchase_order_id(purchaseOrder);
        }
        purchaseOrderDao.save(purchaseOrder);

        //update inventory when grn added
        for (GrnHasItem ghi : grn.getGrnHasItemList()) {//It is used to iterate over a collection — in this case, a list of GrnHasItem objects inside the grn.
          //item without batch no -->if user doesnt enter any batch no
          if (ghi.getBatch_no()==null) {
            Inventory newInventory=new Inventory();//create a new inventory object
            newInventory.setBatch_no(inventoryDao.getnextBatchno());//set next batch no (create using the query in inventory.dao)
            newInventory.setExp_date(ghi.getExp_date());// set exp date                add 14 days to exp date from now-->LocalDate.now().plusDays(14)
            newInventory.setManufacture_date(ghi.getMfd_date());//set mfddate
            newInventory.setItem_id(ghi.getItem_id());//set itemid
            newInventory.setTotal_qty(ghi.getTot_qty());//set totalqty
            newInventory.setAvailable_qty(ghi.getTot_qty());//set availble aty
            newInventory.setSales_price(ghi.getSelling_price());//set selling price
            
            inventoryDao.save(newInventory);
          } 
          //Item With Existing Batch Number
          else {

              Inventory extInventory=inventoryDao.getByItemBatchNo(ghi.getItem_id().getId(),ghi.getBatch_no());
               if(extInventory!=null){
                extInventory.setAvailable_qty(extInventory.getAvailable_qty().add(ghi.getQty()));//set available quantity
                extInventory.setTotal_qty(extInventory.getTotal_qty().add(ghi.getQty()));//set total quantity
                inventoryDao.save(extInventory);
              }
               
                else{
                  //if grn without any inventory 
                  Inventory newInventory=new Inventory();//create a new inventory object
                   
                   newInventory.setExp_date(ghi.getExp_date());//set exp date
                   newInventory.setBatch_no(ghi.getBatch_no());//set batch no
                   newInventory.setManufacture_date(ghi.getMfd_date());//set mfd date
                   newInventory.setItem_id(ghi.getItem_id());//set item id
                   newInventory.setTotal_qty(ghi.getTot_qty());//set qty
                   newInventory.setAvailable_qty(ghi.getTot_qty());//set available qty
                   newInventory.setSales_price(ghi.getSelling_price());//set selling price
                   inventoryDao.save(newInventory);//save
                }
          }
        }


        return "OK";// frontend recieving ok

      } catch (Exception e) {

        return "Save Unsucessfull...!" + e.getMessage();

      }
    }else

  {
    return "Save UnsucessFull..!\nYou Have No Permission to Insert";
  }

  }


  // update
  @PutMapping(value = "/grn/update")
  public String updateGrn(@RequestBody Grn grn) {

    // check authentication and authorization
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    Privilege usrPrivilege = userPrivilegeController.getPrivilegeByUserModule(auth.getName(),
        "Grn");

    if (usrPrivilege.getUpdate_privilege()) {
      // Check exist
      if (grn.getId() == null) {
        return "Update Unsuccessfull ! Grn Doesn't Exist...";// check there is an id of the send
      }

      Grn extById = grnDao.getReferenceById(grn.getId());// check the record is in the db or not
      if (extById == null) {
        return "Update Unsuccessfull ! Grn Doesn't Exist...";
      }

      // check duplication
      /*
       * frontenmd eken ewan username ekt adala username ekk backend table eke tiynwd
       * blnwa query ekkin
       * eka tiynwnm e tiyna eke id ekai dn update krl ewn eke id amanananm awul equal
       * nm awulk naa mokd same row ek ethok
       * differet row unot awul
       */

        Grn existByBatchNo = grnDao.getByGrnNo(grn.getGrn_no());
        if (existByBatchNo != null && existByBatchNo.getId()!=grn.getId()) {
            return "Update Unsuccessfull !\n Entered Batch No : " + grn.getGrn_no() + " Already Exists..!";
        }
   
      // **Get logged-in user ID**
      User loggedUser = userDao.getByUserName(auth.getName()); // Retrieve user by username

      try {
        // set auto added data
        grn.setUpdated_datetime(LocalDateTime.now());// set value for addeddate time
        grn.setUpdated_user_id(loggedUser.getId());

        // api association eke tiyn main side eka block krnw(GrnHasItem.java eke grnid
        // foreign key)
        // main side ek block krm ey natuw submit krnn ba. ek nawath api add krnn one
        // nis list ekaka tiyn eka loop eken read krl, one by one set krnw grn id eka
        for (GrnHasItem grnHasItem : grn.getGrnHasItemList()) {
          grnHasItem.setGrn_id(grn);
        }

        // save operator
        grnDao.save(grn);
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
  @DeleteMapping(value = "grn/delete")
  public String deleteGrn(@RequestBody Grn grn) {// grn is the frontend object(has a primary key)
    // check authorization

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    Privilege usrPrivilege = userPrivilegeController.getPrivilegeByUserModule(auth.getName(),
        "Grn");

    if (usrPrivilege.getDelete_privilege()) {

      // Check exist
      if (grn.getId() == null) {
        return "Delete Unsuccessfull ! Grn Doesn't Exist...";// check there is an id of the send(id ekk nytuw awoth)
      }

      Grn extgrnById = grnDao.getReferenceById(grn.getId());// check the record is in the db or
                                                            // not
      if (extgrnById == null) {
        return "Delete Unsuccessfull ! Grn Doesn't Exist...";
      }

      try {
        // set auto added data
        extgrnById.setDeleted_datetime(LocalDateTime.now());// set value for addeddate time
        extgrnById.setDeleted_user_id(userDao.getByUserName(auth.getName()).getId());
        extgrnById.setGrn_no(grnDao.generateNextGrnNo());// auto generate next grn no

        // update operator
        extgrnById.setGrn_status_id(grnStatusDao.getReferenceById(5));// id=5 is Deleted state in grn Status Table

        // api association eke tiyn main side eka block krnw(grnHasItem.java eke grnid
        // foreign key)
        // main side ek block krm ey natuw submit krnn ba. ek nawath api add krnn one
        // nis list ekaka tiyn eka loop eken read krl, one by one set krnw grn id eka
        for (GrnHasItem grnHasItem : grn.getGrnHasItemList()) {
          grnHasItem.setGrn_id(grn);
        }

        grnDao.save(extgrnById);
        // front end eken ewn eka hora object ekk unoth eken enne chnage wechcha dat
        // wenn puluwn e nisa tamai extid arn save ekrnne

        // grnDao.delete(purchaseOrder); to delete record from the database

        // dependencies

        return "OK";// frontend recieving ok

      } catch (Exception e) {

        return "Delete Unsucessfull...!" + e.getMessage();

      }
    } else {
      return "Delete Unsucessfull...!\nYou Have No Permission to Delete";
    }

  }


  //filter grn based on the selected supplier(/grn/listbysupplier/)
    // mapping to return items list based on the selected supplier(URL-->"grn/listbysupplier?supplierid") to supplier payment  form
     @GetMapping(value = "/grn/listbysupplier/{supplierid}", produces = "application/json")
     public List<Grn> getItemListBYSupplierId(@PathVariable("supplierid") Integer supplierid) {
 
         // Check user authentication and authorization
 
         Authentication auth = SecurityContextHolder.getContext().getAuthentication();
         Privilege usrPrivilege = userPrivilegeController.getPrivilegeByUserModule(auth.getName(),
                 "Grn");// Module name should be checked with the database module table
 
         if (usrPrivilege.getSelect_privilege()) {
             // returning the values in the item table
             return grnDao.getItemListBySupplierId(supplierid);
         } else {
             return new ArrayList<>();// returns an empty array since the user has no privilege to see the data
         }
 
     }
   

}
