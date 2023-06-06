package com.lucaslearning.creditcardtransactions.processors;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.lucaslearning.creditcardtransactions.domain.Client;
import com.lucaslearning.creditcardtransactions.domain.Cliente;
import com.lucaslearning.creditcardtransactions.domain.CreditCardTransaction;

@Component
public class ClientDataProcessor implements ItemProcessor<CreditCardTransaction, CreditCardTransaction> {

	private RestTemplate restTemplate = new RestTemplate();
	
	@Override
	public CreditCardTransaction process(CreditCardTransaction item) throws Exception {
		String uri = String.format("http://my-json-server.typicode.com/giuliana-bezerra/demo/profile/%d", item.getClient().getId());
		ResponseEntity<Cliente> response = restTemplate.getForEntity(uri, Cliente.class);
		
		if (response.getStatusCode() != HttpStatus.OK) {
			throw new ValidationException("Client not found!");
		}
		
		item.setClient(new Client(response.getBody()));
		
		return item;
	}

	
	
}
