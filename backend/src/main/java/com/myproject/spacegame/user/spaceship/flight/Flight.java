package com.myproject.spacegame.user.spaceship.flight;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.myproject.spacegame.user.spaceship.Spaceship;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Flight {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "spaceship_id", nullable = false)
	@JsonManagedReference(value = "flightToSpaceship")
	private Spaceship spaceship;
	
	private String action;
	private boolean isFlying;
	
	private int startGalaxy;
	private int startSystem;
	private int startPosition;
	private LocalDateTime startTime;

	private int destinationGalaxy;
	private int destinationSystem;
	private int destinationPosition;
	private LocalDateTime arrivalTime;
	
	private double metal;
	private double crystal;
	private double hydrogen;
	
	private double hydrogenConsumption;
}
