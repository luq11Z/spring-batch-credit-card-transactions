package com.lucaslearning.creditcardtransactions.domain;

import java.util.ArrayList;
import java.util.List;

public class CreditCardTransaction {

	private Client client;
	private CreditCard creditCard;
	private List<Transaction> transactions = new ArrayList<>();
	
	public CreditCardTransaction() {
		
	}

	public CreditCardTransaction(Client client, CreditCard creditCard, List<Transaction> transactions) {
		this.client = client;
		this.creditCard = creditCard;
		this.transactions = transactions;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public CreditCard getCreditCard() {
		return creditCard;
	}

	public void setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}
	
	public Double getTotal() {
		return transactions.stream()
				.mapToDouble(Transaction::getValue)
				.reduce(0.0, Double::sum);
	}
	
}
