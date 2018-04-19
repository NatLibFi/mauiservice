package fi.nationallibrary.mauiservice.maui;

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

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import com.entopix.maui.filters.MauiFilter;

import fi.nationallibrary.mauiservice.ini.MauiFilterConfiguration;

public class SharedVocabularyMauiFilterFactoryTest {

	SharedVocabularyMauiFilterFactoryImpl factory;
	MauiFilterConfiguration config;
	
	@Before
	public void setUp() throws Exception {
		factory = spy(SharedVocabularyMauiFilterFactoryImpl.class);

		config = new MauiFilterConfiguration();
		config.setConfigurationDirectory(new File("src/test/resources/test-config/"));
		config.setLanguage("fi");
		config.setModel("yso-archaeology.model");
		config.setStemmer("FinnishStemmer");
		config.setStopwords("StopwordsFinnish");
		config.setVocab("yso-archaeology.rdf");
		config.setVocabFormat("skos");
	}

	@Test
	public void testBasics() throws MauiFilterInitializationException {
		MauiFilter f = factory.createFilter(config);
		assertNotNull(f);
		
		verify(factory, times(1)).readModel(same(config), any());
		verify(factory, times(1)).loadStemmer(same(config));
		verify(factory, times(1)).createVocabulary(same(config), any(), any());
		verify(factory, times(1)).cacheVocabulary(any(), any(), any(), any(), any());
	}
	
	@Test
	public void testCachedVocabulary() throws MauiFilterInitializationException {
		MauiFilter f1 = factory.createFilter(config);
		assertNotNull(f1);
		
		MauiFilter f2 = factory.createFilter(config);
		assertNotNull(f2);
		assertNotSame(f1, f2);
		
		verify(factory, times(2)).readModel(same(config), any());
		verify(factory, times(2)).loadStemmer(same(config));
		
		// .. but these were called only once!
		verify(factory, times(1)).createVocabulary(same(config), any(), any());
		verify(factory, times(1)).cacheVocabulary(any(), any(), any(), any(), any());
	}

	
	@Test
	public void testNotCachedVocabulary_different_language() throws MauiFilterInitializationException {
		MauiFilter f1 = factory.createFilter(config);
		assertNotNull(f1);
		
		config.setLanguage("en");
		MauiFilter f2 = factory.createFilter(config);
		assertNotNull(f2);
		assertNotSame(f1, f2);
		
		verify(factory, times(2)).readModel(same(config), any());
		verify(factory, times(2)).loadStemmer(same(config));
		verify(factory, times(2)).createVocabulary(same(config), any(), any());
		verify(factory, times(2)).cacheVocabulary(any(), any(), any(), any(), any());
	}
	
	@Test
	public void testNotCachedVocabulary_different_stopwords() throws MauiFilterInitializationException {
		MauiFilter f1 = factory.createFilter(config);
		assertNotNull(f1);
		
		config.setStopwords("StopwordsEnglish");
		MauiFilter f2 = factory.createFilter(config);
		assertNotNull(f2);
		assertNotSame(f1, f2);
		
		verify(factory, times(2)).readModel(same(config), any());
		verify(factory, times(2)).loadStemmer(same(config));
		verify(factory, times(2)).createVocabulary(same(config), any(), any());
		verify(factory, times(2)).cacheVocabulary(any(), any(), any(), any(), any());
	}

	@Test
	public void testNotCachedVocabulary_different_stemmer() throws MauiFilterInitializationException {
		MauiFilter f1 = factory.createFilter(config);
		assertNotNull(f1);
		
		config.setStemmer("PorterStemmer");
		MauiFilter f2 = factory.createFilter(config);
		assertNotNull(f2);
		assertNotSame(f1, f2);
		
		verify(factory, times(2)).readModel(same(config), any());
		verify(factory, times(2)).loadStemmer(same(config));
		verify(factory, times(2)).createVocabulary(same(config), any(), any());
		verify(factory, times(2)).cacheVocabulary(any(), any(), any(), any(), any());
	}

}
