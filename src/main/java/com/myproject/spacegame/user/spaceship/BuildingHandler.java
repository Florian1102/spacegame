package com.myproject.spacegame.user.spaceship;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BuildingHandler {

	private final SpaceshipRepository spaceshipRepository;
	private final SpaceshipStatsRepository spaceshipStatsRepository;

	public boolean proofUpdatePossible(Spaceship spaceship) throws Exception {

		Long spaceshipNewLvl = spaceship.getSpaceshipLvl() + 1;

		if (spaceship.getRemainingBuildingDuration() > 0) {
			throw new Exception("Es wird schon etwas gebaut");
		} else if (!spaceshipStatsRepository.existsById(spaceshipNewLvl)) {
			throw new Exception("Du hast bereits die Maximalstufe erreicht");
		} else if (spaceship.getIron() < spaceshipStatsRepository.findById(spaceshipNewLvl).get().getNecessaryIron()) {
			throw new Exception("Du hast nicht ausreichend Ressourcen auf dem Raumschiff");
		} else {
			return true;
		}
	}

	public void prepareBuidling(Spaceship spaceshipWithUpdatedRessources) throws Exception {
		ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

		@SuppressWarnings("unused")
		ScheduledFuture<?> scheduledFuture = executorService.schedule(new Callable<Object>() {
			public Object call() throws Exception {
				Spaceship buildedSpaceship = build(spaceshipWithUpdatedRessources.getId());
				return new ResponseEntity<>(buildedSpaceship, HttpStatus.OK);
			}
		}, getSpaceshipStatsOfNewLvl(spaceshipWithUpdatedRessources.getSpaceshipLvl())
				.getBuildingDuration(), TimeUnit.SECONDS);
		executorService.shutdown();
	}

	public Spaceship build(Long id) throws Exception {

		if (!spaceshipRepository.existsById(id)) {
			throw new Exception("Raumschiff existiert nicht");
		}
		Spaceship foundSpaceship = spaceshipRepository.findById(id).get();
		SpaceshipStats spaceshipStatsOfNewLvl = getSpaceshipStatsOfNewLvl(foundSpaceship.getSpaceshipLvl());
		foundSpaceship.setSpaceshipLvl(spaceshipStatsOfNewLvl.getLevel());
		foundSpaceship.setAttackPower(spaceshipStatsOfNewLvl.getAttackPower());
		foundSpaceship.setDefense(spaceshipStatsOfNewLvl.getDefense());
		foundSpaceship.setSpeed(spaceshipStatsOfNewLvl.getSpeed());
		foundSpaceship.setRemainingBuildingDuration(0L);
		spaceshipRepository.save(foundSpaceship);

		System.out.println("Bau Ende und gespeichert");
		return foundSpaceship;
	}
	
	public SpaceshipStats getSpaceshipStatsOfNewLvl(Long currentSpaceshipLvl) throws Exception {
		currentSpaceshipLvl += 1;
		if (!spaceshipStatsRepository.existsById(currentSpaceshipLvl)) {
			throw new Exception("Es sind zurzeit keine Informationen verfügbar");
		}
		SpaceshipStats spaceshipStatsWithLvl = spaceshipStatsRepository.findById(currentSpaceshipLvl).get();
		return spaceshipStatsWithLvl;
	}
}
