package lk.upalisupermarket.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import lk.upalisupermarket.entity.PurchaseOrderStatus;


public interface PurchaseOrderStatusDao extends JpaRepository<PurchaseOrderStatus,Integer> {

}
