package lk.upalisupermarket.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import lk.upalisupermarket.dao.PrivilegeDao;
import lk.upalisupermarket.entity.Privilege;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController // api implement krn mapping siyalla servlet container ekt add wenne me
                // annotation eken
public class PrivilegeController {

  @Autowired // create an instance of Privilege Dao to access the data
  private PrivilegeDao privilegeDao;

  @Autowired
  private UserPrivilegeController userPrivilegeController;// create an instance of UserPrivilegeContoller to access the data for secuirity

  // mapping to return privilege ui(URL-->"/privilege")
  @RequestMapping(value = "/privilege")
  public ModelAndView loadPrivilegePage() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();// get authentication object

    ModelAndView privilegePage = new ModelAndView();
    privilegePage.addObject("title", "Upali Supermarket-Privilege Management");// set the title for page
    privilegePage.addObject("loggedUserName", auth.getName());// get username of the logged user
    privilegePage.setViewName("Privilege.html");

    return privilegePage;
  }

  // mapping to return employee ui(URL-->"/privilege/findall")
  @GetMapping(value = "/privilege/findall", produces = "application/json")
  public List<Privilege> findAllData() {

     // Check user authentication and authorization

     Authentication auth = SecurityContextHolder.getContext().getAuthentication();// Authentication object
     Privilege usrPrivilege = userPrivilegeController.getPrivilegeByUserModule(auth.getName(),
         "Privilege");// privilege Object
    
    if (usrPrivilege.getSelect_privilege()) {
      return privilegeDao.findAll(Sort.by(Direction.DESC, "id"));// returning the values in the privilege table
    } else {
      return new ArrayList<>();//returns an empty array since the user has no privilege to see the data
    }     

  }

  // insert
  @PostMapping("/privilege/insert") // privilege is the frontend object(no primary key since its now getting inserted)
  public String savePrivilegeData(@RequestBody Privilege privilege) // Type should be Privilege and object sent from frontend is privilege
  {
    // Check authorization
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();// Authentication object
    Privilege usrPrivilege = userPrivilegeController.getPrivilegeByUserModule(auth.getName(),
        "Privilege");// privilege Object

    // Check whether the user has permission to insert
    if (usrPrivilege.getInsert_privilege()) {

      // check duplicates using role id and module id
      Privilege existPrivilege = privilegeDao.getPrivilegeRoleModule(privilege.getRole_id().getId(),
          privilege.getModule_id().getId());

      if (existPrivilege != null) {
        return "Save UnsucessFull..! Privilege Already Exists..!\n";
      }

      try {

        // save operator
        privilegeDao.save(privilege);

        return "OK";// frontend recieving ok
      } catch (Exception e) {

        return "Save UnsucessFull..!\n" + e.getMessage();
      }
    } else {
      return "Save UnsucessFull..!\nYou Have No Permission to Insert";
    }

  }

  // update
  @PutMapping("/privilege/update")
  public String updatePrivilegeData(@RequestBody Privilege privilege) {

    // Check authorization
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();// Authentication object
    Privilege usrPrivilege = userPrivilegeController.getPrivilegeByUserModule(auth.getName(),
        "Privilege");// privilege Object

    // Check whether the user has permission to update
    if (usrPrivilege.getUpdate_privilege()) {
      // check existing
      Privilege existPrivilege = privilegeDao.getPrivilegeRoleModule(privilege.getRole_id().getId(),
          privilege.getModule_id().getId());

      if (existPrivilege != null && existPrivilege.getId() != privilege.getId())
      // update krpu privlege ekai exist wena privilege record eaki same nemed blnw
      // same neme nm duplicate ekk
      {
        return "Save UnsucessFull..! Privilege Already Exists..!\n";
      }

      // check duplicates

      try {

        // update operator
        privilegeDao.save(privilege);
        return "OK";
      } catch (Exception e) {

        return "Update Unsucessfull...!\n" + e.getMessage();
      }

    } else {
      return "Update Unsucessfull...!\nYou Have No Permission to Update" ;
    }

  }

  // Delete
  @DeleteMapping("/privilege/delete")
  public String deletePrivilegeData(@RequestBody Privilege privilege) {

    // Check authorization
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();// Authentication object
    Privilege usrPrivilege = userPrivilegeController.getPrivilegeByUserModule(auth.getName(),
        "Privilege");// privilege Object

   

    // Check whether the user has permission to Delete
    if (usrPrivilege.getDelete_privilege()) {

       // check existing

      try {

        // Delete operator
        // changing the privileges to false after delete since there is no status to
        // change like in employee
        privilege.setInsert_privilege(false);
        privilege.setSelect_privilege(false);
        privilege.setUpdate_privilege(false);
        privilege.setDelete_privilege(false);
  
        privilegeDao.save(privilege);
  
        return "OK";
      } catch (Exception e) {
  
        return "Delete Unsucessfull...!\n" + e.getMessage();
      }
  
    } else {
      return "Update Unsucessfull...!\nYou Have No Permission to Delete" ;
    }
   
  }


}
