package lk.upalisupermarket.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import lk.upalisupermarket.entity.Inventory;


public interface InventoryDao  extends JpaRepository<Inventory,Integer>{


    //generte th enext batch no
    @Query(value="select coalesce(concat('BNO',lpad(substring(max(inv.batch_no),4)+1,4,0)), 'BNO0001') from upali_supermarket.item_inventory as inv where inv.batch_no like 'BNO%';",nativeQuery = true)
    String getnextBatchno();

    //Select item by batch no
    @Query(value="SELECT * FROM upali_supermarket.item_inventory as inty where inty.item_id=?1 and inty.batch_no=?2",nativeQuery = true)
    Inventory getByItemBatchNo(Integer itemid, String batchno);

      //Query to show items that a supplier  supply (supplier edit->when selecting items) for quotation  form
    //shi.item_id.id ape association eke tiyn ewa object e nis direct samana krnn ba item ge .id kiyl primary key gnn one
    @Query(value="SELECT i FROM Inventory i WHERE i.item_id.id = ?1")
    List<Inventory> getBatchListByItem(Integer itemid);
      //need to create a constructor in entity file mokd api gnne (id,batch_no,available_qty)  kiy data ek witharai ,all * nm hdn one na 

   
}
