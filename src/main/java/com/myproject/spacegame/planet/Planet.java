package com.myproject.spacegame.planet;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
	
	private int size;
	
	private double iron;
//	private int silver;
	private Long energy;
	
	private Long ironMineLvl;
	private double ironProductionEveryHour;
	
	private Long remainingBuildingDuration;

}
