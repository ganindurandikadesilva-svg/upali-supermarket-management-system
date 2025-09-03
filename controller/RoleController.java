package lk.upalisupermarket.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lk.upalisupermarket.dao.RoleDao;
import lk.upalisupermarket.entity.Role;



@RestController
public class RoleController {

    @Autowired // generate instance
    private RoleDao roleDao;

    // mapping to return all roles data(URL-->"/role/findall")
    @GetMapping(value = "/role/findall",produces = "application/json")
    
    public List<Role> findAllData(){
        return roleDao.findAll();
    }

    
    // mapping to return all  roles without admin(URL-->"/role/findall")
    @GetMapping(value = "/role/rolesWithoutAdmin",produces = "application/json")
    public List<Role> findAllWithountAdmin() {
        return roleDao.rolesWithoutAdmin();
    }
    
  
}
