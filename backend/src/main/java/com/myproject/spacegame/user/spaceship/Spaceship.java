package com.myproject.spacegame.user.spaceship;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.myproject.spacegame.coordinateSystem.CoordinateSystem;
import com.myproject.spacegame.user.User;
import com.myproject.spacegame.user.spaceship.flight.Flight;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class Spaceship {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne(mappedBy = "spaceship")
	@JoinColumn(name="user_id")
	@JsonBackReference(value = "userToSpaceshipReference")
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "currentPosition_id")
	private CoordinateSystem currentPosition;
	
	@OneToMany(mappedBy = "spaceship")
	@JsonManagedReference(value = "flightToSpaceship")
	private List<Flight> flights;
	
	@NotNull
	@Column(nullable = false)
	private int spaceshipLvl = 1;
	
	@NotNull
	@Column(nullable = false)
	private Long attackPower = 100L;
	
	@NotNull
	@Column(nullable = false)
	private Long defense = 100L;
	
	@NotNull
	@Column(nullable = false)
	private Long speed = 10L;
	
	@NotNull
	@Column(nullable = false)
	private double metal = 500;
	
	@NotNull
	@Column(nullable = false)
	private double crystal = 500;
	
	@NotNull
	@Column(nullable = false)
	private double hydrogen = 10;
	
	@NotNull
	@Column(nullable = false)
	private double energy = 0;
	
//	@NotNull
//	@Column(nullable = false)
//	private int metalStoreLvl = 0;
//	
//	@NotNull
//	@Column(nullable = false)
//	private int crystalStoreLvl = 0;
//	
//	@NotNull
//	@Column(nullable = false)
//	private int hydrogenTankLvl = 0;
	
	@NotNull
	@Column(nullable = false)
	private double metalStore = 10000;
	
	@NotNull
	@Column(nullable = false)
	private double crystalStore = 10000;
	
	@NotNull
	@Column(nullable = false)
	private double hydrogenTank = 5000;
	
	@NotNull
	@Column(nullable = false)
	private int researchLaboratoryLvl = 0;
	
	@Column
	private LocalDateTime endOfBuilding = null;
	@Column
	private String nameOfBuilding = null;
	@NotNull
	@Column
	private int currentLvlOfBuilding = 0;
	
	@NotNull
	@Column(nullable = false)
	private double hydrogenConsumption = 10;

	@NotNull
	@Column(nullable = false)
	private double reduceBuildingDuration = 1.0;
	
	@NotNull
	@Column(nullable = false)
	private double reduceResearchDuration = 1.0;
	
	private boolean merchantSpaceship = false;
	private boolean fighterSpaceship = false;
	
	@NotNull
	@Column(nullable = false)
	private int counterOfChangedSpecialization = 0;
	
}
