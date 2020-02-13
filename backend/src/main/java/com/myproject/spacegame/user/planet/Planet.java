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
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.myproject.spacegame.buildingStats.BuildingStats;
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
	@JsonManagedReference
	private User user;
	
	@OneToOne(mappedBy = "planet")
	@JsonManagedReference(value = "planetToCoordinatesReference")
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

	public void setMetalMineLvl(Planet foundPlanet, BuildingStats statsOfMetalMine) {
		this.metalMineLvl = statsOfMetalMine.getLevel();
		this.metalProductionEveryHour = statsOfMetalMine.getProductionMetal() * foundPlanet.getUser().getTechnology().getResourceResearchFactor();
		this.energy -= statsOfMetalMine.getNecessaryEnergy(); //TODO: Wenn eine Stufe erhöht wird, muss die Energy für die alte Stufe wieder addiert werden
	}
	public void setCrystalMineLvl(Planet foundPlanet, BuildingStats statsOfCrystalMine) {
		this.crystalMineLvl = statsOfCrystalMine.getLevel();
		this.crystalProductionEveryHour = statsOfCrystalMine.getProductionCrystal() * foundPlanet.getUser().getTechnology().getResourceResearchFactor();
		this.energy -= statsOfCrystalMine.getNecessaryEnergy(); //TODO: Wenn eine Stufe erhöht wird, muss die Energy für die alte Stufe wieder addiert werden
	}
	public void setHydrogenPlantLvl(Planet foundPlanet, BuildingStats statsOfHydrogenPlant) {
		this.hydrogenPlantLvl = statsOfHydrogenPlant.getLevel();
		this.hydrogenProductionEveryHour = statsOfHydrogenPlant.getProductionHydrogen() * foundPlanet.getUser().getTechnology().getResourceResearchFactor();
		this.energy -= statsOfHydrogenPlant.getNecessaryEnergy(); //TODO: Wenn eine Stufe erhöht wird, muss die Energy für die alte Stufe wieder addiert werden
	}
	public void setSolarPowerPlantLvl(Planet foundPlanet, BuildingStats statsOfSolarPowerPlant) {
		this.solarPowerPlantLvl = statsOfSolarPowerPlant.getLevel();
		this.energy = (statsOfSolarPowerPlant.getProductionEnergy() + (foundPlanet.getSolarSatellite() * 20)) * foundPlanet.getUser().getTechnology().getEnergyResearchFactor();
	}
	public void setSolarSatellite(Planet foundPlanet, BuildingStats statsOfSolarSatellite) {
		this.solarSatellite += 1;
		this.energy = (foundPlanet.getEnergy() + (statsOfSolarSatellite.getProductionEnergy() * foundPlanet.getUser().getTechnology().getEnergyResearchFactor()));
	}
}
