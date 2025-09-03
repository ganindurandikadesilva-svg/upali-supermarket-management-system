package lk.upalisupermarket.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import lk.upalisupermarket.dao.UserDao;
import lk.upalisupermarket.entity.Privilege;
import lk.upalisupermarket.entity.User;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RequestMapping(value = "/user")
@RestController // api implement krn mapping siyalla servlet container ekt add wenne me
                // annotation eken
public class UserController {

    @Autowired
    private UserDao userDao;// create an instance of User Dao to access the data

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserPrivilegeController userPrivilegeController;// create an instance of UserPrivilegeContoller to access
                                                            // the data for secuirity
    // privileged user kenek da kiyl blnna

    // mapping to return user ui(URL-->"/user")
    @RequestMapping()
    public ModelAndView loadUserPage() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();// get authentication object
        User logUser=userDao.getByUserName(auth.getName());
        ModelAndView userPage = new ModelAndView();
        userPage.setViewName("user.html");

        userPage.addObject("title", "Upali Supermarket-User Management");// set the title for page
        userPage.addObject("loggedUserName", auth.getName());// get username of the logged user
        
        userPage.addObject("loggedUserPhoto", logUser.getUser_photo());//get userphoto of the logged user
        return userPage;
    }

    // mapping to return all user data(URL-->"user/findall")
    @GetMapping(value = "/findall", produces = "application/json")
    public List<User> findAllData() {
        // Check user authentication and authorization
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();// Authentication object
        Privilege usrPrivilege = userPrivilegeController.getPrivilegeByUserModule(auth.getName(),
                "User");// Module name should be checked with the database module table

        if (usrPrivilege.getSelect_privilege()) {
            return userDao.findAll(auth.getName());// returning the values in the user table without
            // admin and logged user due to the query written in userdao
        } else {
            return new ArrayList<>();// returns an empty array since the user has no privilege to see the data
        }
    }

    // mapping to delete user data(URL-->"user/delete")
    @DeleteMapping(value = "/delete")
    public String deleteUser(@RequestBody User user) {
        // Check user authentication and authorization
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();// Authentication object
        Privilege usrPrivilege = userPrivilegeController.getPrivilegeByUserModule(auth.getName(),
                "User");// Module name should be checked with the database module table

        if (usrPrivilege.getDelete_privilege()) {
            // Check exist
            if (user.getId() == null) {
                return "Delete Unsuccessfull ! User Doesn't Exist...";// check there is an id of the send
            }
            User extUser = userDao.getReferenceById(user.getId());// check the record is in the db or not
            if (extUser == null) {
                return "Delete Unsuccessfull ! User Doesn't Exist...";
            }
            try {
                // set auto added data

                extUser.setStatus(false);// change the user status
                extUser.setDelete_datetime(LocalDateTime.now());

                userDao.save(extUser);// save
                return "OK";// frontend recieving ok
            } catch (Exception e) {
                return "Delete Unsucessfull...!" + e.getMessage();
            }
        } else {
            return "Delete Unsucessfull...!\nYou Have No Permission to Delete";// since the user has no privilege to
                                                                               // delete the data
        }

    }

    // insert
    @PostMapping(value = "/insert")
    public String insertUser(@RequestBody User user) {
        // Check user authentication and authorization
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();// Authentication object
        Privilege usrPrivilege = userPrivilegeController.getPrivilegeByUserModule(auth.getName(),
                "User");// Module name should be checked with the database module table

        if (usrPrivilege.getInsert_privilege()) {
            // Check duplication using User Name
            /*
             * User existUserByUserName = userDao.getByUserName(user.getUsername());
             * if (existUserByUserName != null) {
             * return "Save Unsuccessfull !\n Entered User Name : " + user.getUsername() +
             * " Already Exists..!";
             * }
             */
            /*
             * // Check duplication using Email
             * User existUserByEmail = userDao.ge(user.getEmail());
             * if (existUserByEmail != null) {
             * return "Save Unsuccessfull !\n Entered User Name : " + user.getEmail() +
             * " Already Exists..!";
             * }
             */
            try {
                // set auto added data

                user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));// encrypted password
                user.setAdded_datetime(LocalDateTime.now());

                userDao.save(user);// save
                return "OK";// frontend recieving ok
            } catch (Exception e) {
                return "Save Unsucessfull...!" + e.getMessage();
            }
        } else {
            return "Save Unsucessfull...!\nYou Have No Permission to Insert";// since the user has no privilege to
                                                                             // delete the data
        }

    }

    // update
    @PutMapping(value = "/update")
    public String updateUser(@RequestBody User user) {

        // Check user authentication and authorization
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();// Authentication object
        Privilege usrPrivilege = userPrivilegeController.getPrivilegeByUserModule(auth.getName(),
                "User");// Module name should be checked with the database module table

        if (usrPrivilege.getUpdate_privilege()) {
            // Check duplication using User Name
             /*   frontenmd eken ewan username ekt adala username ekk backend table eke tiynwd blnwa query ekkin 
            eka tiynwnm e tiyna eke id ekai dn update krl ewn eke id amanananm awul equal nm awulk naa mokd same row ek ethok
            differet row unot awul */

            User existUserByUserName = userDao.getByUserName(user.getUsername());
            if (existUserByUserName != null && existUserByUserName.getId() != user.getId())
            // && existEmployeeByNic.getId()!=employee.getId() change euna ekai existing
            // ekai same record the blnw
            // update unat psse value eka duplicate ek wenna puluywn nisa
            {
                return "Update Unsuccessfull !\n Entered User Name : " + user.getUsername() + " Already Exists..!";
            }

            // Check duplication using Password
            // getByPassword kiyna dao eke query ekt frontend password ek ywnwa check krnn
            /*   frontenmd eken ewan username ekt adala username ekk backend table eke tiynwd blnwa query ekkin 
            eka tiynwnm e tiyna eke id ekai dn update krl ewn eke id amanananm awul equal nm awulk naa mokd same row ek ethok
            differet row unot awul */
            
            User existUserByPassword = userDao.getByPassword(user.getPassword());
            if (existUserByPassword != null && existUserByPassword.getId()!=user.getId()) {
                return "Save Unsuccessfull !\n Entered Password : " + user.getPassword() + " Already Exists..!";
            }

            try {
                // set auto added data

                user.setUpdated_datetime(LocalDateTime.now());
                userDao.save(user);// save

                return "OK";// frontend recieving ok
            } catch (Exception e) {
                return "Update Unsucessfull...!" + e.getMessage();
            }
        } else {
            return "Update Unsucessfull...!\nYou Have No Permission to Update";// since the user has no privilege to
                                                                               // delete the data
        }
    }
}
