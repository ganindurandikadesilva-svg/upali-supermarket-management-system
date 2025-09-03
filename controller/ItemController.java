package lk.upalisupermarket.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


import lk.upalisupermarket.dao.ItemDao;
import lk.upalisupermarket.dao.ItemStatusDao;
import lk.upalisupermarket.dao.UserDao;
import lk.upalisupermarket.entity.Item;
import lk.upalisupermarket.entity.Privilege;
import lk.upalisupermarket.entity.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;



@RestController// api implement krn mapping siyalla servlet container ekt add wenne me
// annotation eken
public class ItemController   {

    @Autowired // generate instance
    private UserDao userDao;

    @Autowired // generate instance
    private ItemStatusDao itemStatusDao;

    @Autowired
    private UserPrivilegeController userPrivilegeController;// create an instance of UserPrivilegeContoller to access
                                                            // the data for secuirity
    // privileged user kenek da kiyl blnna

    @Autowired // generate instance
    private ItemDao itemDao;


// mapping to return employee ui(URL-->"/item")
    @RequestMapping(value = "/item")
    public ModelAndView UI () {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();// get authentication object
        User logUser=userDao.getByUserName(auth.getName());
        ModelAndView itemPage = new ModelAndView();

        itemPage.setViewName("item.html");
        itemPage.addObject("title", "Upali Supermarket-Item Management");// set the title for page
        itemPage.addObject("loggedUserName", auth.getName());// get username of the logged user

        itemPage.addObject("loggedUserPhoto", logUser.getUser_photo());//get userphoto of the logged user
        return itemPage;
    }

    
   // mapping to return all item data(URL-->"item/findall")
    @GetMapping(value = "/item/findall", produces = "application/json")
    public List<Item> findAllData() {

        // Check user authentication and authorization

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Privilege usrPrivilege = userPrivilegeController.getPrivilegeByUserModule(auth.getName(),
                "Item");// Module name should be checked with the database Employee table

        if (usrPrivilege.getSelect_privilege()) {
            // returning the values in the item table
            return itemDao.findAll(Sort.by(Direction.DESC, "id"));//item dao query for selected only the nneded db fileds
            
        } else {
            return new ArrayList<>();// returns an empty array since the user has no privilege to see the data
        }

    }
  

      @PostMapping(value = "/item/insert") // item is the frontend object(no primary key since its now getting inserted)
    public String saveItemData(@RequestBody Item item) { // Type should be item and object sent from frontend is item

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();// get authentication object
        User loggedUser = userDao.getByUserName(auth.getName());// logged user object

        Privilege usrPrivilege = userPrivilegeController.getPrivilegeByUserModule(auth.getName(),"Item");//get Item object 

        // check authorization
        // Check whether the user has permission to insert

        if (usrPrivilege.getInsert_privilege()) {
            // check duplication
            Item existItemByItemname = itemDao.getByItemName(item.getItemname());
            if (existItemByItemname != null) {
                return "Save Unsuccessfull !\n Entered Item : " + item.getItemname() + " Already Exists..!";
            }

            try {
                // set auto added data
                item.setAdded_datetime(LocalDateTime.now());// set value for addeddate time
                item.setAdded_user_id(loggedUser.getId());// database eke load wela tiyn nama withri penanne
                item.setItem_no(itemDao.generateNextItemNo());// auto generate next item no

                // save operator
                itemDao.save(item);
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
    @PutMapping(value = "/item/update")
    public String updateItem(@RequestBody Item item) {

        // check authentication and authorization
        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        Privilege usrPrivilege=userPrivilegeController.getPrivilegeByUserModule(auth.getName(), 
        "Item");

        if (usrPrivilege.getUpdate_privilege()) {
            // Check exist
        if (item.getId() == null) {
            return "Update Unsuccessfull ! Item Doesn't Exist...";// check there is an id of the send
        }

        Item extById = itemDao.getReferenceById(item.getId());// check the record is in the db or not
        if (extById == null) {
            return "Update Unsuccessfull ! Item Doesn't Exist...";
        }

        // check duplication
        /*   frontenmd eken ewan username ekt adala username ekk backend table eke tiynwd blnwa query ekkin 
        eka tiynwnm e tiyna eke id ekai dn update krl ewn eke id amanananm awul equal nm awulk naa mokd same row ek ethok
        differet row unot awul */

        Item existItemByItemname = itemDao.getByItemName(item.getItemname());
        if (existItemByItemname != null && existItemByItemname.getId()!=item.getId()) {
                return "Update Unsuccessfull !\n Entered Item : " + item.getItemname() + " Already Exists..!";
        }

        // **Get logged-in user ID**
        User loggedUser =userDao.getByUserName(auth.getName()); // Retrieve user by username

        try {
            // set auto added data
            item.setUpdated_datetime(LocalDateTime.now());// set value for addeddate time
            item.setUpdated_user_id(loggedUser.getId());

            // save operator
            itemDao.save(item);
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
    @DeleteMapping(value = "item/delete")
    public String deleteItem(@RequestBody Item item) {// item is the frontend object(has a primary key)
        // check authorization

        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        Privilege usrPrivilege=userPrivilegeController.getPrivilegeByUserModule(auth.getName(), 
        "Item");

        if (usrPrivilege.getDelete_privilege()) {
            
        // Check exist
        if (item.getId() == null) {
            return "Delete Unsuccessfull ! Item Doesn't Exist...";// check there is an id of the send(id ekk nytuw awoth)
        }

        Item extItemById = itemDao.getReferenceById(item.getId());// check the record is in the db or
                                                                                  // not
        if (extItemById == null) {
            return "Delete Unsuccessfull ! Item Doesn't Exist...";
        }

        try {
            // set auto added data
            extItemById.setDeleted_datetime(LocalDateTime.now());// set value for addeddate time
            extItemById.setDeleted_user_id(userDao.getByUserName(auth.getName()).getId());
            extItemById.setItem_no(itemDao.generateNextItemNo());// auto generate next item no

            // update operator
            extItemById.setItem_status_id(itemStatusDao.getReferenceById(3));// id=3 is Deleted state in Item
                                                                                    // Status Table

            itemDao.save(extItemById);
            //front end eken ewn eka hora object ekk unoth eken enne chnage  wechcha dat wenn puluwn e nisa tamai extid arn save ekrnne

            // itemDao.delete(item); to delete record from the database

            // dependencies

            return "OK";// frontend recieving ok

        } catch (Exception e) {

            return "Delete Unsucessfull...!" + e.getMessage();

        }
        } else {
            return "Delete Unsucessfull...!\nYou Have No Permission to Delete" ;
        }

    }


     // mapping to return items list that suppliers doesnt supply(URL-->"item/listWithoutSupply") to supplier form
     @GetMapping(value = "/item/listWithoutSupply",params = {"supplierid"}, produces = "application/json")
     public List<Item> getItemListWithoutSupplierById(@RequestParam("supplierid") Integer supplierid) {
 
         // Check user authentication and authorization
 
         Authentication auth = SecurityContextHolder.getContext().getAuthentication();
         Privilege usrPrivilege = userPrivilegeController.getPrivilegeByUserModule(auth.getName(),
                 "Item");// Module name should be checked with the database Employee table
 
         if (usrPrivilege.getUpdate_privilege()) {
             // returning the values in the item table
             return itemDao.getListWithoutSupplier(supplierid);
         } else {
             return new ArrayList<>();// returns an empty array since the user has no privilege to see the data
         }
 
     }
   

     // mapping to return items list based on the selected supplier(URL-->"item/listbysupplier?supplierid") to quotation request form
     @GetMapping(value = "/item/listbysupplier/{supplierid}", produces = "application/json")
     public List<Item> getItemListBYSupplierId(@PathVariable("supplierid") Integer supplierid) {
 
         // Check user authentication and authorization
 
         Authentication auth = SecurityContextHolder.getContext().getAuthentication();
         Privilege usrPrivilege = userPrivilegeController.getPrivilegeByUserModule(auth.getName(),
                 "Item");// Module name should be checked with the database Employee table
 
         if (usrPrivilege.getSelect_privilege()) {
             // returning the values in the item table
             return itemDao.getItemListBySupplier(supplierid);
         } else {
             return new ArrayList<>();// returns an empty array since the user has no privilege to see the data
         }
 
     }
   
  // mapping to return items list based on the selected quotationrequest(URL-->"item/listbyquotationrequest?qrid") to quotation request form
     @GetMapping(value = "/item/listbyquotationrequest/{qrid}", produces = "application/json")
     public List<Item> getItemListBYQuotationRequestId(@PathVariable("qrid") Integer qrid) {
 
         // Check user authentication and authorization
 
         Authentication auth = SecurityContextHolder.getContext().getAuthentication();
         Privilege usrPrivilege = userPrivilegeController.getPrivilegeByUserModule(auth.getName(),
                 "Item");// Module name should be checked with the database Employee table
 
         if (usrPrivilege.getSelect_privilege()) {
             // returning the values in the item table
             return itemDao.getItemListByQuotationRequest(qrid);
         } else {
             return new ArrayList<>();// returns an empty array since the user has no privilege to see the data
         }
 
     }
   

      // mapping to return items list based on the selected quotationre(URL-->"item/listbyquotation?quotationid") to purchase order  form
     @GetMapping(value = "/item/listbyquotation/{quotationid}", produces = "application/json")
     public List<Item> getItemListBYQuotationId(@PathVariable("quotationid") Integer quotationid) {
 
         // Check user authentication and authorization
 
         Authentication auth = SecurityContextHolder.getContext().getAuthentication();
         Privilege usrPrivilege = userPrivilegeController.getPrivilegeByUserModule(auth.getName(),
                 "Item");// Module name should be checked with the database module table
 
         if (usrPrivilege.getSelect_privilege()) {
             // returning the values in the item table
             return itemDao.getItemListByQuotation(quotationid);
         } else {
             return new ArrayList<>();// returns an empty array since the user has no privilege to see the data
         }
 
     }
   


     // mapping to return items list based on the selected purchase order(URL-->"item/listbypod?podid") to grn  form
     @GetMapping(value = "/item/listbypod/{podid}", produces = "application/json")
     public List<Item> getItemListBYPodId(@PathVariable("podid") Integer podid) {
 
         // Check user authentication and authorization
 
         Authentication auth = SecurityContextHolder.getContext().getAuthentication();
         Privilege usrPrivilege = userPrivilegeController.getPrivilegeByUserModule(auth.getName(),
                 "Item");// Module name should be checked with the database module table
 
         if (usrPrivilege.getSelect_privilege()) {
             // returning the values in the item table
             return itemDao.getItemListByPurchaseOrder(podid);
         } else {
             return new ArrayList<>();// returns an empty array since the user has no privilege to see the data
         }
 
     }
   




    // mapping to return items list (URL-->"item/itemlist") to purchaseorder form eke item list for edit form filling
    //only the required colums of the item table are returned (selected one are defined in item.java file)
    //query is written to slect these colums are in item.dao
    @GetMapping(value = "/item/itemlist", produces = "application/json")
    public List<Item> getItemList() {

        // Check user authentication and authorization

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Privilege usrPrivilege = userPrivilegeController.getPrivilegeByUserModule(auth.getName(),
                "Item");// Module name should be checked with the database Employee table

        if (usrPrivilege.getSelect_privilege()) {
            // returning the values in the item table
            return itemDao.getItemList();
        } else {
            return new ArrayList<>();// returns an empty array since the user has no privilege to see the data
        }

    }

      @GetMapping(value = "/item/rop", produces = "application/json")
     public Integer getRopvalue() {

        // Check user authentication and authorization

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Privilege usrPrivilege = userPrivilegeController.getPrivilegeByUserModule(auth.getName(),
                "Item");// Module name should be checked with the database Employee table

        if (usrPrivilege.getSelect_privilege()) {
            // returning the values in the item table
            return itemDao.getRop();
        } else {
            return 0;// returns an empty array since the user has no privilege to see the data
        }

    }

     
      

    
     
   
}