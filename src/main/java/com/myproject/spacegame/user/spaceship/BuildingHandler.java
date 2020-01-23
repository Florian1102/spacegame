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

	private final RessourceHandler ressourceHandler;
	private final SpaceshipRepository spaceshipRepository;

	public Spaceship updateIfPossible(Spaceship spaceship) throws Exception {

		if (spaceship.getRemainingBuildingDuration() > 0) {
			throw new Exception("Das Raumschiff befindet sich bereits im Bau");
		} else {
			Spaceship spaceshipWithNewRessources = ressourceHandler.calculateNewSpaceshipRessources(spaceship);
			System.out.println("Ressourcen wurden überarbeitet");
			System.out.println(spaceshipWithNewRessources.getId() + ": Bau beginnt");

			prepareBuidling(spaceshipWithNewRessources);
			return spaceshipWithNewRessources;
		}
	}

	public void prepareBuidling(Spaceship spaceshipWithUpdatedRessources) throws Exception {
		ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

		ScheduledFuture<?> scheduledFuture = executorService.schedule(new Callable<Object>() {
			public Object call() throws Exception {
				Spaceship buildedSpaceship = buildNewSpaceship(spaceshipWithUpdatedRessources);
				spaceshipRepository.save(buildedSpaceship);
				return new ResponseEntity<>(buildedSpaceship, HttpStatus.OK);
			}
		}, ressourceHandler.getSpaceshipStatsOfNewLvl(spaceshipWithUpdatedRessources.getSpaceshipLvl())
				.getBuildingDuration(), TimeUnit.SECONDS);
		spaceshipWithUpdatedRessources.setRemainingBuildingDuration(scheduledFuture.getDelay(TimeUnit.SECONDS));
		executorService.shutdown();
	}

	public Spaceship buildNewSpaceship(Spaceship spaceship) throws Exception {

		SpaceshipStats spaceshipStatsOfNewLvl = ressourceHandler.getSpaceshipStatsOfNewLvl(spaceship.getSpaceshipLvl());
		spaceship.setSpaceshipLvl(spaceshipStatsOfNewLvl.getLevel());
		spaceship.setAttackPower(spaceshipStatsOfNewLvl.getAttackPower());
		spaceship.setDefense(spaceshipStatsOfNewLvl.getDefense());
		spaceship.setSpeed(spaceshipStatsOfNewLvl.getSpeed());
		spaceship.setRemainingBuildingDuration(0L);

		System.out.println("Bau Ende und gespeichert");
		return spaceship;
	}
}
