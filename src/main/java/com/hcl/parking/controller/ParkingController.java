package com.hcl.parking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hcl.parking.dto.CommonResponse;
import com.hcl.parking.dto.EmployeeDto;
import com.hcl.parking.dto.EmployeeSlotRequestDto;
import com.hcl.parking.dto.SpotOwnerRequestDto;
import com.hcl.parking.service.EmployeeService;

@RestController
@RequestMapping("/parking")
public class ParkingController {

	@Autowired
	EmployeeService employeeService;

	@PostMapping("/registration")
	public ResponseEntity<CommonResponse> registration(@RequestBody EmployeeDto employeeDto) {
		return new ResponseEntity<>(employeeService.registration(employeeDto), HttpStatus.CREATED);
	}
	
	@PostMapping("/addFreeParkingSlot")
	public ResponseEntity<CommonResponse> setFreeParkingSlot(@RequestBody SpotOwnerRequestDto spotOwnerRequestDto) {
		return new ResponseEntity<>(employeeService.setFreeParkingSlot(spotOwnerRequestDto), HttpStatus.CREATED);
	}
	
	@PostMapping("/requestForParkingSlot")
	public ResponseEntity<CommonResponse> empSlotRequest(@RequestBody EmployeeSlotRequestDto employeeSlotRequestDto) {
		return new ResponseEntity<>(employeeService.empSlotRequest(employeeSlotRequestDto), HttpStatus.CREATED);
	}

}
