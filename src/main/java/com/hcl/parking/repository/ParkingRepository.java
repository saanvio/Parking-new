package com.hcl.parking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hcl.parking.entity.ParkingSlot;

@Repository
public interface ParkingRepository extends JpaRepository<ParkingSlot, Long>{

}
