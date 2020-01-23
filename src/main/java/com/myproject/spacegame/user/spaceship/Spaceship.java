package com.myproject.spacegame.user.spaceship;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
	
	private int spaceshipLvl = 0;
	
	private Long attackPower = 100L;
	private Long defense = 100L;
	private Long speed = 100L;
	private Long stockSize = 10000L;
	
	private double metal = 500;
	private double crystal = 500;
	private double hydrogen = 10;
	private double energy = 0;
	
	private Long remainingBuildingDuration = 0L;
	
}
