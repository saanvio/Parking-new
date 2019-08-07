package com.hcl.parking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hcl.parking.entity.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>{

//	select r.parking_slot_id,p.parking_slot_id FROM parking.raffle r,parking.daily_employee_slot e,parking.daily_available_slot p where
//	 r.emp_id=e.daily_employee_slot_id
//	 and e.emp_id=4
//	 and r.parking_slot_id= p.daily_available_slot_id
	
}
