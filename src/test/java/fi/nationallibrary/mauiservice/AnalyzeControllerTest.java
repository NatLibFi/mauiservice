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

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import fi.nationallibrary.mauiservice.ini.MauiConfiguration;
import fi.nationallibrary.mauiservice.ini.MauiModelConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AnalyzeControllerTest {

	@MockBean
	private MauiConfiguration configuration;
	
	@Autowired
	private MockMvc mvc;

	@Test
	public void getExistingService() throws Exception {
		
		Map<String, MauiModelConfiguration> mockConfig = new HashMap<>();
		mockConfig.put("foo", new MauiModelConfiguration());
		
		when(configuration.getConfigurations()).thenReturn(mockConfig);
		
		mvc.perform(MockMvcRequestBuilders
				.get("/maui/foo/analyze")
				.accept(MediaType.APPLICATION_JSON)).
		andExpect(status().isOk())
				.andExpect(content().string(equalTo("Hello world: foo")));
	}
	
	@Test
	public void getNonExistingService() throws Exception {
		
		Map<String, MauiModelConfiguration> mockConfig = new HashMap<>();
		
		when(configuration.getConfigurations()).thenReturn(mockConfig);
		
		mvc.perform(MockMvcRequestBuilders
				.get("/maui/foo/analyze")
				.accept(MediaType.APPLICATION_JSON)).
		andExpect(status().is4xxClientError());
	}
}
