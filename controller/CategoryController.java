package lk.upalisupermarket.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lk.upalisupermarket.dao.CategoryDao;
import lk.upalisupermarket.entity.Category;


@RestController
public class CategoryController {

    @Autowired // generate instance
    private CategoryDao categoryDao;

    // mapping to return category data(URL-->"/category/findall")
    @GetMapping(value = "/category/findall",produces = "application/json")
    
    public List<Category> findAllData(){
        return categoryDao.findAll();
    }
  
}
