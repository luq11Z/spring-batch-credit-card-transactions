package com.lucaslearning.creditcardtransactions.domain;

public class CreditCard {
	
	private Integer creditCardNumber;
	private Client client;
	
	public CreditCard() {
		
	}

	public CreditCard(Integer creditCardNumber, Client client) {
		this.creditCardNumber = creditCardNumber;
		this.client = client;
	}

	public Integer getCreditCardNumber() {
		return creditCardNumber;
	}

	public void setCreditCardNumber(Integer creditCardNumber) {
		this.creditCardNumber = creditCardNumber;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}
	
}
