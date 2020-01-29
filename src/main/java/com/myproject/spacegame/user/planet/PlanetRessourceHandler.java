package com.myproject.spacegame.user.planet;

import org.springframework.stereotype.Service;

import com.myproject.spacegame.services.GetStatsOfBuildingsAndTechnologies;
import com.myproject.spacegame.user.attackAndDefenseSystem.AttackAndDefenseSystemHandler;
import com.myproject.spacegame.user.attackAndDefenseSystem.AttackAndDefenseSystemRepository;
import com.myproject.spacegame.user.attackAndDefenseSystem.AttackAndDefenseSystemStats;
import com.myproject.spacegame.user.planet.buildings.PlanetBuildingHandler;
import com.myproject.spacegame.user.planet.buildings.PlanetBuildingStats;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlanetRessourceHandler {

	private final PlanetRepository planetRepository;
	private final GetStatsOfBuildingsAndTechnologies getStatsOfBuildingsAndTechnologies;
	private final PlanetBuildingHandler planetBuildingHandler;
	private final AttackAndDefenseSystemRepository attackAndDefenseSystemRepository;
	private final AttackAndDefenseSystemHandler attackAndDefenseSystemHandler;
	
	public Planet calculateNewPlanetRessources(Planet planet, String nameOfBuilding) {
		
		try {
			int currentLvlOfSpecificBuilding = planetBuildingHandler.getCurrentLvlOfSpecificBuilding(planet, nameOfBuilding);
			
			PlanetBuildingStats specificBuildingStatsOfNextLvl = getStatsOfBuildingsAndTechnologies.getBuildingStatsOfNextLvl(currentLvlOfSpecificBuilding, nameOfBuilding);
			planet.setMetal(planet.getMetal() - specificBuildingStatsOfNextLvl.getNecessaryMetal());
			planet.setCrystal(planet.getCrystal() - specificBuildingStatsOfNextLvl.getNecessaryCrystal());
			planet.setHydrogen(planet.getHydrogen() - specificBuildingStatsOfNextLvl.getNecessaryHydrogen());
			
			planet.setRemainingBuildingDuration((long) (specificBuildingStatsOfNextLvl.getBuildingDuration()*(Math.pow(0.6, planet.getCommandCentralLvl()-1))));
			planetRepository.save(planet);
			
			planetBuildingHandler.prepareBuilding(planet, specificBuildingStatsOfNextLvl);
			return planet;
			
		} catch (Exception e) {
			e.getMessage();
		}
		return planet;
		
	}
	
	public Planet calculateNewPlanetRessourcesAttackAndDefense(Planet planet, String nameOfAttackOrDefenseSystem) throws Exception {
		AttackAndDefenseSystemStats statsOfAttackOrDefenseSystem = attackAndDefenseSystemRepository.findByNameOfAttackDefenseSystem(nameOfAttackOrDefenseSystem);
		planet.setMetal(planet.getMetal() - statsOfAttackOrDefenseSystem.getNecessaryMetal());
		planet.setCrystal(planet.getCrystal() - statsOfAttackOrDefenseSystem.getNecessaryCrystal());
		planet.setHydrogen(planet.getHydrogen() - statsOfAttackOrDefenseSystem.getNecessaryHydrogen());
		
		planet.setRemainingBuildingDuration((long) (statsOfAttackOrDefenseSystem.getBuildingDuration()*(Math.pow(0.6, planet.getCommandCentralLvl()-1))));
		planetRepository.save(planet);
		attackAndDefenseSystemHandler.prepareBuilding(planet, statsOfAttackOrDefenseSystem);

		return planet;
	}
	
}
