package lk.upalisupermarket.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import lk.upalisupermarket.entity.Brand;

public interface BrandDao extends JpaRepository<Brand,Integer>{

    //query to get Brand by category id
    //bhc.item_category_id.id returens an object so bhc.item_category_id.id.needed to access the value
    @Query(value = "SELECT b FROM Brand as b where b.id in (select bhc.item_brand_id.id from BrandHasCategory as bhc where bhc.item_category_id.id=?1)  ")
    public List<Brand>byCategory(Integer categoryid);
}
