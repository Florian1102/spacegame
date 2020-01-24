package com.myproject.spacegame.services;

import org.springframework.stereotype.Service;

import com.myproject.spacegame.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CalculatePointsOfPlayer {

	private final UserRepository userRepository;
	
	//Jede Stunde erhöhen oder immer wenn man etwas gebaut hat?
	
}
