package lk.upalisupermarket.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import lk.upalisupermarket.entity.ItemStatus;

public interface ItemStatusDao extends JpaRepository<ItemStatus,Integer>{

}
