package com.hcl.parking.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hcl.parking.entity.ParkingSlot;
import com.hcl.parking.entity.Raffle;

@Repository
public interface RaffleRepository extends JpaRepository<Raffle, Long> {
	@Query("select  New com.hcl.parking.entity.ParkingSlot(ps.parkingSlotId,ps.slotName) FROM "
			+ " Raffle r,DailyEmployeeSlot e,DailyAvailableSlot p,SlotAllocation s,ParkingSlot ps "
			+ " where  r.dailyEmployeeSlot.empId=e.dailyEmployeeSlotId "
			+ " and e.empId=:empId "
			+ " and r.dailyAvailableSlot.parkingSlotId= p.dailyAvailableSlotId "
			+ " and p.parkingSlotId=s.slotAllocationId "
			+ " and s.parkingSlot.parkingSlotId=ps.parkingSlotId")
	Optional<ParkingSlot> getParkingSlotName(@Param("empId")Long empId);

}
