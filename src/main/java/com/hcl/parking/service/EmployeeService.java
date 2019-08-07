package com.hcl.parking.service;

import com.hcl.parking.dto.CommonResponse;
import com.hcl.parking.dto.EmployeeDto;
import com.hcl.parking.dto.EmployeeSlotRequestDto;
import com.hcl.parking.dto.SpotOwnerRequestDto;

public interface EmployeeService {
	
	 CommonResponse registration(EmployeeDto employeeDto);
	 
	 CommonResponse setFreeParkingSlot(SpotOwnerRequestDto spotOwnerRequestDto);
	 
	 CommonResponse empSlotRequest(EmployeeSlotRequestDto employeeSlotRequestDto);
	 

}
