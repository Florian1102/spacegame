package com.myproject.spacegame.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.myproject.spacegame.user.planet.Planet;
import com.myproject.spacegame.user.planet.PlanetBuildingHandler;
import com.myproject.spacegame.user.planet.PlanetRepository;
import com.myproject.spacegame.user.spaceship.Spaceship;
import com.myproject.spacegame.user.spaceship.SpaceshipBuildingHandler;
import com.myproject.spacegame.user.spaceship.SpaceshipRepository;
import com.myproject.spacegame.user.technology.Technology;
import com.myproject.spacegame.user.technology.TechnologyRepository;
import com.myproject.spacegame.user.technology.TechnologyResearchHandler;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BuildingHandler {
	
	private final PlanetRepository planetRepository;
	private final SpaceshipRepository spaceshipRepository;
	private final TechnologyRepository technologyRepository;
	private final PlanetBuildingHandler planetBuildingHandler;
	private final SpaceshipBuildingHandler spaceshipBuildingHandler;
	private final TechnologyResearchHandler technologyResearchHandler;
	private final GetStatsOfBuildingsAndTechnologies getStatsOfBuildingsAndTechnologies;

	ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

	ScheduledFuture<?> scheduledFuture = executorService.scheduleAtFixedRate(new Runnable() {

		@Override
		public void run() {
			proofPlanetBuildEveryTimeUnit();
			proofSpaceshipBuildEveryTimeUnit();
			proofResearchEveryTimeUnit();
		}
	}, 1, 5, TimeUnit.SECONDS);
	
	private void proofPlanetBuildEveryTimeUnit() {
		List<Planet> planets = planetRepository.findAll();
		LocalDateTime dateNow = LocalDateTime.now();
		planets.stream().forEach(planet -> {
				if (planet.getEndOfBuilding() != null && dateNow.isAfter(planet.getEndOfBuilding())) {
					try {
						planetBuildingHandler.build(planet.getId(), getStatsOfBuildingsAndTechnologies.getBuildingOrTechnologyStatsOfNextLvl(planet.getCurrentLvlOfBuilding(), planet.getNameOfBuilding()));
					} catch (Exception e) {
						e.getMessage();
					}
			}
		});
	}
	
	private void proofSpaceshipBuildEveryTimeUnit() {
		List<Spaceship> spaceships = spaceshipRepository.findAll();
		LocalDateTime dateNow = LocalDateTime.now();
		spaceships.stream().forEach(spaceship -> {
			if (spaceship.getEndOfBuilding() != null && dateNow.isAfter(spaceship.getEndOfBuilding())) {
				try {
					spaceshipBuildingHandler.build(spaceship.getId(), getStatsOfBuildingsAndTechnologies.getBuildingOrTechnologyStatsOfNextLvl(spaceship.getCurrentLvlOfBuilding(), spaceship.getNameOfBuilding()));
				} catch (Exception e) {
					e.getMessage();
				}
			}
		});
	}
	
	private void proofResearchEveryTimeUnit() {
		List<Technology> technologies = technologyRepository.findAll();
		LocalDateTime dateNow = LocalDateTime.now();
		technologies.stream().forEach(technology -> {
			if (technology.getEndOfResearch() != null && dateNow.isAfter(technology.getEndOfResearch())) {
				try {
					technologyResearchHandler.build(technology.getUser().getSpaceship().getId(), technology.getId(), getStatsOfBuildingsAndTechnologies.getBuildingOrTechnologyStatsOfNextLvl(technology.getCurrentLvlOfResearch(), technology.getNameOfResearch()));
				} catch (Exception e) {
					e.getMessage();
				}
			}
		});
	}

}
