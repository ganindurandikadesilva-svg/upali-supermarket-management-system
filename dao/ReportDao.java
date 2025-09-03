package lk.upalisupermarket.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import lk.upalisupermarket.entity.Employee;


public interface ReportDao extends JpaRepository<Employee,Integer> {

    @Query(value = "SELECT monthname(pod.added_datetime),sum(pod.grandtotal) FROM upali_supermarket.purchase_order as pod where date(pod.added_datetime) between current_date()-interval 6 month and current_date() group by(monthname(pod.added_datetime));",
    nativeQuery = true)
    String [][] getPurchasePayementsPrevioussixMonths();

    //for  user given month
    @Query(value = "SELECT monthname(pod.added_datetime),sum(pod.grandtotal) FROM upali_supermarket.purchase_order as pod where date(pod.added_datetime) between ?1 and ?2 group by(monthname(pod.added_datetime));",
    nativeQuery = true)
    String [][] getPurchasePayementsgivenMonth(String startdate,String enddate);

    //for  user given week
     @Query(value = "SELECT week(pod.added_datetime),sum(pod.grandtotal) FROM upali_supermarket.purchase_order as pod where date(pod.added_datetime) between ?1 and ?2 group by(week(pod.added_datetime));",
    nativeQuery = true)
    String [][] getPurchasePayementsforWeek(String startdate,String enddate);

     



     //Query to return employee list based on status and designation
     @Query(value="SELECT new Employee(e.fullname,e.nic,e.mobile_no,e.email,e.emp_status_id,e.designaton_id) FROM Employee e where e.emp_status_id.id=?1 and e.designaton_id.id=?2")
   List<Employee> getEmployeesbyStatusandDesignation(Integer statusid, Integer designationid);
}
