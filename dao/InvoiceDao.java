package lk.upalisupermarket.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import lk.upalisupermarket.entity.Invoice;


public interface InvoiceDao  extends JpaRepository<Invoice,Integer>{

    
     @Query(value="SELECT coalesce(CONCAT('INVO', LPAD(SUBSTRING(MAX(invoice_no), 5) + 1, 5, '0')),'INVO00001') FROM upali_supermarket.invoice as inv;",nativeQuery = true)
    //liynna puluwan jpa query witharai nativequery liynwnm true krnnn onee
    //LPAD(value, length, pad_string) is a function that left-pads a 

    //coalesce eken  krnne default sup no ekk nattm eya ekk hdnwa

    //LPAD(value, length, pad_string) is a function that left-pads a 
  /*   MAX(cus_no):

    Finds the highest employee number (e.g., 'I00005').
    SUBSTRING(MAX(cus_no), 2):
    
    Extracts the numeric part by removing the leading 'I' (e.g., '00005' → 5).
    SUBSTRING(MAX(cus_no), 2) + 1:
    
    Converts the extracted number to an integer and increments it by 1 (e.g., 5 + 1 = 6).
    LPAD(..., 5, '0'):
    
    Ensures the numeric part is always 5 digits long, padding with zeros if necessary (e.g., 6 → '00006').
    CONCAT('I', ...):
    
    Adds the 'I' prefix back to the new number, resulting in 'I00006'. */

    //string to a specified length with a given character.
    String generateNextInvoiceNo();

    

}
