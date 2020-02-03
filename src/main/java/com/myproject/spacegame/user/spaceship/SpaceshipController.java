package com.myproject.spacegame.user.spaceship;

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

	@GetMapping("/{id}")
	public ResponseEntity<?> showSpaceship(@PathVariable Long id) {

		return ResponseEntity.of(spaceshipRepository.findById(id));
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable Long id, @RequestBody @Valid Spaceship spaceship) {

		if (!spaceshipRepository.existsById(id)) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		spaceship.setId(id);
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

				spaceshipWithUpdatedRessources
						.setRemainingBuildingDuration((long) (statsOfBuildingNextLvl.getBuildingOrResearchDuration()
								* spaceshipWithUpdatedRessources.getReduceBuildingDuration()));

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
			return new ResponseEntity<>("Dein Raumschiff hat noch nicht das erforderliche Level", HttpStatus.BAD_REQUEST);
		}

		try {
			if (fighterOrMerchant.equals("fighter")) {
				if (spaceshipFound.isFighterSpaceship()) {
					throw new Exception("Das Raumschiff ist bereits auf Kampf spezialisert");
				}
				changePropertiesOfSpaceship(spaceshipFound, true, false);
				//TODO Function increaseResourceProduction and changeSpeedOfSpaceship
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			} else if (fighterOrMerchant.equals("merchant")) {
				if (spaceshipFound.isMerchantSpaceship()) {
					throw new Exception("Das Raumschiff ist bereits auf Handel spezialisert");
				}
				changePropertiesOfSpaceship(spaceshipFound, false, true);
				//TODO Function changeAttackpowerOfSpaceship and changeSpeedOfSpaceship
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			} else {
				throw new Exception("Spezialisierung nicht bekannt");
			}
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	private void changePropertiesOfSpaceship(Spaceship spaceshipFound, boolean isFighter, boolean isMerchant) throws Exception {
		Long necessaryMetal = 10L * (spaceshipFound.getCounterOfChangedSpecialization() + 1);
		Long necessaryCrystal = 10L * (spaceshipFound.getCounterOfChangedSpecialization() + 1);
		Long necessaryHydrogen = 10L * (spaceshipFound.getCounterOfChangedSpecialization() + 1);
		Long necessaryEnergy = 10L * (spaceshipFound.getCounterOfChangedSpecialization() + 1);
		
		spaceshipFound.setFighterSpaceship(isFighter);
		spaceshipFound.setMerchantSpaceship(isMerchant);
		spaceshipFound.setCounterOfChangedSpecialization(spaceshipFound.getCounterOfChangedSpecialization() + 1);
		Spaceship updatedSpaceship = resourceHandler.calculateNewSpaceshipRessources(spaceshipFound, necessaryMetal, necessaryCrystal,
				necessaryHydrogen, necessaryEnergy);
		spaceshipRepository.save(updatedSpaceship);
	}

	@PutMapping("/{spaceshipId}/{pickUpOrDeliver}/{planetId}/resources") // es ist auch möglich anstatt der spaceshipId
																			// die userId zu nutzen
	public ResponseEntity<?> pickUpResources(@PathVariable Long spaceshipId, @PathVariable String pickUpOrDeliver,
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

			if (pickUpOrDeliver.equals("pickup")) {
				// TODO zum Planeten fliegen
				if (!spaceshipFound.getUser().getPlanets().contains(planetFound)) {
					throw new Exception("Das Abholen von Ressourcen von einem fremden Planete ist nicht erlaubt");
				} else {
					// TODO zum Planeten fliegen
					resourceHandler.pickUpOrDeliverResources(spaceshipFound, planetFound, metal, crystal, hydrogen,
							true);
					return new ResponseEntity<>(HttpStatus.NO_CONTENT);
				}
			} else if (pickUpOrDeliver.equals("deliver")) {
				resourceHandler.pickUpOrDeliverResources(spaceshipFound, planetFound, metal, crystal, hydrogen, false);
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			} else {
				throw new Exception("Funktion nicht bekannt");
			}
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
}