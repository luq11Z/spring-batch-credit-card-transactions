package com.lucaslearning.creditcardtransactions.writers;

import java.io.IOException;
import java.io.Writer;
import java.text.NumberFormat;

import org.springframework.batch.core.annotation.AfterChunk;
import org.springframework.batch.core.annotation.BeforeWrite;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.file.FlatFileFooterCallback;

import com.lucaslearning.creditcardtransactions.domain.CreditCardTransaction;

public class TransactionSumFooterCallback implements FlatFileFooterCallback {

	private Double total = 0.0;

	@Override
	public void writeFooter(Writer writer) throws IOException {
		writer.write(String.format("\n%121s", "Total: " + NumberFormat.getCurrencyInstance().format(total)));
	}
	
	@BeforeWrite
	public void beforeWrite(Chunk<? extends CreditCardTransaction> items) {
		for (CreditCardTransaction creditCardTransaction : items.getItems()) {
			total += creditCardTransaction.getTotal();
		}
	}
	
	@AfterChunk
	public void afterChunk(ChunkContext chunkContext) {
		total = 0.0;
	}
	
}
