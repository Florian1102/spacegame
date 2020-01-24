package com.myproject.spacegame.user.technology.technologyService;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.myproject.spacegame.services.GetStatsOfBuildingsAndTechnologies;
import com.myproject.spacegame.user.technology.Technology;
import com.myproject.spacegame.user.technology.TechnologyRepository;
import com.myproject.spacegame.user.technology.technologyStats.NamesOfTechnologies;
import com.myproject.spacegame.user.technology.technologyStats.TechnologyStats;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TechnologyHandler {

	private final TechnologyRepository technologyRepository;
	private final GetStatsOfBuildingsAndTechnologies getStatsOfBuildingsAndTechnologies;
	
	public boolean proofIncreasePossible(Technology technology) throws Exception {
		
		if (technology.getRemainingIncreaseDuration() > 0) {
			throw new Exception("Es wird schon etwas erforscht");
		} else if (!getStatsOfBuildingsAndTechnologies.existsNextTechnologyLvl(technology.getEnergyTechnologyLvl(), NamesOfTechnologies.ENERGY)) {
			throw new Exception("Du hast bereits die Maximalstufe erreicht");
		} else {
			TechnologyStats technologyStatsOfNextLvl = getStatsOfBuildingsAndTechnologies.getTechnologyStatsOfNextLvl(technology.getEnergyTechnologyLvl(), NamesOfTechnologies.ENERGY);
			if (technology.getUser().getSpaceship().getMetal() < technologyStatsOfNextLvl.getNecessaryMetal() ||
				technology.getUser().getSpaceship().getCrystal() < technologyStatsOfNextLvl.getNecessaryCrystal() ||
				technology.getUser().getSpaceship().getHydrogen() < technologyStatsOfNextLvl.getNecessaryHydrogen() ||
				technology.getUser().getSpaceship().getEnergy() < technologyStatsOfNextLvl.getNecessaryEnergy()	) {
				
				throw new Exception("Du hast nicht ausreichend Ressourcen auf dem Raumschiff");
			} else {
				return true;
			}
		}
	}
	
	public void prepareBuilding(Technology technology) throws Exception {
		
		TechnologyStats technologyStatsOfNewLvl = getStatsOfBuildingsAndTechnologies.getTechnologyStatsOfNextLvl(technology.getEnergyTechnologyLvl(), NamesOfTechnologies.ENERGY);
		technology.setRemainingIncreaseDuration(technologyStatsOfNewLvl.getBuildingDuration());
		technologyRepository.save(technology);
		
		ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
		
		@SuppressWarnings("unused")
		ScheduledFuture<?> scheduledFuture = executorService.schedule(new Callable<Object>() {
			public Object call() throws Exception {
				Technology finishedTechnology = build(technology.getId());
				
				return new ResponseEntity<>(finishedTechnology, HttpStatus.OK);
			}
		}, technologyStatsOfNewLvl.getBuildingDuration(),TimeUnit.SECONDS);
		
		executorService.shutdown();
	}

	public Technology build(Long id) throws Exception {
		if (!technologyRepository.existsById(id)) {
			throw new Exception("Technologie existiert nicht");
		}
		Technology foundTechnology = technologyRepository.findById(id).get();
		
		foundTechnology.setEnergyTechnologyLvl(foundTechnology.getEnergyTechnologyLvl() + 1);
		foundTechnology.setRemainingIncreaseDuration(0L);
		technologyRepository.save(foundTechnology);
		// TODO Hier muss noch eingefügt werden, was durch die Forschung am Raumschiff oder Planeten verbessert wird. ZB GetRaumschiff, increase speed, save
		
		System.out.println("Forschung abgeschlossen");
		return foundTechnology;
	
	}
	
//	public TechnologyStats getTechnologyStatsOfNewLvl(int currentTechnologyLvl) throws Exception {
//		currentTechnologyLvl += 1;
//		if (!getexistsByLevelAndNameOfTechnology(currentTechnologyLvl, NamesOfTechnologies.ENERGY.toString().toLowerCase())) {
//			throw new Exception("Es sind zurzeit keine Informationen verfügbar");
//		}
//		TechnologyStats technologyStatsWithLvl = technologyStatsRepository.findByLevelAndNameOfTechnology(currentTechnologyLvl, NamesOfTechnologies.ENERGY.toString().toLowerCase());
//		return technologyStatsWithLvl;
//	}
}
