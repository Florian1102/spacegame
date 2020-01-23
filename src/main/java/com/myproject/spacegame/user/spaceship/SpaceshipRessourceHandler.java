package com.myproject.spacegame.user.spaceship;

import org.springframework.stereotype.Service;

import com.myproject.spacegame.user.technology.Technology;
import com.myproject.spacegame.user.technology.technologyService.TechnologyHandler;
import com.myproject.spacegame.user.technology.technologyStats.TechnologyStats;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SpaceshipRessourceHandler {
	
	private final SpaceshipRepository spaceshipRepository;
	private final TechnologyHandler technologyHandler;
	private final BuildingHandler buildingHandler;

	public Spaceship calculateNewSpaceshipRessources(Spaceship spaceship) throws Exception {

		SpaceshipStats spaceshipStatsOfNewLvl = buildingHandler.getSpaceshipStatsOfNewLvl(spaceship.getSpaceshipLvl());
		Long necessaryIron = spaceshipStatsOfNewLvl.getNecessaryIron();
		if (spaceship.getIron() < necessaryIron) {
			throw new Exception("Du hast nicht ausreichend Ressourcen");
		} else {
			spaceship.setIron(spaceship.getIron() - necessaryIron);
			spaceship.setRemainingBuildingDuration(spaceshipStatsOfNewLvl.getBuildingDuration());
			spaceshipRepository.save(spaceship);
			return spaceship;
		}
	}
	
	public void calculateNewSpaceshipRessourcesTechnology(Spaceship spaceship, Technology technology) throws Exception {
		
		TechnologyStats technologyStatsOfNewLvl = technologyHandler.getTechnologyStatsOfNewLvl(technology.getEnergyTechnologyLvl());
		
		if (spaceship.getIron() < technologyStatsOfNewLvl.getNecessaryIron()) {
			throw new Exception("Du hast nicht ausreichend Ressourcen");
		} else {
			spaceship.setIron(spaceship.getIron() - technologyStatsOfNewLvl.getNecessaryIron());
			spaceshipRepository.save(spaceship);
			System.out.println("Ressourcen neu berechnet");
		}
	}
	
	public void addRessources(Long id, Long iron, boolean addOrRemove) throws Exception {
		if (!spaceshipRepository.existsById(id)) {
			throw new Exception("Das Raumschiff existiert nicht");
		} else {
			Spaceship spaceshipFound = spaceshipRepository.findById(id).get();
			if (addOrRemove) {
				spaceshipFound.setIron(spaceshipFound.getIron() + iron);				
			} else {
				if (spaceshipFound.getIron() <= iron) {
					throw new Exception("Du hast nicht ausreichend Ressourcen");
				} else {
					spaceshipFound.setIron(spaceshipFound.getIron() - iron);				
				}
			}
			spaceshipRepository.save(spaceshipFound);
		}
	}
}
