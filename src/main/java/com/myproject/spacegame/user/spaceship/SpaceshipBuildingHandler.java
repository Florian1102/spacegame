package com.myproject.spacegame.user.spaceship;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

	public boolean proofBuildPossible(Spaceship spaceship) throws Exception {
		if (spaceship.getRemainingBuildingDuration() > 0) {
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

	public void prepareBuild(Spaceship spaceshipWithUpdatedRessources, BuildingStats statsOfBuildingNextLvl)
			throws Exception {

		ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

		@SuppressWarnings("unused")
		ScheduledFuture<?> scheduledFuture = executorService.schedule(new Callable<Object>() {
			public Object call() throws Exception {
				Spaceship spacehipWithFinishedBuilding = build(spaceshipWithUpdatedRessources.getId(),
						statsOfBuildingNextLvl);

				return new ResponseEntity<>(spacehipWithFinishedBuilding, HttpStatus.OK);
			}
		}, (long) (statsOfBuildingNextLvl.getBuildingOrResearchDuration()
				* spaceshipWithUpdatedRessources.getReduceBuildingDuration()), TimeUnit.SECONDS);
		executorService.shutdown();
	}

	private Spaceship build(Long id, BuildingStats statsOfBuildingNextLvl) throws Exception {
		if (!spaceshipRepository.existsById(id)) {
			throw new Exception("Raumschiff existiert nicht");
		}
		Spaceship foundSpaceship = spaceshipRepository.findById(id).get();

		foundSpaceship = setSomeStatsDependentOnWhichBuilding(foundSpaceship, statsOfBuildingNextLvl);
		foundSpaceship.setRemainingBuildingDuration(0L);

		spaceshipRepository.save(foundSpaceship);
		calculatePointsOfPlayer.calculateAndSaveNewPoints(foundSpaceship.getUser().getId(), statsOfBuildingNextLvl.getNecessaryMetal(), statsOfBuildingNextLvl.getNecessaryCrystal(), statsOfBuildingNextLvl.getNecessaryHydrogen());

		return foundSpaceship;
	}

	private Spaceship setSomeStatsDependentOnWhichBuilding(Spaceship foundSpaceship,
			BuildingStats statsOfBuildingNextLvl) throws Exception {

		String nameOfBuilding = statsOfBuildingNextLvl.getNameOfBuildingOTechnology();

		if (nameOfBuilding.equalsIgnoreCase(NamesOfSpaceshipBuildings.SPACESHIP.toString())) {
			foundSpaceship.setSpaceshipLvl(statsOfBuildingNextLvl.getLevel());
			foundSpaceship.setEnergy(foundSpaceship.getEnergy() - statsOfBuildingNextLvl.getNecessaryEnergy());
			foundSpaceship.setAttackPower(foundSpaceship.getAttackPower() + statsOfBuildingNextLvl.getAttackPower());
			foundSpaceship.setDefense(foundSpaceship.getDefense() + statsOfBuildingNextLvl.getDefense());
			foundSpaceship.setSpeed(foundSpaceship.getSpeed() + statsOfBuildingNextLvl.getSpeed());
			foundSpaceship.setReduceBuildingDuration(statsOfBuildingNextLvl.getReduceBuildingDuration());
		} else if (nameOfBuilding.equalsIgnoreCase(NamesOfSpaceshipBuildings.RESEARCHLABORATORY.toString())) {
			foundSpaceship.setResearchLaboratoryLvl(statsOfBuildingNextLvl.getLevel());
			foundSpaceship.setReduceResearchDuration(statsOfBuildingNextLvl.getReduceResearchDuration());
		} else if (nameOfBuilding.equalsIgnoreCase(NamesOfSpaceshipBuildings.SOLARCELL.toString())) {
			foundSpaceship.setEnergy(foundSpaceship.getEnergy() + statsOfBuildingNextLvl.getProductionEnergy());
		} else {
			throw new Exception("Das erhöhen des Gebäudelevels ist fehlgeschlagen");
		}
		return foundSpaceship;
	}
}
