package com.myproject.spacegame.user.attackAndDefenseSystem;

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
import com.myproject.spacegame.user.planet.buildings.NamesOfPlanetBuildings;
import com.myproject.spacegame.user.planet.buildings.PlanetBuildingStats;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttackAndDefenseSystemHandler {

	private final AttackAndDefenseSystemRepository attackAndDefenseSystemRepository;
	private final PlanetRepository planetRepository;

	public boolean proofBuildPossibleAndCalculateRessources(Planet planetFound, String nameOfAttackOrDefenseSystem) throws Exception {

		if (!attackAndDefenseSystemRepository.existsByNameOfAttackDefenseSystem(nameOfAttackOrDefenseSystem)) {
			throw new Exception("Das Gebäude existiert nicht");
		} else {
			AttackAndDefenseSystemStats statsOfAttackOrDefenseSystem = attackAndDefenseSystemRepository.findByNameOfAttackDefenseSystem(nameOfAttackOrDefenseSystem);
			if (planetFound.getMetal() < statsOfAttackOrDefenseSystem.getNecessaryMetal()
					|| planetFound.getCrystal() < statsOfAttackOrDefenseSystem.getNecessaryCrystal()
					|| planetFound.getHydrogen() < statsOfAttackOrDefenseSystem.getNecessaryHydrogen()) {
				
				throw new Exception("Du hast nicht ausreichend Ressourcen auf dem Planeten");
			} else {
				planetFound.setMetal(planetFound.getMetal() - statsOfAttackOrDefenseSystem.getNecessaryMetal());
				planetFound.setCrystal(planetFound.getCrystal() - statsOfAttackOrDefenseSystem.getNecessaryCrystal());
				planetFound.setHydrogen(planetFound.getHydrogen() - statsOfAttackOrDefenseSystem.getNecessaryHydrogen());
				
				planetFound.setRemainingBuildingDuration((long) (statsOfAttackOrDefenseSystem.getBuildingDuration()*(Math.pow(0.5, planetFound.getCommandCentralLvl()-1))));
				planetRepository.save(planetFound);
				return true;
			}
		}
	}
	
	public void prepareBuilding(Planet planetWithUpdatedRessources, AttackAndDefenseSystemStats statsOfAttackOrDefenseSystem)
			throws Exception {

		ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

		@SuppressWarnings("unused")
		ScheduledFuture<?> scheduledFuture = executorService.schedule(new Callable<Object>() {
			public Object call() throws Exception {
				Planet planetWithFinishedBuilding = build(planetWithUpdatedRessources.getId(), statsOfAttackOrDefenseSystem);

				return new ResponseEntity<>(planetWithFinishedBuilding, HttpStatus.OK);
			}
		}, (long) (statsOfAttackOrDefenseSystem.getBuildingDuration()*(Math.pow(0.6, planetWithUpdatedRessources.getCommandCentralLvl()-1))), TimeUnit.SECONDS);

		executorService.shutdown();
	}

	public Planet build(Long id, AttackAndDefenseSystemStats statsOfAttackOrDefenseSystem) throws Exception {
		if (!planetRepository.existsById(id)) {
			throw new Exception("Planet existiert nicht");
		}
		Planet foundPlanet = planetRepository.findById(id).get();

		foundPlanet.setRemainingBuildingDuration(0L);
		foundPlanet.setSolarSatellite(foundPlanet.getSolarSatellite() + 1);
		foundPlanet.setEnergy(foundPlanet.getEnergy() + statsOfAttackOrDefenseSystem.getEnergy());

		planetRepository.save(foundPlanet);
		return foundPlanet;
	}
}
