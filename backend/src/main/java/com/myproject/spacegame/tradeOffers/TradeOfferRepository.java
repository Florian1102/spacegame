package com.myproject.spacegame.tradeOffers;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeOfferRepository extends JpaRepository<TradeOffer, Long>  {

	List<TradeOffer> findAllByTradeOfferOfUserId(Long userId);

}
