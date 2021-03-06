package com.myproject.spacegame.user.spaceship;

import java.time.LocalDateTime;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.myproject.spacegame.buildingStats.BuildingStats;
import com.myproject.spacegame.coordinateSystem.CoordinateSystemRepository;
import com.myproject.spacegame.services.CalculatePointsOfPlayer;
import com.myproject.spacegame.services.GetStatsOfBuildingsAndTechnologies;
import com.myproject.spacegame.user.planet.ResourceHandler;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/spaceships")
@RequiredArgsConstructor
public class SpaceshipController {

	private final SpaceshipRepository spaceshipRepository;
	private final SpaceshipBuildingHandler spaceshipBuildingHandler;
	private final GetStatsOfBuildingsAndTechnologies getStatsOfBuildingsAndTechnologies;
	private final ResourceHandler resourceHandler;
	private final CalculatePointsOfPlayer calculatePointsOfPlayer;
	private final SpaceshipHandler spaceshipHandler;
	private final CoordinateSystemRepository coordinateSystemRepository;

	@GetMapping("/{spaceshipId}")
	public ResponseEntity<?> showSpaceship(@PathVariable Long spaceshipId) {

		return ResponseEntity.of(spaceshipRepository.findById(spaceshipId));
	}
	
	@GetMapping("/{spaceshipId}/calculateHydrogenConsumptionForFlightTo")
	public ResponseEntity<?> getHydrogenConsumptionForFlight(@PathVariable Long spaceshipId,
			@RequestParam(required = true) int galaxy, @RequestParam(required = true) int system,
			@RequestParam(required = true) int position) {

		if (!spaceshipRepository.existsById(spaceshipId)) {
			return new ResponseEntity<>("Raumschiff wurde nicht gefunden", HttpStatus.NOT_FOUND);
		} else if (!coordinateSystemRepository.existsByGalaxyAndSystemAndPosition(galaxy, system, position)) {
			return new ResponseEntity<>("Koordinate wurde nicht gefunden", HttpStatus.NOT_FOUND);
		} else {
			Spaceship spaceshipFound = spaceshipRepository.findById(spaceshipId).get();
			double hydrogenConsumption = spaceshipHandler.calculateHydrogenConsumption(spaceshipFound, galaxy, system,
					position);
			return new ResponseEntity<>(hydrogenConsumption, HttpStatus.OK);
		}
	}
	
	@GetMapping("/{spaceshipId}/calculateDurationForFlightTo")
	public ResponseEntity<?> getDurationForFlight(@PathVariable Long spaceshipId,
			@RequestParam(required = true) int galaxy, @RequestParam(required = true) int system,
			@RequestParam(required = true) int position) {
		
		if (!spaceshipRepository.existsById(spaceshipId)) {
			return new ResponseEntity<>("Raumschiff wurde nicht gefunden", HttpStatus.NOT_FOUND);
		} else if (!coordinateSystemRepository.existsByGalaxyAndSystemAndPosition(galaxy, system, position)) {
			return new ResponseEntity<>("Koordinate wurde nicht gefunden", HttpStatus.NOT_FOUND);
		} else {
			Spaceship spaceshipFound = spaceshipRepository.findById(spaceshipId).get();
			Long flightDuration = spaceshipHandler.calculateFlightDuration(spaceshipFound, galaxy, system, position);
			LocalDateTime date = LocalDateTime.now();
			LocalDateTime arrivalTime = date.plusSeconds(flightDuration);
			return new ResponseEntity<>(arrivalTime, HttpStatus.OK);
		}
	}


	@PutMapping("/{spaceshipId}")
	public ResponseEntity<?> update(@PathVariable Long spaceshipId, @RequestBody @Valid Spaceship spaceship) {

		if (!spaceshipRepository.existsById(spaceshipId)) {
			return new ResponseEntity<>("Raumschiff exisitiert nicht", HttpStatus.NOT_FOUND);
		}

		spaceship.setId(spaceshipId);
		spaceshipRepository.save(spaceship);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@PutMapping("/{spaceshipId}/{nameOfBuilding}/build")
	public ResponseEntity<?> levelUpSpaceshipBuilding(@PathVariable Long spaceshipId,
			@PathVariable String nameOfBuilding) {
		if (!spaceshipRepository.existsById(spaceshipId)) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		Spaceship spaceshipFound = spaceshipRepository.findById(spaceshipId).get();

		try {
			if (!spaceshipBuildingHandler.proofBuildPossible(spaceshipFound)) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			} else {
				int currentLvlOfSpecificBuilding = spaceshipBuildingHandler
						.getCurrentLvlOfSpecificBuilding(spaceshipFound, nameOfBuilding);
				BuildingStats statsOfBuildingNextLvl = getStatsOfBuildingsAndTechnologies
						.getBuildingOrTechnologyStatsOfNextLvl(currentLvlOfSpecificBuilding, nameOfBuilding);
				Spaceship spaceshipWithUpdatedRessources = resourceHandler.calculateNewSpaceshipRessources(
						spaceshipFound, statsOfBuildingNextLvl.getNecessaryMetal(),
						statsOfBuildingNextLvl.getNecessaryCrystal(), statsOfBuildingNextLvl.getNecessaryHydrogen(),
						statsOfBuildingNextLvl.getNecessaryEnergy());
				spaceshipWithUpdatedRessources.setNameOfBuilding(nameOfBuilding);
				spaceshipWithUpdatedRessources.setCurrentLvlOfBuilding(currentLvlOfSpecificBuilding);
				LocalDateTime date = LocalDateTime.now();
				spaceshipWithUpdatedRessources.setEndOfBuilding(date.plusSeconds((long) (statsOfBuildingNextLvl.getBuildingOrResearchDuration()*spaceshipWithUpdatedRessources.getReduceBuildingDuration())));
				spaceshipRepository.save(spaceshipWithUpdatedRessources);

				return new ResponseEntity<>(HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/{spaceshipId}/choosefighterormerchant/{fighterOrMerchant}")
	public ResponseEntity<?> changeToFighterOrMerchant(@PathVariable Long spaceshipId,
			@PathVariable String fighterOrMerchant) {

		if (!spaceshipRepository.existsById(spaceshipId)) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		Spaceship spaceshipFound = spaceshipRepository.findById(spaceshipId).get();
		if (spaceshipFound.getSpaceshipLvl() < 5) {
			return new ResponseEntity<>("Dein Raumschiff hat noch nicht das erforderliche Level",
					HttpStatus.BAD_REQUEST);
		}

		try {
			if (fighterOrMerchant.equals("fighter")) {
				if (spaceshipFound.isFighterSpaceship()) {
					throw new Exception("Das Raumschiff ist bereits auf Kampf spezialisert");
				}
				Spaceship spaceshipWithUpdatedSpecialization = changePropertiesOfSpaceship(spaceshipFound, true, false);
				Spaceship spaceshipWithUpdatedStats = spaceshipHandler
						.calculateAndSaveSpaceshipStats(spaceshipWithUpdatedSpecialization);
				return new ResponseEntity<>(spaceshipWithUpdatedStats, HttpStatus.NO_CONTENT);
			} else if (fighterOrMerchant.equals("merchant")) {
				if (spaceshipFound.isMerchantSpaceship()) {
					throw new Exception("Das Raumschiff ist bereits auf Handel spezialisert");
				}
				Spaceship spaceshipWithUpdatedSpecialization = changePropertiesOfSpaceship(spaceshipFound, false, true);
				Spaceship spaceshipWithUpdatedStats = spaceshipHandler
						.calculateAndSaveSpaceshipStats(spaceshipWithUpdatedSpecialization);

				return new ResponseEntity<>(spaceshipWithUpdatedStats, HttpStatus.NO_CONTENT);
			} else {
				throw new Exception("Spezialisierung nicht bekannt");
			}
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	private Spaceship changePropertiesOfSpaceship(Spaceship spaceshipFound, boolean isFighter, boolean isMerchant)
			throws Exception {
		Long necessaryMetal = 10L * (spaceshipFound.getCounterOfChangedSpecialization() + 1);
		Long necessaryCrystal = 10L * (spaceshipFound.getCounterOfChangedSpecialization() + 1);
		Long necessaryHydrogen = 10L * (spaceshipFound.getCounterOfChangedSpecialization() + 1);
		Long necessaryEnergy = 10L * (spaceshipFound.getCounterOfChangedSpecialization() + 1);

		spaceshipFound.setFighterSpaceship(isFighter);
		spaceshipFound.setMerchantSpaceship(isMerchant);
		spaceshipFound.setCounterOfChangedSpecialization(spaceshipFound.getCounterOfChangedSpecialization() + 1);
		Spaceship updatedSpaceship = resourceHandler.calculateNewSpaceshipRessources(spaceshipFound, necessaryMetal,
				necessaryCrystal, necessaryHydrogen, necessaryEnergy);
		calculatePointsOfPlayer.calculateAndSaveNewPoints(spaceshipFound.getUser().getId(), necessaryMetal,
				necessaryCrystal, necessaryHydrogen);
		return updatedSpaceship;
	}

}