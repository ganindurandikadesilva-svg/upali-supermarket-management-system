package lk.upalisupermarket.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


import lk.upalisupermarket.dao.CustomerPaymentMethodDao;
import lk.upalisupermarket.entity.CustomerPaymentMethod;




@RestController//api implement krn mapping siyalla servlet container ekt add wenne me annotation eken
public class CustomerPaymentMethodController {

    @Autowired// generate instance
    private CustomerPaymentMethodDao customerPaymentMethodDao;

    // mapping to return supplierpaymentmethod data(URL-->"/supplierpaymentmethod/findall")
    @GetMapping(value = "/customerpaymentmethod/findall",produces = "application/json")

    public List<CustomerPaymentMethod>findAllData(){
     return customerPaymentMethodDao.findAll();
    }
}
