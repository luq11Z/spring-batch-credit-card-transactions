package com.lucaslearning.creditcardtransactions.readers;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import com.lucaslearning.creditcardtransactions.domain.Client;
import com.lucaslearning.creditcardtransactions.domain.CreditCard;
import com.lucaslearning.creditcardtransactions.domain.Transaction;

@Configuration
public class TransactionsReaderConfig {

	@Bean
	public JdbcCursorItemReader<Transaction> transactionsReader(@Qualifier("appDataSource") DataSource dataSource) {
		return new JdbcCursorItemReaderBuilder<Transaction>()
				.name("transactionsReader")
				.dataSource(dataSource)
				.sql("SELECT * FROM transaction JOIN credit_card USING (credit_card_number) ORDER BY credit_card_number")
				.rowMapper(rowMapperTransaction()) // mapping because Transaction is a "complex" object
				.build();
	}

	/**
	 * Map data read to the Transaction object.
	 * @return the rows mapped to an Transaction object.
	 */
	private RowMapper<Transaction> rowMapperTransaction() {
		return new RowMapper<Transaction>() {
			public Transaction mapRow(ResultSet rs, int rowNum) throws SQLException {
				CreditCard creditCard = new CreditCard();
				creditCard.setCreditCardNumber(rs.getInt("credit_card_number"));
				
				Client client = new Client();
				client.setId(rs.getInt("client"));
				
				creditCard.setClient(client);
				
				Transaction transaction = new Transaction();
				transaction.setId(rs.getInt("id"));
				transaction.setCreditCard(creditCard);
				transaction.setDate(rs.getDate("date").toLocalDate());
				transaction.setValue(rs.getDouble("value"));
				transaction.setDescription(rs.getString("description"));
				
				return transaction;
			}
		};
	}
	
}
