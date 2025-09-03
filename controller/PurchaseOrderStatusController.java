package lk.upalisupermarket.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import lk.upalisupermarket.dao.PurchaseOrderStatusDao;
import lk.upalisupermarket.entity.PurchaseOrderStatus;



@RestController//api implement krn mapping siyalla servlet container ekt add wenne me annotation eken
public class PurchaseOrderStatusController {

    @Autowired// generate instance
    private PurchaseOrderStatusDao purchaseOrderStatusDao;

    // mapping to return puchaseorder status data(URL-->"/purchaseorderstatus/findall")
    @GetMapping(value = "/purchaseorderstatus/findall",produces = "application/json")

    public List<PurchaseOrderStatus>findAllData(){
     return purchaseOrderStatusDao.findAll();
    }
}
