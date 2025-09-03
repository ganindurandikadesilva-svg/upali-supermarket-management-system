package lk.upalisupermarket.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import lk.upalisupermarket.entity.Employee;

public interface EmployeeDao  extends JpaRepository<Employee,Integer>{

    @Query(value="SELECT lpad(max(emp_no)+1,8,0) FROM upali_supermarket.employee as e;",nativeQuery = true)
    //SELECT coalesce(lpad(max(emp_no)+1,8,0),'00000001') FROM upali_supermarket.employee as e;
    //liynna puluwan jpa query witharai nativequery liynwnm true krnnn onee
    //coalesce eken  krnne default emp no ekk nattm eya ekk hdnwa

    //LPAD(value, length, pad_string) is a function that left-pads a 
  /*   MAX(emp_no):

    Finds the highest employee number (e.g., 'I00005').
    SUBSTRING(MAX(emp_no), 2):
    
    Extracts the numeric part by removing the leading 'I' (e.g., '00005' → 5).
    SUBSTRING(MAX(emp_no), 2) + 1:
    
    Converts the extracted number to an integer and increments it by 1 (e.g., 5 + 1 = 6).
    LPAD(..., 5, '0'):
    
    Ensures the numeric part is always 5 digits long, padding with zeros if necessary (e.g., 6 → '00006').
    CONCAT('I', ...):
    
    Adds the 'I' prefix back to the new number, resulting in 'I00006'. */
    String generateNextEmpNo();//its automatically adds the output of the query to this functions body

    @Query(value = "Select e from Employee e where e.nic=?1")//JP Query
    //query to check duplicates
    //?1 is nic parameter(first parameter) in the following function
    Employee getByNic(String nic);//its automatically adds the output of the query to this functions body


    @Query(value="Select e from Employee e where e.email=?1")
    Employee getByEmail(String email);
/* 
    @Query(value="Select e from Employee e where e.email=:email")
    Employee getByEmail(@param("email")String email); */ //email value eak param ekak widiht argen eka uda use krnwa


    //query to select employees without user accounts
    //not in dnn ba null value thiyn nisa
    @Query("SELECT e FROM Employee e WHERE e.id NOT IN (SELECT u.employee_id.id FROM User u WHERE u.employee_id IS NOT NULL)")
    List<Employee> findEmployeeWithoutUserAccount();
    


}
