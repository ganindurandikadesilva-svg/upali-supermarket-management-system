package lk.upalisupermarket.dao;

import org.springframework.data.jpa.repository.JpaRepository;


import lk.upalisupermarket.entity.QuotationRequestStatus;


public interface QuotationRequestStatusDao extends JpaRepository<QuotationRequestStatus,Integer> {

}
