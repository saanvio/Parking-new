package com.hcl.parking.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeSlotRequestDto implements  Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long empId;
	private String fromDate;
	private String toDate;

}
