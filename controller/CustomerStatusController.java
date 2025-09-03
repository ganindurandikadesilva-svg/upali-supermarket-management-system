package lk.upalisupermarket.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lk.upalisupermarket.dao.CustomerStatusDao;
import lk.upalisupermarket.entity.CustomerStatus;



@RestController//api implement krn mapping siyalla servlet container ekt add wenne me annotation eken
public class CustomerStatusController {

    @Autowired// generate instance
    private CustomerStatusDao customerStatusDao;

    // mapping to return customerstatus data(URL-->"/customerstatus/findall")
    @GetMapping(value = "/customerstatus/findall",produces = "application/json")

    public List<CustomerStatus>findAllData(){
     return customerStatusDao.findAll();
    }
}
