package com.myproject.spacegame.coordinateSystem;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/coordinatesystem")
@RequiredArgsConstructor
public class CoordinateSystemController {

private final CoordinateSystemRepository coordinateSystemRepository;
	
	@GetMapping()
	@ResponseStatus(HttpStatus.OK)
	public List<CoordinateSystem> showCoordinateSystem(@RequestParam(required = true) int galaxy, @RequestParam(required = true) int system) {
		
		return coordinateSystemRepository.findAllByGalaxyAndSystem(galaxy, system);
	}
}
