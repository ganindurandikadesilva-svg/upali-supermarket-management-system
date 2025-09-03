package lk.upalisupermarket.dao;


import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import lk.upalisupermarket.entity.ItemSeasonalDiscount;

public interface ItemSeasonalDiscountDao extends JpaRepository<ItemSeasonalDiscount,Integer> {



    //query to return discount rate if there are fro the values of invoices(itemid,date,lineprice)
    @Query(value = "SELECT new ItemSeasonalDiscount(isd.discount_rate) FROM ItemSeasonalDiscount isd where isd.item_id.id=?1 and (?2 between isd.start_date and isd.end_date) and isd.minimum_qty<?3")
    List<ItemSeasonalDiscount> getDiscountByItemidDateandAmount(Integer itemid, LocalDate date, Integer qty);
    //need to create constructors in entiry file beacuase we are only returning discount rate value(isd.discount_rate)
    
}
