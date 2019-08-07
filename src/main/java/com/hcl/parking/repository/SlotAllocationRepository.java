package com.hcl.parking.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hcl.parking.entity.SlotAllocation;

@Repository
public interface SlotAllocationRepository extends JpaRepository<SlotAllocation, Long> {

	@Query("select p from SlotAllocation p where employee.empId=:empId")
	Optional<SlotAllocation> findByParkingSlot(@Param("empId") Long empId);
}
