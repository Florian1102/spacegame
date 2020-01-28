package com.myproject.spacegame.services;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.myproject.spacegame.user.planet.Planet;
import com.myproject.spacegame.user.planet.PlanetRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RessourceProduction {

	private final PlanetRepository planetRepository;

		ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

		ScheduledFuture<?> scheduledFuture = executorService.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				List<Planet> updatedPlanets = ressourceProductionForAllPlanets();
				planetRepository.saveAll(updatedPlanets);
			}
		}, 10, 10, TimeUnit.SECONDS);

	public List<Planet> ressourceProductionForAllPlanets() {
		List<Planet> planets = planetRepository.findAll();
		
		planets.stream().forEach(planet -> {
			if (planet.getEnergy() >= 0) {
				if (planet.getMetal() <= planet.getMetalStorehouse()) {
					planet.setMetal(planet.getMetal() + (planet.getMetalProductionEveryHour()/360));
				}
				if (planet.getCrystal() <= planet.getCrystalStorehouse()) {
					planet.setCrystal(planet.getCrystal() + (planet.getCrystalProductionEveryHour()/360));
				}
				if (planet.getHydrogen() <= planet.getHydrogenTank()) {
					planet.setHydrogen(planet.getHydrogen() + (planet.getHydrogenProductionEveryHour()/360));
				}
			} else {
				if (planet.getMetal() <= planet.getMetalStorehouse()) {
					planet.setMetal(planet.getMetal() + (planet.getMetalProductionEveryHour()/360/2));
				}
				if (planet.getCrystal() <= planet.getCrystalStorehouse()) {
					planet.setCrystal(planet.getCrystal() + (planet.getCrystalProductionEveryHour()/360/2));
				}
				if (planet.getHydrogen() <= planet.getHydrogenTank()) {
					planet.setHydrogen(planet.getHydrogen() + (planet.getHydrogenProductionEveryHour()/360/2));
				}
			}
		});
	
		return planets;
	}
}
