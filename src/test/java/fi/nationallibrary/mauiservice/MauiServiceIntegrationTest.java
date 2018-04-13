package fi.nationallibrary.mauiservice;

/*-
 * #%L
 * fi.nationallibrary:mauiservice
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2018 National Library Finland
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MauiServiceIntegrationTest {

	@Configuration
	@Import(ApplicationConfiguration.class)
	@ComponentScan(basePackages = "fi.nationallibrary.mauiservice")
	public static class IntegrationTestConfiguration {
		@Bean
		@Qualifier("configFileName")
		public String configFileName() {
			return "src/test/resources/test-config/test.ini";
		}
	}

	@Autowired
	private MockMvc mvc;
	
	public static final MediaType APPLICATION_JSON_UTF8 =
			new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	@Test
	public void postToTestService() throws Exception {
		
		Map<String, String> request = new HashMap<>();
		request.put("text", "Arkeologiaa sanotaan joskus myös muinaistutkimukseksi tai muinaistieteeksi. Se on humanistinen tiede tai oikeammin joukko tieteitä, jotka tutkivat ihmisen menneisyyttä. Tutkimusta tehdään analysoimalla muinaisjäännöksiä eli niitä jälkiä, joita ihmisten toiminta on jättänyt maaperään tai vesistöjen pohjaan");
		
		ObjectMapper mapper = new ObjectMapper();
		
		String json = mapper.writeValueAsString(request);
		
		mvc.perform(MockMvcRequestBuilders
				.post("/maui/test/analyze")
				.contentType(MediaType.APPLICATION_JSON)
	            .content(json)
				.accept(MediaType.APPLICATION_JSON)).
		andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.results[0].uri", is("http://www.yso.fi/onto/yso/p5340")))
				.andExpect(jsonPath("$.results[0].label", is("muinaisjäännökset")));
	}
	
}
