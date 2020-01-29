package com.myproject.spacegame.user.attackAndDefenseSystem;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class AttackAndDefenseSystemStats {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String nameOfAttackDefenseSystem;
	
	private Long necessaryMetal;
	private Long necessaryCrystal;
	private Long necessaryHydrogen;
	private Long necessaryEnergy;
	
	private double energy;
	
	private Long attackPower;
	private Long defense;
	  
	private Long buildingDuration;
}
