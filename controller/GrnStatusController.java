package lk.upalisupermarket.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lk.upalisupermarket.dao.GrnStatusDao;
import lk.upalisupermarket.entity.GrnStatus;




@RestController//api implement krn mapping siyalla servlet container ekt add wenne me annotation eken
public class GrnStatusController {

    @Autowired// generate instance
    private GrnStatusDao grnStatusDao;

    // mapping to return grn status data(URL-->"/grnstatus/findall")
    @GetMapping(value = "/grnstatus/findall",produces = "application/json")

    public List<GrnStatus>findAllData(){
     return grnStatusDao.findAll();
    }
}
