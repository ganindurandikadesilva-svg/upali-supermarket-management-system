package lk.upalisupermarket.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import lk.upalisupermarket.entity.Item;

public interface ItemDao extends JpaRepository<Item,Integer>{

/* 
  //overriding the findAll to get only the needed data from the db
  @Query( "Select new Item(i.id,i.item_no,i.itemname,i.rop,i.roq,i.item_brand_id,item_sub_category_id,i.sales_price,i.item_status_id) from Item i order by i.id desc")
  List<Item> findAll();

 */



  @Query(value="SELECT coalesce(CONCAT('I', LPAD(SUBSTRING(MAX(item_no), 2) + 1, 5, '0')),'I00001') FROM upali_supermarket.item as i;",nativeQuery = true)
    //liynna puluwan jpa query witharai nativequery liynwnm true krnnn onee
  //coalesce eken  krnne default item no ekk nattm eya ekk hdnwa

    //LPAD(value, length, pad_string) is a function that left-pads a 
  /*   MAX(item_no):

    Finds the highest employee number (e.g., 'I00005').
    SUBSTRING(MAX(item_no), 2):
    
    Extracts the numeric part by removing the leading 'I' (e.g., '00005' → 5).
    SUBSTRING(MAX(item_no), 2) + 1:
    
    Converts the extracted number to an integer and increments it by 1 (e.g., 5 + 1 = 6).
    LPAD(..., 5, '0'):
    
    Ensures the numeric part is always 5 digits long, padding with zeros if necessary (e.g., 6 → '00006').
    CONCAT('I', ...):
    
    Adds the 'I' prefix back to the new number, resulting in 'I00006'. */


    //string to a specified length with a given character.
    String generateNextItemNo();//its automatically adds the output of the query to this functions body

    //Query to show items that a supplier doesnt supply (supplier edit->when selecting items)
    //shi.item_id.id ape association eke tiyn ewa object e nis direct samana krnn ba item ge .id kiyl primary key gnn one
    @Query(value="SELECT i from Item i where i.id not in (select shi.item_id.id from SupplierHasItem shi where shi.supplier_id.id=?1)")
    List<Item> getListWithoutSupplier(Integer supplierid);


     //Query to show items that a supplier  supply (supplier edit->when selecting items) for quotation request form
    //shi.item_id.id ape association eke tiyn ewa object e nis direct samana krnn ba item ge .id kiyl primary key gnn one
    @Query(value="SELECT new Item(i.id,i.itemname,i.item_no,i.purchase_price) from Item i where i.id  in (select shi.item_id.id from SupplierHasItem shi where shi.supplier_id.id=?1)")
    List<Item>  getItemListBySupplier(Integer supplierid);
    //for getting only the needed field a constructor must be defined in item.java 

    //query get item list where item status=1 for purchase order form item
    @Query(value="SELECT new Item(i.id,i.itemname,i.item_no,i.purchase_price) from Item i where i.item_status_id.id=1")
    List<Item> getItemList();

    
    //Query to show items that thwe quotation has for purchase order form
    //qhi.item_id.id ape association eke tiyn ewa object e nis direct samana krnn ba item ge .id kiyl primary key gnn one
    @Query(value="SELECT new Item(i.id,i.itemname,i.item_no,i.purchase_price) from Item i where i.id  in (select qhi.item_id.id from QuotationHasItem qhi where qhi.quotation_id.id=?1)")
    List<Item> getItemListByQuotation(Integer quotationid);



      //Query to show items based on quotation request (supplier edit->when selecting items) for quotation request form
    //qrhi.item_id.id ape association eke tiyn ewa object e nis direct samana krnn ba item ge .id kiyl primary key gnn one
    @Query(value="SELECT new Item(i.id,i.itemname,i.item_no,i.purchase_price) from Item i where i.id  in (select qrhi.item_id.id from QuotationRequestHasItem qrhi where qrhi.quotation_request_id.id=?1)")
    //for getting only the needed field a constructor must be defined in item.java 
    List<Item> getItemListByQuotationRequest(Integer qrid);

    //Query to show items based on purchase order (supplier edit->when selecting items) for grn form
    //pohi.item_id.id ape association eke tiyn ewa object e nis direct samana krnn ba item ge .id kiyl primary key gnn one
    @Query(value="SELECT new Item(i.id,i.itemname,i.item_no,i.purchase_price) from Item i where i.id  in (select pohi.item_id.id from PurchaseOrderHasItem pohi where pohi.purchase_order_id.id=?1)")
    //for getting only the needed field a constructor must be defined in item.java 
    List<Item> getItemListByPurchaseOrder(Integer podid);


     @Query( value = "SELECT i FROM Item i WHERE i.itemname=?1")
    Item getByItemName(String itemname);
    //for getting only the needed field a constructor must be defined in item.java 

     @Query( value = "SELECT i.rop FROM Item i")//return rop
     Integer getRop();




   

}
