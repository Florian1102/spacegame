package com.myproject.spacegame.user.planet.buildings;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.myproject.spacegame.services.GetStatsOfBuildings;
import com.myproject.spacegame.user.planet.Planet;
import com.myproject.spacegame.user.planet.PlanetRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlanetBuildingHandler {

	private final GetStatsOfBuildings getStatsOfBuildings;
	private final PlanetRepository planetRepository;
	
	public boolean proofBuildingPossible(Planet planet) throws Exception {
		
		if (planet.getRemainingBuildingDuration() > 0) {
			throw new Exception("Es wird schon etwas gebaut");
		} else if (!getStatsOfBuildings.existsNextBuildingLvl(planet.getMetalMineLvl(), NamesOfPlanetBuildings.METALMINE)) {
			throw new Exception("Du hast bereits die Maximalstufe erreicht");
		} else if (planet.getSize() < 1) {
			throw new Exception("Du hast keinen Platz mehr auf dem Planeten");
		} else if (planet.getMetal() < getSpecificPlanetBuildingStatsOfNewLvl(planet.getMetalMineLvl(), NamesOfPlanetBuildings.METALMINE).getNecessaryMetal()) {
			throw new Exception("Du hast nicht ausreichend Ressourcen auf dem Planeten");
		} else {
			return true;
		}
	}

	public void prepareBuilding(Planet planetWithUpdatedRessources) throws Exception {
		
		ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
		
		@SuppressWarnings("unused")
		ScheduledFuture<?> scheduledFuture = executorService.schedule(new Callable<Object>() {
			public Object call() throws Exception {
				Planet planetWithFinishedBuilding = build(planetWithUpdatedRessources.getId());
				
				return new ResponseEntity<>(planetWithFinishedBuilding, HttpStatus.OK);
			}
		}, getSpecificPlanetBuildingStatsOfNewLvl(planetWithUpdatedRessources.getMetalMineLvl(), NamesOfPlanetBuildings.METALMINE).getBuildingDuration(), TimeUnit.SECONDS);
		
		executorService.shutdown();
	}

	public Planet build(Long id) throws Exception {
		if (!planetRepository.existsById(id)) {
			throw new Exception("Planet existiert nicht");
		}
		Planet foundPlanet = planetRepository.findById(id).get();
		PlanetBuildingStats buildingStatsOfNewLvl = getSpecificPlanetBuildingStatsOfNewLvl(foundPlanet.getMetalMineLvl(), NamesOfPlanetBuildings.METALMINE);
		foundPlanet.setMetalMineLvl(buildingStatsOfNewLvl.getLevel());
		foundPlanet.setMetalProductionEveryHour(buildingStatsOfNewLvl.getProductionMetal());
		foundPlanet.setSize(foundPlanet.getSize() - 1);
		foundPlanet.setRemainingBuildingDuration(0L);
		foundPlanet.setEnergy(foundPlanet.getEnergy() - buildingStatsOfNewLvl.getNecessaryEnergy());
		planetRepository.save(foundPlanet);
		System.out.println("Bau Ende und gespeichert");
		return foundPlanet;
	}

	public PlanetBuildingStats getSpecificPlanetBuildingStatsOfNewLvl(int currentBuildingLvl, NamesOfPlanetBuildings nameOfBuilding) throws Exception {
		
		PlanetBuildingStats buildingStatsOfNextLvl = getStatsOfBuildings.getIronMineStatsOfNextLvl(currentBuildingLvl, nameOfBuilding);
		return buildingStatsOfNextLvl;
	}
	
}
