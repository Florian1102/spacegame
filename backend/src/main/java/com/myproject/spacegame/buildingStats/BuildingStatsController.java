package com.myproject.spacegame.buildingStats;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/buildingstats")
@RequiredArgsConstructor
public class BuildingStatsController {

	private final BuildingStatsRepository buildingStatsRepository;
	
	@GetMapping
	public ResponseEntity<?> getBuildingStatsOfBuilding(
			@RequestParam(required = true) String nameOfBuilding, 
			@RequestParam(required = true) int level) {
		if(!buildingStatsRepository.existsByLevelAndNameOfBuildingOTechnology(level, nameOfBuilding)) {
			return new ResponseEntity<>("Die Stufe von " + nameOfBuilding + " existiert nicht", HttpStatus.NOT_FOUND);
		}
		BuildingStats buildingStats = buildingStatsRepository.findByLevelAndNameOfBuildingOTechnology(level, nameOfBuilding);
		return new ResponseEntity<>(buildingStats, HttpStatus.OK);
	}
}
