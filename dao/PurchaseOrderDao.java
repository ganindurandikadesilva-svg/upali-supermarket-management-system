package lk.upalisupermarket.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import lk.upalisupermarket.entity.PurchaseOrder;

public interface PurchaseOrderDao extends JpaRepository<PurchaseOrder,Integer>{

    
  @Query(value="SELECT coalesce(CONCAT('POD', LPAD(SUBSTRING(MAX(pod_no), 4) + 1, 5, '0')),'POD00001') FROM upali_supermarket.purchase_order as pod;",nativeQuery = true)
    //liynna puluwan jpa query witharai nativequery liynwnm true krnnn onee
  //coalesce eken  krnne default pod no ekk nattm eya ekk hdnwa

    //LPAD(value, length, pad_string) is a function that left-pads a 
  /*   MAX(pod_no):

    Finds the highest pod number (e.g., 'POD00005').
    SUBSTRING(MAX(pod_no), 4):
    
    Extracts the numeric part by removing the leading 'I' (e.g., '00005' → 5).
    SUBSTRING(MAX(pod_no), 4) + 1:
    
    Converts the extracted number to an integer and increments it by 1 (e.g., 5 + 1 = 6).
    LPAD(..., 5, '0'):
    
    Ensures the numeric part is always 5 digits long, padding with zeros if necessary (e.g., 6 → '00006').
    CONCAT('POD', ...):
    
    Adds the 'POD' prefix back to the new number, resulting in 'POD00006'. */


    //string to a specified length with a given character.
    String generateNextPurchaseOrderNo();//its automatically adds the output of the query to this functions body

    
  //Query to show purchase orders based on supplier  for grn form
    //pod.supplier_id.id ape association eke tiyn ewa object e nis direct samana krnn ba supplierge ge .id kiyl primary key gnn one
    @Query(value="SELECT new PurchaseOrder(pod.id,pod.pod_no,pod.grandtotal) from PurchaseOrder pod where pod.supplier_id.id=?1 and pod.purchase_order_status_id.id=1 ")
    List<PurchaseOrder> getPodListBySupplier(Integer supplierid);
      //need to create a constructor in entity file mokd api gnne (id,pod_no,pod,grandtotal)  kiy data ek witharai ,all * nm hdn one na 

   //Query to return qty for grn form      
    @Query(value = "SELECT p.qty FROM PurchaseOrderHasItem p WHERE p.purchase_order_id.id = ?1 AND p.item_id.id = ?2")      
    BigDecimal getQtybyPodandItemid(Integer podid, Integer itemid);

 //Query to return qty for grn form      
    @Query(value = "SELECT p.purchase_price FROM PurchaseOrderHasItem p WHERE p.purchase_order_id.id = ?1 AND p.item_id.id = ?2")
    BigDecimal getUnitPbyPodandItemid(Integer podid, Integer itemid);


    
}
