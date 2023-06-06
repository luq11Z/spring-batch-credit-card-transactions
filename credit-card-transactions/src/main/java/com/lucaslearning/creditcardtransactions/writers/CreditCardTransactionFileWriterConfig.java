package com.lucaslearning.creditcardtransactions.writers;

import java.io.IOException;
import java.io.Writer;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;

import org.springframework.batch.item.file.FlatFileFooterCallback;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.MultiResourceItemWriter;
import org.springframework.batch.item.file.ResourceSuffixCreator;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.builder.MultiResourceItemWriterBuilder;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import com.lucaslearning.creditcardtransactions.domain.CreditCardTransaction;
import com.lucaslearning.creditcardtransactions.domain.Transaction;

@Configuration
public class CreditCardTransactionFileWriterConfig {

	@Bean
	public MultiResourceItemWriter<CreditCardTransaction> creditCardTransactionFiles() {
		return new MultiResourceItemWriterBuilder<CreditCardTransaction>()
				.name("creditCardTransactionFiles")
				.resource(new FileSystemResource("files/fatura"))
				.itemCountLimitPerResource(1)
				.resourceSuffixCreator(suffixCreator())
				.delegate(creditCardTransactionFile())
				.build();	
	}

	/**
	 * "Single" writer to write a detailed "report" of the credit card transaction to a file.
	 * @return the credit card transaction file writer.
	 */
	private FlatFileItemWriter<CreditCardTransaction> creditCardTransactionFile() {
		return new FlatFileItemWriterBuilder<CreditCardTransaction>()
				.name("creditCardTransactionFile")
				.resource(new FileSystemResource("files/fatura.txt"))
				.lineAggregator(lineAggregator())
				.headerCallback(headerCallback())
				.footerCallback(footerCallback())
				.build();
				
	}

	/**
	 * Aggregate transactions.
	 * @return the details of the credit card transaction.
	 */
	private LineAggregator<CreditCardTransaction> lineAggregator() {
		return new LineAggregator<CreditCardTransaction>() {

			@Override
			public String aggregate(CreditCardTransaction item) {
				StringBuilder writer = new StringBuilder();
				writer.append(String.format("Name: %s\n", item.getClient().getName()));
				writer.append(String.format("Address: %s\n\n\n", item.getClient().getAddress()));
				writer.append(String.format("Card: %d details:\n", item.getCreditCard().getCreditCardNumber()));
				writer.append("---------------------------------------------------------------------------------------\n");
				writer.append("DATE DESCRIPTION VALUE\n");
				writer.append("---------------------------------------------------------------------------------------\n");
				
				for(Transaction transaction: item.getTransactions()) {
					writer.append(String.format("\n[%10s] %-80s - %s", 
							String.valueOf(transaction.getDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))),
							transaction.getDescription(), 
							NumberFormat.getCurrencyInstance().format(transaction.getValue())));
				}
				
				return writer.toString();
			}
			
		};
	}
	
	
	/**
	 * create a footer to each line in the file.
	 * @return the footer of the line.
	 */
	@Bean
	public FlatFileFooterCallback footerCallback() {
		return new TransactionSumFooterCallback();
	}

	/**
	 * create a header to each line in the file.
	 * @return the header of the line.
	 */
	private FlatFileHeaderCallback headerCallback() {
		return new FlatFileHeaderCallback() {
			
			@Override
			public void writeHeader(Writer writer) throws IOException {
				writer.append(String.format("%121s\n", "Card XPTO"));
				writer.append(String.format("%121s\n\n", "Street Something, 131"));
			}
		}; 
	}

	/**
	 * Create a suffix for the files.
	 * @return
	 */
	private ResourceSuffixCreator suffixCreator() {
		return new ResourceSuffixCreator() {
			
			@Override
			public String getSuffix(int index) {
				return index + ".txt";
			}
		};
	}
	
}
