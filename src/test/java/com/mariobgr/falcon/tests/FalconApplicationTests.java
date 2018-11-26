package com.mariobgr.falcon.tests;

import com.mariobgr.falcon.FalconApplication;
import com.mariobgr.falcon.controllers.ApiController;
import com.mariobgr.falcon.models.MessageModel;
import com.mariobgr.falcon.services.MessageSenderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;

import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;
import static org.assertj.core.api.BDDAssertions.then;
import static org.hamcrest.CoreMatchers.containsString;

@EnableAutoConfiguration
@RunWith(SpringRunner.class)
@SuppressWarnings("RedundantThrows")
@TestPropertySource(value={"classpath:tests.properties"})
@SpringBootTest(classes = {FalconApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FalconApplicationTests {

	private MessageModel messageModel;

	@MockBean
	private MessageSenderService service;

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Test
	public void messageModelGenerator() {

		long unixTimestamp = System.currentTimeMillis() / 1000L;

		MessageModel messageModel = this.generateTestMessage();

		assertEquals("test message", messageModel.getMessage());
		assertNotEquals(unixTimestamp, messageModel.getRandom() + ":" + messageModel.getTimestamp());

	}

	@Test
	public void checkMessagePost() throws Exception {


		MessageModel messageModel = this.generateTestMessage();

		ResponseEntity<String> entity = testRestTemplate.postForEntity(
				"/api/sendRequest", messageModel , String.class);

		then(entity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
	}

	@Test
	public void checkMessagePostLogs() throws Exception {


		Logger testLogger = (Logger) LoggerFactory.getLogger(ApiController.class);

		ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
		listAppender.start();

		testLogger.addAppender(listAppender);

		MessageModel messageModel = this.generateTestMessage();

		testRestTemplate.postForEntity(
				"/api/sendRequest", messageModel, String.class);

		List<ILoggingEvent> logsList = listAppender.list;

		assertThat(logsList.get(0).getMessage(), containsString("sent to API"));
		assertThat(logsList.get(1).getMessage(), containsString("saved to RabbitMQ"));
		assertThat(logsList.get(2).getMessage(), containsString("saved to Redis"));

		listAppender.stop();

	}

	@Test
	public void checkAllMessagesStatus() throws Exception {

		ResponseEntity<String> entity = this.testRestTemplate.getForEntity("/api/getAll", String.class);

		then(entity.getStatusCode()).isEqualTo(HttpStatus.OK);

	}

	@Test
	public void checkAllMessagesStatusWithPages() throws Exception {

		String url;
		Random rand = new Random();

		for (int i=0; i<10; i++) {

			url = String.format("/api/getAll?page=%d", rand.nextInt(1000));

			ResponseEntity<String> entity = this.testRestTemplate.getForEntity(
					url, String.class);

			then(entity.getStatusCode()).isEqualTo(HttpStatus.OK);

		}

	}

	@Test
	public void checkAllMessagesStatusWithString() throws Exception {

		String url = String.format("/api/getAll?page=%s", "falcon");

		ResponseEntity<String> entity = testRestTemplate.getForEntity(
				url, String.class);

		then(entity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

	}

	@Test
	public void checkAllMessagesBody() throws Exception {

		ResponseEntity<List<MessageModel>> entity = testRestTemplate.exchange(
				"/api/getAll", HttpMethod.GET, null, new ParameterizedTypeReference<List<MessageModel>>(){});

		List<MessageModel> messages = entity.getBody();

		assert(messages.size() <= 20);

	}

	private MessageModel generateTestMessage() {

		Random rand = new Random();
		long unixTimestamp = System.currentTimeMillis() / 1000L;

		messageModel = new MessageModel("test message", rand.nextInt(10000), Long.toString(unixTimestamp), -1);

		return messageModel;

	}

}
