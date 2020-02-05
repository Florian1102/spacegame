package com.myproject.spacegame.services;

import org.springframework.stereotype.Service;

import com.myproject.spacegame.user.User;
import com.myproject.spacegame.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CalculatePointsOfPlayer {

	private final UserRepository userRepository;

	public void calculateAndSaveNewPoints(Long userId, Long necessaryMetal, Long necessaryCrystal,
			Long necessaryHydrogen) throws Exception {

		if (!userRepository.existsById(userId)) {
			throw new Exception("Punkte berechnen nicht möglich, da es den Spieler nicht gibt");
		} else {
			User user = userRepository.findById(userId).get();
			double newPoints = user.getPoints() + ((necessaryMetal + necessaryCrystal + necessaryHydrogen)/1000.0);
			user.setPoints(newPoints);
			userRepository.save(user);
		}
	}
}
