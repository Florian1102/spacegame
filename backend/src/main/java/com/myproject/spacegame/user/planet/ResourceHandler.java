package com.myproject.spacegame.user.planet;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.myproject.spacegame.user.spaceship.Spaceship;
import com.myproject.spacegame.user.spaceship.SpaceshipRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResourceHandler {

	private final SpaceshipRepository spaceshipRepository;
	private final PlanetRepository planetRepository;

	public Planet calculateNewPlanetRessources(Planet planet, Long necessaryMetal, Long necessaryCrystal,
			Long necessaryHydrogen) throws Exception {

		if (!proofPlanetRessourcesEnough(planet, necessaryMetal, necessaryCrystal, necessaryHydrogen)) {
			throw new Exception("Du hast nicht ausreichend Ressourcen auf dem Planeten");
		} else {
			planet.setMetal(planet.getMetal() - necessaryMetal);
			planet.setCrystal(planet.getCrystal() - necessaryCrystal);
			planet.setHydrogen(planet.getHydrogen() - necessaryHydrogen);
			return planet;
		}
	}

	private boolean proofPlanetRessourcesEnough(Planet planet, Long necessaryMetal, Long necessaryCrystal,
			Long necessaryHydrogen) throws Exception {
		if (planet.getMetal() < necessaryMetal || planet.getCrystal() < necessaryCrystal
				|| planet.getHydrogen() < necessaryHydrogen) {
			return false;
		} else {
			return true;
		}
	}

	public Spaceship calculateNewSpaceshipRessources(Spaceship spaceship, Long necessaryMetal, Long necessaryCrystal,
			Long necessaryHydrogen, Long necessaryEnergy) throws Exception {

		if (!proofSpaceshipRessourcesEnough(spaceship, necessaryMetal, necessaryCrystal, necessaryHydrogen, necessaryEnergy)) {
			throw new Exception("Du hast nicht ausreichend Ressourcen auf dem Raumschiff");
		} else {
			spaceship.setMetal(spaceship.getMetal() - necessaryMetal);
			spaceship.setCrystal(spaceship.getCrystal() - necessaryCrystal);
			spaceship.setHydrogen(spaceship.getHydrogen() - necessaryHydrogen);
			spaceship.setEnergy(spaceship.getEnergy() - necessaryEnergy);
			return spaceship;
		}
	}

	public boolean proofSpaceshipRessourcesEnough(Spaceship spaceship, Long necessaryMetal, Long necessaryCrystal,
			Long necessaryHydrogen, Long necessaryEnergy) throws Exception {
		if (spaceship.getMetal() < necessaryMetal || spaceship.getCrystal() < necessaryCrystal
				|| spaceship.getHydrogen() < necessaryHydrogen || spaceship.getEnergy() < necessaryEnergy) {
			return false;
		} else {
			return true;
		}
	}

	public ResponseEntity<?> pickUpOrDeliverResources(Long spaceshipId, Long planetId, Long metal, Long crystal, Long hydrogen,
			boolean pickUpOrDeliver) throws Exception {
		Spaceship spaceshipFound = spaceshipRepository.findById(spaceshipId).get();
		Planet planetFound = planetRepository.findById(planetId).get();
		if (pickUpOrDeliver) {
			if (!proofPlanetRessourcesEnough(planetFound, metal, crystal, hydrogen)) {
				return new ResponseEntity<>("Du hast nicht so viele Rohstoffe auf dem Planeten", HttpStatus.BAD_REQUEST);
			} else {
				double deltaMetal = (metal > (spaceshipFound.getMetalStore() - spaceshipFound.getMetal())) ? (spaceshipFound.getMetalStore() - spaceshipFound.getMetal()) : metal;
				double deltaCrystal = (crystal > (spaceshipFound.getCrystalStore() - spaceshipFound.getCrystal())) ? (spaceshipFound.getCrystalStore() - spaceshipFound.getCrystal()) : crystal;
				double deltaHydrogen = (metal > (spaceshipFound.getHydrogenTank() - spaceshipFound.getHydrogen())) ? (spaceshipFound.getHydrogenTank() - spaceshipFound.getHydrogen()) : hydrogen;
				
				spaceshipFound.setMetal(spaceshipFound.getMetal() + deltaMetal);
				spaceshipFound.setCrystal(spaceshipFound.getCrystal() + deltaCrystal);
				spaceshipFound.setHydrogen(spaceshipFound.getHydrogen() + deltaHydrogen);
				planetFound.setMetal(planetFound.getMetal() - deltaMetal);
				planetFound.setCrystal(planetFound.getCrystal() - deltaCrystal);
				planetFound.setHydrogen(planetFound.getHydrogen() - deltaHydrogen);
			}
		} else {
			if (!proofSpaceshipRessourcesEnough(spaceshipFound, metal, crystal, hydrogen, 0L)) {
				return new ResponseEntity<>("Du hast nicht so viele Rohstoffe auf dem Raumschiff", HttpStatus.BAD_REQUEST);
			} else {
				spaceshipFound.setMetal(spaceshipFound.getMetal() - metal);
				spaceshipFound.setCrystal(spaceshipFound.getCrystal() - crystal);
				spaceshipFound.setHydrogen(spaceshipFound.getHydrogen() - hydrogen);
				planetFound.setMetal(planetFound.getMetal() + metal);
				planetFound.setCrystal(planetFound.getCrystal() + crystal);
				planetFound.setHydrogen(planetFound.getHydrogen() + hydrogen);
			}
		}
		spaceshipRepository.save(spaceshipFound);
		planetRepository.save(planetFound);
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
}
