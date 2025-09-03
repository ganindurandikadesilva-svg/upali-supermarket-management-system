package lk.upalisupermarket.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import lk.upalisupermarket.entity.LoyaltyPoints;

public interface LoyaltyPointsDao extends JpaRepository<LoyaltyPoints,Integer>{

//Query to return discounts if available based on min.invoiceamount,loyaltly points
@Query(value = "SELECT new LoyaltyPoints (lt.point_increase_rate,lt.discount_rate) FROM LoyaltyPoints  lt where  discount_availability=true and (?1 between lt.start_point and lt.end_point) and ?2>=invoice_amount_limit  ")
List<LoyaltyPoints> byLoyaltyPointsandAmount(BigDecimal loyaltypoints, BigDecimal invoiceamount);

@Query(value = "SELECT lt from LoyaltyPoints lt where ?1 between lt.start_point and lt.end_point")
LoyaltyPoints byLoyaltyPoints(BigDecimal currentPoints);

}
