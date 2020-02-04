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

import com.myproject.spacegame.buildingStats.BuildingStats;
import com.myproject.spacegame.coordinateSystem.CoordinateSystem;
import com.myproject.spacegame.coordinateSystem.CoordinateSystemRepository;
import com.myproject.spacegame.services.GetStatsOfBuildingsAndTechnologies;
import com.myproject.spacegame.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class PlanetController {

	private final PlanetRepository planetRepository;
	private final PlanetBuildingHandler planetBuildingHandler;
	private final ResourceHandler resourceHandler;
	private final GetStatsOfBuildingsAndTechnologies getStatsOfBuildingsAndTechnologies;
	private final UserRepository userRepository;
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

	@PostMapping("/{userId}/planets/add/{galaxy}/{system}/{position}")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> create(@PathVariable Long userId, @PathVariable int galaxy, @PathVariable int system, @PathVariable int position) {
		try {
			if (!userRepository.existsById(userId)) {
				return new ResponseEntity<>("Spieler wurde nicht gefunden", HttpStatus.NOT_FOUND);
			} else if (!allowToAddPlanet(userId)) {
				return new ResponseEntity<>("Du kannst keine weiteren Planeten kolonisieren", HttpStatus.BAD_REQUEST);
			} else if (!isCoordinateAvailable(galaxy, system, position)) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			} else {
				Planet createdPlanet = setupPlanet(userId, galaxy, system, position);
				planetRepository.save(createdPlanet);
				CoordinateSystem coordinate = coordinateSystemRepository.findByGalaxyAndSystemAndPosition(galaxy, system, position);
				coordinate.setPlanet(createdPlanet);
				coordinateSystemRepository.save(coordinate);
				return new ResponseEntity<>(createdPlanet, HttpStatus.CREATED);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	private boolean allowToAddPlanet(Long userId) throws Exception {

		if (planetRepository.countByUserId(userId) >= 3) {
			throw new Exception("Du kannst keinen weiteren Planeten kolonisieren");
			//TODO: Abhängig von einer Forschung machen
		} else {
			return true;
		}
	}

	private boolean isCoordinateAvailable(int galaxy, int system, int position) throws Exception {
		if (!coordinateSystemRepository.existsByGalaxyAndSystemAndPosition(galaxy, system, position)) {
			throw new Exception("Die Koordinate gibt es nicht");
		} else if (position == 0) {
			throw new Exception("Die Position kann nicht besiedelt werden");
		} else if (!(coordinateSystemRepository.findByGalaxyAndSystemAndPosition(galaxy, system, position) == null)) {
				throw new Exception("Der Planet ist schon besiedelt");
		} else {
			return true;
		}
	}
	
	private Planet setupPlanet(Long userId, int galaxy, int system, int position) {
		Planet planet = new Planet();
		planet.setId(null);
		planet.setUser(userRepository.findById(userId).get());
		Random random = new Random();
		planet.setCoordinates(new Coordinates(galaxy, system, position));
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

	@PutMapping("/planets/{id}")
	public ResponseEntity<?> update(@PathVariable Long id, @RequestBody @Valid Planet planet) {

		if (!planetRepository.existsById(id)) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		planet.setId(id);
		planetRepository.save(planet);
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
				
				planetWithUpdatedRessources.setRemainingBuildingDuration((long) (statsOfBuildingNextLvl.getBuildingOrResearchDuration() * planetWithUpdatedRessources.getReduceBuildingDuration()));
				planetRepository.save(planetWithUpdatedRessources);
				planetBuildingHandler.prepareBuild(planetWithUpdatedRessources, statsOfBuildingNextLvl);
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
		CoordinateSystem coordinate = coordinateSystemRepository.findByGalaxyAndSystemAndPosition(planet.getCoordinates().getGalaxy(), planet
				.getCoordinates().getSystem(), planet.getCoordinates().getPosition());
		coordinate.setPlanet(null);
		coordinateSystemRepository.save(coordinate);
		planetRepository.deleteById(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
