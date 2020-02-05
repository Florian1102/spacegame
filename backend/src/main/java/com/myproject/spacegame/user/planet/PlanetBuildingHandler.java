package com.myproject.spacegame.user.planet;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.myproject.spacegame.buildingStats.BuildingStats;
import com.myproject.spacegame.buildingStats.NamesOfPlanetBuildings;
import com.myproject.spacegame.services.CalculatePointsOfPlayer;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlanetBuildingHandler {

	private final PlanetRepository planetRepository;
	private final CalculatePointsOfPlayer calculatePointsOfPlayer;

	public boolean proofBuildPossible(Planet planet) throws Exception {
		if (planet.getRemainingBuildingDuration() > 0) {
			throw new Exception("Es wird schon etwas gebaut");
		} else if (planet.getRemainingFields() < 1) {
			throw new Exception("Du hast keinen Platz mehr auf dem Planeten");
		} else {
			return true;
		}
	}

	public int getCurrentLvlOfSpecificBuilding(Planet planet, String nameOfBuilding) throws Exception {
		if (nameOfBuilding.equalsIgnoreCase(NamesOfPlanetBuildings.METALMINE.toString())) {
			return planet.getMetalMineLvl();
		} else if (nameOfBuilding.equalsIgnoreCase(NamesOfPlanetBuildings.CRYSTALMINE.toString())) {
			return planet.getCrystalMineLvl();
		} else if (nameOfBuilding.equalsIgnoreCase(NamesOfPlanetBuildings.HYDROGENPLANT.toString())) {
			return planet.getHydrogenPlantLvl();
		} else if (nameOfBuilding.equalsIgnoreCase(NamesOfPlanetBuildings.SOLARPOWERPLANT.toString())) {
			return planet.getSolarPowerPlantLvl();
		} else if (nameOfBuilding.equalsIgnoreCase(NamesOfPlanetBuildings.METALSTOREHOUSE.toString())) {
			return planet.getMetalStorehouseLvl();
		} else if (nameOfBuilding.equalsIgnoreCase(NamesOfPlanetBuildings.CRYSTALSTOREHOUSE.toString())) {
			return planet.getCrystalStorehouseLvl();
		} else if (nameOfBuilding.equalsIgnoreCase(NamesOfPlanetBuildings.HYDROGENTANK.toString())) {
			return planet.getHydrogenTankLvl();
		} else if (nameOfBuilding.equalsIgnoreCase(NamesOfPlanetBuildings.COMMANDCENTRAL.toString())) {
			return planet.getCommandCentralLvl();
		} else if (nameOfBuilding.equalsIgnoreCase(NamesOfPlanetBuildings.SOLARSATELLITE.toString())) {
			return 0;
		} else {
			throw new Exception("Es liegen keine Informationen über das aktuelle Level des Gebäudes vor");
		}
	}

	public void prepareBuild(Planet planetWithUpdatedRessources, BuildingStats statsOfBuildingNextLvl)
			throws Exception {

		ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

		@SuppressWarnings("unused")
		ScheduledFuture<?> scheduledFuture = executorService.schedule(new Callable<Object>() {
			public Object call() throws Exception {
				Planet planetWithFinishedBuilding = build(planetWithUpdatedRessources.getId(), statsOfBuildingNextLvl);

				return new ResponseEntity<>(planetWithFinishedBuilding, HttpStatus.OK);
			}
		}, (long) (statsOfBuildingNextLvl.getBuildingOrResearchDuration()
				* planetWithUpdatedRessources.getReduceBuildingDuration()), TimeUnit.SECONDS);

		executorService.shutdown();
	}

	public Planet build(Long id, BuildingStats statsOfBuildingNextLvl) throws Exception {
		if (!planetRepository.existsById(id)) {
			throw new Exception("Planet existiert nicht");
		}
		Planet foundPlanet = planetRepository.findById(id).get();
		foundPlanet = setSomeStatsDependentOnWhichBuilding(foundPlanet, statsOfBuildingNextLvl);

		planetRepository.save(foundPlanet);
		calculatePointsOfPlayer.calculateAndSaveNewPoints(foundPlanet.getUser().getId(),
				statsOfBuildingNextLvl.getNecessaryMetal(), statsOfBuildingNextLvl.getNecessaryCrystal(),
				statsOfBuildingNextLvl.getNecessaryHydrogen());

		return foundPlanet;
	}

	private Planet setSomeStatsDependentOnWhichBuilding(Planet foundPlanet,
			BuildingStats specificBuildingStatsOfNextLvl) throws Exception {

		String nameOfBuilding = specificBuildingStatsOfNextLvl.getNameOfBuildingOTechnology();

		if (nameOfBuilding.equalsIgnoreCase(NamesOfPlanetBuildings.METALMINE.toString())) {
			System.out.println("Start");
			foundPlanet.setMetalMineLvl(foundPlanet, specificBuildingStatsOfNextLvl);
			foundPlanet.setRemainingFields(foundPlanet.getRemainingFields() - 1);
		} else if (nameOfBuilding.equalsIgnoreCase(NamesOfPlanetBuildings.CRYSTALMINE.toString())) {
			foundPlanet.setCrystalMineLvl(foundPlanet, specificBuildingStatsOfNextLvl);
			foundPlanet.setRemainingFields(foundPlanet.getRemainingFields() - 1);
		} else if (nameOfBuilding.equalsIgnoreCase(NamesOfPlanetBuildings.HYDROGENPLANT.toString())) {
			foundPlanet.setHydrogenPlantLvl(foundPlanet, specificBuildingStatsOfNextLvl);
			foundPlanet.setRemainingFields(foundPlanet.getRemainingFields() - 1);
		} else if (nameOfBuilding.equalsIgnoreCase(NamesOfPlanetBuildings.SOLARPOWERPLANT.toString())) {
			foundPlanet.setSolarPowerPlantLvl(foundPlanet, specificBuildingStatsOfNextLvl);
			foundPlanet.setRemainingFields(foundPlanet.getRemainingFields() - 1);
		} else if (nameOfBuilding.equalsIgnoreCase(NamesOfPlanetBuildings.METALSTOREHOUSE.toString())) {
			foundPlanet.setMetalStorehouseLvl(specificBuildingStatsOfNextLvl.getLevel());
			foundPlanet.setMetalStorehouse(specificBuildingStatsOfNextLvl.getMetalStorehouse());
			foundPlanet.setRemainingFields(foundPlanet.getRemainingFields() - 1);
		} else if (nameOfBuilding.equalsIgnoreCase(NamesOfPlanetBuildings.CRYSTALSTOREHOUSE.toString())) {
			foundPlanet.setCrystalStorehouseLvl(specificBuildingStatsOfNextLvl.getLevel());
			foundPlanet.setCrystalStorehouse(specificBuildingStatsOfNextLvl.getCrystalStorehouse());
			foundPlanet.setRemainingFields(foundPlanet.getRemainingFields() - 1);
		} else if (nameOfBuilding.equalsIgnoreCase(NamesOfPlanetBuildings.HYDROGENTANK.toString())) {
			foundPlanet.setHydrogenTankLvl(specificBuildingStatsOfNextLvl.getLevel());
			foundPlanet.setHydrogenTank(specificBuildingStatsOfNextLvl.getHydrogenTank());
			foundPlanet.setRemainingFields(foundPlanet.getRemainingFields() - 1);
		} else if (nameOfBuilding.equalsIgnoreCase(NamesOfPlanetBuildings.COMMANDCENTRAL.toString())) {
			foundPlanet.setCommandCentralLvl(specificBuildingStatsOfNextLvl.getLevel());
			foundPlanet.setRemainingFields(foundPlanet.getRemainingFields() - 1);
			foundPlanet.setReduceBuildingDuration(specificBuildingStatsOfNextLvl.getReduceBuildingDuration());
		} else if (nameOfBuilding.equalsIgnoreCase(NamesOfPlanetBuildings.SOLARSATELLITE.toString())) {
			foundPlanet.setSolarSatellite(foundPlanet, specificBuildingStatsOfNextLvl);
		} else {
			throw new Exception("Das erhöhen des Gebäudelevels ist fehlgeschlagen");
		}
		foundPlanet.setRemainingBuildingDuration(0L);
		return foundPlanet;
	}
}
