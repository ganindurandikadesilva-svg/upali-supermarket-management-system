package lk.upalisupermarket.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import lk.upalisupermarket.dao.BrandDao;
import lk.upalisupermarket.entity.Brand;


@RestController
public class BrandController {

    @Autowired // generate instance
    private BrandDao brandDao;

    // mapping to return brand data(URL-->"/brand/findall")
    @GetMapping(value = "/brand/findall",produces = "application/json")
    
    public List<Brand> findAllData(){
        return brandDao.findAll();
    }
  
    
    // mapping to return brand data(URL-->"/brand/bycategory/{categoryid}")
    @GetMapping(value = "/brand/bycategory/{categoryid}",produces = "application/json")
    
    public List<Brand> brandbyCategory(@PathVariable("categoryid") Integer categoryid){
        return brandDao.byCategory(categoryid);//call the function in brand dao
    }
}
