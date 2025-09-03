package lk.upalisupermarket.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lk.upalisupermarket.dao.EmployeeStatusDao;
import lk.upalisupermarket.entity.EmployeeStatus;


@RestController//api implement krn mapping siyalla servlet container ekt add wenne me annotation eken
public class EmployeeStatusController {

    @Autowired// generate instance
    private EmployeeStatusDao employeeStatusDao;

    // mapping to return designation data(URL-->"/employeestatus/findall")
    @GetMapping(value = "/employeestatus/findall",produces = "application/json")

    public List<EmployeeStatus>findAllData(){
     return employeeStatusDao.findAll();
    }
}
