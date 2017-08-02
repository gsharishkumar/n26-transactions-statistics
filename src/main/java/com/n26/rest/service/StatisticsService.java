package com.n26.rest.service;

import java.util.DoubleSummaryStatistics;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.n26.rest.model.Statistics;
import com.n26.rest.model.Transaction;
import com.n26.rest.utils.StatisticsUtility;

/**
 * Service which will do all data persistance, retrieval and manipulation work
 *
 * @author Harish Kumar Govindaswamy Saravanam
 * @version 1.0
 * @since 2017-08-01
 */
@Service
public class StatisticsService {

	private Map<Long, Transaction> transactionMap;
	private StatisticsUtility utility;

	public StatisticsService() {
		this.transactionMap = new ConcurrentHashMap<>();
		this.utility = new StatisticsUtility();
	}

	public Map<Long, Transaction> getTransactionMap() {
		return transactionMap;
	}

	public void setTransactionMap(Map<Long, Transaction> transactionMap) {
		this.transactionMap = transactionMap;
	}

	// ------add transaction to ConcurrentHashMap object-------------------------
	public void addTransaction(Transaction transaction) {
		this.getTransactionMap().put(transaction.getTimestamp(), transaction);
	}

	// ------generates statistics for the last 1 minute and return---------------
	public Statistics generateStatistics() {
		Statistics statistics = new Statistics();

		if (transactionMap.size() == 0) {
			return statistics;
		}

		DoubleSummaryStatistics statsSummary = transactionMap
				.values()
				.stream()
				.filter(transaction -> !utility
						.isTimestampOlderThan1Min(transaction.getTimestamp()))
				.mapToDouble(transaction -> transaction.getAmount())
				.summaryStatistics();

		if (statsSummary.getCount() == 0) {
			return statistics;
		}

		statistics.setCount(statsSummary.getCount());
		statistics.setMin(statsSummary.getMin());
		statistics.setMax(statsSummary.getMax());
		statistics.setSum(statsSummary.getSum());
		statistics.setAvg(statsSummary.getAverage());

		return statistics;

	}

	// ------clears ConcurrentHashMap object----------------------------------------
	public void clearTransactions() {
		this.getTransactionMap().clear();
	}

}
