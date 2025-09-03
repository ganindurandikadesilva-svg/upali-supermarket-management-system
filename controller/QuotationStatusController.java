package lk.upalisupermarket.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import lk.upalisupermarket.dao.QuotationStatusDao;
import lk.upalisupermarket.entity.QuotationStatus;



@RestController//api implement krn mapping siyalla servlet container ekt add wenne me annotation eken
public class QuotationStatusController {

    @Autowired// generate instance
    private QuotationStatusDao quotationStatusDao;

    // mapping to return quotation status data(URL-->"/quotationstatus/findall")
    @GetMapping(value = "/quotationstatus/findall",produces = "application/json")

    public List<QuotationStatus>findAllData(){
     return quotationStatusDao.findAll();
    }
}
