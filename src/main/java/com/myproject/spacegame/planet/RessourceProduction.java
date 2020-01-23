package com.myproject.spacegame.planet;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RessourceProduction {

	private final PlanetRepository planetRepository;

		ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

		ScheduledFuture<?> scheduledFuture = executorService.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				List<Planet> updatedPlanets = ressourceProductionEveryTenSecondsForAllPlanets();
				planetRepository.saveAll(updatedPlanets);
//				System.out.println("Ressources updated");
			}
		}, 10, 10, TimeUnit.SECONDS);

	public List<Planet> ressourceProductionEveryTenSecondsForAllPlanets() {
		List<Planet> planets = planetRepository.findAll();
		planets.stream().forEach(planet -> planet.setIron(planet.getIron() + (planet.getIronProductionEveryHour()/360)));
	
		return planets;
	}
}
