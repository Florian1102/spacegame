package com.myproject.spacegame.user.planet.buildings;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.myproject.spacegame.services.GetStatsOfBuildingsAndTechnologies;
import com.myproject.spacegame.user.planet.Planet;
import com.myproject.spacegame.user.planet.PlanetRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlanetBuildingHandler {

	private final GetStatsOfBuildingsAndTechnologies getStatsOfBuildingsAndTechnologies;
	private final PlanetRepository planetRepository;

	public boolean proofBuildingPossible(Planet planet, String nameOfBuilding) throws Exception {

		if (planet.getRemainingBuildingDuration() > 0) {
			throw new Exception("Es wird schon etwas gebaut");
		} else if (!getStatsOfBuildingsAndTechnologies.existsBuilding(nameOfBuilding)) {
			throw new Exception("Das Gebäude existiert nicht");
		} else if (!getStatsOfBuildingsAndTechnologies.existsNextBuildingLvl(planet.getMetalMineLvl(),
				nameOfBuilding)) {
			throw new Exception("Du hast bereits die Maximalstufe erreicht");
		} else if (planet.getRemainingFields() < 1) {
			throw new Exception("Du hast keinen Platz mehr auf dem Planeten");
		} else {
			int currentLvlOfSpecificBuilding = getCurrentLvlOfSpecificBuilding(planet, nameOfBuilding);
			PlanetBuildingStats planetBuildingStatsOfNextLvl = getStatsOfBuildingsAndTechnologies
					.getBuildingStatsOfNextLvl(currentLvlOfSpecificBuilding, nameOfBuilding);
			if (planet.getMetal() < planetBuildingStatsOfNextLvl.getNecessaryMetal()
					|| planet.getCrystal() < planetBuildingStatsOfNextLvl.getNecessaryCrystal()
					|| planet.getHydrogen() < planetBuildingStatsOfNextLvl.getNecessaryHydrogen()) {

				throw new Exception("Du hast nicht ausreichend Ressourcen auf dem Planeten");
			} else {
				return true;
			}
		}
	}

	public int getCurrentLvlOfSpecificBuilding(Planet planet, String nameOfBuilding) throws Exception {
		if (nameOfBuilding.equalsIgnoreCase(NamesOfPlanetBuildings.METALMINE.toString())) {
			return planet.getMetalMineLvl();
		} else if (nameOfBuilding.equalsIgnoreCase(NamesOfPlanetBuildings.CRYSTALMINE.toString())) {
			return planet.getCrystalMineLvl();
		} else if (nameOfBuilding.equalsIgnoreCase(NamesOfPlanetBuildings.HYDROGENPLANT.toString())) {
			return planet.getCrystalMineLvl();
		} else if (nameOfBuilding.equalsIgnoreCase(NamesOfPlanetBuildings.SOLARPOWERPLANT.toString())) {
			return planet.getCrystalMineLvl();
		} else if (nameOfBuilding.equalsIgnoreCase(NamesOfPlanetBuildings.METALSTOREHOUSE.toString())) {
			return planet.getCrystalMineLvl();
		} else if (nameOfBuilding.equalsIgnoreCase(NamesOfPlanetBuildings.CRYSTALSTOREHOUSE.toString())) {
			return planet.getCrystalMineLvl();
		} else if (nameOfBuilding.equalsIgnoreCase(NamesOfPlanetBuildings.HYDROGENTANK.toString())) {
			return planet.getCrystalMineLvl();
		} else if (nameOfBuilding.equalsIgnoreCase(NamesOfPlanetBuildings.COMMANDCENTRAL.toString())) {
			return planet.getCrystalMineLvl();
		} else {
			throw new Exception("Es liegen keine Informationen über das aktuelle Level des Gebäudes vor");
		}
	}

	public void prepareBuilding(Planet planetWithUpdatedRessources, PlanetBuildingStats specificBuildingStatsOfNextLvl)
			throws Exception {

		ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

		@SuppressWarnings("unused")
		ScheduledFuture<?> scheduledFuture = executorService.schedule(new Callable<Object>() {
			public Object call() throws Exception {
				Planet planetWithFinishedBuilding = build(planetWithUpdatedRessources.getId(),
						specificBuildingStatsOfNextLvl);

				return new ResponseEntity<>(planetWithFinishedBuilding, HttpStatus.OK);
			}
		}, specificBuildingStatsOfNextLvl.getBuildingDuration(), TimeUnit.SECONDS);

		executorService.shutdown();
	}

	public Planet build(Long id, PlanetBuildingStats specificBuildingStatsOfNextLvl) throws Exception {
		if (!planetRepository.existsById(id)) {
			throw new Exception("Planet existiert nicht");
		}
		Planet foundPlanet = planetRepository.findById(id).get();

		foundPlanet = setSomeStatsDependentOnWhichBuilding(foundPlanet, specificBuildingStatsOfNextLvl);

		foundPlanet.setRemainingFields(foundPlanet.getRemainingFields() - 1);
		foundPlanet.setRemainingBuildingDuration(0L);
		foundPlanet.setEnergy(foundPlanet.getEnergy() - specificBuildingStatsOfNextLvl.getNecessaryEnergy());

		planetRepository.save(foundPlanet);
		System.out.println("Bau Ende und gespeichert");
		return foundPlanet;
	}

	private Planet setSomeStatsDependentOnWhichBuilding(Planet foundPlanet,
			PlanetBuildingStats specificBuildingStatsOfNextLvl) throws Exception {

		String nameOfBuilding = specificBuildingStatsOfNextLvl.getNameOfBuilding();

		if (nameOfBuilding.equalsIgnoreCase(NamesOfPlanetBuildings.METALMINE.toString())) {
			foundPlanet.setMetalMineLvl(specificBuildingStatsOfNextLvl.getLevel());
			foundPlanet.setMetalProductionEveryHour(specificBuildingStatsOfNextLvl.getProductionMetal());
		} else if (nameOfBuilding.equalsIgnoreCase(NamesOfPlanetBuildings.CRYSTALMINE.toString())) {
			foundPlanet.setCrystalMineLvl(specificBuildingStatsOfNextLvl.getLevel());
			foundPlanet.setCrystalProductionEveryHour(specificBuildingStatsOfNextLvl.getProductionCrystal());
		} else if (nameOfBuilding.equalsIgnoreCase(NamesOfPlanetBuildings.HYDROGENPLANT.toString())) {
			foundPlanet.setHydrogenPlantLvl(specificBuildingStatsOfNextLvl.getLevel());
			foundPlanet.setHydrogenProductionEveryHour(specificBuildingStatsOfNextLvl.getProductionHydrogen());
		} else if (nameOfBuilding.equalsIgnoreCase(NamesOfPlanetBuildings.SOLARPOWERPLANT.toString())) {
			foundPlanet.setSolarPowerPlantLvl(specificBuildingStatsOfNextLvl.getLevel());
			foundPlanet.setEnergy(foundPlanet.getEnergy() + specificBuildingStatsOfNextLvl.getProductionEnergy());
		} else if (nameOfBuilding.equalsIgnoreCase(NamesOfPlanetBuildings.METALSTOREHOUSE.toString())) {
			foundPlanet.setMetalStorehouseLvl(specificBuildingStatsOfNextLvl.getLevel());
			foundPlanet.setMetalStorehouse(specificBuildingStatsOfNextLvl.getMetalStorehouse());
		} else if (nameOfBuilding.equalsIgnoreCase(NamesOfPlanetBuildings.CRYSTALSTOREHOUSE.toString())) {
			foundPlanet.setCrystalStorehouseLvl(specificBuildingStatsOfNextLvl.getLevel());
			foundPlanet.setCrystalStorehouse(specificBuildingStatsOfNextLvl.getCrystalStorehouse());
		} else if (nameOfBuilding.equalsIgnoreCase(NamesOfPlanetBuildings.HYDROGENTANK.toString())) {
			foundPlanet.setHydrogenTankLvl(specificBuildingStatsOfNextLvl.getLevel());
			foundPlanet.setHydrogenTank(specificBuildingStatsOfNextLvl.getHydrogenTank());
		} else if (nameOfBuilding.equalsIgnoreCase(NamesOfPlanetBuildings.COMMANDCENTRAL.toString())) {
			foundPlanet.setCommandCentralLvl(specificBuildingStatsOfNextLvl.getLevel());
		} else {
			throw new Exception("Das erhöhen des Gebäudelevels ist fehlgeschlagen");
		}
		return foundPlanet;
	}

}
