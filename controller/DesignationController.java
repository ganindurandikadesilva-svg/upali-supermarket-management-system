package lk.upalisupermarket.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


import lk.upalisupermarket.dao.DesignationDao;
import lk.upalisupermarket.entity.Designation;

@RestController
public class DesignationController {

    @Autowired // generate instance
    private DesignationDao designationDao;

    // mapping to return designation data(URL-->"/designation/findall")
    @GetMapping(value = "/designation/findall",produces = "application/json")
    
    public List<Designation> findAllData(){
        return designationDao.findAll();
    }
  
}
