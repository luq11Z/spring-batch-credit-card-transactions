package com.lucaslearning.creditcardtransactions.readers;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;

import com.lucaslearning.creditcardtransactions.domain.CreditCardTransaction;
import com.lucaslearning.creditcardtransactions.domain.Transaction;

public class CreditCardTransactionReader implements ItemStreamReader<CreditCardTransaction> {
	
	private ItemStreamReader<Transaction> delegate;
	private Transaction atualTransaction;
	
	public CreditCardTransactionReader(ItemStreamReader<Transaction> delegate) {
		this.delegate = delegate;
	}
	
	/**
	 * Customized reading of transactions by credit card. 
	 */
	@Override
	public CreditCardTransaction read() throws Exception {
		if (atualTransaction == null) {
			atualTransaction = delegate.read();
		}
		
		CreditCardTransaction creditCardTransaction = null;
		Transaction transaction = atualTransaction;
		atualTransaction = null;
		
		// associate the transactions
		if (transaction != null) {
			creditCardTransaction = new CreditCardTransaction();
			creditCardTransaction.setCreditCard(transaction.getCreditCard());
			creditCardTransaction.setClient(transaction.getCreditCard().getClient());
			creditCardTransaction.getTransactions().add(transaction);
			
			while (isTransactionRelated(transaction)) {
				creditCardTransaction.getTransactions().add(atualTransaction);
			}
		}
		
		return creditCardTransaction;
	}

	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException {
		delegate.open(executionContext);
	}
	
	@Override
	public void update(ExecutionContext executionContext) throws ItemStreamException {
		delegate.update(executionContext);
	}
	
	@Override
	public void close() throws ItemStreamException {
		delegate.close();
	}
	
	/**
	 * Verify if the transaction belongs to the card.
	 * @param transaction
	 * @return
	 * @throws Exception
	 */
	private boolean isTransactionRelated(Transaction transaction) throws Exception {
		return peek() != null && transaction.getCreditCard().getCreditCardNumber().equals(atualTransaction.getCreditCard().getCreditCardNumber());
	}

	/**
	 * Peek the transaction ahead (next transaction) to verify if
	 * the transaction belongs to the card. 
	 * @return 
	 * @throws Exception
	 */
	private Transaction peek() throws Exception {
		atualTransaction = delegate.read();
		return atualTransaction;
	}
	
}
