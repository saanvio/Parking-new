package com.hcl.parking.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hcl.parking.dto.CommonResponse;
import com.hcl.parking.dto.EmployeeDto;
import com.hcl.parking.dto.SpotOwnerRequestDto;
import com.hcl.parking.entity.Raffle;
import com.hcl.parking.service.EmployeeService;

@RestController
@RequestMapping("/parking")
public class ParkingController {
	public static final Logger LOGGER = LoggerFactory.getLogger(ParkingController.class);

	@Autowired
	EmployeeService employeeService;

	@PostMapping("/registration")
	public ResponseEntity<CommonResponse> registration(@RequestBody EmployeeDto employeeDto) {
		LOGGER.info("Registration controller");
		return new ResponseEntity<>(employeeService.registration(employeeDto), HttpStatus.CREATED);
	}

	@PostMapping("/slotAlloactions")
	public ResponseEntity<CommonResponse> slotAllowcations(@RequestParam Long empId) {
		LOGGER.info("slot allocation controller");
		return new ResponseEntity<>(employeeService.slotAllocations(empId), HttpStatus.CREATED);
	}

	@PostMapping("/releaseParking")
	public ResponseEntity<CommonResponse> releaseParking(@RequestBody SpotOwnerRequestDto spotOwnerRequestDto) {
		LOGGER.info("release parking controller");
		return new ResponseEntity<>(employeeService.releaseParking(spotOwnerRequestDto), HttpStatus.CREATED);
	}

	@PostMapping("/requestParking")
	public ResponseEntity<CommonResponse> requestParking(@RequestParam Long empId) {
		LOGGER.info("Request parking controller");
		return new ResponseEntity<>(employeeService.requestParking(empId), HttpStatus.CREATED);
	}

	@PostMapping("/doRaffle")
	public ResponseEntity<List<Raffle>> doRaffle() {
		LOGGER.info("Raffle controller");
		return new ResponseEntity<>(employeeService.doRaffle(), HttpStatus.CREATED);
	}

}
