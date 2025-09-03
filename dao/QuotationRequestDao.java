package lk.upalisupermarket.dao;



import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import lk.upalisupermarket.entity.QuotationRequest;

public interface QuotationRequestDao extends JpaRepository<QuotationRequest,Integer>{

    
  @Query(value="SELECT coalesce(CONCAT('QR', LPAD(SUBSTRING(MAX(quotation_request_no), 3) + 1, 5, '0')),'QR00001') FROM upali_supermarket.quotation_request as qr;",nativeQuery = true)
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
    String generateNextQuotationRequestNo();//its automatically adds the output of the query to this functions body



     //Query to show items that a supplier  supply (supplier edit->when selecting items) for quotation request form
    //shi.item_id.id ape association eke tiyn ewa object e nis direct samana krnn ba item ge .id kiyl primary key gnn one
    @Query(value="SELECT new QuotationRequest(qr.id,qr.quotation_request_no,qr.requested_date) from QuotationRequest qr where qr.supplier_id.id=?1 and qr.quotation_request_status_id.id=1  ")
    List<QuotationRequest>  getQrListBySupplier(Integer supplierid);
    //for getting only the needed field a constructor must be defined in item.java 
          //need to create a constructor in entity file mokd api gnne (id,quotation_request_no,requested_date)  kiy data ek witharai ,all * nm hdn one na 



    //Query to return qty for quotation form      
    @Query(value = "SELECT q.qty FROM QuotationRequestHasItem q WHERE q.quotation_request_id.id = ?1 AND q.item_id.id = ?2")      
    BigDecimal getQtybyQrandItemId(Integer qrid, Integer itemid);


  }
