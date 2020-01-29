package com.myproject.spacegame.user.planet;

import org.springframework.stereotype.Service;

import com.myproject.spacegame.user.spaceship.Spaceship;
import com.myproject.spacegame.user.spaceship.SpaceshipRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlanetRessourceHandler {

	private final PlanetRepository planetRepository;
	private final SpaceshipRepository spaceshipRepository;

	public Planet calculateNewPlanetRessources(Planet planet, Long necessaryMetal, Long necessaryCrystal,
			Long necessaryHydrogen, Long necessaryEnergy, Long buildingDuration) throws Exception {

		if (!proofPlanetRessourcesEnough(planet, necessaryMetal, necessaryCrystal, necessaryHydrogen)) {
			throw new Exception("Du hast nicht ausreichend Ressourcen auf dem Planeten");
		} else {
			planet.setMetal(planet.getMetal() - necessaryMetal);
			planet.setCrystal(planet.getCrystal() - necessaryCrystal);
			planet.setHydrogen(planet.getHydrogen() - necessaryHydrogen);
			planet.setEnergy(planet.getEnergy() - necessaryEnergy);
			planet.setRemainingBuildingDuration((long) (buildingDuration * Math.pow(0.6, planet.getCommandCentralLvl() - 1)));

			planetRepository.save(planet);
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
			Long necessaryHydrogen, Long necessaryEnergy, Long buildingDuration) throws Exception {

		if (!proofSpaceshipRessourcesEnough(spaceship, necessaryMetal, necessaryCrystal, necessaryHydrogen)) {
			throw new Exception("Du hast nicht ausreichend Ressourcen auf dem Planeten");
		} else {
			spaceship.setMetal(spaceship.getMetal() - necessaryMetal);
			spaceship.setCrystal(spaceship.getCrystal() - necessaryCrystal);
			spaceship.setHydrogen(spaceship.getHydrogen() - necessaryHydrogen);
			spaceship.setEnergy(spaceship.getEnergy() - necessaryEnergy);
			spaceship.setRemainingBuildingDuration((long) (buildingDuration * Math.pow(0.6, spaceship.getSpaceshipLvl() - 1)));

			spaceshipRepository.save(spaceship);
			return spaceship;
		}
	}

	private boolean proofSpaceshipRessourcesEnough(Spaceship spaceship, Long necessaryMetal, Long necessaryCrystal,
			Long necessaryHydrogen) throws Exception {
		if (spaceship.getMetal() < necessaryMetal || spaceship.getCrystal() < necessaryCrystal
				|| spaceship.getHydrogen() < necessaryHydrogen) {
			return false;
		} else {
			return true;
		}
	}


}
