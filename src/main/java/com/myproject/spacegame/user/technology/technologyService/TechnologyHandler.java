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
	
	public boolean proofIncreasePossible(Technology technology, String technologyName) throws Exception {
		
		if (technology.getRemainingIncreaseDuration() > 0) {
			throw new Exception("Es wird schon etwas erforscht");
		} else if (!getStatsOfBuildingsAndTechnologies.existsTechnology(technologyName)) {
			throw new Exception("Die Technologie existiert nicht");
		} else if (!getStatsOfBuildingsAndTechnologies.existsNextTechnologyLvl(technology.getEnergyTechnologyLvl(), technologyName)) {
			throw new Exception("Du hast bereits die Maximalstufe erreicht");
		} else {
			int currentLvlOfSpecificTechnology = getCurrentLvlOfSpecificTechnology(technology, technologyName);
			TechnologyStats technologyStatsOfNextLvl = getStatsOfBuildingsAndTechnologies.getTechnologyStatsOfNextLvl(currentLvlOfSpecificTechnology, technologyName);
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
	
	public int getCurrentLvlOfSpecificTechnology(Technology technology, String nameOfTechnology) throws Exception {
		if (nameOfTechnology.equalsIgnoreCase(NamesOfTechnologies.ENERGY.toString())) {
			return technology.getEnergyTechnologyLvl();
			
		//TODO: Um weitere Technologien ergänzen
		} else {
			throw new Exception("Es liegen keine Informationen über das aktuelle Level der Technologie vor");
		}
	}
	
	public void prepareBuilding(Technology technology, TechnologyStats technologyStatsOfNewLvl) throws Exception {
		
		technology.setRemainingIncreaseDuration(technologyStatsOfNewLvl.getBuildingDuration());
		technologyRepository.save(technology);
		
		ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
		
		@SuppressWarnings("unused")
		ScheduledFuture<?> scheduledFuture = executorService.schedule(new Callable<Object>() {
			public Object call() throws Exception {
				Technology finishedTechnology = build(technology.getId(), technologyStatsOfNewLvl);
				
				return new ResponseEntity<>(finishedTechnology, HttpStatus.OK);
			}
		}, technologyStatsOfNewLvl.getBuildingDuration(),TimeUnit.SECONDS);
		
		executorService.shutdown();
	}

	public Technology build(Long id, TechnologyStats technologyStatsOfNewLvl) throws Exception {
		if (!technologyRepository.existsById(id)) {
			throw new Exception("Technologie existiert nicht");
		}
		Technology foundTechnology = technologyRepository.findById(id).get();
		
		setSomeStatsDependentOnWhichTechnology(foundTechnology, technologyStatsOfNewLvl);
		
		foundTechnology.setRemainingIncreaseDuration(0L);
		technologyRepository.save(foundTechnology);
		// TODO Hier muss noch eingefügt werden, was durch die Forschung am Raumschiff oder Planeten verbessert wird. ZB GetRaumschiff, increase speed, save
		
		System.out.println("Forschung abgeschlossen");
		return foundTechnology;
	
	}
	private Technology setSomeStatsDependentOnWhichTechnology(Technology foundTechnology,
			TechnologyStats technologyStatsOfNewLvl) throws Exception {

		String nameOfTechnology = technologyStatsOfNewLvl.getNameOfTechnology();

		if (nameOfTechnology.equalsIgnoreCase(NamesOfTechnologies.ENERGY.toString())) {
			foundTechnology.setEnergyTechnologyLvl(technologyStatsOfNewLvl.getLevel());
		// TODO: Hier müssen alle Forschungen eingefügt werden	
		
		} else {
			throw new Exception("Das erhöhen des Gebäudelevels ist fehlgeschlagen");
		}
		return foundTechnology;
	}
}
