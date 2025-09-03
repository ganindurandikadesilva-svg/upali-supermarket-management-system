package lk.upalisupermarket.dao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import lk.upalisupermarket.entity.Quotation;

public interface QuotationDao extends JpaRepository<Quotation,Integer>{

  @Query(value="SELECT coalesce(CONCAT('Q', LPAD(SUBSTRING(MAX(quotation_no), 2) + 1, 5, '0')),'Q00001') FROM upali_supermarket.quotation as q;",nativeQuery = true)
    //liynna puluwan jpa query witharai nativequery liynwnm true krnnn onee
  //coalesce eken  krnne default qr no ekk nattm eya ekk hdnwa

    //LPAD(value, length, pad_string) is a function that left-pads a 
  /*   MAX(quotation_request_no):

    Finds the highest qr number (e.g., 'QR00001').
    SUBSTRING(MAX(quotation_request_no), 4):
    
    Extracts the numeric part by removing the leading 'QR' (e.g., '00005' → 5).
    SUBSTRING(MAX(quotation_request_no), 4) + 1:
    
    Converts the extracted number to an integer and increments it by 1 (e.g., 5 + 1 = 6).
    LPAD(..., 5, '0'):
    
    Ensures the numeric part is always 5 digits long, padding with zeros if necessary (e.g., 6 → '00006').
    CONCAT('QR', ...):
    
    Adds the 'QR' prefix back to the new number, resulting in 'QR00006'. */


    //string to a specified length with a given character.
    String generateNextQuotationNo();//its automatically adds the output of the query to this functions body


  //Query to show items that a supplier  supply (supplier edit->when selecting items) for quotation  form
    //shi.item_id.id ape association eke tiyn ewa object e nis direct samana krnn ba item ge .id kiyl primary key gnn one
    @Query(value="SELECT new Quotation(q.id,q.quotation_no,q.received_date) from Quotation q where q.supplier_id.id=?1 and q.quotation_status_id.id=1 and q.deadline_date >?2")
  List<Quotation> getQuotationListBySupplier(Integer supplierid,LocalDate requireddate);
      //need to create a constructor in entity file mokd api gnne (id,quotation_no,received_date)  kiy data ek witharai ,all * nm hdn one na 


       //Query to return qty for purchase order form      
    @Query(value = "SELECT qt.qty FROM QuotationHasItem qt WHERE qt.quotation_id.id = ?1 AND qt.item_id.id = ?2")      
    BigDecimal getQtybyQtionandItemId(Integer qtid, Integer itemid);


    //Qurry to return unit price for purchase order form
     @Query(value = "SELECT qt.last_price FROM QuotationHasItem qt WHERE qt.quotation_id.id = ?1 AND qt.item_id.id = ?2") 
    BigDecimal getUnitPricebyQItemId(Integer qtid, Integer itemid);



}
