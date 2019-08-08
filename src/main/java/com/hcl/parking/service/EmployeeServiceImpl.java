package com.hcl.parking.service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcl.parking.dto.CommonResponse;
import com.hcl.parking.dto.EmployeeDto;
import com.hcl.parking.dto.ParkingSlotDto;
import com.hcl.parking.dto.SpotOwnerRequestDto;
import com.hcl.parking.entity.DailyAvailableSlot;
import com.hcl.parking.entity.DailyEmployeeSlot;
import com.hcl.parking.entity.Employee;
import com.hcl.parking.entity.ParkingSlot;
import com.hcl.parking.entity.Raffle;
import com.hcl.parking.entity.SlotAllocation;
import com.hcl.parking.exception.CommonException;
import com.hcl.parking.exception.UserNotFoundException;
import com.hcl.parking.repository.DailyAvailableSlotRepository;
import com.hcl.parking.repository.DailyEmployeeSlotRepository;
import com.hcl.parking.repository.EmployeeRepository;
import com.hcl.parking.repository.ParkingRepository;
import com.hcl.parking.repository.RaffleRepository;
import com.hcl.parking.repository.SlotAllocationRepository;
import com.hcl.parking.util.ParkingConstants;

@Service
public class EmployeeServiceImpl implements EmployeeService {
	public static final Logger LOGGER = LoggerFactory.getLogger(EmployeeServiceImpl.class);
	@Autowired
	EmployeeRepository employeeRepository;

	@Autowired
	ParkingRepository parkingRepository;

	@Autowired
	DailyAvailableSlotRepository dailyAvailableslotRepo;

	@Autowired
	DailyEmployeeSlotRepository dailyEmployeeSlotRepository;

	@Autowired
	RaffleRepository raffleRepository;

	@Autowired
	SlotAllocationRepository slotAllocationRepository;

	Random rand = new Random();

	@Override
	public CommonResponse registration(EmployeeDto employeeDto) {
		LOGGER.info("Registration service impl");
		Employee employee = new Employee();
		LocalDate dateOfJoiningLocalDate = getLocalDate(employeeDto.getDateOfJoining());
		Period diff = Period.between(dateOfJoiningLocalDate, LocalDate.now());
		if (diff.getYears() >= 15 || employeeDto.getDesignation().equalsIgnoreCase("vice president"))
			employee.setRole("Spot owner");
		else
			employee.setRole("Hq employees");

		BeanUtils.copyProperties(employeeDto, employee);
		employeeRepository.save(employee);

		return new CommonResponse(ParkingConstants.REGISTRATION_MESSAGE);
	}

	@Override
	public CommonResponse slotAllocations(Long empId) {
		LOGGER.info("Slot allocation  service impl");
		Optional<Employee> employee = employeeRepository.findById(empId);
		if (!employee.isPresent())
			throw new UserNotFoundException(ParkingConstants.ERROR_USER_NOT_FOUND_MESSAGE);
		if (!employee.get().getRole().equalsIgnoreCase("spot owner"))
			throw new UserNotFoundException("Not Spotowner");

		ParkingSlot parkingSlot = new ParkingSlot();
		parkingSlot.setSlotName("p" + empId);
		parkingRepository.save(parkingSlot);

		SlotAllocation slotAllocation = new SlotAllocation();
		slotAllocation.setEmployee(employee.get());
		slotAllocation.setParkingSlot(parkingSlot);
		slotAllocationRepository.save(slotAllocation);
		return new CommonResponse("Succesfully created and your slot is:" + parkingSlot.getSlotName());
	}

	@Override
	public CommonResponse releaseParking(SpotOwnerRequestDto spotOwnerRequestDto) {
		LOGGER.info("release parking impl");
		Optional<Employee> employee = employeeRepository.findById(spotOwnerRequestDto.getOwnerId());
		if (!employee.isPresent())
			throw new UserNotFoundException(ParkingConstants.ERROR_USER_NOT_FOUND_MESSAGE);
		if (!employee.get().getRole().equalsIgnoreCase("spot owner"))
			throw new UserNotFoundException("Not Spotowner");

		Optional<SlotAllocation> parkingSlot = slotAllocationRepository
				.findByParkingSlot(spotOwnerRequestDto.getOwnerId());
		if (!parkingSlot.isPresent())
			throw new UserNotFoundException(ParkingConstants.ERROR_NO_PARKING_SLOTS);

		LocalDate fromDate = getLocalDate(spotOwnerRequestDto.getFromDate());
		LocalDate toDate = getLocalDate(spotOwnerRequestDto.getToDate());

		if (fromDate.compareTo(LocalDate.now()) != 0 || fromDate.compareTo(LocalDate.now()) < 0)
			throw new CommonException(ParkingConstants.ERROR_FROM_DATE);

		if (fromDate.compareTo(toDate) != 0 || fromDate.compareTo(toDate) > 0)
			throw new CommonException(ParkingConstants.ERROR_TO_DATE);

		List<LocalDate> dates = new ArrayList<LocalDate>();
		LocalDate current = fromDate;
		while (current.isBefore(toDate) || current.equals(toDate)) {
			dates.add(current);
			DailyAvailableSlot dailyAvailableSlot = new DailyAvailableSlot();
			dailyAvailableSlot.setParkingSlotId(parkingSlot.get().getSlotAllocationId());
			dailyAvailableSlot.setAvailableDates(current);
			current = current.plusDays(1);
			dailyAvailableslotRepo.save(dailyAvailableSlot);
		}
		return new CommonResponse(ParkingConstants.SUCCESS);

	}

	@Override
	public CommonResponse requestParking(Long empId) {
		LOGGER.info("Request aprking service impl");
		Optional<Employee> employee = employeeRepository.findById(empId);
		if (!employee.isPresent())
			throw new UserNotFoundException(ParkingConstants.ERROR_USER_NOT_FOUND_MESSAGE);
		if (!employee.get().getRole().equalsIgnoreCase("Hq employees"))
			throw new UserNotFoundException("Not Hq employee");
		LocalDate currentDate = getLocalDate(LocalDate.now().toString());
		if (currentDate.compareTo(LocalDate.now()) != 0 || currentDate.compareTo(LocalDate.now()) < 0)
			throw new CommonException(ParkingConstants.ERROR_FROM_DATE);

		DailyEmployeeSlot dailyEmployeeSlot = new DailyEmployeeSlot();
		dailyEmployeeSlot.setEmpId(employee.get().getEmpId());
		dailyEmployeeSlot.setAvailableDates(currentDate);
		dailyEmployeeSlotRepository.save(dailyEmployeeSlot);

		return new CommonResponse(ParkingConstants.SUCCESS);

	}

	@Override
	public List<Raffle> doRaffle() {
		LOGGER.info("raffle service impl");

		LocalDate currentDate = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate localDateForamt = LocalDate.parse(currentDate.toString(), formatter);
		List<DailyAvailableSlot> dailyAvalibaleSlotLists = dailyAvailableslotRepo.findByAvailableDates(localDateForamt);
		List<DailyEmployeeSlot> dailyEmployeeSlotLists = dailyEmployeeSlotRepository
				.findByAvailableDates(localDateForamt);
		for (int i = 0; i < dailyAvalibaleSlotLists.size(); i++) {
			for (int j = 0; j < dailyEmployeeSlotLists.size(); j++) {
				int randomIndex = rand.nextInt(dailyEmployeeSlotLists.size() - 1);
				Long employeeId = dailyEmployeeSlotLists.get(randomIndex).getDailyEmployeeSlotId();
				Long slotId = dailyAvalibaleSlotLists.get(randomIndex).getDailyAvailableSlotId();

				dailyEmployeeSlotLists.remove(randomIndex);
				dailyAvalibaleSlotLists.remove(randomIndex);

				DailyEmployeeSlot dailyEmployeeSlot = new DailyEmployeeSlot();
				dailyEmployeeSlot.setDailyEmployeeSlotId(employeeId);

				DailyAvailableSlot dailyAvailableSlot = new DailyAvailableSlot();
				dailyAvailableSlot.setDailyAvailableSlotId(slotId);

				Raffle raffle = new Raffle();
				raffle.setDailyAvailableSlot(dailyAvailableSlot);
				raffle.setDailyEmployeeSlot(dailyEmployeeSlot);

				raffleRepository.save(raffle);

			}
		}
		return raffleRepository.findAll();
	}

	@Override
	public ParkingSlotDto getSlotDetails(Long empId) {
		return null;
	}

	public LocalDate getLocalDate(String data) {
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(ParkingConstants.DATE_FORMAT);
		LocalDate dateOfJoiningLocalDate = LocalDate.parse(data, dateTimeFormatter);
		return dateOfJoiningLocalDate;

	}

}
