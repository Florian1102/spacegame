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

	private boolean proofPlanetRessourcesEnough(Planet planet, double necessaryMetal, double necessaryCrystal,
			double necessaryHydrogen) throws Exception {
		if (planet.getMetal() < necessaryMetal || planet.getCrystal() < necessaryCrystal
				|| planet.getHydrogen() < necessaryHydrogen) {
			return false;
		} else {
			return true;
		}
	}

	public Spaceship calculateNewSpaceshipRessources(Spaceship spaceship, double necessaryMetal, double necessaryCrystal,
			double necessaryHydrogen, double necessaryEnergy) throws Exception {

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

	public boolean proofSpaceshipRessourcesEnough(Spaceship spaceship, double necessaryMetal, double necessaryCrystal,
			double necessaryHydrogen, double necessaryEnergy) throws Exception {
		if (spaceship.getMetal() < necessaryMetal || spaceship.getCrystal() < necessaryCrystal
				|| spaceship.getHydrogen() < necessaryHydrogen || spaceship.getEnergy() < necessaryEnergy) {
			return false;
		} else {
			return true;
		}
	}

	public Spaceship pickUpOrDeliverResources(Spaceship spaceship, Long planetId, double metal, double crystal, double hydrogen,
			boolean pickUpOrDeliver) throws Exception {
		Planet planetFound = planetRepository.findById(planetId).get();
		if (pickUpOrDeliver) {
			if (!proofPlanetRessourcesEnough(planetFound, metal, crystal, hydrogen)) {
				throw new Exception("Du hast nicht so viele Rohstoffe auf dem Planeten");
			} else {
				double deltaMetal = (metal > (spaceship.getMetalStore() - spaceship.getMetal())) ? (spaceship.getMetalStore() - spaceship.getMetal()) : metal;
				double deltaCrystal = (crystal > (spaceship.getCrystalStore() - spaceship.getCrystal())) ? (spaceship.getCrystalStore() - spaceship.getCrystal()) : crystal;
				double deltaHydrogen = (hydrogen > (spaceship.getHydrogenTank() - spaceship.getHydrogen())) ? (spaceship.getHydrogenTank() - spaceship.getHydrogen()) : hydrogen;
				spaceship.setMetal(spaceship.getMetal() + deltaMetal);
				spaceship.setCrystal(spaceship.getCrystal() + deltaCrystal);
				spaceship.setHydrogen(spaceship.getHydrogen() + deltaHydrogen);
				planetFound.setMetal(planetFound.getMetal() - deltaMetal);
				planetFound.setCrystal(planetFound.getCrystal() - deltaCrystal);
				planetFound.setHydrogen(planetFound.getHydrogen() - deltaHydrogen);
			}
		} else {
			if (!proofSpaceshipRessourcesEnough(spaceship, metal, crystal, hydrogen, 0L)) {
				throw new Exception("Du hast nicht so viele Rohstoffe auf dem Raumschiff");
			} else {
				spaceship.setMetal(spaceship.getMetal() - metal);
				spaceship.setCrystal(spaceship.getCrystal() - crystal);
				spaceship.setHydrogen(spaceship.getHydrogen() - hydrogen);
				planetFound.setMetal(planetFound.getMetal() + metal);
				planetFound.setCrystal(planetFound.getCrystal() + crystal);
				planetFound.setHydrogen(planetFound.getHydrogen() + hydrogen);
			}
		}
		planetRepository.save(planetFound);
		return spaceship;
	}
}
