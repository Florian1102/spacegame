package com.myproject.spacegame.user.spaceship;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myproject.spacegame.services.GetStatsOfBuildingsAndTechnologies;
import com.myproject.spacegame.user.planet.RessourceHandler;
import com.myproject.spacegame.user.planet.buildings.BuildingStats;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/spaceships")
@RequiredArgsConstructor
public class SpaceshipController {

	private final SpaceshipRepository spaceshipRepository;
	private final SpaceshipBuildingHandler spaceshipBuildingHandler;
	private final GetStatsOfBuildingsAndTechnologies getStatsOfBuildingsAndTechnologies;
	private final RessourceHandler ressourceHandler;

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
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PutMapping("/{spaceshipId}/{nameOfBuilding}/build")
	public ResponseEntity<?> levelUpSpaceshipBuilding(@PathVariable Long spaceshipId, @PathVariable String nameOfBuilding) {
		if (!spaceshipRepository.existsById(spaceshipId)) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		Spaceship spaceshipFound = spaceshipRepository.findById(spaceshipId).get();

		try {
			if (!spaceshipBuildingHandler.proofBuildPossible(spaceshipFound)) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			} else {
				int currentLvlOfSpecificBuilding = spaceshipBuildingHandler.getCurrentLvlOfSpecificBuilding(spaceshipFound,
						nameOfBuilding);
				BuildingStats statsOfBuildingNextLvl = getStatsOfBuildingsAndTechnologies
						.getBuildingOrTechnologyStatsOfNextLvl(currentLvlOfSpecificBuilding, nameOfBuilding);
				Spaceship spaceshipWithUpdatedRessources = ressourceHandler.calculateNewSpaceshipRessources(spaceshipFound,
						statsOfBuildingNextLvl.getNecessaryMetal(), statsOfBuildingNextLvl.getNecessaryCrystal(),
						statsOfBuildingNextLvl.getNecessaryHydrogen(), statsOfBuildingNextLvl.getNecessaryEnergy());
				
				spaceshipWithUpdatedRessources.setRemainingBuildingDuration((long) (statsOfBuildingNextLvl.getBuildingOrResearchDuration() * spaceshipWithUpdatedRessources.getReduceBuildingDuration()));

				spaceshipRepository.save(spaceshipWithUpdatedRessources);

				spaceshipBuildingHandler.prepareBuild(spaceshipWithUpdatedRessources, statsOfBuildingNextLvl);
				return new ResponseEntity<>(HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
}