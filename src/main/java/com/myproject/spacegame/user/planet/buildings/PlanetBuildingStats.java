package com.myproject.spacegame.user.planet.buildings;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class PlanetBuildingStats {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private int level;
	
	private String nameOfBuilding;
	
	private Long necessaryMetal;
	private Long necessaryCrystal;
	private Long necessaryHydrogen;
	private Long necessaryEnergy;
	
	private double productionMetal;
	private double productionCrystal;
	private double productionHydrogen;
	private double productionEnergy;
	
	private double metalStorehouse;
	private double crystalStorehouse;
	private double hydrogenTank;
	  
	private Long buildingDuration;
}
