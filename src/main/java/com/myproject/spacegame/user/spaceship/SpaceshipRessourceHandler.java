package com.myproject.spacegame.user.spaceship;

import org.springframework.stereotype.Service;

import com.myproject.spacegame.services.GetStatsOfBuildingsAndTechnologies;
import com.myproject.spacegame.user.technology.Technology;
import com.myproject.spacegame.user.technology.technologyService.TechnologyHandler;
import com.myproject.spacegame.user.technology.technologyStats.NamesOfTechnologies;
import com.myproject.spacegame.user.technology.technologyStats.TechnologyStats;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SpaceshipRessourceHandler {
	
	private final SpaceshipRepository spaceshipRepository;
	private final GetStatsOfBuildingsAndTechnologies getStatsOfBuildingsAndTechnologies;
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
		
		TechnologyStats technologyStatsOfNewLvl = getStatsOfBuildingsAndTechnologies.getTechnologyStatsOfNextLvl(technology.getEnergyTechnologyLvl(), NamesOfTechnologies.ENERGY);
		
		if (spaceship.getMetal() < technologyStatsOfNewLvl.getNecessaryMetal() || 
			spaceship.getCrystal() < technologyStatsOfNewLvl.getNecessaryCrystal() || 
			spaceship.getHydrogen() < technologyStatsOfNewLvl.getNecessaryHydrogen() || 
			spaceship.getEnergy() < technologyStatsOfNewLvl.getNecessaryEnergy()  ) {
			
			throw new Exception("Du hast nicht ausreichend Ressourcen");
		} else {
			spaceship.setMetal(spaceship.getMetal() - technologyStatsOfNewLvl.getNecessaryMetal());
			spaceship.setMetal(spaceship.getCrystal() - technologyStatsOfNewLvl.getNecessaryCrystal());
			spaceship.setMetal(spaceship.getHydrogen() - technologyStatsOfNewLvl.getNecessaryHydrogen());
			//Energie wird nicht abgezogen, sondern braucht man nur einmalig für die Technologie
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
