package com.myproject.spacegame.user.spaceship;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

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
import com.myproject.spacegame.coordinateSystem.CoordinateSystem;
import com.myproject.spacegame.coordinateSystem.CoordinateSystemRepository;
import com.myproject.spacegame.services.CalculatePointsOfPlayer;
import com.myproject.spacegame.services.GetStatsOfBuildingsAndTechnologies;
import com.myproject.spacegame.user.planet.Planet;
import com.myproject.spacegame.user.planet.PlanetRepository;
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
	private final PlanetRepository planetRepository;
	private final CalculatePointsOfPlayer calculatePointsOfPlayer;
	private final SpaceshipHandler spaceshipHandler;
	private final CoordinateSystemRepository coordinateSystemRepository;

	@GetMapping("/{spaceshipId}")
	public ResponseEntity<?> showSpaceship(@PathVariable Long spaceshipId) {

		return ResponseEntity.of(spaceshipRepository.findById(spaceshipId));
	}
	
	@GetMapping("/{spaceshipId}/calculatedurationtocoordinates")
	public ResponseEntity<?> showFlightDurationToCoordinate(
			@PathVariable Long spaceshipId, 
			@RequestParam(required = true) int galaxy, 
			@RequestParam(required = true) int system,
			@RequestParam(required = true) int position) {
		
		if (!spaceshipRepository.existsById(spaceshipId)) {
			return new ResponseEntity<>("Raumschiff exisitiert nicht", HttpStatus.NOT_FOUND);
		} else if (!coordinateSystemRepository.existsByGalaxyAndSystemAndPosition(galaxy, system, position)) {
			return new ResponseEntity<>("Koordinaten existieren nicht", HttpStatus.NOT_FOUND);
		}
		Spaceship spaceshipFound = spaceshipRepository.findById(spaceshipId).get();
		CoordinateSystem coordinatesFound = coordinateSystemRepository.findByGalaxyAndSystemAndPosition(galaxy, system, position);
		Long calculatedDuration = spaceshipHandler.calculateFlightDuration(spaceshipFound, coordinatesFound);
		return new ResponseEntity<>(calculatedDuration, HttpStatus.OK);
	}
	
	@GetMapping("/{spaceshipId}/calculatehydrogenconsumption")
	public ResponseEntity<?> showHydrogenConsumptionToCoordinate(
			@PathVariable Long spaceshipId, 
			@RequestParam(required = true) int galaxy, 
			@RequestParam(required = true) int system,
			@RequestParam(required = true) int position) {
		
		if (!spaceshipRepository.existsById(spaceshipId)) {
			return new ResponseEntity<>("Raumschiff exisitiert nicht", HttpStatus.NOT_FOUND);
		} else if (!coordinateSystemRepository.existsByGalaxyAndSystemAndPosition(galaxy, system, position)) {
			return new ResponseEntity<>("Koordinaten existieren nicht", HttpStatus.NOT_FOUND);
		}
		Spaceship spaceshipFound = spaceshipRepository.findById(spaceshipId).get();
		CoordinateSystem coordinatesFound = coordinateSystemRepository.findByGalaxyAndSystemAndPosition(galaxy, system, position);
		double calculatedDuration = spaceshipHandler.calculateHydrogenConsumption(spaceshipFound, coordinatesFound);
		return new ResponseEntity<>(calculatedDuration, HttpStatus.OK);
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
				spaceshipWithUpdatedRessources.setRemainingBuildingDuration(statsOfBuildingNextLvl.getBuildingOrResearchDuration());
				spaceshipRepository.save(spaceshipWithUpdatedRessources);

				spaceshipBuildingHandler.prepareBuild(spaceshipWithUpdatedRessources, statsOfBuildingNextLvl);
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
				Spaceship spaceshipWithUpdatedStats = spaceshipHandler.calculateAndSaveSpaceshipStats(spaceshipWithUpdatedSpecialization);
				return new ResponseEntity<>(spaceshipWithUpdatedStats, HttpStatus.NO_CONTENT);
			} else if (fighterOrMerchant.equals("merchant")) {
				if (spaceshipFound.isMerchantSpaceship()) {
					throw new Exception("Das Raumschiff ist bereits auf Handel spezialisert");
				}
				Spaceship spaceshipWithUpdatedSpecialization = changePropertiesOfSpaceship(spaceshipFound, false, true);
				Spaceship spaceshipWithUpdatedStats = spaceshipHandler.calculateAndSaveSpaceshipStats(spaceshipWithUpdatedSpecialization);

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

	@PutMapping("/{spaceshipId}/{pickUpOrDeliver}/{planetId}/resources") //
	public ResponseEntity<?> pickUpOrDeliverResources(@PathVariable Long spaceshipId, @PathVariable String pickUpOrDeliver,
			@PathVariable Long planetId, @RequestParam(required = true) Long metal,
			@RequestParam(required = true) Long crystal, @RequestParam(required = true) Long hydrogen) {

		if (!spaceshipRepository.existsById(spaceshipId)) {
			return new ResponseEntity<>("Das Raumschiff existiert nicht", HttpStatus.NOT_FOUND);
		} else if (!planetRepository.existsById(planetId)) {
			return new ResponseEntity<>("Der Planet existiert nicht", HttpStatus.NOT_FOUND);
		}
		try {
			Spaceship spaceshipFound = spaceshipRepository.findById(spaceshipId).get();
			Planet planetFound = planetRepository.findById(planetId).get();
			ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
			if (spaceshipFound.getFlightDuration() > 0) {
				return new ResponseEntity<>("Das Raumschiff fliegt bereits", HttpStatus.BAD_REQUEST);
			} else if (pickUpOrDeliver.equals("pickup")) {
				if (!spaceshipFound.getUser().getPlanets().contains(planetFound)) {
					throw new Exception("Das Abholen von Ressourcen von einem fremden Planete ist nicht erlaubt");
				} else {
					double hydrogenConsumptionForTheFlight = spaceshipHandler.calculateHydrogenConsumption(spaceshipFound, planetFound.getCoordinates());
					if (hydrogenConsumptionForTheFlight > spaceshipFound.getHydrogen()) {
						throw new Exception("Du hast nicht ausreichend Wasserstoff");
					}
					spaceshipFound.setHydrogen(spaceshipFound.getHydrogen() - hydrogenConsumptionForTheFlight);
					Long flightDuration = spaceshipHandler.calculateFlightDuration(spaceshipFound, planetFound.getCoordinates()); //getCoordniates zu coordinatesystem ändern
					spaceshipFound.setFlightDuration(flightDuration * 2);
					spaceshipRepository.save(spaceshipFound);
					
					@SuppressWarnings("unused")
					ScheduledFuture<?> scheduledFuture = executorService.schedule(new Callable<Object>() {
						public Object call() throws Exception {
							try {
								resourceHandler.pickUpOrDeliverResources(spaceshipFound.getId(), planetFound.getId(), metal,
										crystal, hydrogen, true);
								spaceshipHandler.flyBack(spaceshipFound.getId(), flightDuration);
								return new ResponseEntity<>(HttpStatus.NO_CONTENT);
							} catch (Exception e) {
								return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
							}
						}
					}, flightDuration, TimeUnit.SECONDS);
					executorService.shutdown();
					return new ResponseEntity<>(HttpStatus.ACCEPTED);
				}
			} else if (pickUpOrDeliver.equals("deliver")) {
				double hydrogenConsumptionForTheFlight = spaceshipHandler.calculateHydrogenConsumption(spaceshipFound, planetFound.getCoordinates());
				if (hydrogenConsumptionForTheFlight > spaceshipFound.getHydrogen()) {
					throw new Exception("Du hast nicht ausreichend Wasserstoff");
				}
				spaceshipFound.setHydrogen(spaceshipFound.getHydrogen() - hydrogenConsumptionForTheFlight);
				Long flightDuration = spaceshipHandler.calculateFlightDuration(spaceshipFound, planetFound.getCoordinates()); 
				spaceshipFound.setFlightDuration(flightDuration * 2);
				spaceshipRepository.save(spaceshipFound);

				@SuppressWarnings("unused")
				ScheduledFuture<?> scheduledFuture = executorService.schedule(new Callable<Object>() {
					public Object call() throws Exception {
						resourceHandler.pickUpOrDeliverResources(spaceshipFound.getId(), planetFound.getId(), metal,
								crystal, hydrogen, false);
						spaceshipHandler.flyBack(spaceshipFound.getId(), flightDuration);
						return new ResponseEntity<>(HttpStatus.NO_CONTENT);
					}
				}, flightDuration, TimeUnit.SECONDS); 
				executorService.shutdown();
				return new ResponseEntity<>(HttpStatus.ACCEPTED);
			} else {
				throw new Exception("Funktion nicht bekannt");
			}
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
}