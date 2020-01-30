package com.myproject.spacegame.user.technology;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.myproject.spacegame.buildingStats.BuildingStats;
import com.myproject.spacegame.services.GetStatsOfBuildingsAndTechnologies;
import com.myproject.spacegame.user.planet.RessourceHandler;
import com.myproject.spacegame.user.spaceship.Spaceship;
import com.myproject.spacegame.user.spaceship.SpaceshipRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/technology")
@RequiredArgsConstructor
public class TechnologyController {

	private final TechnologyRepository technologyRepository;
	private final TechnologyResearchHandler technologyResearchHandler;
	private final GetStatsOfBuildingsAndTechnologies getStatsOfBuildingsAndTechnologies;
	private final RessourceHandler ressourceHandler;
	private final SpaceshipRepository spaceshipRepository;
	
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<Technology> showTechnologies() {

		return technologyRepository.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> showTechnology(@PathVariable Long id) {

		return ResponseEntity.of(technologyRepository.findById(id));
	}
	
	@PutMapping("/{technologyFromPlayerWithId}/{technologyName}/research")
	public ResponseEntity<?> researchTechnology(@PathVariable Long technologyFromPlayerWithId, @PathVariable String technologyName) {
		if (!technologyRepository.existsById(technologyFromPlayerWithId)) {
			return new ResponseEntity<>("Die Technologien des Spielers nicht gefunden", HttpStatus.NOT_FOUND);
		}
		Technology technologyFound = technologyRepository.findById(technologyFromPlayerWithId).get();
		if (!spaceshipRepository.existsById(technologyFound.getUser().getSpaceship().getId())) {
			return new ResponseEntity<>("Das Raumschiff des Spieler nicht gefunden", HttpStatus.NOT_FOUND);
		}
		Spaceship spaceshipOfPlayer = technologyFound.getUser().getSpaceship();
		try {
			if (!technologyResearchHandler.proofBuildPossible(spaceshipOfPlayer)) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			} else {
				int currentLvlOfSpecificTechnology = technologyResearchHandler.getCurrentLvlOfSpecificTechnology(technologyFound,
						technologyName);
				BuildingStats statsOfTechnologyNextLvl = getStatsOfBuildingsAndTechnologies
						.getBuildingOrTechnologyStatsOfNextLvl(currentLvlOfSpecificTechnology, technologyName);
				Spaceship spaceshipWithUpdatedRessources = ressourceHandler.calculateNewSpaceshipRessources(spaceshipOfPlayer,
						statsOfTechnologyNextLvl.getNecessaryMetal(), statsOfTechnologyNextLvl.getNecessaryCrystal(),
						statsOfTechnologyNextLvl.getNecessaryHydrogen(), statsOfTechnologyNextLvl.getNecessaryEnergy());
				
				spaceshipWithUpdatedRessources.setRemainingResearchDuration((long) (statsOfTechnologyNextLvl.getBuildingOrResearchDuration() * spaceshipWithUpdatedRessources.getReduceResearchDuration()));

				spaceshipRepository.save(spaceshipWithUpdatedRessources);

				technologyResearchHandler.prepareBuild(spaceshipWithUpdatedRessources, technologyFound, statsOfTechnologyNextLvl);
				return new ResponseEntity<>(HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
}
