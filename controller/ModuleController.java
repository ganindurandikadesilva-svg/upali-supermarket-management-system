package lk.upalisupermarket.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lk.upalisupermarket.dao.ModuleDao;
import lk.upalisupermarket.entity.Module;

@RestController
public class ModuleController {

    @Autowired // generate instance
    private ModuleDao moduleDao;

    // mapping to return designation data(URL-->"/role/findall")
    @GetMapping(value = "/module/findall",produces = "application/json")
    
    public List<Module> findAllData(){
        return moduleDao.findAll();
    }
}
