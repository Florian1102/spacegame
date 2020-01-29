package com.myproject.spacegame.user.planet.buildings;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.myproject.spacegame.user.planet.Planet;
import com.myproject.spacegame.user.planet.PlanetRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlanetBuildingHandler {

	private final PlanetRepository planetRepository;

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

	public void prepareBuild(Planet planetWithUpdatedRessources, PlanetBuildingStats statsOfBuildingNextLvl)
			throws Exception {

		ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

		@SuppressWarnings("unused")
		ScheduledFuture<?> scheduledFuture = executorService.schedule(new Callable<Object>() {
			public Object call() throws Exception {
				Planet planetWithFinishedBuilding = build(planetWithUpdatedRessources.getId(),
						statsOfBuildingNextLvl);

				return new ResponseEntity<>(planetWithFinishedBuilding, HttpStatus.OK);
			}
		}, (long) (statsOfBuildingNextLvl.getBuildingDuration()
				* (Math.pow(0.6, planetWithUpdatedRessources.getCommandCentralLvl() - 1))), TimeUnit.SECONDS);

		executorService.shutdown();
	}

	public Planet build(Long id, PlanetBuildingStats statsOfBuildingNextLvl) throws Exception {
		if (!planetRepository.existsById(id)) {
			throw new Exception("Planet existiert nicht");
		}
		Planet foundPlanet = planetRepository.findById(id).get();

		foundPlanet = setSomeStatsDependentOnWhichBuilding(foundPlanet, statsOfBuildingNextLvl);

		foundPlanet.setRemainingBuildingDuration(0L);
		foundPlanet.setEnergy(foundPlanet.getEnergy() - statsOfBuildingNextLvl.getNecessaryEnergy());

		planetRepository.save(foundPlanet);
		return foundPlanet;
	}
	
	private Planet setSomeStatsDependentOnWhichBuilding(Planet foundPlanet,
			PlanetBuildingStats specificBuildingStatsOfNextLvl) throws Exception {

		String nameOfBuilding = specificBuildingStatsOfNextLvl.getNameOfBuilding();

		if (nameOfBuilding.equalsIgnoreCase(NamesOfPlanetBuildings.METALMINE.toString())) {
			foundPlanet.setMetalMineLvl(specificBuildingStatsOfNextLvl.getLevel());
			foundPlanet.setMetalProductionEveryHour(specificBuildingStatsOfNextLvl.getProductionMetal());
			foundPlanet.setRemainingFields(foundPlanet.getRemainingFields() - 1);
		} else if (nameOfBuilding.equalsIgnoreCase(NamesOfPlanetBuildings.CRYSTALMINE.toString())) {
			foundPlanet.setCrystalMineLvl(specificBuildingStatsOfNextLvl.getLevel());
			foundPlanet.setCrystalProductionEveryHour(specificBuildingStatsOfNextLvl.getProductionCrystal());
			foundPlanet.setRemainingFields(foundPlanet.getRemainingFields() - 1);
		} else if (nameOfBuilding.equalsIgnoreCase(NamesOfPlanetBuildings.HYDROGENPLANT.toString())) {
			foundPlanet.setHydrogenPlantLvl(specificBuildingStatsOfNextLvl.getLevel());
			foundPlanet.setHydrogenProductionEveryHour(specificBuildingStatsOfNextLvl.getProductionHydrogen());
			foundPlanet.setRemainingFields(foundPlanet.getRemainingFields() - 1);
		} else if (nameOfBuilding.equalsIgnoreCase(NamesOfPlanetBuildings.SOLARPOWERPLANT.toString())) {
			foundPlanet.setSolarPowerPlantLvl(specificBuildingStatsOfNextLvl.getLevel());
			foundPlanet.setEnergy(foundPlanet.getEnergy() + specificBuildingStatsOfNextLvl.getProductionEnergy());
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
		} else if (nameOfBuilding.equalsIgnoreCase(NamesOfPlanetBuildings.SOLARSATELLITE.toString())) {
			foundPlanet.setSolarSatellite(foundPlanet.getSolarSatellite() + 1);
			foundPlanet.setEnergy(foundPlanet.getEnergy() + specificBuildingStatsOfNextLvl.getProductionEnergy());
		} else {
			throw new Exception("Das erhöhen des Gebäudelevels ist fehlgeschlagen");
		}		return foundPlanet;
	}

}
