package lk.upalisupermarket.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import lk.upalisupermarket.entity.Category;


public interface CategoryDao  extends JpaRepository<Category,Integer>{

}
