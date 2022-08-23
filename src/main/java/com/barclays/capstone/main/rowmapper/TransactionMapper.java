package com.barclays.capstone.main.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.barclays.capstone.main.model.Transaction;



public class TransactionMapper implements RowMapper<Transaction> {

	@Override
	public Transaction mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		Transaction transaction= new Transaction();
		int transactionId=rs.getInt("trans_id");
		transaction.setTransactionReferenceNumber(Integer.toString(transactionId));
		transaction.setFromAccountNumber(rs.getString("trans_from"));
		transaction.setToAccountNumber(rs.getString("trans_to"));
		transaction.setAmount(rs.getFloat("trans_amount"));
		transaction.setType(rs.getString("transaction_type"));
		
		return null;
	}
	
	

}