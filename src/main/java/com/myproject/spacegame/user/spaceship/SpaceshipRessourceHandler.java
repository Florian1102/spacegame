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
	private final SpaceshipBuildingHandler spaceshipBuildingHandler;

	public Spaceship calculateNewSpaceshipRessources(Spaceship spaceship) throws Exception {

		SpaceshipStats spaceshipStatsOfNewLvl = spaceshipBuildingHandler.getSpaceshipStatsOfNewLvl(spaceship.getSpaceshipLvl());
		Long necessaryMetal = spaceshipStatsOfNewLvl.getNecessaryMetal();
		if (spaceship.getMetal() < necessaryMetal) {
			throw new Exception("Du hast nicht ausreichend Ressourcen");
		} else {
			spaceship.setMetal(spaceship.getMetal() - necessaryMetal);
			spaceship.setRemainingBuildingDuration(spaceshipStatsOfNewLvl.getBuildingDuration());
			spaceshipRepository.save(spaceship);
			return spaceship;
		}
	}
	
	public void calculateNewSpaceshipRessourcesTechnology(Spaceship spaceship, Technology technology) throws Exception {
		
		TechnologyStats technologyStatsOfNewLvl = technologyHandler.getTechnologyStatsOfNewLvl(technology.getEnergyTechnologyLvl());
		
		if (spaceship.getMetal() < technologyStatsOfNewLvl.getNecessaryMetal()) {
			throw new Exception("Du hast nicht ausreichend Ressourcen");
		} else {
			spaceship.setMetal(spaceship.getMetal() - technologyStatsOfNewLvl.getNecessaryMetal());
			spaceshipRepository.save(spaceship);
			System.out.println("Ressourcen neu berechnet");
		}
	}
	
	public void addRessources(Long id, double metal, double crystal, double hydrogen, boolean addOrRemove) throws Exception {
		if (!spaceshipRepository.existsById(id)) {
			throw new Exception("Das Raumschiff existiert nicht");
		} else {
			Spaceship spaceshipFound = spaceshipRepository.findById(id).get();
			if (addOrRemove) {
				spaceshipFound.setMetal(spaceshipFound.getMetal() + metal);				
				spaceshipFound.setCrystal(spaceshipFound.getCrystal() + crystal);				
				spaceshipFound.setHydrogen(spaceshipFound.getHydrogen() + hydrogen);				
			} else {
				if (spaceshipFound.getMetal() <= metal || spaceshipFound.getCrystal() <= crystal || spaceshipFound.getHydrogen() <= hydrogen) {
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
