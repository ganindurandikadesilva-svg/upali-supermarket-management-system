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

import lk.upalisupermarket.dao.SupplierDao;
import lk.upalisupermarket.dao.SupplierStatusDao;
import lk.upalisupermarket.dao.UserDao;
import lk.upalisupermarket.entity.Privilege;
import lk.upalisupermarket.entity.Supplier;
import lk.upalisupermarket.entity.User;

@RestController
public class SupplierController {

    @Autowired
    private UserPrivilegeController userPrivilegeController;// create an instance of UserPrivilegeContoller to access
                                                            // the data for secuirity
    // privileged user kenek da kiyl blnna

    @Autowired
    private SupplierDao supplierDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private SupplierStatusDao supplierStatusDao;

      // mapping to return supplier ui(URL-->"/supplier")
    @RequestMapping(value = "/supplier")
    public ModelAndView loadSupplierPage() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();// get authentication object
        User logUser=userDao.getByUserName(auth.getName());
        ModelAndView supplierPage = new ModelAndView();

        supplierPage.setViewName("supplier.html");
        supplierPage.addObject("title", "Upali Supermarket-Supplier Management");// set the title for page
        supplierPage.addObject("loggedUserName", auth.getName());// get username of the logged user
        
        supplierPage.addObject("loggedUserPhoto", logUser.getUser_photo());//get userphoto of the logged user
        return supplierPage;
    }

       // mapping to return all supplier data(URL-->"supplier/findall")
    @GetMapping(value = "/supplier/findall", produces = "application/json")
    public List<Supplier> findAllData() {

        // Check user authentication and authorization

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Privilege usrPrivilege = userPrivilegeController.getPrivilegeByUserModule(auth.getName(),
                "Supplier");// Module name should be checked with the database module table

        if (usrPrivilege.getSelect_privilege()) {
            // returning the values in the supplier table
            return supplierDao.findAll(Sort.by(Direction.DESC, "id"));//sort decending from cusid
        } else {
            return new ArrayList<>();// returns an empty array since the user has no privilege to see the data
        }

    }

      // insert
    @PostMapping(value = "/supplier/insert") // supplier is the frontend object(no primary key since its now getting
                                             // inserted)
    public String saveSupplierData(@RequestBody Supplier supplier) { // Type should be Supplier and object sent from
                                                                     // frontend is supplier

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();// get authentication object
        User loggedUser = userDao.getByUserName(auth.getName());// logged user object

        Privilege usrPrivilege = userPrivilegeController.getPrivilegeByUserModule(auth.getName(),
                "Supplier");//get Supplier object 

        // check authorization
        // Check whether the user has permission to insert

        if (usrPrivilege.getInsert_privilege()) {
            // check duplication

            //check duplication for buisness registration no
            Supplier existSupplierByBusinessRegno = supplierDao.getByBusinessRegno(supplier.getBuisness_reg_no());// supplier.getEmail() is
                                                                                        // frontend email
            if (existSupplierByBusinessRegno != null) {
                return "Save Unsuccessfull !\n Entered Buisness Regno : " + supplier.getBuisness_reg_no() + " Already Exists..!";
            }

            // Check duplication using Email value
            Supplier existSupplierByEmail = supplierDao.getByEmail(supplier.getEmail());// supplier.getEmail() is
                                                                                        // frontend email
            if (existSupplierByEmail != null) {
                return "Save Unsuccessfull !\n Entered Email : " + supplier.getEmail() + " Already Exists..!";
            }

            try {
                // set auto added data
                supplier.setAdded_datetime(LocalDateTime.now());// set value for addeddate time
                supplier.setAdded_user_id(loggedUser.getId());// database eke load wela tiyn nama withri penanne
                supplier.setSupplier_regno(supplierDao.generateNextSupplerRegNo());// auto generate next supplier reg no

                // save operator
                supplierDao.save(supplier);
                // dependencies
                return "OK";// frontend recieving ok


                }  
            catch (Exception e) {

                return "Save Unsucessfull...!" + e.getMessage();

            }
        }else {
            return "Save UnsucessFull..!\nYou Have No Permission to Insert";
        }

    }

     // update
    @PutMapping(value = "/supplier/update")
    public String updateSupplier(@RequestBody Supplier supplier) {

        // check authentication and authorization
        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        Privilege usrPrivilege=userPrivilegeController.getPrivilegeByUserModule(auth.getName(), 
        "Supplier");

        if (usrPrivilege.getUpdate_privilege()) {
            // Check exist
        if (supplier.getId() == null) {
            return "Update Unsuccessfull ! Supplier Doesn't Exist...";// check there is an id of the send
        }

        Supplier extById = supplierDao.getReferenceById(supplier.getId());// check the record is in the db or not
        if (extById == null) {
            return "Update Unsuccessfull ! Supplier Doesn't Exist...";
        }

        // check duplication
        /*   frontenmd eken ewan username ekt adala username ekk backend table eke tiynwd blnwa query ekkin 
        eka tiynwnm e tiyna eke id ekai dn update krl ewn eke id amanananm awul equal nm awulk naa mokd same row ek ethok
        differet row unot awul */

        
        // Check duplication using Email value

        /*frontenmd eken ewan username ekt adala username ekk backend table eke tiynwd blnwa query ekkin 
        eka tiynwnm e tiyna eke id ekai dn update krl ewn eke id amanananm awul equal nm awulk naa mokd same row ek ethok
        differet row unot awul */

            //check duplication for buisness registration no
        Supplier existSupplierByBusinessRegno = supplierDao.getByBusinessRegno(supplier.getBuisness_reg_no());// supplier.getEmail() is
                                                                                    // frontend email
        if (existSupplierByBusinessRegno != null && existSupplierByBusinessRegno.getId()!=supplier.getId()) {
            return "Update Unsuccessfull !\n Entered Business Regno : " + supplier.getBuisness_reg_no() + " Already Exists..!";
        }
            
        Supplier existSupplierByEmail = supplierDao.getByEmail(supplier.getEmail());// supplier.getEmail() is frontend
                                                                                    // email
        if (existSupplierByEmail != null && existSupplierByEmail.getId() != supplier.getId()) {
            return "Update Unsuccessfull !\n Entered Email : " + supplier.getEmail() + " Already Exists..!";
        }

        try {
            // set auto added data
            supplier.setUpdated_datetime(LocalDateTime.now());// set value for addeddate time
            supplier.setUpdated_user_id(userDao.getByUserName(auth.getName()).getId());

            // save operator
            supplierDao.save(supplier);
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
    @DeleteMapping(value = "/supplier/delete")
    public String deleteSupplier(@RequestBody Supplier supplier) {// supplier is the frontend object(has a primary key)
        // check authorization

        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        Privilege usrPrivilege=userPrivilegeController.getPrivilegeByUserModule(auth.getName(), 
        "Supplier");

        if (usrPrivilege.getDelete_privilege()) {
            
        // Check exist
        if (supplier.getId() == null) {
            return "Delete Unsuccessfull ! Supplier Doesn't Exist...";// check there is an id of the send
        }

        Supplier extSupplierById = supplierDao.getReferenceById(supplier.getId());// check the record is in the db or
                                                                                  // not
        if (extSupplierById == null) {
            return "Delete Unsuccessfull ! Supplier Doesn't Exist...";
        }

        try {
            // set auto added data
            extSupplierById.setDeleted_datetime(LocalDateTime.now());// set value for addeddate time
            extSupplierById.setDeleted_user_id(userDao.getByUserName(auth.getName()).getId());
            extSupplierById.setSupplier_regno(supplierDao.generateNextSupplerRegNo());// auto generate next supllier no

            // update operator
            extSupplierById.setSupplier_status_id(supplierStatusDao.getReferenceById(3));// id=3 is Deleted state in Supplier
                                                                                    // Status Table

            supplierDao.save(extSupplierById);

            // supplierDao.delete(supplier); to delete record from the database

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
