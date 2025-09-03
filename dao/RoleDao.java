package lk.upalisupermarket.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import lk.upalisupermarket.entity.Role;

public interface RoleDao  extends JpaRepository<Role,Integer>{

    //Query to return roles without admin -->used in user form-->roles
    @Query(value = "Select r from Role r where r.name<>'Admin'")
    List<Role>rolesWithoutAdmin();


}
