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
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.myproject.spacegame.tradeOffers.TradeOffer;
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
	@Size(min = 3, max = 12)
	@Column(nullable = false)
	private String name;
	
	@NotNull
	@Column(nullable = false)
	private double points = 0;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "spaceship_id")
	@JsonManagedReference(value = "userToSpaceshipReference")
	private Spaceship spaceship;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "technology_id")
	@JsonManagedReference(value = "userToTechnologyReference")
	private Technology technology;
	
	@OneToMany(mappedBy = "user")
	@JsonManagedReference(value = "userToPlanetReference")
	private List<Planet> planets;
	
	@OneToMany(mappedBy = "tradeOfferOfUser")
	@JsonIgnore
	private List<TradeOffer> tradeOffersOfUser;

	@OneToMany(mappedBy = "acceptedByUser")
	@JsonIgnore
	private List<TradeOffer> tradeOfferAcceptedByUser;
	
	@NotNull
	@Column(nullable = false)
	private int daysLoggedIn = 0;
}
