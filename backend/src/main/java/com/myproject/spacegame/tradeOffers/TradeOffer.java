package com.myproject.spacegame.tradeOffers;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.myproject.spacegame.user.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class TradeOffer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@Column(nullable = false)
	private double metal;
	@NotNull
	@Column(nullable = false)
	private double crystal;
	@NotNull
	@Column(nullable = false)
	private double hydrogen;
	
	private String comment;
	
	@ManyToOne
	@JoinColumn(name = "tradeOfferOfUser_id", nullable = false)
	@JsonManagedReference(value = "tradeOffersOfUser")
	private User tradeOfferOfUser;
	
	@ManyToOne
	@JoinColumn(name = "acceptedByUser_id")
	@JsonManagedReference(value = "tradeOfferAcceptedByUser")
	private User acceptedByUser;
	
	@NotNull
	@Column(nullable = false)
	private boolean offerActive;
	
	@NotNull
	@Column(nullable = false)
	private boolean search;
	
	@NotNull
	@Column(nullable = false)
	private boolean offer;
	
	@NotNull
	@Column(nullable = false)
	private String course;
	
}
