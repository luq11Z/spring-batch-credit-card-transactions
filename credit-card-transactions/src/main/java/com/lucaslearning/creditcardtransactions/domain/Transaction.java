package com.lucaslearning.creditcardtransactions.domain;

import java.time.LocalDate;

public class Transaction {

	private Integer id;
	private CreditCard creditCard;
	private Double value;
	private LocalDate date;
	private String description;
	
	public Transaction() {
	
	}
	
	public Transaction(Integer id, CreditCard creditCard, Double value, LocalDate date, String description) {
		this.id = id;
		this.creditCard = creditCard;
		this.value = value;
		this.date = date;
		this.description = description;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public CreditCard getCreditCard() {
		return creditCard;
	}

	public void setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
