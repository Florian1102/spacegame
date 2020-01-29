package com.myproject.spacegame.user.technology;

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
public class Technology {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne(mappedBy = "technology")
	@JsonBackReference
	private User user;
	
	private int energyTechnologyLvl = 0;
}
