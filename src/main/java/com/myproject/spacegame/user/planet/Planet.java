package com.myproject.spacegame.user.planet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.myproject.spacegame.coordinateSystem.CoordinateSystem;
import com.myproject.spacegame.user.User;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="planets")
@Getter
@Setter
public class Planet {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	@JsonBackReference
	private User user;
	
	@OneToOne(mappedBy = "planet")
	@JsonBackReference(value = "spaceshipToCoordinatesReference")
	private CoordinateSystem coordinates;
	
	@NotNull
	@Column(nullable = false)
	@Min(100)
	@Max(200)
	private int fields;
	
	@NotNull
	@Column(nullable = false)
	private int remainingFields;
	
	@NotNull
	@Column(nullable = false)
	private double metal;
	@NotNull
	@Column(nullable = false)
	private double crystal;
	@NotNull
	@Column(nullable = false)
	private double hydrogen;
	@NotNull
	@Column(nullable = false)
	private double energy;
	
	@NotNull
	@Column(nullable = false)
	private double metalProductionEveryHour;
	@NotNull
	@Column(nullable = false)
	private double crystalProductionEveryHour;
	@NotNull
	@Column(nullable = false)
	private double hydrogenProductionEveryHour;
	
	@NotNull
	@Column(nullable = false)
	private int metalMineLvl;
	@NotNull
	@Column(nullable = false)
	private int crystalMineLvl;
	@NotNull
	@Column(nullable = false)
	private int hydrogenPlantLvl;
	@NotNull
	@Column(nullable = false)
	private int solarPowerPlantLvl;
	@NotNull
	@Column(nullable = false)
	private int metalStorehouseLvl;
	@NotNull
	@Column(nullable = false)
	private int crystalStorehouseLvl;
	@NotNull
	@Column(nullable = false)
	private int hydrogenTankLvl;
	@NotNull
	@Column(nullable = false)
	private double metalStorehouse;
	@NotNull
	@Column(nullable = false)
	private double crystalStorehouse;
	@NotNull
	@Column(nullable = false)
	private double hydrogenTank;
	@NotNull
	@Column(nullable = false)
	private int commandCentralLvl;
	// TODO weiteres Gebäude um schneller bauen zu können

	@NotNull
	@Column(nullable = false)
	private int solarSatellite;
	@NotNull
	@Column(nullable = false)
	private int defenseTower;
	
	@NotNull
	@Column(nullable = false)
	private Long remainingBuildingDuration;
	@NotNull
	@Column(nullable = false)
	private double reduceBuildingDuration;

}
