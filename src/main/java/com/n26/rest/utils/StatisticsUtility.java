package com.n26.rest.utils;

/**
 * Utility having a functionwhich will find if the transaction timestamp
 * is older than 1 minute 
 *
 * @author Harish Kumar Govindaswamy Saravanam
 * @version 1.0
 * @since 2017-08-01
 */
public class StatisticsUtility {
	
	private static final Long ONE_MINUTE = 60000L;
	
	public boolean isTimestampOlderThan1Min(long timestamp){
		return System.currentTimeMillis() - timestamp > ONE_MINUTE;
	}

}
