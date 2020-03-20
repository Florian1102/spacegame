package com.myproject.spacegame.user.spaceship;

import org.springframework.stereotype.Service;

import com.myproject.spacegame.buildingStats.BuildingStats;
import com.myproject.spacegame.services.GetStatsOfBuildingsAndTechnologies;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SpaceshipHandler {

	private final SpaceshipRepository spaceshipRepository;
	private final GetStatsOfBuildingsAndTechnologies getStatsOfBuildingsAndTechnologies;

	public Spaceship calculateAndSaveSpaceshipStats(Spaceship spaceshipFound) throws Exception {

		Long attackPower = 0L;
		Long defense = 0L;
		Long speed = 0L;
		double hydrogenConsumption = 0.0;
		try {
			BuildingStats spaceshipStats = getStatsOfBuildingsAndTechnologies
					.getBuildingOrTechnologyStats(spaceshipFound.getSpaceshipLvl() + 1, "spaceship");
			// TODO: alle Buildings und Forschungen vom Rauschiff berücksichtigen
			attackPower = attackPower + spaceshipStats.getAttackPower(); // und hier addieren
			defense = defense + spaceshipStats.getDefense();
			speed = speed + spaceshipStats.getSpeed();
			hydrogenConsumption = hydrogenConsumption + spaceshipStats.getHydrogenConsumption();
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}

		if (spaceshipFound.isFighterSpaceship()) {
			attackPower = (long) (attackPower * 1.3); // TODO: Faktoren anpassen, welchen Bonus das Spaceship erhält
			speed = (long) (speed * 1.2);
		} else if (spaceshipFound.isMerchantSpaceship()) {
			defense = (long) (defense * 1.2);
			speed = (long) (speed * 1.4);
			hydrogenConsumption = (long) (hydrogenConsumption * 0.8);
		}
		spaceshipFound.setAttackPower(attackPower);
		spaceshipFound.setDefense(defense);
		spaceshipFound.setSpeed(speed);
		spaceshipFound.setHydrogenConsumption(hydrogenConsumption);
		spaceshipRepository.save(spaceshipFound);

		return spaceshipFound;
	}

	public Long calculateFlightDuration(Spaceship spaceship, int galaxy, int system, int position) {

		Long galaxyDistance = (long) Math.abs((spaceship.getCurrentPosition().getGalaxy() - galaxy));
		Long systemDistance = (long) Math.abs((spaceship.getCurrentPosition().getSystem() - system));
		Long positionDistance = (long) Math.abs((spaceship.getCurrentPosition().getPosition() - position));

		Long timeForDistance = (galaxyDistance * 60) + (systemDistance * 20) + (positionDistance * 10); // TODO: ändern
																										// in 5 STunden,
																										// halbe Stunde,
																										// 15 Minuten
		Long flightDuration = (long) Math.abs((timeForDistance - spaceship.getSpeed() / 10));
		return flightDuration;
	}

	public double calculateHydrogenConsumption(Spaceship spaceship, int galaxy, int system, int position) {
		Long galaxyDistance = (long) Math.abs((spaceship.getCurrentPosition().getGalaxy() - galaxy));
		Long systemDistance = (long) Math.abs((spaceship.getCurrentPosition().getSystem() - system));
		Long positionDistance = (long) Math.abs((spaceship.getCurrentPosition().getPosition() - position));

		Long hydrogenConsumptionForDistance = (galaxyDistance * 300) + (systemDistance * 15) + (positionDistance * 10); // TODO:
																														// evtl.
		// Werte
		// anpassen
		double hydrogenConsumption = hydrogenConsumptionForDistance * spaceship.getHydrogenConsumption();
		return hydrogenConsumption;
	}


//	public void flyBack(Long spaceshipId, Long flightDuration, CoordinateSystem lastPosition) {
//				Spaceship spaceshipFound = spaceshipRepository.findById(spaceshipId).get();
//				spaceshipFound.setCurrentPosition(lastPosition);
//				spaceshipRepository.save(spaceshipFound);
//	}

}
