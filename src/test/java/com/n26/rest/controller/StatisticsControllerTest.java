package com.n26.rest.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.n26.rest.Application;
import com.n26.rest.model.Statistics;
import com.n26.rest.model.Transaction;
import com.n26.rest.service.StatisticsService;

/**
 * Junit Test class for testing statistics controller functionalities
 *
 * @author Harish Kumar Govindaswamy Saravanam
 * @version 1.0
 * @since 2017-08-01
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StatisticsControllerTest {

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext wac;

	@Autowired
	private StatisticsService service;

	// ------clear the transaction data from the hash map-----------------------------------------------------
	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
		service.clearTransactions();
	}

	// ------test to verify if the transaction is created properly in case of success--------------------------
	@Test
	public void verify1CreatedStatusPostTransaction() throws Exception {
		Transaction transaction = new Transaction(Double.valueOf(4.3),
				System.currentTimeMillis());

		mockMvc.perform(
				post("/transactions").contentType(MediaType.APPLICATION_JSON)
						.content(jsonObjectToString(transaction)))
				.andExpect(status().is(201)).andExpect(content().string(""));
	}

	// ------test to verify if the transaction is older than 60 seconds-----------------------------------------
	@Test
	public void verify2NoContentStatusPostTransaction() throws Exception {
		Transaction transaction = new Transaction(Double.valueOf(4.3),
				System.currentTimeMillis() - (61 * 1000));

		mockMvc.perform(
				post("/transactions").contentType(MediaType.APPLICATION_JSON)
						.content(jsonObjectToString(transaction)))
				.andExpect(status().is(204)).andExpect(content().string(""));
	}

	// ------test to verify GetStatistics functionality in case of no transaction present in hash map------------
	@Test
	public void verify3GetStatisticsWithoutTransactionsData() throws Exception {

		Thread.sleep(61 * 1000);

		mockMvc.perform(
				get("/statistics").contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.count").value(0))
				.andExpect(jsonPath("$.max").value(0.0))
				.andExpect(jsonPath("$.min").value(0.0))
				.andExpect(jsonPath("$.sum").value(0.0))
				.andExpect(jsonPath("$.avg").value(0.0));
	}

	// ------test to verify GetStatistics functionality after adding transactions using Service class------------
	@Test
	public void verify4GetStatisticsUsingServiceAddTransaction() throws Exception {

		long currentTime = System.currentTimeMillis();
		// add transactions are added within the past 60 seconds
		service.addTransaction(new Transaction(Double.valueOf(10.3), currentTime));
		service.addTransaction(new Transaction(Double.valueOf(5.5), currentTime+1));
		service.addTransaction(new Transaction(Double.valueOf(8.0), currentTime+2));
		service.addTransaction(new Transaction(Double.valueOf(25.7), currentTime+3));
		service.addTransaction(new Transaction(Double.valueOf(12.6), currentTime+4));

		MvcResult result = mockMvc.perform(
				get("/statistics").contentType(MediaType.APPLICATION_JSON)).andReturn();
		
		Statistics resultStatistics = new ObjectMapper().readValue(result.getResponse().getContentAsString(), Statistics.class);
		
		Assert.assertEquals(5, resultStatistics.getCount());
        Assert.assertEquals(25.7, resultStatistics.getMax(), 0.001);
        Assert.assertEquals(5.5, resultStatistics.getMin(), 0.001);
        Assert.assertEquals(62.1, resultStatistics.getSum(), 0.001);
        Assert.assertEquals(12.42, resultStatistics.getAvg(), 0.001);
	}

	// ------test to verify GetStatistics functionality after adding transactions using transactions APIs class----
	@Test
	public void verify5GetStatisticsUsingPostTransactionAPI() throws Exception {

		Thread.sleep(61 * 1000);
		
		long currentTime = System.currentTimeMillis();
		// transaction input which is more than two min older
		mockMvc.perform(
				post("/transactions").contentType(MediaType.APPLICATION_JSON)
						.content(jsonObjectToString(new Transaction(Double.valueOf(5.3), currentTime- (61 * 2000)))));
		// transaction input which is within the past 60 seconds
		mockMvc.perform(
				post("/transactions").contentType(MediaType.APPLICATION_JSON)
						.content(jsonObjectToString(new Transaction(Double.valueOf(16.3), currentTime-20))));
		// transaction input which is within the past 60 seconds
		mockMvc.perform(
				post("/transactions").contentType(MediaType.APPLICATION_JSON)
						.content(jsonObjectToString(new Transaction(Double.valueOf(12.0), currentTime))));

		MvcResult result = mockMvc.perform(
				get("/statistics").contentType(MediaType.APPLICATION_JSON)).andReturn();
		
		Statistics resultStatistics = new ObjectMapper().readValue(result.getResponse().getContentAsString(), Statistics.class);
		
		Assert.assertEquals(2, resultStatistics.getCount());
        Assert.assertEquals(16.3, resultStatistics.getMax(), 0.001);
        Assert.assertEquals(12.0, resultStatistics.getMin(), 0.001);
        Assert.assertEquals(28.3, resultStatistics.getSum(), 0.001);
        Assert.assertEquals(14.15, resultStatistics.getAvg(), 0.001);
	}

	// ------function to convert json object to string-------------------------------------------------------------
	public String jsonObjectToString(Transaction transaction)
			throws JsonProcessingException {
		return new ObjectMapper().writeValueAsString(transaction);
	}

}
