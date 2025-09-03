package lk.upalisupermarket.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import lk.upalisupermarket.dao.QuotationRequestStatusDao;
import lk.upalisupermarket.entity.QuotationRequestStatus;



@RestController//api implement krn mapping siyalla servlet container ekt add wenne me annotation eken
public class QuotationRequestStatusController {

    @Autowired// generate instance
    private QuotationRequestStatusDao quotationRequestStatusDao;

    // mapping to return puchaseorder status data(URL-->"/quotationrequeststatus/findall")
    @GetMapping(value = "/quotationrequeststatus/findall",produces = "application/json")

    public List<QuotationRequestStatus>findAllData(){
     return quotationRequestStatusDao.findAll();
    }
}
