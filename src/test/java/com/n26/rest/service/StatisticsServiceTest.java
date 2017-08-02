package com.n26.rest.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.WebApplicationContext;

import com.n26.rest.Application;
import com.n26.rest.model.Statistics;
import com.n26.rest.model.Transaction;

/**
 * Junit Test class for testing statistics service functionalities
 *
 * @author Harish Kumar Govindaswamy Saravanam
 * @version 1.0
 * @since 2017-08-01
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StatisticsServiceTest {

	@Autowired
	private WebApplicationContext wac;

	@Autowired
	private StatisticsService service;

	@Before
	public void setup() {
		service.clearTransactions();
	}
	
	// ------test to verify Generate Statistics functionality with empty transactions------------
	@Test
    public void verify1EmptyTransactionsGenerateStatistics(){
        Statistics statistics = new Statistics();
        
        Statistics resultStatistics = service.generateStatistics();
        
        Assert.assertEquals(statistics.getCount(), resultStatistics.getCount());
        Assert.assertEquals(statistics.getMax(), resultStatistics.getMax(), 0.001);
        Assert.assertEquals(statistics.getMin(), resultStatistics.getMin(), 0.001);
        Assert.assertEquals(statistics.getSum(), resultStatistics.getSum(), 0.001);
        Assert.assertEquals(statistics.getAvg(), resultStatistics.getAvg(), 0.001);
    }

	// ------test to verify add transaction into hash map functionality--------------------------
	@Test
	public void verify2AddTransaction() throws Exception {
		Transaction transaction = new Transaction(Double.valueOf(4.3),
				System.currentTimeMillis());
		
		service.addTransaction(transaction);

		Assert.assertEquals(transaction.getAmount(), service.getTransactionMap().get(transaction.getTimestamp()).getAmount(), 0.001);
		Assert.assertEquals(transaction.getTimestamp(), service.getTransactionMap().get(transaction.getTimestamp()).getTimestamp());
	}
	
	// ------test to verify Generate Statistics functionality after adding transactions------------
	@Test
    public void verify3GenerateStatistics(){
		service.clearTransactions();
		
		long currentTime = System.currentTimeMillis();
		
		// add transactions are added within the past 60 seconds
		service.addTransaction(new Transaction(Double.valueOf(10.3), currentTime));
		service.addTransaction(new Transaction(Double.valueOf(5.5), currentTime+1));
		service.addTransaction(new Transaction(Double.valueOf(8.0), currentTime+2));
		service.addTransaction(new Transaction(Double.valueOf(25.7), currentTime-30));
		service.addTransaction(new Transaction(Double.valueOf(12.6), currentTime-400));
		
		// transaction inputs which is more than two min older
		service.addTransaction(new Transaction(Double.valueOf(14.1), currentTime - (61 * 3000)));
		service.addTransaction(new Transaction(Double.valueOf(50.3), currentTime - (61 * 2000)));

		Statistics statistics = new Statistics();
		statistics.setCount(5L);
		statistics.setMin(5.5D);
		statistics.setMax(25.7D);
		statistics.setSum(62.1D);
		statistics.setAvg(12.42D);
		        
		Statistics resultStatistics = service.generateStatistics();
		
		Assert.assertEquals(statistics.getCount(), resultStatistics.getCount());
        Assert.assertEquals(statistics.getMax(), resultStatistics.getMax(), 0.001);
        Assert.assertEquals(statistics.getMin(), resultStatistics.getMin(), 0.001);
        Assert.assertEquals(statistics.getSum(), resultStatistics.getSum(), 0.001);
        Assert.assertEquals(statistics.getAvg(), resultStatistics.getAvg(), 0.001);
    }
}
