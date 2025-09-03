package lk.upalisupermarket.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import lk.upalisupermarket.entity.CustomerStatus;


public interface CustomerStatusDao extends JpaRepository<CustomerStatus,Integer> {

}
