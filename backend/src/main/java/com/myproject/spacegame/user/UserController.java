package com.myproject.spacegame.user;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.myproject.spacegame.coordinateSystem.CoordinateSystemRepository;
import com.myproject.spacegame.user.planet.Planet;
import com.myproject.spacegame.user.spaceship.Spaceship;
import com.myproject.spacegame.user.technology.Technology;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

	private final UserRepository userRepository;
	private final CoordinateSystemRepository coordinateSystemRepository;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> showUserByName(@RequestParam(required = false) String username) {

		if (username == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else if (!userRepository.existsByNameEquals(username)) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			User user = userRepository.findByNameEquals(username);
			return new ResponseEntity<>(user, HttpStatus.OK);
		}
	}

	@GetMapping("/highscore")
	@ResponseStatus(HttpStatus.OK)
	public List<User> showHighscore() {

		return userRepository.findAllByOrderByPointsDesc();
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> showUser(@PathVariable Long id) {

		return ResponseEntity.of(userRepository.findById(id));
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public User create(@RequestBody @Valid User user) {

		user.setId(null);
		user.setPlanets(new ArrayList<Planet>());

		Spaceship spaceship = new Spaceship();
		Random random = new Random();
		int galaxy = random.nextInt(10) + 1;
		int system = random.nextInt(100) + 1; 
		spaceship.setCurrentPosition(coordinateSystemRepository.findByGalaxyAndSystemAndPosition(galaxy, system, 0)); 
		user.setSpaceship(spaceship);

		Technology technology = new Technology();
		user.setTechnology(technology);
		return userRepository.save(user);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable Long id, @RequestBody @Valid User user) {

		if (!userRepository.existsById(id)) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		// TODO Hier muss evtl noch ein User gefunden und dann geädert werden
		user.setId(id);
		userRepository.save(user);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PutMapping("/{userId}/rename")
	public ResponseEntity<?> renameUser(@PathVariable Long userId, @RequestBody String name) {
		
		if (!userRepository.existsById(userId)) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		User userFound = userRepository.findById(userId).get();
		
		userFound.setName(name);
		userRepository.save(userFound);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {

		if (!userRepository.existsById(id)) {
			return new ResponseEntity<>("Der User existiert nicht", HttpStatus.NOT_FOUND);
		}
		userRepository.deleteById(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
