package lk.upalisupermarket.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import lk.upalisupermarket.dao.UserDao;
import lk.upalisupermarket.entity.User;




@RestController// api implement krn mapping siyalla servlet container ekt add wenne me
public class ReportUIController {

  @Autowired // generate instance
  private UserDao userDao;


   // mapping to return report ui(URL-->"/report")
  @RequestMapping(value = "/report")
  public ModelAndView loadReportPage() {

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();// get authentication object
    User logUser=userDao.getByUserName(auth.getName());
    ModelAndView reportPage = new ModelAndView();

    reportPage.setViewName("report.html");
    reportPage.addObject("title", "Upali Supermarket-Report Management");// set the title for page
    reportPage.addObject("loggedUserName", auth.getName());// get username of the logged user
    
    reportPage.addObject("loggedUserPhoto", logUser.getUser_photo());//get userphoto of the logged user
    return reportPage;
  }
  

  // mapping to return report ui(URL-->"/purchasepaymentreport")
  @RequestMapping(value = "/purchasepaymentreport")
  public ModelAndView loadPurchasePaymentReportPage() {

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();// get authentication object
    User logUser=userDao.getByUserName(auth.getName());
    ModelAndView reportPage = new ModelAndView();

    reportPage.setViewName("purchasereport.html");
    reportPage.addObject("title", "Upali Supermarket-Purchase Payment Report Management");// set the title for page
    reportPage.addObject("loggedUserName", auth.getName());// get username of the logged user
    
    reportPage.addObject("loggedUserPhoto", logUser.getUser_photo());//get userphoto of the logged user
    return reportPage;
  }
  
    
    

      // mapping to return report ui(URL-->"/purchasepaymentreport")
  @RequestMapping(value = "/employeereport")
  public ModelAndView loadEmployeeReportPage() {

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();// get authentication object
    User logUser=userDao.getByUserName(auth.getName());
    ModelAndView reportPage = new ModelAndView();

    reportPage.setViewName("employeereport.html");
    reportPage.addObject("title", "Upali Supermarket-Employee Report Management");// set the title for page
    reportPage.addObject("loggedUserName", auth.getName());// get username of the logged user
    
    reportPage.addObject("loggedUserPhoto", logUser.getUser_photo());//get userphoto of the logged user
    return reportPage;
  }
  
}
