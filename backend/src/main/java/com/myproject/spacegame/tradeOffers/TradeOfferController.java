package com.myproject.spacegame.tradeOffers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.myproject.spacegame.user.User;
import com.myproject.spacegame.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/tradeoffers")
@RequiredArgsConstructor
public class TradeOfferController {

	private final TradeOfferRepository tradeOfferRepository;
	private final UserRepository userRepository;
	
	@GetMapping()
	@ResponseStatus(HttpStatus.OK)
	public List<TradeOffer> showTradeOffers() {

		return tradeOfferRepository.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> showTradeOffer(@PathVariable Long id) {

		return ResponseEntity.of(tradeOfferRepository.findById(id));
	}
	
	@GetMapping("/user/{userId}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> showTradeOffersOfUser(@PathVariable Long userId) {

		if (userId == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else {
			List<TradeOffer> tradeOffers = tradeOfferRepository.findAllByTradeOfferOfUserId(userId);
			return new ResponseEntity<>(tradeOffers, HttpStatus.OK);
		}
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public TradeOffer create(@RequestBody @Valid TradeOffer tradeOffer) {

		tradeOffer.setId(null);
		tradeOffer.setOfferActive(true);
		return tradeOfferRepository.save(tradeOffer);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable Long id, @RequestBody @Valid TradeOffer tradeOffer) {

		if (!tradeOfferRepository.existsById(id)) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		tradeOffer.setId(id);
		tradeOfferRepository.save(tradeOffer);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {

		if (!tradeOfferRepository.existsById(id)) {
			return new ResponseEntity<>("Das Angebot existiert nicht", HttpStatus.NOT_FOUND);
		}
		tradeOfferRepository.deleteById(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@PutMapping("/{id}/accept")
	public ResponseEntity<?> acceptTradeOffer(@PathVariable Long id, @RequestParam(required = true) Long userid) {

		if (!tradeOfferRepository.existsById(id)) {
			return new ResponseEntity<>("Angebot nicht gefunden", HttpStatus.NOT_FOUND);
		} else if (!userRepository.existsById(userid)) {
			return new ResponseEntity<>("User nicht gefunden", HttpStatus.NOT_FOUND);
		}
		TradeOffer tradeOffer = tradeOfferRepository.findById(id).get();
		if (tradeOffer.isOfferActive() == false) {
			return new ResponseEntity<>("Das Angebot ist nicht mehr verfügbar", HttpStatus.BAD_REQUEST);
		}
		tradeOffer.setId(id);
		tradeOffer.setOfferActive(false);
		User user = userRepository.findById(userid).get();
		tradeOffer.setAcceptedByUser(user);
		tradeOfferRepository.save(tradeOffer);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PutMapping("/{id}/decline")
	public ResponseEntity<?> declineTradeOffer(@PathVariable Long id, @RequestParam(required = true) Long userid) {

		if (!tradeOfferRepository.existsById(id)) {
			return new ResponseEntity<>("Angebot nicht gefunden", HttpStatus.NOT_FOUND);
		} else if (!userRepository.existsById(userid)) {
			return new ResponseEntity<>("User nicht gefunden", HttpStatus.NOT_FOUND);
		}
		TradeOffer tradeOffer = tradeOfferRepository.findById(id).get();
		User user = userRepository.findById(userid).get();
		if (!tradeOffer.getAcceptedByUser().equals(user)) {
			return new ResponseEntity<>("Du kannst das Angebot nicht ablehnen", HttpStatus.BAD_REQUEST);
		}
		tradeOffer.setId(id);
		tradeOffer.setOfferActive(true);
		tradeOffer.setAcceptedByUser(null);
		tradeOfferRepository.save(tradeOffer);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
