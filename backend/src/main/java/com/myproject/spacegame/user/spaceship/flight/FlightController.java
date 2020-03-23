package com.myproject.spacegame.user.spaceship.flight;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.myproject.spacegame.coordinateSystem.CoordinateSystemRepository;
import com.myproject.spacegame.user.planet.ColonizePlanet;
import com.myproject.spacegame.user.planet.Planet;
import com.myproject.spacegame.user.spaceship.Spaceship;
import com.myproject.spacegame.user.spaceship.SpaceshipHandler;
import com.myproject.spacegame.user.spaceship.SpaceshipRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/flights")
@RequiredArgsConstructor
public class FlightController {

	private final FlightRepository flightRepository;
	private final SpaceshipRepository spaceshipRepository;
	private final CoordinateSystemRepository coordinateSystemRepository;
	private final SpaceshipHandler spaceshipHandler;
	private final ColonizePlanet colonizePlanet;

	@GetMapping()
	@ResponseStatus(HttpStatus.OK)
	public List<Flight> showCurrentFlights() {

		return flightRepository.findAllByIsFlyingTrue();
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> showFlight(@PathVariable Long id) {

		return ResponseEntity.of(flightRepository.findById(id));
	}

	@GetMapping("/{spaceshipId}/activeflight")
	public ResponseEntity<?> showActiveFlightOfUser(@PathVariable Long spaceshipId) {

//		if (!flightRepository.existsBySpaceshipIdAndIsFlyingTrue(spaceshipId)) {
//			return new ResponseEntity<>("Kein aktiver Flug", HttpStatus.NOT_FOUND);
//		}
		Flight flight = flightRepository.findBySpaceshipIdAndIsFlyingTrue(spaceshipId);
		return new ResponseEntity<>(flight, HttpStatus.OK);
	}
	
	@PostMapping("/{spaceshipId}/{action}")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> create(@PathVariable Long spaceshipId, @PathVariable String action,
			@RequestParam(required = true) int galaxy, @RequestParam(required = true) int system,
			@RequestParam(required = true) int position, @RequestParam(required = true) double metal,
			@RequestParam(required = true) double crystal, @RequestParam(required = true) double hydrogen) {
		try {
			if (!spaceshipRepository.existsById(spaceshipId)) {
				return new ResponseEntity<>("Raumschiff wurde nicht gefunden", HttpStatus.NOT_FOUND);
			} else if (!proofAction(action)) {
				return new ResponseEntity<>("Aktion nicht bekannt", HttpStatus.BAD_REQUEST);
			} else if (!coordinateSystemRepository.existsByGalaxyAndSystemAndPosition(galaxy, system, position)) {
				return new ResponseEntity<>("Koordinate wurde nicht gefunden", HttpStatus.NOT_FOUND);
			} else {
				Spaceship spaceshipFound = spaceshipRepository.findById(spaceshipId).get();
				if (spaceshipFound.getCurrentPosition() == null) {
					return new ResponseEntity<>("Das Raumschiff fliegt bereits", HttpStatus.BAD_REQUEST);
				}

				Planet planetFound = coordinateSystemRepository
						.findByGalaxyAndSystemAndPosition(galaxy, system, position).getPlanet();
				if ((action.equals("station") || action.equals("pickup"))
						&& !spaceshipFound.getUser().getPlanets().contains(planetFound)) {
					return new ResponseEntity<>("Aktion bei einem fremden Planeten nicht erlaubt",
							HttpStatus.BAD_REQUEST);
				} else if (action.equals("attack") && spaceshipFound.getUser().getPlanets().contains(planetFound)) {
					return new ResponseEntity<>("Du kannst deinen eigenen Planeten nicht angreifen",
							HttpStatus.BAD_REQUEST);
				} else if (action.equals("colonize") && (coordinateSystemRepository
						.findByGalaxyAndSystemAndPosition(galaxy, system, position).getPlanet() != null
						|| !colonizePlanet.allowToAddPlanet(spaceshipFound.getUser().getId()))) {
					return new ResponseEntity<>("Planet besiedeln ist nicht möglich", HttpStatus.BAD_REQUEST);
				}

				double hydrogenConsumption = spaceshipHandler.calculateHydrogenConsumption(spaceshipFound, galaxy,
						system, position);
				if (spaceshipFound.getHydrogen() < hydrogenConsumption) {
					return new ResponseEntity<>("Nicht ausreichend Wasserstoff vorhanden", HttpStatus.BAD_REQUEST);
				} else {
					spaceshipFound.setHydrogen(spaceshipFound.getHydrogen() - hydrogenConsumption);
				}
				Flight flight = setupFlight(spaceshipFound, action, galaxy, system, position, metal, crystal, hydrogen,
						hydrogenConsumption);
				flightRepository.save(flight);

				spaceshipFound.setCurrentPosition(null);
				spaceshipRepository.save(spaceshipFound);

				return new ResponseEntity<>(flight, HttpStatus.CREATED);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	private Flight setupFlight(Spaceship spaceship, String action, int galaxy, int system, int position, double metal,
			double crystal, double hydrogen, double hydrogenConsumption) {
		Flight flight = new Flight();
		flight.setSpaceship(spaceship);
		flight.setAction(action);
		flight.setStartGalaxy(spaceship.getCurrentPosition().getGalaxy());
		flight.setStartSystem(spaceship.getCurrentPosition().getSystem());
		flight.setStartPosition(spaceship.getCurrentPosition().getPosition());
		flight.setDestinationGalaxy(galaxy);
		flight.setDestinationSystem(system);
		flight.setDestinationPosition(position);
		LocalDateTime date = LocalDateTime.now();
		flight.setStartTime(date);
		Long flightDuration = spaceshipHandler.calculateFlightDuration(spaceship, galaxy, system, position);
		LocalDateTime arrivalTime = date.plusSeconds(flightDuration);
		flight.setArrivalTime(arrivalTime);
		flight.setFlying(true);
		flight.setHydrogenConsumption(hydrogenConsumption);

		if (action.equals("deliver") || action.equals("station") || action.equals("pickup")
				|| action.equals("colonize")) {
			flight.setMetal(metal);
			flight.setCrystal(crystal);
			flight.setHydrogen(hydrogen);
		}
		return flight;
	}

	private boolean proofAction(String action) {
		if (action.equals("deliver") || action.equals("attack") || action.equals("station") || action.equals("pickup")
				|| action.equals("colonize")) {
			return true;
		} else {
			return false;
		}
	}

}
