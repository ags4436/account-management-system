package com.barclays.capstone.main.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;


@Service
public class BankRepository {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	Logger logger=LoggerFactory.getLogger(BankRepository.class);
	
	
}
