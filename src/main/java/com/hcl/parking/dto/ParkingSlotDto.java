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
public class ParkingSlotDto implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	private Long parkingSlotId;
	private String slotName;


}
