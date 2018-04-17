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

import static org.junit.Assert.*;
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
import fi.nationallibrary.mauiservice.response.AnalyzerResponse;
import fi.nationallibrary.mauiservice.response.AnalyzerResponse.AnalyzerResult;

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
	
	@Autowired
	private AnalyzeController analyzeController;
	
	public static final MediaType APPLICATION_JSON_UTF8 =
			new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	@Test
	public void postExistingService() throws Exception {
		
		when(filters.getFilter("foo")).thenReturn(mock(MauiFilter.class));
		
		AnalyzerResponse response = new AnalyzerResponse();
		AnalyzerResult result = new AnalyzerResult();
		result.setLabel("Hello world: foo!");
		response.getResults().add(result);
		
		when(analyzer.analyze(any(), any())).thenReturn(response);
		
		Map<String, String> request = new HashMap<>();
		request.put("text", "Puolustusvoimien ortoilmakuvat ovat koko maan kattava oikaistu ilmakuva-aineisto. Ortoilmakuva vastaa geometrialtaan karttaa. Maastoresoluutio on 1 m. Puolustusvoimien ortoilmakuvia ei enää päivitetä. Ortoilmakuvia käytetään kartoituksessa, ympäristön suunnittelussa ja muutosten seurannassa. Mustavalkoinen ortoilmakuva sopii suunnitteluun tai tausta-aineistoksi erilaisille karttaesityksille.");
		
		String json = "{\"text\":\"world, hello\"}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/maui/foo/analyze")
				.contentType(MediaType.APPLICATION_JSON)
	            .content(json)
				.accept(MediaType.APPLICATION_JSON)).
		andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.results[0].label", is("Hello world: foo!")));
	}
	
	@Test
	public void getNonExistingService() throws Exception {
		when(filters.getFilter("foo")).thenReturn(null);
		mvc.perform(MockMvcRequestBuilders
				.post("/maui/foo/analyze")
				.accept(MediaType.APPLICATION_JSON)).
		andExpect(status().is4xxClientError());
	}
	
	@Test
	public void testResultOrdering_OutOfOrder() {
		AnalyzerResult tmp1 = new AnalyzerResult();
		tmp1.setScore(1.0);
		AnalyzerResult tmp2 = new AnalyzerResult();
		tmp2.setScore(2.0);
		
		AnalyzerResponse response = new AnalyzerResponse();
		response.getResults().add(tmp1);
		response.getResults().add(tmp2);
		
		analyzeController.sortResults(response);
		
		assertEquals(2, response.getResults().size());
		assertSame(tmp2, response.getResults().get(0));
		assertSame(tmp1, response.getResults().get(1));
	}
	
	@Test
	public void testResultOrdering_InOrder() {
		AnalyzerResult tmp1 = new AnalyzerResult();
		tmp1.setScore(1.0);
		AnalyzerResult tmp2 = new AnalyzerResult();
		tmp2.setScore(2.0);
		
		AnalyzerResponse response = new AnalyzerResponse();
		response.getResults().add(tmp2);
		response.getResults().add(tmp1);
		
		analyzeController.sortResults(response);
		
		assertEquals(2, response.getResults().size());
		assertSame(tmp2, response.getResults().get(0));
		assertSame(tmp1, response.getResults().get(1));
	}
	
	@Test
	public void testResultLimit_NoLimitingRequired() {
		AnalyzerResult tmp1 = new AnalyzerResult();
		tmp1.setScore(1.0);
		AnalyzerResult tmp2 = new AnalyzerResult();
		tmp2.setScore(2.0);
		
		AnalyzerResponse response = new AnalyzerResponse();
		response.getResults().add(tmp1);
		response.getResults().add(tmp2);
		
		analyzeController.limitResults(response, 3);
		
		assertEquals(2, response.getResults().size());
	}
	

	@Test
	public void testResultLimit_LimitingRequired() {
		AnalyzerResult tmp1 = new AnalyzerResult();
		tmp1.setScore(1.0);
		AnalyzerResult tmp2 = new AnalyzerResult();
		tmp2.setScore(2.0);
		
		AnalyzerResponse response = new AnalyzerResponse();
		response.getResults().add(tmp1);
		response.getResults().add(tmp2);
		
		analyzeController.limitResults(response, 1);
		
		assertEquals(1, response.getResults().size());
		assertSame(tmp1, response.getResults().get(0));
	}
	
}
