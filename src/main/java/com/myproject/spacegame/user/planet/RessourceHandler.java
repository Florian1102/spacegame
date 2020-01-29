package com.myproject.spacegame.user.planet;

import org.springframework.stereotype.Service;

import com.myproject.spacegame.user.spaceship.Spaceship;
import com.myproject.spacegame.user.spaceship.SpaceshipRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RessourceHandler {

	private final SpaceshipRepository spaceshipRepository;

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

		if (!proofSpaceshipRessourcesEnough(spaceship, necessaryMetal, necessaryCrystal, necessaryHydrogen)) {
			throw new Exception("Du hast nicht ausreichend Ressourcen auf dem Raumschiff");
		} else {
			spaceship.setMetal(spaceship.getMetal() - necessaryMetal);
			spaceship.setCrystal(spaceship.getCrystal() - necessaryCrystal);
			spaceship.setHydrogen(spaceship.getHydrogen() - necessaryHydrogen);
			spaceship.setEnergy(spaceship.getEnergy() - necessaryEnergy);

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

	public void addRessources(Long id, double metal, double crystal, double hydrogen, boolean addOrRemove)
			throws Exception {
		if (!spaceshipRepository.existsById(id)) {
			throw new Exception("Das Raumschiff existiert nicht");
		} else {
			Spaceship spaceshipFound = spaceshipRepository.findById(id).get();
			if (addOrRemove) {
				spaceshipFound.setMetal(spaceshipFound.getMetal() + metal);
				spaceshipFound.setCrystal(spaceshipFound.getCrystal() + crystal);
				spaceshipFound.setHydrogen(spaceshipFound.getHydrogen() + hydrogen);
			} else {
				if (spaceshipFound.getMetal() <= metal || spaceshipFound.getCrystal() <= crystal
						|| spaceshipFound.getHydrogen() <= hydrogen) {
					throw new Exception("Du hast nicht ausreichend Ressourcen");
				} else {
					spaceshipFound.setMetal(spaceshipFound.getMetal() - metal);
					spaceshipFound.setCrystal(spaceshipFound.getCrystal() - crystal);
					spaceshipFound.setHydrogen(spaceshipFound.getHydrogen() - hydrogen);
				}
			}
			spaceshipRepository.save(spaceshipFound);
		}
	}

}
