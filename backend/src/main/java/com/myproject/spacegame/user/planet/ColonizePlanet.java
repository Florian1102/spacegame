package com.myproject.spacegame.user.planet;

import java.util.Random;

import org.springframework.stereotype.Service;

import com.myproject.spacegame.coordinateSystem.CoordinateSystem;
import com.myproject.spacegame.coordinateSystem.CoordinateSystemRepository;
import com.myproject.spacegame.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ColonizePlanet {

	private final CoordinateSystemRepository coordinateSystemRepository;
	private final UserRepository userRepository;
	private final PlanetRepository planetRepository;
	
	public void colonizePlanet(Long userId, int galaxy, int system, int position) {
		try {
			if (!userRepository.existsById(userId)) {
				throw new Exception("Spieler wurde nicht gefunden");
			} else if (!allowToAddPlanet(userId)) {
				throw new Exception("Du kannst keine weiteren Planeten kolonisieren");
			} else if (!isCoordinateAvailable(galaxy, system, position)) {
				throw new Exception("Die Koordinate gibt es nicht");
			} else {
				Planet createdPlanet = setupPlanet(userId, galaxy, system, position);
				planetRepository.save(createdPlanet);
				CoordinateSystem coordinate = coordinateSystemRepository.findByGalaxyAndSystemAndPosition(galaxy, system, position);
				coordinate.setPlanet(createdPlanet);
				coordinateSystemRepository.save(coordinate);
			}
		} catch (Exception e) {
			e.getMessage();
		}
	}

	public boolean allowToAddPlanet(Long userId) throws Exception {

		if (planetRepository.countByUserId(userId) >= 3) {
			throw new Exception("Du kannst keinen weiteren Planeten kolonisieren");
			// TODO: Abhängig von einer Forschung machen
		} else {
			return true;
		}
	}

	public boolean isCoordinateAvailable(int galaxy, int system, int position) throws Exception {
		if (!coordinateSystemRepository.existsByGalaxyAndSystemAndPosition(galaxy, system, position)) {
			throw new Exception("Die Koordinate gibt es nicht");
		} else if (position == 0) {
			throw new Exception("Die Position kann nicht besiedelt werden");
		} else if ((coordinateSystemRepository.findByGalaxyAndSystemAndPosition(galaxy, system, position)
				.getPlanet() != null)) {
			throw new Exception("Der Planet ist schon besiedelt");
		} else {
			return true;
		}
	}

	private Planet setupPlanet(Long userId, int galaxy, int system, int position) {
		Planet planet = new Planet();
		planet.setId(null);
		planet.setUser(userRepository.findById(userId).get());
		planet.setCoordinates(coordinateSystemRepository.findByGalaxyAndSystemAndPosition(galaxy, system, position));
		planet.setName("Planet");
		Random random = new Random();
		planet.setFields(100 + random.nextInt(200 - 100 + 1));
		planet.setRemainingFields(planet.getFields());
		planet.setMetal(500);
		planet.setCrystal(500);
		planet.setHydrogen(0);
		planet.setEnergy(0.0);
		planet.setMetalProductionEveryHour(10);
		planet.setCrystalProductionEveryHour(10);
		planet.setHydrogenProductionEveryHour(0);
		planet.setCommandCentralLvl(0);
		planet.setMetalMineLvl(0);
		planet.setCrystalMineLvl(0);
		planet.setHydrogenPlantLvl(0);
		planet.setSolarPowerPlantLvl(0);
		planet.setMetalStorehouseLvl(0);
		planet.setCrystalStorehouseLvl(0);
		planet.setHydrogenTankLvl(0);
		planet.setMetalStorehouse(5000);
		planet.setCrystalStorehouse(5000);
		planet.setHydrogenTank(1000);
		planet.setReduceBuildingDuration(1.0);
		return planet;
	}
}
