package com.hcl.parking.serviceimpl;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.hcl.parking.dto.CommonResponse;
import com.hcl.parking.dto.EmployeeDto;
import com.hcl.parking.dto.SpotOwnerRequestDto;
import com.hcl.parking.entity.DailyAvailableSlot;
import com.hcl.parking.entity.Employee;
import com.hcl.parking.entity.ParkingSlot;
import com.hcl.parking.entity.SlotAllocation;
import com.hcl.parking.exception.UserNotFoundException;
import com.hcl.parking.repository.DailyAvailableSlotRepository;
import com.hcl.parking.repository.EmployeeRepository;
import com.hcl.parking.repository.ParkingRepository;
import com.hcl.parking.repository.SlotAllocationRepository;
import com.hcl.parking.service.EmployeeServiceImpl;
import com.hcl.parking.util.ParkingConstants;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeServiceImplTest {
	@InjectMocks
	EmployeeServiceImpl employeeServiceImpl;

	@Mock
	EmployeeRepository employeeRepository;

	@Mock
	ParkingRepository parkingRepository;

	@Mock
	SlotAllocationRepository slotAllocationRepository;
	
	@Mock
	DailyAvailableSlotRepository dailyAvailableSlotRepository;

	Employee employee;
	EmployeeDto employeeDto;
	ParkingSlot parkingSlot;
	SlotAllocation slotAllocation;
	Employee employeeSpotOwner;
	EmployeeDto employeeSpotOwnerDto;
	SpotOwnerRequestDto spotOwnerRequestDto;
	DailyAvailableSlot dailyAvailableSlot;

	@Before
	public void setUp() {
		employee = getEmployee();
		employeeDto = getEmployeeDto();
		parkingSlot = getParkingSlot();
		slotAllocation = getSlotAllocation();
		employeeSpotOwner = getEmployeeSpotOwner();
		employeeSpotOwnerDto = getEmployeeSpotOwnerDto();
		spotOwnerRequestDto=getSpotOwnerRequestDto();
	}

	@Test
	public void testRegistration() {

		Mockito.when(employeeRepository.save(Mockito.any())).thenReturn(employee);
		CommonResponse actual = employeeServiceImpl.registration(employeeDto);
		Assert.assertEquals(ParkingConstants.REGISTRATION_MESSAGE, actual.getMessage());

	}

	@Test
	public void testRegistrationForAbove15Years() {

		Mockito.when(employeeRepository.save(Mockito.any())).thenReturn(employeeSpotOwner);
		CommonResponse actual = employeeServiceImpl.registration(employeeSpotOwnerDto);
		Assert.assertEquals(ParkingConstants.REGISTRATION_MESSAGE, actual.getMessage());

	}

	@Test
	public void testSlotAllocations() {

		Mockito.when(employeeRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(employeeSpotOwner));
		Mockito.when(parkingRepository.save(Mockito.any())).thenReturn(parkingSlot);
		Mockito.when(slotAllocationRepository.save(Mockito.any())).thenReturn(slotAllocation);
		CommonResponse actual = employeeServiceImpl.slotAllocations(employeeSpotOwner.getEmpId());
		Assert.assertEquals("Succesfully created and your slot is:p" + employeeSpotOwner.getEmpId(),
				actual.getMessage());

	}

	@Test(expected = UserNotFoundException.class)
	public void testSlotAllocationsForUserNotFound() {
		Mockito.when(employeeRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
		employeeServiceImpl.slotAllocations(employeeSpotOwner.getEmpId());

	}

	@Test(expected = UserNotFoundException.class)
	public void testSlotAllocationsForNotSpotOwner() {
		Mockito.when(employeeRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(employee));
		employeeServiceImpl.slotAllocations(employeeSpotOwner.getEmpId());

	}

	@Test()
	public void testReleaseParking() {
		//Mockito.when(employeeRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(employeeSpotOwner));
	}

	public EmployeeDto getEmployeeDto() {
		return new EmployeeDto("Priya", "Bang", 567567L, "lead", "2019-05-23");
	}

	public Employee getEmployee() {
		return new Employee(1L, "Priya", "Bang", 567567L, "lead", "2019-05-23", "Hq employees");
	}

	public Employee getEmployeeSpotOwner() {
		return new Employee(2L, "hari", "Bang", 567568L, "lead", "2001-05-23", "Spot owner");
	}

	public EmployeeDto getEmployeeSpotOwnerDto() {
		return new EmployeeDto("hari", "Bang", 567568L, "lead", "2001-05-23");
	}

	public ParkingSlot getParkingSlot() {
		return new ParkingSlot(3L, "p1");
	}

	public SlotAllocation getSlotAllocation() {
		return new SlotAllocation(4L, parkingSlot, employee);
	}
	
	public SpotOwnerRequestDto getSpotOwnerRequestDto()
	{
		return new SpotOwnerRequestDto(employeeSpotOwner.getEmpId(),"2019-08-08","2019-08-10");
	}
	
	public DailyAvailableSlot getDailyAvailableSlot()
	{
		return new DailyAvailableSlot(5L,6L,LocalDate.now());
	}

}
