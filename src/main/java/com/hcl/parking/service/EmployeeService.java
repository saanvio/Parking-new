package com.hcl.parking.service;

import java.util.List;

import com.hcl.parking.dto.CommonResponse;
import com.hcl.parking.dto.EmployeeDto;
import com.hcl.parking.dto.ParkingSlotDto;
import com.hcl.parking.dto.SpotOwnerRequestDto;
import com.hcl.parking.entity.Raffle;

public interface EmployeeService {
	
	 CommonResponse registration(EmployeeDto employeeDto);
	 
	 CommonResponse slotAllocations(Long empId);
	 
	 CommonResponse releaseParking(SpotOwnerRequestDto spotOwnerRequestDto);
	 
	 CommonResponse requestParking(Long empId);
	 
	 ParkingSlotDto getSlotDetails(Long empId);
	 
	 List<Raffle> doRaffle();
	 
	 
	 

}
