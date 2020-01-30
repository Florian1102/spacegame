package com.myproject.spacegame.coordinateSystem;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Setter;

import lombok.Getter;

@Entity
@Getter
@Setter
public class CoordinateSystem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private int galaxy;
	private int system;
	private int position;
	private boolean isAvailable;
}
