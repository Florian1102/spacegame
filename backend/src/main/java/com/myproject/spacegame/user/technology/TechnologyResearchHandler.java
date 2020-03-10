package com.myproject.spacegame.user.technology;

import org.springframework.stereotype.Service;

import com.myproject.spacegame.buildingStats.BuildingStats;
import com.myproject.spacegame.buildingStats.NamesOfTechnologies;
import com.myproject.spacegame.services.CalculatePointsOfPlayer;
import com.myproject.spacegame.user.spaceship.Spaceship;
import com.myproject.spacegame.user.spaceship.SpaceshipRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TechnologyResearchHandler {

	private final TechnologyRepository technologyRepository;
	private final SpaceshipRepository spaceshipRepository;
	private final CalculatePointsOfPlayer calculatePointsOfPlayer;

	public boolean proofBuildPossible(Technology technology, Spaceship spaceship) throws Exception {
		if (technology.getEndOfResearch() != null) {
			throw new Exception("Es wird schon etwas erforscht");
		} else {
			// TODO: Hier müssen weitere Einschränkungen eingefügt werden. Manche
			// Forschungen sollen erst ab einem bestimmten Lvl vom Spaceship oder
			// Forschungslabor möglich sein.
			return true;
		}
	}

	public int getCurrentLvlOfSpecificTechnology(Technology technology, String nameOfTechnology) throws Exception {
		if (nameOfTechnology.equalsIgnoreCase(NamesOfTechnologies.ENERGYRESEARCH.toString())) {
			return technology.getEnergyResearchLvl();
		} else if (nameOfTechnology.equalsIgnoreCase(NamesOfTechnologies.RESOURCERESEARCH.toString())) {
			return technology.getResourceResearchLvl();
			// TODO: Weitere Technologien ergänzen
		} else {
			throw new Exception("Es liegen keine Informationen über das aktuelle Level der Technologie vor");
		}
	}

	public Technology build(Long spaceshipId, Long technologyId, BuildingStats statsOfTechnologyNextLvl)
			throws Exception {
		if (!spaceshipRepository.existsById(spaceshipId)) {
			throw new Exception("Raumschiff existiert nicht");
		}
		if (!technologyRepository.existsById(technologyId)) {
			throw new Exception("Technologie existiert nicht");
		}

		Spaceship foundSpaceship = spaceshipRepository.findById(spaceshipId).get();
//		spaceshipHandler.calculateAndSaveSpaceshipStats(foundSpaceship);
		// TODO Auswirkungen von erhöhter Forschung auf Raumschiff und Planeten hier einfügen
		spaceshipRepository.save(foundSpaceship);
		Technology foundTechnology = technologyRepository.findById(technologyId).get();
		foundTechnology = setSomeStatsDependentOnWhichTechnology(foundTechnology, statsOfTechnologyNextLvl);
		technologyRepository.save(foundTechnology);
		calculatePointsOfPlayer.calculateAndSaveNewPoints(foundSpaceship.getUser().getId(),
				statsOfTechnologyNextLvl.getNecessaryMetal(), statsOfTechnologyNextLvl.getNecessaryCrystal(),
				statsOfTechnologyNextLvl.getNecessaryHydrogen());

		return foundTechnology;
	}

	private Technology setSomeStatsDependentOnWhichTechnology(Technology foundTechnology,
			BuildingStats statsOfTechnologyNextLvl) throws Exception {

		String nameOfTechnology = statsOfTechnologyNextLvl.getNameOfBuildingOTechnology();

		if (nameOfTechnology.equalsIgnoreCase(NamesOfTechnologies.ENERGYRESEARCH.toString())) {
			foundTechnology.setEnergyResearchLvl(statsOfTechnologyNextLvl);
		} else if (nameOfTechnology.equalsIgnoreCase(NamesOfTechnologies.RESOURCERESEARCH.toString())) {
			foundTechnology.setResourceResearchLvl(statsOfTechnologyNextLvl);
			// TODO Weitere Forschungen hinzufügen
		} else {
			throw new Exception("Das erhöhen des Gebäudelevels ist fehlgeschlagen");
		}
		foundTechnology.setNameOfResearch(null);
		foundTechnology.setCurrentLvlOfResearch(0);
		foundTechnology.setEndOfResearch(null);
		return foundTechnology;
	}
}
