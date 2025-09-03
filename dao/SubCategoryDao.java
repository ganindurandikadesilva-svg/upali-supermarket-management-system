package lk.upalisupermarket.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import lk.upalisupermarket.entity.SubCategory;



public interface SubCategoryDao  extends JpaRepository<SubCategory,Integer>{

    //query to get subcategory by category id
    //sc.item_category_id.id since category id is a object in subcategory table so need to access the id of that
    @Query(value = "SELECT sc FROM SubCategory sc where sc.item_category_id.id=?1")
    public List<SubCategory> byCategory(Integer categoryid);//its automatically adds the output of the query to this functions body

    
    @Query(value ="SELECT sc FROM SubCategory sc WHERE sc.name = ?1 AND sc.item_category_id.id = ?2")
    public SubCategory getSubNameByCategory(String name, Integer id);
}
