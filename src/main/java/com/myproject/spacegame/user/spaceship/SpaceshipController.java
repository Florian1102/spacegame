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

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/spaceships")
@RequiredArgsConstructor
public class SpaceshipController {

	private final SpaceshipRepository spaceshipRepository;
	private final SpaceshipBuildingHandler spaceshipBuildingHandler;
	private final SpaceshipRessourceHandler spaceshipRessourceHandler;

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

	@PutMapping("/{id}/levelup")
	public ResponseEntity<?> levelUpSpaceship(@PathVariable Long id) {
		if (!spaceshipRepository.existsById(id)) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		Spaceship spaceshipFound = spaceshipRepository.findById(id).get();

		try {
			if (!spaceshipBuildingHandler.proofUpdatePossible(spaceshipFound)) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			} else {
				@SuppressWarnings("unused")
				Spaceship spaceshipWithUpdatedRessources = spaceshipRessourceHandler.calculateNewSpaceshipRessources(spaceshipFound);
				return new ResponseEntity<>(HttpStatus.OK);

			}
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
}