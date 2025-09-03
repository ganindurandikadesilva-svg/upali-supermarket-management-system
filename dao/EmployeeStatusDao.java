package lk.upalisupermarket.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import lk.upalisupermarket.entity.EmployeeStatus;

public interface EmployeeStatusDao extends JpaRepository<EmployeeStatus,Integer> {

}
