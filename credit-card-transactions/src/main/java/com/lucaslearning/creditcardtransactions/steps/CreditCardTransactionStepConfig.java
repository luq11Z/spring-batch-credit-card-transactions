package com.lucaslearning.creditcardtransactions.steps;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.lucaslearning.creditcardtransactions.domain.CreditCardTransaction;
import com.lucaslearning.creditcardtransactions.domain.Transaction;
import com.lucaslearning.creditcardtransactions.readers.CreditCardTransactionReader;
import com.lucaslearning.creditcardtransactions.writers.TransactionSumFooterCallback;

@Configuration
public class CreditCardTransactionStepConfig {

	@Bean
	public Step creditCardTransactionStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
			ItemStreamReader<Transaction> transactionsReader,
			ItemProcessor<CreditCardTransaction, CreditCardTransaction> creditCardTransactionProcessor,
			ItemWriter<CreditCardTransaction> creditCardTransactionWriter, TransactionSumFooterCallback listener) {
		return new StepBuilder("creditCardTransactionStep", jobRepository)
				.<CreditCardTransaction, CreditCardTransaction>chunk(1, transactionManager)
				.reader(new CreditCardTransactionReader(transactionsReader))
				.processor(creditCardTransactionProcessor)
				.writer(creditCardTransactionWriter)
				.listener(listener)
				.build();
	}

}
