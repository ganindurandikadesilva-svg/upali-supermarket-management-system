package lk.upalisupermarket.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;



import lk.upalisupermarket.dao.SupplierHasItemDao;
import lk.upalisupermarket.entity.SupplierHasItem;

@RestController
public class SupplierHasItemController {

    @Autowired // generate instance
    private SupplierHasItemDao supplierHasItemDao;

    // mapping to return supplierhasitem data(URL-->"/supplierhasitem/findall")
    @GetMapping(value = "/supplierhasitem/findall",produces = "application/json")
    
    public List<SupplierHasItem> findAllData(){
        return supplierHasItemDao.findAll();
    }
  
}
