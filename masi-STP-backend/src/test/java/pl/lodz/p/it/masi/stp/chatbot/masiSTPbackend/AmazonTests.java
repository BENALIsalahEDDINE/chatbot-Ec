package pl.lodz.p.it.masi.stp.chatbot.masiSTPbackend;

import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.lodz.p.it.masi.stp.chatbot.dtos.MessageDto;
import pl.lodz.p.it.masi.stp.chatbot.services.ConversationServiceImpl;

import java.io.*;
import java.net.URL;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AmazonTests {

	@Autowired
	private ConversationServiceImpl conversationService;
	
	@Test
	public void getBooksUrl() {
		MessageDto testMessage = new MessageDto();
		testMessage.setMessage("I want history books.");
		conversationService.initialize();
		MessageResponse watsonResponse = conversationService.getWatsonResponse(testMessage, testMessage, null, null);
		conversationService.getAmazonResponse(testMessage, watsonResponse, null);
		assertNotNull(testMessage.getUrl());
		assertNotEquals(testMessage.getUrl(),"");
		assertTrue(testMessage.getUrl().contains("https://"));
		assertTrue(testMessage.getUrl().contains("amazon.com"));
		try {
			String content = readContent(testMessage.getUrl());
			assertTrue(content.contains("<title>Amazon.com: History: Books"));
		} catch (IOException e) { e.printStackTrace(); }
	}
	
	private static String readContent(String address) throws IOException {
		URL url = new URL(address);
		BufferedReader in = new BufferedReader(
				new InputStreamReader(url.openStream()));
		StringBuilder sb = new StringBuilder();
		String inputLine;
		while ((inputLine = in.readLine()) != null)
			sb.append(inputLine);
		in.close();
		return sb.toString();
	}
	
	@Test
	public void checkSpecificBooksFromSubcategory() {
		MessageDto testMessage = new MessageDto();
		testMessage.setMessage("I want biography books about holocaust.");
		conversationService.initialize();
		testMessage.setMessage("historical");
		MessageResponse watsonResponse = conversationService.getWatsonResponse(testMessage, testMessage, null, null);
		conversationService.getAmazonResponse(testMessage, watsonResponse, null);
		assertNotNull(testMessage.getUrl());
		assertNotEquals(testMessage.getUrl(),"");
		assertTrue(testMessage.getUrl().contains("https://"));
		assertTrue(testMessage.getUrl().contains("amazon.com"));
		try {
			String content = readContent(testMessage.getUrl());
			String[] contentSearch = new String[] {"<span class=\"a-color-link a-text-bold\">Canadian", "Holocaust</span>"};
			for (String search : contentSearch) {
				assertTrue(content.contains(search));
			}
		} catch (IOException e) { e.printStackTrace(); }
	}
}
