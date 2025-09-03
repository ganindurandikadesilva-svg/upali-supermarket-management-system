package lk.upalisupermarket.dao;

import org.springframework.data.jpa.repository.JpaRepository;


import lk.upalisupermarket.entity.SupplierPaymentMethod;

public interface SupplierPaymentMethodDao extends JpaRepository<SupplierPaymentMethod,Integer> {

}
