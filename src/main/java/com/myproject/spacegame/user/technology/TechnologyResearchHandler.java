package com.myproject.spacegame.user.technology;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.myproject.spacegame.buildingStats.BuildingStats;
import com.myproject.spacegame.buildingStats.NamesOfTechnologies;
import com.myproject.spacegame.services.CalculatePointsOfPlayer;
import com.myproject.spacegame.user.spaceship.Spaceship;
import com.myproject.spacegame.user.spaceship.SpaceshipRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TechnologyResearchHandler {

	private final TechnologyRepository technologyRepository;
	private final SpaceshipRepository spaceshipRepository;
	private final CalculatePointsOfPlayer calculatePointsOfPlayer;
	
	public boolean proofBuildPossible(Spaceship spaceship) throws Exception {
		if (spaceship.getRemainingResearchDuration() > 0) {
			throw new Exception("Es wird schon etwas erforscht");
		} else {
			//TODO: Hier müssen weitere Einschränkungen eingefügt werden. Manche Forschungen sollen erst ab einem bestimmten Lvl vom Spaceship oder Forschungslabor möglich sein.
			return true;
		}
	}
	public int getCurrentLvlOfSpecificTechnology(Technology technology, String nameOfTechnology) throws Exception {
		if (nameOfTechnology.equalsIgnoreCase(NamesOfTechnologies.ENERGYTECHNOLOGY.toString())) {
			return technology.getEnergyTechnologyLvl();
		//TODO: Weitere Technologien ergänzen
		} else {
			throw new Exception("Es liegen keine Informationen über das aktuelle Level der Technologie vor");
		}
	}

	public void prepareBuild(Spaceship spaceshipWithUpdatedRessources, Technology technologyFound, BuildingStats statsOfTechnologyNextLvl)
			throws Exception {

		ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

		@SuppressWarnings("unused")
		ScheduledFuture<?> scheduledFuture = executorService.schedule(new Callable<Object>() {
			public Object call() throws Exception {
				Technology researchFinished = build(spaceshipWithUpdatedRessources.getId(), technologyFound.getId(),
						statsOfTechnologyNextLvl);

				return new ResponseEntity<>(researchFinished, HttpStatus.OK);
			}
		}, (long) (statsOfTechnologyNextLvl.getBuildingOrResearchDuration()
				* spaceshipWithUpdatedRessources.getReduceResearchDuration()), TimeUnit.SECONDS);
		executorService.shutdown();
	}

	private Technology build(Long spaceshipId, Long technologyId, BuildingStats statsOfTechnologyNextLvl) throws Exception {
		if (!spaceshipRepository.existsById(spaceshipId)) {
			throw new Exception("Raumschiff existiert nicht");
		}
		if (!technologyRepository.existsById(technologyId)) {
			throw new Exception("Technologie existiert nicht");
		}

		Spaceship foundSpaceship = spaceshipRepository.findById(spaceshipId).get();
		foundSpaceship.setRemainingResearchDuration(0L);
		//TODO Auswirkungen von erhöhter Forschung hier einfügen
		spaceshipRepository.save(foundSpaceship);
		
		Technology foundTechnology = technologyRepository.findById(technologyId).get();
		foundTechnology = setSomeStatsDependentOnWhichTechnology(foundTechnology, statsOfTechnologyNextLvl);
		technologyRepository.save(foundTechnology);
		calculatePointsOfPlayer.calculateAndSaveNewPoints(foundSpaceship.getUser().getId(), statsOfTechnologyNextLvl.getNecessaryMetal(), statsOfTechnologyNextLvl.getNecessaryCrystal(), statsOfTechnologyNextLvl.getNecessaryHydrogen());

		return foundTechnology;
	}

	private Technology setSomeStatsDependentOnWhichTechnology(Technology foundTechnology,
			BuildingStats statsOfTechnologyNextLvl) throws Exception {

		String nameOfTechnology = statsOfTechnologyNextLvl.getNameOfBuildingOTechnology();

		if (nameOfTechnology.equalsIgnoreCase(NamesOfTechnologies.ENERGYTECHNOLOGY.toString())) {
			foundTechnology.setEnergyTechnologyLvl(statsOfTechnologyNextLvl.getLevel());
			//TODO Weitere Forschungen hinzufügen
		} else {
			throw new Exception("Das erhöhen des Gebäudelevels ist fehlgeschlagen");
		}
		return foundTechnology;
	}
}
