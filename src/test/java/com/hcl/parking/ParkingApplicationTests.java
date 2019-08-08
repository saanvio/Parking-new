package com.hcl.parking;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.hcl.parking.service.EmployeeServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ParkingApplicationTests {
	
	@Autowired
	EmployeeServiceImpl employeeServiceImpl;
	
	

	@Test
	public void contextLoads() {
	}
	


}
