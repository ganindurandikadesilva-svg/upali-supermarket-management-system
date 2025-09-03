package lk.upalisupermarket.controller;
import java.time.LocalDate;
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

import lk.upalisupermarket.dao.ItemSeasonalDiscountDao;
import lk.upalisupermarket.dao.UserDao;
import lk.upalisupermarket.entity.ItemSeasonalDiscount;
import lk.upalisupermarket.entity.Privilege;
import lk.upalisupermarket.entity.User;



@RestController
public class ItemSeasonalDiscountController {

    @Autowired // generate instance
    private ItemSeasonalDiscountDao itemSeasonalDiscountDao;

    
    @Autowired // generate instance
    private UserDao userDao;

    @Autowired
    private UserPrivilegeController userPrivilegeController;// create an instance of UserPrivilegeContoller to access
                                                            // the data for secuirity
    // privileged user kenek da kiyl blnna



     // mapping to return item Discount ui(URL-->"/itemdiscount")
    @RequestMapping(value = "/itemseasonaldiscount")
    public ModelAndView loadItemSeasonalDiscountPage() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();// get authentication object
        User logUser=userDao.getByUserName(auth.getName());
        ModelAndView itemDiscountPage = new ModelAndView();

        itemDiscountPage.setViewName("itemDiscount.html");
        itemDiscountPage.addObject("title", "Upali Supermarket-Item Seasonal Discount Management");// set the title for page
        itemDiscountPage.addObject("loggedUserName", auth.getName());// get username of the logged user
        
        itemDiscountPage.addObject("loggedUserPhoto", logUser.getUser_photo());//get userphoto of the logged user
        return itemDiscountPage;
    }

    // mapping to return unittype data(URL-->"/itemseasonaldiscount/findall")
    @GetMapping(value = "/itemseasonaldiscount/findall",produces = "application/json")
    
    public List<ItemSeasonalDiscount> findAllData(){

         // Check user authentication and authorization

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Privilege usrPrivilege = userPrivilegeController.getPrivilegeByUserModule(auth.getName(),
                "Item_Seasonal_Discount");// Module name should be checked with the database module table

        if (usrPrivilege.getSelect_privilege()) {
            // returning the values in the item_seasonal_discount table
            return itemSeasonalDiscountDao.findAll(Sort.by(Direction.DESC, "id"));//item dao query for selected only the nneded db fileds
            
        } else {
            return new ArrayList<>();// returns an empty array since the user has no privilege to see the data
        }
        
    }
  

    
      @PostMapping(value = "/itemseasonaldiscount/insert") // item is the frontend object(no primary key since its now getting inserted)
    public String saveItemSeasonalDiscountData(@RequestBody ItemSeasonalDiscount itemSeasonalDiscount) { // Type should be ItemSeasonalDiscount and object sent from frontend is itemSeasonalDiscount

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();// get authentication object
      //  User loggedUser = userDao.getByUserName(auth.getName());// logged user object

        Privilege usrPrivilege = userPrivilegeController.getPrivilegeByUserModule(auth.getName(),"Item_Seasonal_Discount");//get itemSeasonalDiscount object 

        // check authorization
        // Check whether the user has permission to insert

        if (usrPrivilege.getInsert_privilege()) {
            // check duplication


            try {
                // set auto added data
                //No auto added data
            /*     itemSeasonalDiscount.setAdded_datetime(LocalDateTime.now());// set value for addeddate time
                itemSeasonalDiscount.setAdded_user_id(loggedUser.getId());// database eke load wela tiyn nama withri penanne 
                itemSeasonalDiscount.setItem_no(itemSeasonalDiscountDao.generateNextItemSeasonalDiscountNo());// auto generate next itemSeasonalDiscount no
              
                */
                // save operator
                itemSeasonalDiscountDao.save(itemSeasonalDiscount);
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
    @PutMapping(value = "/itemseasonaldiscount/update")
    public String updateItemSeasonalDiscountData(@RequestBody ItemSeasonalDiscount itemSeasonalDiscount) {

        // check authentication and authorization
        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        Privilege usrPrivilege=userPrivilegeController.getPrivilegeByUserModule(auth.getName(), 
        "Item_Seasonal_Discount");

        if (usrPrivilege.getUpdate_privilege()) {
            // Check exist
        if (itemSeasonalDiscount.getId() == null) {
            return "Update Unsuccessfull ! Item Seasonal Discount Doesn't Exist...";// check there is an id of the send
        }

        ItemSeasonalDiscount extById = itemSeasonalDiscountDao.getReferenceById(itemSeasonalDiscount.getId());// check the record is in the db or not
        if (extById == null) {
            return "Update Unsuccessfull ! Item Seasonal Discount Doesn't Exist...";
        }

        // check duplication
        /*   frontenmd eken ewan username ekt adala username ekk backend table eke tiynwd blnwa query ekkin 
        eka tiynwnm e tiyna eke id ekai dn update krl ewn eke id amanananm awul equal nm awulk naa mokd same row ek ethok
        differet row unot awul */

        // **Get logged-in user ID**
      //  User loggedUser =userDao.getByUserName(auth.getName()); // Retrieve user by username

        try {
            // set auto added data
            //No auto added data
           /*  itemSeasonalDiscount.setUpdated_datetime(LocalDateTime.now());// set value for addeddate time
            itemSeasonalDiscount.setUpdated_user_id(loggedUser.getId());
           */
            // save operator
            itemSeasonalDiscountDao.save(itemSeasonalDiscount);
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
    @DeleteMapping(value = "itemseasonaldiscount/delete")
    public String deleteItemSeasonalDiscount(@RequestBody ItemSeasonalDiscount itemSeasonalDiscount) {// item is the frontend object(has a primary key)
        // check authorization

        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        Privilege usrPrivilege=userPrivilegeController.getPrivilegeByUserModule(auth.getName(), 
        "Item_Seasonal_Discount");

        if (usrPrivilege.getDelete_privilege()) {
            
        // Check exist
        if (itemSeasonalDiscount.getId() == null) {
            return "Delete Unsuccessfull ! Item Seasonal Discount  Doesn't Exist...";// check there is an id of the send(id ekk nytuw awoth)
        }

        ItemSeasonalDiscount extItemDiscountById = itemSeasonalDiscountDao.getReferenceById(itemSeasonalDiscount.getId());// check the record is in the db or
                                                                                  // not
        if (extItemDiscountById == null) {
            return "Delete Unsuccessfull ! Item Seasonal Discount  Doesn't Exist...";
        }

        try {
            // set auto added data
             //No auto added data
        /*     extItemById.setDeleted_datetime(LocalDateTime.now());// set value for addeddate time
            extItemById.setDeleted_user_id(userDao.getByUserName(auth.getName()).getId());
            extItemById.setItem_no(itemDao.generateNextItemNo());// auto generate next item no */

            // update operator
           
            extItemDiscountById.setItem_id(null);                                                                                                                                                        
            itemSeasonalDiscountDao.save(extItemDiscountById);
            //front end eken ewn eka hora object ekk unoth eken enne chnage  wechcha dat wenn puluwn e nisa tamai extid arn save ekrnne

            // itemSeasonalDiscountDao.delete(itemSeasonalDiscount); to delete record from the database

            // dependencies

            return "OK";// frontend recieving ok

        } catch (Exception e) {

            return "Delete Unsucessfull...!" + e.getMessage();

        }
        } else {
            return "Delete Unsucessfull...!\nYou Have No Permission to Delete" ;
        }

    }




          // mapping to return discounys based on the selected item and date and lineprice(URL-->"/discount/seasonldiscountsbyitemidanddateandamount?itemid?date") to invoice form
     @GetMapping(value = "/discount/seasonldiscountsbyitemidanddateandqty/{itemid}/{date}/{qty}", produces = "application/json")//qty is line price
     public List<ItemSeasonalDiscount> seasonalDiscountByItemIdandDate(@PathVariable("itemid") Integer itemid,@PathVariable("date") LocalDate date,@PathVariable("qty") Integer qty) {
 
         // Check user authentication and authorization
 
         Authentication auth = SecurityContextHolder.getContext().getAuthentication();
         Privilege usrPrivilege = userPrivilegeController.getPrivilegeByUserModule(auth.getName(),
                 "Item_Seasonal_Discount");// Module name should be checked with the database module table
 
         if (usrPrivilege.getSelect_privilege()) {
             // returning the values in the item table
             return itemSeasonalDiscountDao.getDiscountByItemidDateandAmount(itemid,date,qty);
         } else {
             return new ArrayList<>();// returns an empty array since the user has no privilege to see the data
         }
 
     }
   
}
