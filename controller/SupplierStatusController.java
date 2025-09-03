package lk.upalisupermarket.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lk.upalisupermarket.dao.SupplierStatusDao;
import lk.upalisupermarket.entity.SupplierStatus;



@RestController//api implement krn mapping siyalla servlet container ekt add wenne me annotation eken
public class SupplierStatusController {

    @Autowired// generate instance
    private SupplierStatusDao supplierStatusDao;

    // mapping to return supplierstatus data(URL-->"/supplierstatus/findall")
    @GetMapping(value = "/supplierstatus/findall",produces = "application/json")

    public List<SupplierStatus>findAllData(){
     return supplierStatusDao.findAll();
    }
}
