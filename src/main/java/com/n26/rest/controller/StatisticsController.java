package com.n26.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.n26.rest.model.Statistics;
import com.n26.rest.model.Transaction;
import com.n26.rest.service.StatisticsService;
import com.n26.rest.utils.StatisticsUtility;

/**
 * REST based controller for implementing transactions and
 * statistics APIs
 *
 * @author Harish Kumar Govindaswamy Saravanam
 * @version 1.0
 * @since 2017-08-01
 */
@RestController
public class StatisticsController {

	@Autowired
	StatisticsService service; //Service which will do all data persistance, retrieval and manipulation work

	StatisticsUtility utility; //Utility which will find if the transaction timestamp is older than 1 minute

	public StatisticsController() {
		this.utility = new StatisticsUtility();
	}

	// ------post/create transaction----------------------------------------------------------------------------------
	@RequestMapping(value = "/transactions", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> postTransaction(
			@RequestBody Transaction transaction) {
		if (utility.isTimestampOlderThan1Min(transaction.getTimestamp())) {
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT); // return empty body with 204 status
		} else {
			service.addTransaction(transaction);					// function call to add transaction to the data structure
			return new ResponseEntity<Void>(HttpStatus.CREATED);	// return empty body with 201 status
		}
	}

	// ------get/generate statistics based of the transactions of the last 60 seconds---------------------------------
	@RequestMapping(value = "/statistics", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Statistics> getStatistics() {
		return new ResponseEntity<Statistics>(service.generateStatistics(),
				HttpStatus.OK);										// generate statistics for the transactions of last 1 min and return as json
	}

}