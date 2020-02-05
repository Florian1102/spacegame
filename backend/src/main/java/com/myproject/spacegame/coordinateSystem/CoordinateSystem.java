package com.myproject.spacegame.coordinateSystem;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.myproject.spacegame.user.planet.Planet;
import com.myproject.spacegame.user.spaceship.Spaceship;

import lombok.Setter;

import lombok.Getter;

@Entity
@Getter
@Setter
public class CoordinateSystem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private int galaxy;
	private int system;
	private int position;
	
	@OneToOne
	@JoinColumn(name = "planet_id")
	@JsonManagedReference(value = "spaceshipToCoordinatesReference")
	private Planet planet;
	
	@OneToMany(mappedBy = "currentPosition")
	@JsonManagedReference(value = "listOfSpaceshipsInThisSystemReference")
	private List<Spaceship> listOfSpaceshipsInThisSystem;
}
