package com.myproject.spacegame.user.spaceship;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RessourceHandler {
	
	private final SpaceshipRepository spaceshipRepository;
	private final SpaceshipStatsRepository spaceshipStatsRepository;

	public Spaceship calculateNewSpaceshipRessources(Spaceship spaceship) throws Exception {

		SpaceshipStats spaceshipStatsOfNewLvl = getSpaceshipStatsOfNewLvl(spaceship.getSpaceshipLvl());
		Long necessaryIron = spaceshipStatsOfNewLvl.getNecessaryIron();
		if (spaceship.getIron() < necessaryIron) {
			throw new Exception("Du hast nicht ausreichend Ressourcen");
		} else {
			spaceship.setIron(spaceship.getIron() - necessaryIron);
			return spaceship;
		}
	}
	
	public SpaceshipStats getSpaceshipStatsOfNewLvl(Long spaceshipLvl) throws Exception {
		spaceshipLvl += 1;
		if (!spaceshipStatsRepository.existsById(spaceshipLvl)) {
			throw new Exception("Es sind zurzeit keine Informationen verfügbar");
		}
		SpaceshipStats spaceshipStatsWithLvl = spaceshipStatsRepository.findById(spaceshipLvl).get();
		return spaceshipStatsWithLvl;
	}
	
	public void addRessources(Long id, Long iron, boolean addOrRemove) throws Exception {
		if (!spaceshipRepository.existsById(id)) {
			throw new Exception("Das Raumschiff existiert nicht");
		} else {
			Spaceship spaceshipFound = spaceshipRepository.findById(id).get();
			if (addOrRemove) {
				spaceshipFound.setIron(spaceshipFound.getIron() + iron);				
			} else {
				if (spaceshipFound.getIron() <= iron) {
					throw new Exception("Du hast nicht ausreichend Ressourcen");
				} else {
					spaceshipFound.setIron(spaceshipFound.getIron() - iron);				
				}
			}
			spaceshipRepository.save(spaceshipFound);
		}
	}
}
