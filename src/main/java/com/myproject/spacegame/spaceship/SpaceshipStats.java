package com.myproject.spacegame.spaceship;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class SpaceshipStats {

	@Id
	private Long level;
	
	private Long attackPower;
	private Long defense;
	private Long speed;
//	private int stocksize;
	private Long buildingDuration;
	private Long necessaryIron;
	private double fuelConsumption;
}
