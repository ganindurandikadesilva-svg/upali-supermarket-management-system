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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import lk.upalisupermarket.dao.CustomerDao;
import lk.upalisupermarket.dao.CustomerStatusDao;
import lk.upalisupermarket.dao.UserDao;
import lk.upalisupermarket.entity.Customer;
import lk.upalisupermarket.entity.Privilege;
import lk.upalisupermarket.entity.User;



@RestController
public class CustomerController {

     @Autowired
    private UserPrivilegeController userPrivilegeController;// create an instance of UserPrivilegeContoller to access
                                                            // the data for secuirity
    // privileged user kenek da kiyl blnna

    @Autowired
    private CustomerDao customerDao;

    
    @Autowired // generate instance
    private UserDao userDao;

    @Autowired
    private CustomerStatusDao customerStatusDao;

 
    // mapping to return customer ui(URL-->"/customer")
    @RequestMapping(value = "/customer")
    public ModelAndView loadCustomerPage() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();// get authentication object
        User logUser=userDao.getByUserName(auth.getName());
        ModelAndView customerPage = new ModelAndView();

        customerPage.setViewName("customer.html");
        customerPage.addObject("title", "Upali Supermarket-Customer Management");// set the title for page
        customerPage.addObject("loggedUserName", auth.getName());// get username of the logged user
        customerPage.addObject("loggedUserPhoto", logUser.getUser_photo());//get userphoto of the logged use
        return customerPage;
    }

     // mapping to return all customer data(URL-->"customer/findall")
    @GetMapping(value = "/customer/findall", produces = "application/json")
    public List<Customer> findAllData() {

        // Check user authentication and authorization

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Privilege usrPrivilege = userPrivilegeController.getPrivilegeByUserModule(auth.getName(),
                "Customer");// Module name is checked with the database module table

        if (usrPrivilege.getSelect_privilege()) {
            // returning the values in the customer table
            return customerDao.findAll(Sort.by(Direction.DESC, "id"));//sort decending from cusid
        } else {
            return new ArrayList<>();// returns an empty array since the user has no privilege to see the data
        }

    }

     // insert
    @PostMapping(value = "/customer/insert") // customer is the frontend object(no primary key since its now getting
                                             // inserted)
    public String saveCustomerData(@RequestBody Customer customer) { // Type should be Customer and object sent from
                                                                     // frontend is customer

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();// get authentication object
        User loggedUser = userDao.getByUserName(auth.getName());// logged user object

        Privilege usrPrivilege = userPrivilegeController.getPrivilegeByUserModule(auth.getName(),
                "Customer");//get Customer object 

        // check authorization
        // Check whether the user has permission to insert

        if (usrPrivilege.getInsert_privilege()) {
            // check duplication

            // Check duplication using NIC value
            Customer existCustomerByNic = customerDao.getByNic(customer.getNic());
            if (existCustomerByNic != null) {
                return "Save Unsuccessfull !\n Entered Nic : " + customer.getNic() + " Already Exists..!";
            }

            // Check duplication using Email value
            Customer existCustomerByEmail = customerDao.getByEmail(customer.getEmail());// customer.getEmail() is
                                                                                        // frontend email
            if (existCustomerByEmail != null) {
                return "Save Unsuccessfull !\n Entered Email : " + customer.getEmail() + " Already Exists..!";
            }

            try {
                // set auto added data
                customer.setAdded_datetime(LocalDateTime.now());// set value for addeddate time
                customer.setAdded_user_id(loggedUser.getId());// database eke load wela tiyn nama withri penanne
                customer.setCus_no(customerDao.generateNextCusNo());// auto generate next cus no
                customer.setLoyalty_point(BigDecimal.ZERO);//set loyalty points to zero
                // save operator
                customerDao.save(customer);
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
    @PutMapping(value = "/customer/update")
    public String updateCustomer(@RequestBody Customer customer) {

        // check authentication and authorization
        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        Privilege usrPrivilege=userPrivilegeController.getPrivilegeByUserModule(auth.getName(), 
        "Customer");

        if (usrPrivilege.getUpdate_privilege()) {
            // Check exist
        if (customer.getId() == null) {
            return "Update Unsuccessfull ! Customer Doesn't Exist...";// check there is an id of the send
        }

        Customer extById = customerDao.getReferenceById(customer.getId());// check the record is in the db or not
        if (extById == null) {
            return "Update Unsuccessfull ! Customer Doesn't Exist...";
        }

        // check duplication
        /*   frontenmd eken ewan username ekt adala username ekk backend table eke tiynwd blnwa query ekkin 
        eka tiynwnm e tiyna eke id ekai dn update krl ewn eke id amanananm awul equal nm awulk naa mokd same row ek ethok
        differet row unot awul */

        Customer existCustomerByNic = customerDao.getByNic(customer.getNic());
        if (existCustomerByNic != null && existCustomerByNic.getId() != customer.getId())
        // && existCustomerByNic.getId()!=customer.getId() change euna ekai existing
        // ekai same record the blnw
        // update unat psse value eka duplicate ek wenna puluywn nisa
        {
            return "Update Unsuccessfull !\n Entered Nic : " + customer.getNic() + " Already Exists..!";
        }

        // Check duplication using Email value

        /*frontenmd eken ewan username ekt adala username ekk backend table eke tiynwd blnwa query ekkin 
        eka tiynwnm e tiyna eke id ekai dn update krl ewn eke id amanananm awul equal nm awulk naa mokd same row ek ethok
        differet row unot awul */
            
        Customer existCustomerByEmail = customerDao.getByEmail(customer.getEmail());// customer.getEmail() is frontend
                                                                                    // email
        if (existCustomerByEmail != null && existCustomerByEmail.getId() != customer.getId()) {
            return "Update Unsuccessfull !\n Entered Email : " + customer.getEmail() + " Already Exists..!";
        }

        try {
            // set auto added data
            customer.setUpdated_datetime(LocalDateTime.now());// set value for addeddate time
            customer.setUpdated_user_id(userDao.getByUserName(auth.getName()).getId());

            // save operator
            customerDao.save(customer);
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
    @DeleteMapping(value = "/customer/delete")
    public String deleteCustomer(@RequestBody Customer customer) {// customer is the frontend object(has a primary key)
        // check authorization

        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        Privilege usrPrivilege=userPrivilegeController.getPrivilegeByUserModule(auth.getName(), 
        "Customer");

        if (usrPrivilege.getDelete_privilege()) {
            
        // Check exist
        if (customer.getId() == null) {
            return "Delete Unsuccessfull ! Customer Doesn't Exist...";// check there is an id of the send
        }

        Customer extCustomerById = customerDao.getReferenceById(customer.getId());// check the record is in the db or
                                                                                  // not
        if (extCustomerById == null) {
            return "Delete Unsuccessfull ! Customer Doesn't Exist...";
        }

        try {
            // set auto added data
            extCustomerById.setDeleted_datetime(LocalDateTime.now());// set value for addeddate time
            extCustomerById.setDeleted_user_id(userDao.getByUserName(auth.getName()).getId());
            extCustomerById.setCus_no(customerDao.generateNextCusNo());// auto generate next cus no

            // update operator
            extCustomerById.setCustomer_status_id(customerStatusDao.getReferenceById(3));// id=3 is Deleted state in Customer
                                                                                    // Status Table

            customerDao.save(extCustomerById);

            // customerDao.delete(customer); to delete record from the database

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
