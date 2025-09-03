package lk.upalisupermarket.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import lk.upalisupermarket.entity.CustomerPaymentMethod;


public interface CustomerPaymentMethodDao extends JpaRepository<CustomerPaymentMethod,Integer> {

}
