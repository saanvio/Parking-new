package com.hcl.parking.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hcl.parking.entity.DailyEmployeeSlot;

@Repository
public interface DailyEmployeeSlotRepository extends JpaRepository<DailyEmployeeSlot, Long> {
	List<DailyEmployeeSlot> findByAvailableDates(LocalDate date);
}
