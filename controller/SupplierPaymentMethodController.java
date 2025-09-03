package lk.upalisupermarket.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lk.upalisupermarket.dao.SupplierPaymentMethodDao;
import lk.upalisupermarket.entity.SupplierPaymentMethod;



@RestController//api implement krn mapping siyalla servlet container ekt add wenne me annotation eken
public class SupplierPaymentMethodController {

    @Autowired// generate instance
    private SupplierPaymentMethodDao supplierPaymentMethodDao;

    // mapping to return supplierpaymentmethod data(URL-->"/supplierpaymentmethod/findall")
    @GetMapping(value = "/supplierpaymentmethod/findall",produces = "application/json")

    public List<SupplierPaymentMethod>findAllData(){
     return supplierPaymentMethodDao.findAll();
    }
}
