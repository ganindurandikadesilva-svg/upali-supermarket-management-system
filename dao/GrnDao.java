package lk.upalisupermarket.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import lk.upalisupermarket.entity.Grn;


public interface GrnDao extends JpaRepository<Grn,Integer>{

    
  @Query(value="SELECT coalesce(CONCAT('GRN', LPAD(SUBSTRING(MAX(grn_no), 4) + 1, 5, '0')),'GRN00001') FROM upali_supermarket.grn as grn;",nativeQuery = true)
    //liynna puluwan jpa query witharai nativequery liynwnm true krnnn onee
  //coalesce eken  krnne default pod no ekk nattm eya ekk hdnwa

    //LPAD(value, length, pad_string) is a function that left-pads a 
  /*   MAX(grn_no):

    Finds the highest pod number (e.g., 'POD00005').
    SUBSTRING(MAX(grn_no), 4):
    
    Extracts the numeric part by removing the leading 'I' (e.g., '00005' → 5).
    SUBSTRING(MAX(grn_no), 4) + 1:
    
    Converts the extracted number to an integer and increments it by 1 (e.g., 5 + 1 = 6).
    LPAD(..., 5, '0'):
    
    Ensures the numeric part is always 5 digits long, padding with zeros if necessary (e.g., 6 → '00006').
    CONCAT('POD', ...):
    
    Adds the 'POD' prefix back to the new number, resulting in 'POD00006'. */


    //string to a specified length with a given character.
    String generateNextGrnNo();//its automatically adds the output of the query to this functions body

  //Query to select grn based on selected supplier id and Grnstatus="Received"
  //g.supplier_id.id ape association eke tiyn ewa object e nis direct samana krnn ba supplierge ge .id kiyl primary key gnn one
  @Query(value="SELECT new Grn(g.id,g.grn_no,g.grandtotal,g.net_amount,g.paid_amount) FROM Grn g where g.supplier_id.id=?1 and g.grn_status_id.id=1 and g.net_amount<>g.paid_amount")
  List<Grn> getItemListBySupplierId(Integer supplierid);
  //need to creat a constructor in entity file mokd api gnne grn_no,id kiy data ek witharai ,all * nm hdn one na 

  @Query( value = "SELECT g FROM Grn g WHERE g.grn_no=?1")
  Grn getByGrnNo(String grn_no);
}
