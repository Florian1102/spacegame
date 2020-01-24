package com.myproject.spacegame.user.spaceship;

import org.springframework.stereotype.Service;

import com.myproject.spacegame.services.GetStatsOfBuildingsAndTechnologies;
import com.myproject.spacegame.user.technology.Technology;
import com.myproject.spacegame.user.technology.technologyService.TechnologyHandler;
import com.myproject.spacegame.user.technology.technologyStats.TechnologyStats;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SpaceshipRessourceHandler {

	private final SpaceshipRepository spaceshipRepository;
	private final GetStatsOfBuildingsAndTechnologies getStatsOfBuildingsAndTechnologies;
	private final SpaceshipBuildingHandler spaceshipBuildingHandler;
	private final TechnologyHandler technologyHandler;

	public Spaceship calculateNewSpaceshipRessources(Spaceship spaceship) throws Exception {

		SpaceshipStats spaceshipStatsOfNewLvl = spaceshipBuildingHandler
				.getSpaceshipStatsOfNewLvl(spaceship.getSpaceshipLvl());

		spaceship.setMetal(spaceship.getMetal() - spaceshipStatsOfNewLvl.getNecessaryMetal());
		spaceship.setCrystal(spaceship.getCrystal() - spaceshipStatsOfNewLvl.getNecessaryCrystal());
		spaceship.setHydrogen(spaceship.getHydrogen() - spaceshipStatsOfNewLvl.getNecessaryHydrogen());
		spaceship.setRemainingBuildingDuration(spaceshipStatsOfNewLvl.getBuildingDuration());
		spaceshipRepository.save(spaceship);
		spaceshipBuildingHandler.prepareBuidling(spaceship, spaceshipStatsOfNewLvl);
		return spaceship;
	}

	public void calculateNewSpaceshipRessourcesTechnology(Spaceship spaceship, Technology technology, String nameOfTechnology) throws Exception {

		int currentLvlOfSpecificTechnology = technologyHandler.getCurrentLvlOfSpecificTechnology(technology, nameOfTechnology);
		TechnologyStats technologyStatsOfNewLvl = getStatsOfBuildingsAndTechnologies
				.getTechnologyStatsOfNextLvl(currentLvlOfSpecificTechnology, nameOfTechnology);

		spaceship.setMetal(spaceship.getMetal() - technologyStatsOfNewLvl.getNecessaryMetal());
		spaceship.setCrystal(spaceship.getCrystal() - technologyStatsOfNewLvl.getNecessaryCrystal());
		spaceship.setHydrogen(spaceship.getHydrogen() - technologyStatsOfNewLvl.getNecessaryHydrogen());
		spaceshipRepository.save(spaceship);
		System.out.println("Ressourcen neu berechnet");
		technologyHandler.prepareBuilding(technology, technologyStatsOfNewLvl);

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
