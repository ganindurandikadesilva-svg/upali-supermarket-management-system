package lk.upalisupermarket.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lk.upalisupermarket.dao.ReportDao;
import lk.upalisupermarket.entity.Employee;

@RestController // api implement krn mapping siyalla servlet container ekt add wenne me
public class ReportDataController {

    @Autowired
    private ReportDao reportDao;




    // mapping to return items list that suppliers doesnt supply(URL-->"purchasepaymentreport/bysdedtype?startdate=&enddate=&type=") to purchase report form
     @GetMapping(value = "/purchasepaymentreport/bysdedtype",params = {"startdate","enddate","type"}, produces = "application/json")
     public String[][] getPurchasePaymentReport(@RequestParam("startdate") String startdate,@RequestParam("enddate") String enddate,
     @RequestParam("type") String type) {
 
       
            if (type.equals("Weekly")) {
                 return reportDao.getPurchasePayementsforWeek(startdate,enddate);
            }
            if (type.equals("Monthly")) {
                  // returning the values 
                 return reportDao.getPurchasePayementsgivenMonth(startdate,enddate);
            }
             return null;
         
 
     }
   

      // mapping to return (URL-->"purchasepaymentreport/bysixmonths") to purchase report form
     @GetMapping(value = "/purchasepaymentreport/bysixmonths",produces = "application/json")
     public String[][] getPurchasePaymentReportsixMonth() {
 
             return reportDao.getPurchasePayementsPrevioussixMonths();
         
 
     }
   


     
  // mapping to return employees based on status and designation
  // supplier and required date(URL-->"/employeereport/listbystatusanddesignation/?statusid?designationid") to quotation request form
  @GetMapping(value = "/employeereport/listbystatusanddesignation/{statusid}/{designationid}", produces = "application/json")
  public List<Employee> getEmployeebystatusandDesignation(@PathVariable("statusid") Integer statusid,@PathVariable("designationid") Integer designationid) {

    // Check user authentication and authorization
  
      return reportDao.getEmployeesbyStatusandDesignation(statusid,designationid);
  
  }

}
