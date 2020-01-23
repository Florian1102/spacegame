package com.myproject.spacegame.user.spaceship;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class SpaceshipStats {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private int level;
	
	private Long attackPower;
	private Long defense;
	private Long speed;
//	private int stocksize;
	
	private Long buildingDuration;
	
	private Long necessaryMetal;
	private Long necessaryCrystal;
	private Long necessaryHydrogen;
	private Long necessaryEnergy;
	
	private double hydrogenConsumption;
}
