package lk.upalisupermarket.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import lk.upalisupermarket.entity.SupplierPayment;


public interface SupplierPaymentDao extends JpaRepository<SupplierPayment,Integer> {

    @Query(value = "SELECT coalesce(concat('SBL',lpad(substring(max(bill_no),4)+1,4,'0')),'SBL00001') FROM upali_supermarket.supplier_payments ;",nativeQuery = true)
    //liynna puluwan jpa query witharai nativequery liynwnm true krnnn onee
  //coalesce eken  krnne default bill no ekk nattm eya ekk hdnwa

    //LPAD(value, length, pad_string) is a function that left-pads a 
  /*   MAX(bill_no):

    Finds the highest bill number (e.g., 'SBL00005').
    SUBSTRING(MAX(bill_no), 4):
    
    Extracts the numeric part by removing the leading 'SBL' (e.g., '00005' → 5).
    SUBSTRING(MAX(bill_no), 4) + 1:
    
    Converts the extracted number to an integer and increments it by 1 (e.g., 5 + 1 = 6).
    LPAD(..., 5, '0'):
    
    Ensures the numeric part is always 5 digits long, padding with zeros if necessary (e.g., 6 → '00006').
    CONCAT('SBL', ...):
    
    Adds the 'SBL' prefix back to the new number, resulting in 'SBL00006'. */


    //string to a specified length with a given character.
    
    String generateNextSupplierPayBillNo();//its automatically adds the output of the query to this functions body

    //Query to cheque duplicate cheque no
    @Query(value = "SELECT sp FROM SupplierPayment sp WHERE sp.cheque_no=?1")
    SupplierPayment getByChequeNo(String chequeno);
}
