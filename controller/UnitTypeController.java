package lk.upalisupermarket.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lk.upalisupermarket.dao.UnitTypeDao;
import lk.upalisupermarket.entity.UnitType;


@RestController
public class UnitTypeController {

    @Autowired // generate instance
    private UnitTypeDao unitTypeDao;

    // mapping to return unittype data(URL-->"/unittype/findall")
    @GetMapping(value = "/unittype/findall",produces = "application/json")
    
    public List<UnitType> findAllData(){
        return unitTypeDao.findAll();
    }
  
}
