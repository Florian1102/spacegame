package com.myproject.spacegame.user.spaceship;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.myproject.spacegame.coordinateSystem.CoordinateSystem;
import com.myproject.spacegame.user.User;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Spaceship {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne(mappedBy = "spaceship")
	@JsonBackReference
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "currentPosition_id", nullable = false)
	@JsonBackReference(value = "listOfSpaceshipsInThisSystemReference")
	private CoordinateSystem currentPosition;
	
	private int spaceshipLvl = 1;
	
	private Long attackPower = 100L;
	private Long defense = 100L;
	private Long speed = 100L;
	
	private double metal = 500;
	private double crystal = 500;
	private double hydrogen = 10;
	private double energy = 0;
	
	private int metalStoreLvl = 0;
	private int crystalStoreLvl = 0;
	private int hydrogenTankLvl = 0;
	private double metalStore = 0.0;
	private double crystalStore = 0.0;
	private double hydrogenTank = 0.0;
	private int researchLaboratoryLvl = 0;
	
	private Long remainingBuildingDuration = 0L;
	private Long remainingResearchDuration = 0L;

	private double hydrogenConsumption;

	private double reduceBuildingDuration = 1.0;
	private double reduceResearchDuration = 1.0;
	
	private boolean merchantSpaceship = false;
	private boolean fighterSpaceship = false;
	private int counterOfChangedSpecialization = 0;
	
}
