package lk.upalisupermarket.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import lk.upalisupermarket.entity.Supplier;

public interface SupplierDao extends JpaRepository<Supplier,Integer> {

    @Query(value="SELECT coalesce(concat('SUP',lpad(substring(max(s.supplier_regno),4)+1,5,'0')),'SUP00001') from upali_supermarket.supplier s ;",nativeQuery = true)
     //liynna puluwan jpa query witharai nativequery liynwnm true krnnn onee
  //coalesce eken  krnne default sup no ekk nattm eya ekk hdnwa

    //LPAD(value, length, pad_string) is a function that left-pads a 
  /*   MAX(supplier_regno):

    Finds the highest employee number (e.g., 'I00005').
    SUBSTRING(MAX(supplier_regno), 2):
    
    Extracts the numeric part by removing the leading 'I' (e.g., '00005' → 5).
    SUBSTRING(MAX(supplier_regno), 2) + 1:
    
    Converts the extracted number to an integer and increments it by 1 (e.g., 5 + 1 = 6).
    LPAD(..., 5, '0'):
    
    Ensures the numeric part is always 5 digits long, padding with zeros if necessary (e.g., 6 → '00006').
    CONCAT('I', ...):
    
    Adds the 'I' prefix back to the new number, resulting in 'I00006'. */
    
    String generateNextSupplerRegNo();//its automatically adds the output of the query to this functions body


    //Query to getemail based onthe input email 
    @Query(value="Select s from Supplier s where s.email=?1")
    Supplier getByEmail(String email);
/* 
    @Query(value="Select c from Supplier s where s.email=:cmail")
    Supplier getByEmail(@param("email")String email); */ //email value eak param ekak widiht argen eka uda use krnwa


    //Query to get buisness reg no based onthe input reg no to check buisness reg no duplication
    @Query(value = "Select s from Supplier s where s.buisness_reg_no=?1")
    Supplier getByBusinessRegno(String buisness_reg_no);
    
}
