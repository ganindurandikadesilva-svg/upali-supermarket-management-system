package lk.upalisupermarket.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import lk.upalisupermarket.entity.User;

public interface UserDao extends JpaRepository<User,Integer> {

       //Query to check db eke fronened eken ewn username ekt  same  wena record tiynwd blnw eyw select kri
    @Query(value = "Select u from User u where u.username=?1")
    User getByUserName(String username);//its automatically adds the output of the query to this functions body

     @Query(value = "Select e from User e where e.password=?1")//JP Query
    //query to check duplicates
    //?1 is password parameter(first parameter) in the following function
    User getByPassword(String password);//its automatically adds the output of the query to this functions body

    //logged wuna user and admin wa natuwa user list eka return krn query eka
    @Query(value = "Select u from User u where u.username<>?1 and u.username<>'Admin' order by u.id desc")
    List<User>findAll(String username);
}
