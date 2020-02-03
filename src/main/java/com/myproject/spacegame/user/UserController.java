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

import com.myproject.spacegame.coordinateSystem.CoordinateSystem;
import com.myproject.spacegame.coordinateSystem.CoordinateSystemRepository;
import com.myproject.spacegame.user.planet.Coordinates;
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
	public List<User> showUsers(@RequestParam(required = false) String username) {
		
		if (username != null) {
			return userRepository.findByUsernameContains(username);
		}
		return userRepository.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> showUser(@PathVariable Long id) {
		
		return ResponseEntity.of(userRepository.findById(id));
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public User create(@RequestBody @Valid User user) {

		user.setId(null);
		user.setPoints(0);
		user.setPlanets(new ArrayList<Planet>());
		
		Spaceship spaceship = new Spaceship();
		Random random = new Random();
		int galaxy = random.nextInt(1) + 1;
		int system = random.nextInt(1) + 1;
		spaceship.setCurrentPosition(coordinateSystemRepository.findByGalaxyAndSystemAndPosition(galaxy, system, 0)); //Zahlen kann man noch abhängig von dem CoordinateRepository machen
		user.setSpaceship(spaceship);
		
		CoordinateSystem coordinate = coordinateSystemRepository.findByGalaxyAndSystemAndPosition(galaxy, system, 0);
		coordinate.getListOfSpaceshipsInThisSystem().add(spaceship);
		coordinateSystemRepository.save(coordinate);
		
		Technology technology = new Technology();
		user.setTechnology(technology);
		return userRepository.save(user);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable Long id, @RequestBody @Valid User user) {
		
		if (!userRepository.existsById(id)) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		user.setId(id);
		userRepository.save(user);
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
