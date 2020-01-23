package com.myproject.spacegame.planet.buildings;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class PlanetBuildingStats {

	@Id
	private Long level;
	
	private String name;
	
	private Long necessaryIron;
	private Long necessaryEnergy;
	
	private Long productionIron;
	  
	private Long buildingDuration;
}
