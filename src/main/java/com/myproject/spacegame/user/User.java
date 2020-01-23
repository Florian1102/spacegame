package com.myproject.spacegame.user;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.myproject.spacegame.user.planet.Planet;
import com.myproject.spacegame.user.spaceship.Spaceship;
import com.myproject.spacegame.user.technology.Technology;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="users")
@Getter
@Setter
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@NotBlank
	@Column(nullable = false)
	private String username;
	
	private int points = 0;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "spaceship_id")
	@JsonManagedReference
	private Spaceship spaceship;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "technology_id")
	@JsonManagedReference
	private Technology technology;
	
	@OneToMany(mappedBy = "user")
	@JsonManagedReference
	private List<Planet> planets;
	
}
