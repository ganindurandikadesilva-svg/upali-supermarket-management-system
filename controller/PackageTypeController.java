package lk.upalisupermarket.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lk.upalisupermarket.dao.PackageTypeDao;
import lk.upalisupermarket.entity.PackageType;


@RestController
public class PackageTypeController {

    @Autowired // generate instance
    private PackageTypeDao packageTypeDao;

    // mapping to return packagetype data(URL-->"/packagetype/findall")
    @GetMapping(value = "/packagetype/findall",produces = "application/json")
    
    public List<PackageType> findAllData(){
        return packageTypeDao.findAll();
    }
  
}
