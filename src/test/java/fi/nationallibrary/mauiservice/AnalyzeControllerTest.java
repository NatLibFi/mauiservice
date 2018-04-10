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

import com.entopix.maui.filters.MauiFilter;

import fi.nationallibrary.mauiservice.ini.MauiConfiguration;
import fi.nationallibrary.mauiservice.maui.MauiFilters;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AnalyzeControllerTest {

	@MockBean
	private MauiConfiguration configuration;
	
	@MockBean
	private MauiFilters filters;
	
	@MockBean
	private Analyzer analyzer;
	
	@Autowired
	private MockMvc mvc;
	

	@Test
	public void postExistingService() throws Exception {
		
		when(filters.getFilter("foo")).thenReturn(mock(MauiFilter.class));
		
		when(analyzer.analyze(any(), any())).thenReturn("Hello world: foo!");
		
		Map<String, String> request = new HashMap<>();
		request.put("text", "Puolustusvoimien ortoilmakuvat ovat koko maan kattava oikaistu ilmakuva-aineisto. Ortoilmakuva vastaa geometrialtaan karttaa. Maastoresoluutio on 1 m. Puolustusvoimien ortoilmakuvia ei enää päivitetä. Ortoilmakuvia käytetään kartoituksessa, ympäristön suunnittelussa ja muutosten seurannassa. Mustavalkoinen ortoilmakuva sopii suunnitteluun tai tausta-aineistoksi erilaisille karttaesityksille.");
		
		String json = "{\"text\":\"world, hello\"}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/maui/foo/analyze")
				.contentType(MediaType.APPLICATION_JSON)
	            .content(json)
				.accept(MediaType.APPLICATION_JSON)).
		andExpect(status().isOk())
				.andExpect(content().string(equalTo("Hello world: foo!")));
	}
	
	@Test
	public void getNonExistingService() throws Exception {
		
		/*
		Map<String, MauiFilterConfiguration> mockConfig = new HashMap<>();
		
		when(configuration.getConfigurations()).thenReturn(mockConfig);
		*/
		when(filters.getFilter("foo")).thenReturn(null);
		mvc.perform(MockMvcRequestBuilders
				.post("/maui/foo/analyze")
				.accept(MediaType.APPLICATION_JSON)).
		andExpect(status().is4xxClientError());
	}
}
