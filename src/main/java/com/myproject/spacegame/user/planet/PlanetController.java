package com.myproject.spacegame.user.planet;

import java.util.List;
import java.util.Random;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.myproject.spacegame.services.GetStatsOfBuildingsAndTechnologies;
import com.myproject.spacegame.user.planet.buildings.PlanetBuildingHandler;
import com.myproject.spacegame.user.planet.buildings.BuildingStats;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/planets")
@RequiredArgsConstructor
public class PlanetController {

	private final PlanetRepository planetRepository;
	private final PlanetBuildingHandler planetBuildingHandler;
	private final RessourceHandler ressourceHandler;
	private final GetStatsOfBuildingsAndTechnologies getStatsOfBuildingsAndTechnologies;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<Planet> showPlanets() {

		return planetRepository.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> showPlanet(@PathVariable Long id) {

		return ResponseEntity.of(planetRepository.findById(id));
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> create(@RequestBody @Valid Planet planet) {

		try {
			Planet setupPlanet = proofUserPlanets(planet);
			planetRepository.save(setupPlanet);
			return new ResponseEntity<>(setupPlanet, HttpStatus.CREATED);

		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	public Planet proofUserPlanets(Planet planet) throws Exception {

		if (planetRepository.countByUserId(planet.getUser().getId()) >= 3) {
			throw new Exception("Du kannst keinen weiteren Planeten kolonisieren");
		} else {
			Planet setupPlanet = setupPlanet(planet);
			return setupPlanet;
		}
	}

	public Planet setupPlanet(Planet planet) {
		planet.setId(null);
		Random random = new Random();
		planet.setFields(100 + random.nextInt(200 - 100 + 1));
		planet.setRemainingFields(planet.getFields());
		planet.setMetal(500);
		planet.setCrystal(500);
		planet.setHydrogen(0);
		planet.setEnergy(0);
		planet.setMetalProductionEveryHour(10);
		planet.setCrystalProductionEveryHour(10);
		planet.setHydrogenProductionEveryHour(0);
		planet.setMetalMineLvl(0);
		planet.setCrystalMineLvl(0);
		planet.setHydrogenPlantLvl(0);
		planet.setSolarPowerPlantLvl(0);
		planet.setMetalStorehouseLvl(0);
		planet.setCrystalStorehouseLvl(0);
		planet.setHydrogenTankLvl(0);
		planet.setMetalStorehouse(5000);
		planet.setCrystalStorehouse(5000);
		planet.setHydrogenTank(1000);
		planet.setCommandCentralLvl(1);
		planet.setRemainingBuildingDuration(0L);
		planet.setReduceBuildingDuration(1.0);
		return planet;
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable Long id, @RequestBody @Valid Planet planet) {

		if (!planetRepository.existsById(id)) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		planet.setId(id);
		planetRepository.save(planet);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PutMapping("/{planetId}/{nameOfBuilding}/build")
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
						.getBuildingStatsOfNextLvl(currentLvlOfSpecificBuilding, nameOfBuilding);
				Planet planetWithUpdatedRessources = ressourceHandler.calculateNewPlanetRessources(planetFound,
						statsOfBuildingNextLvl.getNecessaryMetal(), statsOfBuildingNextLvl.getNecessaryCrystal(),
						statsOfBuildingNextLvl.getNecessaryHydrogen());
				
				planetWithUpdatedRessources.setRemainingBuildingDuration((long) (statsOfBuildingNextLvl.getBuildingDuration() * planetWithUpdatedRessources.getReduceBuildingDuration()));
				planetRepository.save(planetWithUpdatedRessources);
				planetBuildingHandler.prepareBuild(planetWithUpdatedRessources, statsOfBuildingNextLvl);
				return new ResponseEntity<>(HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {

		if (!planetRepository.existsById(id)) {
			return new ResponseEntity<>("Der Planet existiert nicht", HttpStatus.NOT_FOUND);
		}
		planetRepository.deleteById(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
