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

	private final IronMineStatsRepository ironMineStatsRepository;
	private final PlanetRepository planetRepository;
	
	public boolean proofBuildingPossible(Planet planet) throws Exception {
		Long ironMineNewLvl = planet.getIronMineLvl() + 1;
		
		if (planet.getRemainingBuildingDuration() > 0) {
			throw new Exception("Es wird schon etwas gebaut");
		} else if (!ironMineStatsRepository.existsById(ironMineNewLvl)) {
			throw new Exception("Du hast bereits die Maximalstufe erreicht");
		} else if (planet.getSize() < 1) {
			throw new Exception("Du hast keinen Platz mehr auf dem Planeten");
		} else if (planet.getIron() < ironMineStatsRepository.findById(ironMineNewLvl).get().getNecessaryIron()) {
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
		}, getPlanetBuildingStatsOfNewLvl(planetWithUpdatedRessources.getIronMineLvl()).getBuildingDuration(), TimeUnit.SECONDS);
		
		executorService.shutdown();
	}

	public Planet build(Long id) throws Exception {
		if (!planetRepository.existsById(id)) {
			throw new Exception("Planet existiert nicht");
		}
		Planet foundPlanet = planetRepository.findById(id).get();
		PlanetBuildingStats buildingStatsOfNewLvl = getPlanetBuildingStatsOfNewLvl(foundPlanet.getIronMineLvl());
		foundPlanet.setIronMineLvl(buildingStatsOfNewLvl.getLevel());
		foundPlanet.setIronProductionEveryHour(buildingStatsOfNewLvl.getProductionIron());
		foundPlanet.setSize(foundPlanet.getSize() - 1);
		foundPlanet.setRemainingBuildingDuration(0L);
		foundPlanet.setEnergy(foundPlanet.getEnergy() - buildingStatsOfNewLvl.getNecessaryEnergy());
		planetRepository.save(foundPlanet);
		System.out.println("Bau Ende und gespeichert");
		return foundPlanet;
	}

	public PlanetBuildingStats getPlanetBuildingStatsOfNewLvl(Long buildingLvl) throws Exception {
		buildingLvl += 1;
		if (!ironMineStatsRepository.existsById(buildingLvl)) {
			throw new Exception("Es sind zurzeit keine Informationen verfügbar");
		}
		PlanetBuildingStats buildingStatsWithLvl = ironMineStatsRepository.findById(buildingLvl).get();
		return buildingStatsWithLvl;
	}
	
}
