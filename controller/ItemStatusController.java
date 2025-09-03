package lk.upalisupermarket.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


import lk.upalisupermarket.dao.ItemStatusDao;
import lk.upalisupermarket.entity.ItemStatus;


@RestController
public class ItemStatusController {

    @Autowired // generate instance
    private ItemStatusDao itemStatusDao;

    // mapping to return itemstatus data(URL-->"/itemstatus/findall")
    @GetMapping(value = "/itemstatus/findall",produces = "application/json")
    
    public List<ItemStatus> findAllData(){
        return itemStatusDao.findAll();
    }
  
}
