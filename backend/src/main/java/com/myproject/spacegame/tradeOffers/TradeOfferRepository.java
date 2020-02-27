package com.myproject.spacegame.tradeOffers;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeOfferRepository extends JpaRepository<TradeOffer, Long>  {

	List<TradeOffer> findAllByTradeOfferOfUserIdOrAcceptedByUserId(Long userId, Long userIdTwo);
	List<TradeOffer> findAllByOfferActiveTrue();
	List<TradeOffer> findAllByOfferActiveFalse();
}
