package com.myproject.spacegame.user.planet;

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
		// TODO Nur wenn Energy positiv ist ansonsten weniger
		
		planets.stream().forEach(planet -> {
			planet.setMetal(planet.getMetal() + (planet.getMetalProductionEveryHour()/360));
			planet.setCrystal(planet.getCrystal() + (planet.getCrystalProductionEveryHour()/360));
			planet.setCrystal(planet.getHydrogen() + (planet.getHydrogenProductionEveryHour()/360));
		});
	
		return planets;
	}
}
