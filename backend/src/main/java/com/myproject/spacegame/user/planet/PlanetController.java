package com.myproject.spacegame.user.planet;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.myproject.spacegame.buildingStats.BuildingStats;
import com.myproject.spacegame.coordinateSystem.CoordinateSystem;
import com.myproject.spacegame.coordinateSystem.CoordinateSystemRepository;
import com.myproject.spacegame.services.GetStatsOfBuildingsAndTechnologies;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class PlanetController {

	private final PlanetRepository planetRepository;
	private final PlanetBuildingHandler planetBuildingHandler;
	private final ResourceHandler resourceHandler;
	private final GetStatsOfBuildingsAndTechnologies getStatsOfBuildingsAndTechnologies;
	private final CoordinateSystemRepository coordinateSystemRepository;

	@GetMapping("/planets")
	@ResponseStatus(HttpStatus.OK)
	public List<Planet> showPlanets() {

		return planetRepository.findAll();
	}

	@GetMapping("/planets/{id}")
	public ResponseEntity<?> showPlanet(@PathVariable Long id) {

		return ResponseEntity.of(planetRepository.findById(id));
	}
	
	@GetMapping("/planetsofuser/{userId}")
	public ResponseEntity<?> showPlanetsOfUser(@PathVariable Long userId) {
		
		List<Planet> planets = planetRepository.findAllByUserId(userId);
		return new ResponseEntity<>(planets, HttpStatus.OK);
	}


	@PutMapping("/planets/{planetId}/rename")
	public ResponseEntity<?> renamePlanet(@PathVariable Long planetId, @RequestBody String name) {
		
		if (!planetRepository.existsById(planetId)) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		Planet planetFound = planetRepository.findById(planetId).get();
		
		planetFound.setName(name);
		planetRepository.save(planetFound);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PutMapping("/planets/{planetId}/{nameOfBuilding}/build")
	public ResponseEntity<?> levelUpPlanetBuilding(@PathVariable Long planetId, @PathVariable String nameOfBuilding) {
		if (!planetRepository.existsById(planetId)) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		Planet planetFound = planetRepository.findById(planetId).get();

		try {
			if (!planetBuildingHandler.proofBuildPossible(planetFound)) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			} else {
				int currentLvlOfSpecificBuilding = planetBuildingHandler.getCurrentLvlOfSpecificBuilding(planetFound,
						nameOfBuilding);
				BuildingStats statsOfBuildingNextLvl = getStatsOfBuildingsAndTechnologies
						.getBuildingOrTechnologyStatsOfNextLvl(currentLvlOfSpecificBuilding, nameOfBuilding);
				Planet planetWithUpdatedRessources = resourceHandler.calculateNewPlanetRessources(planetFound,
						statsOfBuildingNextLvl.getNecessaryMetal(), statsOfBuildingNextLvl.getNecessaryCrystal(),
						statsOfBuildingNextLvl.getNecessaryHydrogen());
				planetWithUpdatedRessources.setNameOfBuilding(nameOfBuilding);
				planetWithUpdatedRessources.setCurrentLvlOfBuilding(currentLvlOfSpecificBuilding);
				LocalDateTime date = LocalDateTime.now();
				planetWithUpdatedRessources.setEndOfBuilding(date.plusSeconds((long) (statsOfBuildingNextLvl.getBuildingOrResearchDuration()* planetWithUpdatedRessources.getReduceBuildingDuration())));
				planetRepository.save(planetWithUpdatedRessources);
				return new ResponseEntity<>(HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping("/planets/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {

		if (!planetRepository.existsById(id)) {
			return new ResponseEntity<>("Der Planet existiert nicht", HttpStatus.NOT_FOUND);
		}
		Planet planet = planetRepository.findById(id).get();
		CoordinateSystem coordinate = coordinateSystemRepository.findByGalaxyAndSystemAndPosition(
				planet.getCoordinates().getGalaxy(), planet.getCoordinates().getSystem(),
				planet.getCoordinates().getPosition());
		coordinate.setPlanet(null);
		coordinateSystemRepository.save(coordinate);
		planetRepository.deleteById(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
