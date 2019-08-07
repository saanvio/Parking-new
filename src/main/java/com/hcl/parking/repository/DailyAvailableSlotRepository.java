package com.hcl.parking.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hcl.parking.entity.DailyAvailableSlot;

@Repository
public interface DailyAvailableSlotRepository extends JpaRepository<DailyAvailableSlot, Long>{
	List<DailyAvailableSlot> findByAvailableDates(LocalDate date);

}
