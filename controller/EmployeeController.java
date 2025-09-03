package lk.upalisupermarket.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import lk.upalisupermarket.dao.EmployeeDao;
import lk.upalisupermarket.dao.EmployeeStatusDao;
import lk.upalisupermarket.dao.RoleDao;
import lk.upalisupermarket.dao.UserDao;
import lk.upalisupermarket.entity.Employee;
import lk.upalisupermarket.entity.Privilege;
import lk.upalisupermarket.entity.Role;
import lk.upalisupermarket.entity.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class EmployeeController {

    @Autowired // generate instance
    private UserDao userDao;

    @Autowired
    private UserPrivilegeController userPrivilegeController;// create an instance of UserPrivilegeContoller to access
                                                            // the data for secuirity
    // privileged user kenek da kiyl blnna

    @Autowired // generate instance
    private EmployeeDao employeeDao;

    @Autowired // generate instance
    private EmployeeStatusDao employeeStatusDao;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private RoleDao roleDao;

    // mapping to return employee ui(URL-->"/employee")
    @RequestMapping(value = "/employee")
    public ModelAndView loadEmployeePage() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();// get authentication object
        User logUser=userDao.getByUserName(auth.getName());

        ModelAndView employeePage = new ModelAndView();

        employeePage.setViewName("employee.html");
        employeePage.addObject("title", "Upali Supermarket-Employee Management");// set the title for page
        employeePage.addObject("loggedUserName", auth.getName());// get username of the logged user
        
        employeePage.addObject("loggedUserPhoto", logUser.getUser_photo());//get userphoto of the logged user
        return employeePage;
    }

    // mapping to return all employee data(URL-->"employee/findall")
    @GetMapping(value = "/employee/findall", produces = "application/json")
    public List<Employee> findAllData() {

        // Check user authentication and authorization

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Privilege usrPrivilege = userPrivilegeController.getPrivilegeByUserModule(auth.getName(),
                "Employee");// Module name should be checked with the database module table

        if (usrPrivilege.getSelect_privilege()) {
            // returning the values in the Employee table
            return employeeDao.findAll(Sort.by(Direction.DESC, "id"));//sort decending from empid
        } else {
            return new ArrayList<>();// returns an empty array since the user has no privilege to see the data
        }

    }

    // insert
    @PostMapping(value = "/employee/insert") // employee is the frontend object(no primary key since its now getting
                                             // inserted)
    public String saveEmployeeData(@RequestBody Employee employee) { // Type should be Employee and object sent from
                                                                     // frontend is employee

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();// get authentication object
        User loggedUser = userDao.getByUserName(auth.getName());// logged user object

        Privilege usrPrivilege = userPrivilegeController.getPrivilegeByUserModule(auth.getName(),
                "Employee");//get Employee object 

        // check authorization
        // Check whether the user has permission to insert

        if (usrPrivilege.getInsert_privilege()) {
            // check duplication

            // Check duplication using NIC value
            Employee existEmployeeByNic = employeeDao.getByNic(employee.getNic());
            if (existEmployeeByNic != null) {
                return "Save Unsuccessfull !\n Entered Nic : " + employee.getNic() + " Already Exists..!";
            }

            // Check duplication using Email value
            Employee existEmployeeByEmail = employeeDao.getByEmail(employee.getEmail());// employee.getEmail() is
                                                                                        // frontend email
            if (existEmployeeByEmail != null) {
                return "Save Unsuccessfull !\n Entered Email : " + employee.getEmail() + " Already Exists..!";
            }

            try {
                // set auto added data
                employee.setAdded_datetime(LocalDateTime.now());// set value for addeddate time
                employee.setAdded_user_id(loggedUser.getId());// database eke load wela tiyn nama withri penanne
                employee.setEmp_no(employeeDao.generateNextEmpNo());// auto generate next emp no

                // save operator
                employeeDao.save(employee);
                // dependencies
                if (employee.getDesignaton_id().getUser_account()) {// creating a new user account when creating a
                                                                    // employee
                    User user = new User();
                    user.setUsername(employee.getEmp_no());// Set EmployeeNo as Username
                    if (employee.getEmp_photo()!=null) {
                        user.setUser_photo(employee.getEmp_photo());//employee photo will be saved as user photo for new employee
                    }
                    user.setPassword(bCryptPasswordEncoder.encode(employee.getNic()));// set Nic as Password
                    user.setEmail(employee.getEmail());
                    user.setStatus(true);
                    user.setEmployee_id(employeeDao.getByNic(employee.getNic()));//set employee id of the user account (foreign key)
                    user.setAdded_datetime(LocalDateTime.now());

                    // create a set of roles
                    Set<Role> roles = new HashSet<>();
                    Role role = roleDao.getReferenceById(employee.getDesignaton_id().getRole_id());
                    roles.add(role);
                    user.setRoles(roles);

                    userDao.save(user);// save
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
    @PutMapping(value = "/employee/update")
    public String updateEmployee(@RequestBody Employee employee) {

        // check authentication and authorization
        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        Privilege usrPrivilege=userPrivilegeController.getPrivilegeByUserModule(auth.getName(), 
        "Employee");

        if (usrPrivilege.getUpdate_privilege()) {
            // Check exist
        if (employee.getId() == null) {
            return "Update Unsuccessfull ! Employee Doesn't Exist...";// check there is an id of the send
        }

        Employee extById = employeeDao.getReferenceById(employee.getId());// check the record is in the db or not
        if (extById == null) {
            return "Update Unsuccessfull ! Employee Doesn't Exist...";
        }

        // check duplication
        /*   frontenmd eken ewan username ekt adala username ekk backend table eke tiynwd blnwa query ekkin 
        eka tiynwnm e tiyna eke id ekai dn update krl ewn eke id amanananm awul equal nm awulk naa mokd same row ek ethok
        differet row unot awul */

        Employee existEmployeeByNic = employeeDao.getByNic(employee.getNic());
        if (existEmployeeByNic != null && existEmployeeByNic.getId() != employee.getId())
        // && existEmployeeByNic.getId()!=employee.getId() change euna ekai existing
        // ekai same record the blnw
        // update unat psse value eka duplicate ek wenna puluywn nisa
        {
            return "Update Unsuccessfull !\n Entered Nic : " + employee.getNic() + " Already Exists..!";
        }

        // Check duplication using Email value

        /*frontenmd eken ewan username ekt adala username ekk backend table eke tiynwd blnwa query ekkin 
        eka tiynwnm e tiyna eke id ekai dn update krl ewn eke id amanananm awul equal nm awulk naa mokd same row ek ethok
        differet row unot awul */
            
        Employee existEmployeeByEmail = employeeDao.getByEmail(employee.getEmail());// employee.getEmail() is frontend
                                                                                    // email
        if (existEmployeeByEmail != null && existEmployeeByEmail.getId() != employee.getId()) {
            return "Update Unsuccessfull !\n Entered Email : " + employee.getEmail() + " Already Exists..!";
        }

        try {
            // set auto added data
            employee.setUpdated_datetime(LocalDateTime.now());// set value for addeddate time
            employee.setUpdated_user_id(userDao.getByUserName(auth.getName()).getId());

            // save operator
            employeeDao.save(employee);
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
    @DeleteMapping(value = "employee/delete")
    public String deleteEmployee(@RequestBody Employee employee) {// employee is the frontend object(has a primary key)
        // check authorization

        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        Privilege usrPrivilege=userPrivilegeController.getPrivilegeByUserModule(auth.getName(), 
        "Employee");

        if (usrPrivilege.getDelete_privilege()) {
            
        // Check exist
        if (employee.getId() == null) {
            return "Delete Unsuccessfull ! Employee Doesn't Exist...";// check there is an id of the send
        }

        Employee extEmployeeById = employeeDao.getReferenceById(employee.getId());// check the record is in the db or
                                                                                  // not
        if (extEmployeeById == null) {
            return "Delete Unsuccessfull ! Employee Doesn't Exist...";
        }

        try {
            // set auto added data
            extEmployeeById.setDeleted_datetime(LocalDateTime.now());// set value for addeddate time
            extEmployeeById.setDeleted_user_id(1);
            extEmployeeById.setEmp_no(employeeDao.generateNextEmpNo());// auto generate next emp no

            // update operator
            extEmployeeById.setEmp_status_id(employeeStatusDao.getReferenceById(3));// id=3 is Deleted state in Employee
                                                                                    // Status Table

            employeeDao.save(extEmployeeById);

            // employeeDao.delete(employee); to delete record from the database

            // dependencies

            return "OK";// frontend recieving ok

        } catch (Exception e) {

            return "Delete Unsucessfull...!" + e.getMessage();

        }
        } else {
            return "Delete Unsucessfull...!\nYou Have No Permission to Delete" ;
        }

    }


    //mapping to get employees without user accounts
      // mapping to return all employee data(URL-->"employee/employeewithoutuseraccount")
    @GetMapping(value = "/employee/employeewithoutuseraccount", produces = "application/json")
    public List<Employee> findEmployeesWithoutUserAccounts() {

        // Check user authentication and authorization

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Privilege usrPrivilege = userPrivilegeController.getPrivilegeByUserModule(auth.getName(),
                "Employee");// Module name should be checked with the database module table

        if (usrPrivilege.getSelect_privilege()) {
            // returning the values in the Employee table
            return employeeDao.findEmployeeWithoutUserAccount();//sort decending from empid
        } else {
            return new ArrayList<>();// returns an empty array since the user has no privilege to see the data
        }

    }

}
