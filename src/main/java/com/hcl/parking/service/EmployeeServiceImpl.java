package com.hcl.parking.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcl.parking.dto.CommonResponse;
import com.hcl.parking.dto.EmployeeDto;
import com.hcl.parking.dto.EmployeeSlotRequestDto;
import com.hcl.parking.dto.SpotOwnerRequestDto;
import com.hcl.parking.entity.DailyAvailableSlot;
import com.hcl.parking.entity.DailyEmployeeSlot;
import com.hcl.parking.entity.Employee;
import com.hcl.parking.entity.ParkingSlot;
import com.hcl.parking.entity.Role;
import com.hcl.parking.exception.CommonException;
import com.hcl.parking.exception.UserNotFoundException;
import com.hcl.parking.repository.DailyAvailableSlotRepository;
import com.hcl.parking.repository.DailyEmployeeSlotRepository;
import com.hcl.parking.repository.EmployeeRepository;
import com.hcl.parking.repository.ParkingRepository;

@Service
public class EmployeeServiceImpl implements EmployeeService {
	@Autowired
	EmployeeRepository employeeRepository;

	@Autowired
	ParkingRepository parkingRepository;

	@Autowired
	DailyAvailableSlotRepository dailyAvailableslotRepo;

	@Autowired
	DailyEmployeeSlotRepository dailyEmployeeSlotRepository;
	Random rand = new Random();

	@Override
	public CommonResponse registration(EmployeeDto employeeDto) {

		Employee employee = new Employee();
		BeanUtils.copyProperties(employeeDto, employee);
		employeeRepository.save(employee);

		if (employeeDto.getRole().equals(Role.SPOT_OWNER)) {
			ParkingSlot parkingSlot = new ParkingSlot();
			parkingSlot.setLocation(employee.getEmpId() + "-A");
			parkingSlot.setEmployee(employee);
			parkingRepository.save(parkingSlot);
		}

		return new CommonResponse("Registration success");
	}

	@Override
	public CommonResponse setFreeParkingSlot(SpotOwnerRequestDto spotOwnerRequestDto) {

		Optional<Employee> employee = employeeRepository.findById(spotOwnerRequestDto.getOwnerId());
		if (!employee.get().getRole().equals(Role.SPOT_OWNER))
			throw new UserNotFoundException("Not Spotowner");

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate fromDate = LocalDate.parse(spotOwnerRequestDto.getFromDate(), formatter);
		LocalDate toDate = LocalDate.parse(spotOwnerRequestDto.getToDate(), formatter);
		if (fromDate.compareTo(LocalDate.now()) == 0 || fromDate.compareTo(LocalDate.now()) > 0) {

			if (fromDate.compareTo(toDate) == 0 || fromDate.compareTo(toDate) < 0) {

				List<LocalDate> dates = new ArrayList<LocalDate>();
				LocalDate current = fromDate;
				while (current.isBefore(toDate) || current.equals(toDate)) {
					dates.add(current);
					current = current.plusDays(1);
					DailyAvailableSlot dailyAvailableSlot = new DailyAvailableSlot();
					dailyAvailableSlot.setTempOwnerId(0L);
					dailyAvailableSlot.setParkingSlotId(employee.get().getEmpId());
					dailyAvailableSlot.setAvailableDates(current);

					dailyAvailableslotRepo.save(dailyAvailableSlot);

				}
				return new CommonResponse("success");

			} else {
				throw new CommonException("To date always greater than or equal to from date");
			}
		} else {
			throw new CommonException("From date always greater tha or equal to current date");
		}

	}

	@Override
	public CommonResponse empSlotRequest(EmployeeSlotRequestDto employeeSlotRequestDto) {

		Optional<Employee> employee = employeeRepository.findById(employeeSlotRequestDto.getEmpId());
		if (!employee.get().getRole().equals(Role.HQ_EMPLOYEES))
			throw new UserNotFoundException("Not Hq employee");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate fromDate = LocalDate.parse(employeeSlotRequestDto.getFromDate(), formatter);
		LocalDate toDate = LocalDate.parse(employeeSlotRequestDto.getToDate(), formatter);
		if (fromDate.compareTo(LocalDate.now()) == 0 || fromDate.compareTo(LocalDate.now()) > 0) {

			if (fromDate.compareTo(toDate) == 0 || fromDate.compareTo(toDate) < 0) {

				List<LocalDate> dates = new ArrayList<>();
				LocalDate current = fromDate;
				while (current.isBefore(toDate) || current.equals(toDate)) {
					dates.add(current);
					current = current.plusDays(1);
					DailyEmployeeSlot dailyEmployeeSlot = new DailyEmployeeSlot();
					dailyEmployeeSlot.setEmpId(employee.get().getEmpId());
					dailyEmployeeSlot.setParkingSlotId(0L);
					dailyEmployeeSlot.setAvailableDates(current);

					dailyEmployeeSlotRepository.save(dailyEmployeeSlot);

				}
				return new CommonResponse("success");

			} else {
				throw new CommonException("To date always greater than or equal to from date");
			}
		} else {
			throw new CommonException("From date always greater tha or equal to current date");
		}
	}

	public void doRaffle(String reqDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate localDateForamt = LocalDate.parse(reqDate, formatter);
		List<DailyAvailableSlot> dailyAvalibaleSlotLists = dailyAvailableslotRepo.findByAvailableDates(localDateForamt);
		List<DailyEmployeeSlot> dailyEmployeeSlotLists = dailyEmployeeSlotRepository.findByAvailableDates(localDateForamt);
		for (int i = 0; i < dailyAvalibaleSlotLists.size(); i++) {
			for (int j = 0; j < dailyEmployeeSlotLists.size(); j++) {
				int randomIndex = rand.nextInt(dailyEmployeeSlotLists.size());
				Long randomEmployeeId = dailyEmployeeSlotLists.get(randomIndex).getEmpId();
				dailyEmployeeSlotLists.remove(randomIndex);
			}
		}
	}

}
