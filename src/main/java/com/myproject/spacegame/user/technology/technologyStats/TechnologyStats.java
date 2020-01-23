package com.myproject.spacegame.user.technology.technologyStats;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class TechnologyStats {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private int level;
	
	private String nameOfTechnology;
	
	private String name;
	
	private Long necessaryMetal;
	private Long necessaryCrystal;
	private Long necessaryHydrogen;
	private Long necessaryEnergy;
	
// TODO Was bringt diese FOrschung integrieren
	
	private Long buildingDuration;
}
