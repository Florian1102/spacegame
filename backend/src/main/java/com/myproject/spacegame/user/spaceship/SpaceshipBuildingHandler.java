package com.myproject.spacegame.user.spaceship;

import org.springframework.stereotype.Service;

import com.myproject.spacegame.buildingStats.BuildingStats;
import com.myproject.spacegame.buildingStats.NamesOfSpaceshipBuildings;
import com.myproject.spacegame.services.CalculatePointsOfPlayer;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SpaceshipBuildingHandler {

	private final SpaceshipRepository spaceshipRepository;
	private final CalculatePointsOfPlayer calculatePointsOfPlayer;
	private final SpaceshipHandler spaceshipHandler;

	public boolean proofBuildPossible(Spaceship spaceship) throws Exception {
		if (spaceship.getEndOfBuilding() != null) {
			throw new Exception("Es wird schon etwas gebaut");
		} else {
			return true;
		}
	}

	public int getCurrentLvlOfSpecificBuilding(Spaceship spaceship, String nameOfBuilding) throws Exception {
		if (nameOfBuilding.equalsIgnoreCase(NamesOfSpaceshipBuildings.SPACESHIP.toString())) {
			return spaceship.getSpaceshipLvl();
		} else if (nameOfBuilding.equalsIgnoreCase(NamesOfSpaceshipBuildings.RESEARCHLABORATORY.toString())) {
			return spaceship.getResearchLaboratoryLvl();
		} else if (nameOfBuilding.equalsIgnoreCase(NamesOfSpaceshipBuildings.SOLARCELL.toString())) {
			return 0;
		} else {
			throw new Exception("Es liegen keine Informationen über das aktuelle Level des Gebäudes vor");
		}
	}

	public Spaceship build(Long id, BuildingStats statsOfBuildingNextLvl) throws Exception {
		if (!spaceshipRepository.existsById(id)) {
			throw new Exception("Raumschiff existiert nicht");
		}
		Spaceship spaceshipFound = spaceshipRepository.findById(id).get();
		spaceshipFound = increaseLvlOfSpecificBuilding(spaceshipFound, statsOfBuildingNextLvl);
		spaceshipFound = spaceshipHandler.calculateAndSaveSpaceshipStats(spaceshipFound);
		calculatePointsOfPlayer.calculateAndSaveNewPoints(spaceshipFound.getUser().getId(), statsOfBuildingNextLvl.getNecessaryMetal(), statsOfBuildingNextLvl.getNecessaryCrystal(), statsOfBuildingNextLvl.getNecessaryHydrogen());

		return spaceshipFound;
	}

	private Spaceship increaseLvlOfSpecificBuilding(Spaceship foundSpaceship,
			BuildingStats statsOfBuildingNextLvl) throws Exception {

		String nameOfBuilding = statsOfBuildingNextLvl.getNameOfBuildingOTechnology();

		if (nameOfBuilding.equalsIgnoreCase(NamesOfSpaceshipBuildings.SPACESHIP.toString())) {
			foundSpaceship.setSpaceshipLvl(statsOfBuildingNextLvl.getLevel());
			foundSpaceship.setEnergy(foundSpaceship.getEnergy() - statsOfBuildingNextLvl.getNecessaryEnergy());
			foundSpaceship.setReduceBuildingDuration(statsOfBuildingNextLvl.getReduceBuildingDuration());
			foundSpaceship.setMetalStore(statsOfBuildingNextLvl.getMetalStorehouse());
			foundSpaceship.setCrystalStore(statsOfBuildingNextLvl.getCrystalStorehouse());
			foundSpaceship.setHydrogenTank(statsOfBuildingNextLvl.getHydrogenTank());
		} else if (nameOfBuilding.equalsIgnoreCase(NamesOfSpaceshipBuildings.RESEARCHLABORATORY.toString())) {
			foundSpaceship.setResearchLaboratoryLvl(statsOfBuildingNextLvl.getLevel());
			foundSpaceship.setReduceResearchDuration(statsOfBuildingNextLvl.getReduceResearchDuration());
		} else if (nameOfBuilding.equalsIgnoreCase(NamesOfSpaceshipBuildings.SOLARCELL.toString())) {
			foundSpaceship.setEnergy(foundSpaceship.getEnergy() + statsOfBuildingNextLvl.getProductionEnergy());
		} else {
			throw new Exception("Das erhöhen des Gebäudelevels ist fehlgeschlagen");
		}
		foundSpaceship.setEndOfBuilding(null);
		foundSpaceship.setNameOfBuilding(null);
		foundSpaceship.setCurrentLvlOfBuilding(0);
		return foundSpaceship;
	}
}
