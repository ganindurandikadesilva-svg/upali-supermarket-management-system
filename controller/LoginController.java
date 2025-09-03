package lk.upalisupermarket.controller;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import lk.upalisupermarket.dao.RoleDao;
import lk.upalisupermarket.dao.UserDao;
import lk.upalisupermarket.entity.Role;
import lk.upalisupermarket.entity.User;

@RestController // api implement krn mapping siyalla servlet container ekt add wenne me
                // annotation eken
public class LoginController {

    @Autowired // generate instance
    private UserDao userDao;

    @Autowired // generate instance
    private RoleDao roleDao;

    @Autowired // generate instance
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    // mapping to return login ui(URL-->"/login")
    @RequestMapping(value = "/login")
    public ModelAndView loadLoginPage() {

        ModelAndView loginPage = new ModelAndView();
        loginPage.addObject("title","Upali Supermarket-Login");//set the title for page
        loginPage.setViewName("login.html");
        return loginPage;

    }

    // mapping for dashboard.html url-->("/dashboard" or "/") Dashboard

    @RequestMapping(value = { "/dashboard", "/" })
    public ModelAndView dashboardPageUI() {

        Authentication auth= SecurityContextHolder.getContext().getAuthentication();//get authentication object
        User logUser=userDao.getByUserName(auth.getName());

        ModelAndView dashboardPage = new ModelAndView();// constructor
        dashboardPage.addObject("title","Upali Supermarket-Dashboard");//set the title for page
        dashboardPage.addObject("loggedUserName", auth.getName());//get username of the logged user

        dashboardPage.addObject("loggedUserPhoto", logUser.getUser_photo());//get userphoto of the logged user
        dashboardPage.setViewName("dashboard.html");
        return dashboardPage;
    }

    // mapping to return error ui(URL-->"/errorpage")
    @RequestMapping(value = "/errorpage")
    public ModelAndView loadErrorPage() {

        ModelAndView errorPage = new ModelAndView();
        errorPage.addObject("title","ERROR!- Page Not Found.");//set the title for page
        errorPage.setViewName("errorpage.html");
        return errorPage;

    }

    // createadmin
    // mapping to return error ui(URL-->"/errorpage")
    @RequestMapping(value = "/createadmin")
    public ModelAndView loadCreateAdmin() {

        User extAdminUser = userDao.getByUserName("Admin");
        if (extAdminUser == null) {
            User adminUser = new User();
            adminUser.setUsername("Admin");
            adminUser.setPassword(bCryptPasswordEncoder.encode("12345"));
            adminUser.setEmail("admin123@gmail.com");
            adminUser.setStatus(true);
            adminUser.setAdded_datetime(LocalDateTime.now());

            // create a set of roles
            Set<Role> roles = new HashSet<>();
            Role adminRole = roleDao.getReferenceById(1);
            roles.add(adminRole);
            adminUser.setRoles(roles);

            userDao.save(adminUser);// save
        }

        ModelAndView loginPage = new ModelAndView();
        loginPage.setViewName("login.html");
        return loginPage;

    }

}
