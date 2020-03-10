package com.myproject.spacegame.user.technology;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.myproject.spacegame.buildingStats.BuildingStats;
import com.myproject.spacegame.user.User;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Technology {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne(mappedBy = "technology")
	@JsonBackReference(value = "userToTechnologyReference")
	private User user;
	
	private int energyResearchLvl = 0;
	private double energyResearchFactor = 1;
	
	private int resourceResearchLvl = 0;
	private double resourceResearchFactor = 1;
	//TODO: Weitere Forschungen hinzufügen
	
	@Column
	private LocalDateTime endOfResearch = null;
	@Column
	private String nameOfResearch = null;
	@NotNull
	@Column
	private int currentLvlOfResearch = 0;
	
	public void setEnergyResearchLvl(BuildingStats statsOfEnergyReserach) {
		this.energyResearchLvl = statsOfEnergyReserach.getLevel();
		this.energyResearchFactor = statsOfEnergyReserach.getEnergyResearchFactor();
	}
	public void setResourceResearchLvl(BuildingStats statsOfResourceReserach) {
		this.resourceResearchLvl = statsOfResourceReserach.getLevel();
		this.resourceResearchFactor = statsOfResourceReserach.getResourceResearchFactor();
	}
}
