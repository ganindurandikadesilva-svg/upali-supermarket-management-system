package lk.upalisupermarket.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


import lk.upalisupermarket.dao.LoyaltyPointsDao;
import lk.upalisupermarket.entity.LoyaltyPoints;


@RestController
public class LoyaltyPointsController {

    @Autowired // generate instance
    private LoyaltyPointsDao loyaltyPointsDao;

    // mapping to return loyaltypoints data(URL-->"/loyaltypoints/findall")
    @GetMapping(value = "/loyaltypoints/findall",produces = "application/json")
    
    public List<LoyaltyPoints> findAllData(){
        return loyaltyPointsDao.findAll();
    }
  
    
    // mapping to return loyalty dicounts based on invoice amount and customerloyaltypoints(URL-->""/loyalty/listbypointsandtotalamount/{loyaltypoints}/{invoiceamount}")
    @GetMapping(value = "/loyalty/listbypointsandtotalamount/{loyaltypoints}/{invoiceamount}",produces = "application/json")
    
    public List<LoyaltyPoints> discountbyloyaltyPointsandAmount(@PathVariable("loyaltypoints") BigDecimal loyaltypoints,@PathVariable("invoiceamount") BigDecimal invoiceamount){
        return loyaltyPointsDao.byLoyaltyPointsandAmount(loyaltypoints,invoiceamount);//call the function in brand dao
    }
}
