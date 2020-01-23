package com.myproject.spacegame.user.technology.technologyStats;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class TechnologyStats {

	@Id
	private Long level;
	
	private String name;
	
	private Long necessaryIron;
	private Long necessaryEnergy;
	
// was bringt es
	
	private Long buildingDuration;
}
