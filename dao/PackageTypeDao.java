package lk.upalisupermarket.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import lk.upalisupermarket.entity.PackageType;

public interface PackageTypeDao extends JpaRepository<PackageType,Integer> {

}
