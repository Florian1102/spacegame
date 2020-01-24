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

import com.myproject.spacegame.user.spaceship.SpaceshipRessourceHandler;
import com.myproject.spacegame.user.technology.technologyService.TechnologyHandler;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/technology")
@RequiredArgsConstructor
public class TechnologyController {

	private final TechnologyRepository technologyRepository;
	private final TechnologyHandler technologyHandler;
	private final SpaceshipRessourceHandler spaceshipRessourceHandler;
	
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<Technology> showTechnologies() {

		return technologyRepository.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> showTechnology(@PathVariable Long id) {

		return ResponseEntity.of(technologyRepository.findById(id));
	}
	
	@PutMapping("/{technologyFromPlayerWithId}/{technologyName}/levelup")
	public ResponseEntity<?> levelUpEnergyTechnology(@PathVariable Long technologyFromPlayerWithId, @PathVariable String technologyName) {
		if (!technologyRepository.existsById(technologyFromPlayerWithId)) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		Technology foundTechnology = technologyRepository.findById(technologyFromPlayerWithId).get();
		try {
			if (!technologyHandler.proofIncreasePossible(foundTechnology, technologyName)) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			} else {
				spaceshipRessourceHandler.calculateNewSpaceshipRessourcesTechnology(foundTechnology.getUser().getSpaceship(), foundTechnology, technologyName);
				
				return new ResponseEntity<>(HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
}
