package lk.upalisupermarket.dao;

import org.springframework.data.jpa.repository.JpaRepository;


import lk.upalisupermarket.entity.QuotationStatus;

public interface QuotationStatusDao extends JpaRepository<QuotationStatus,Integer>{

}
