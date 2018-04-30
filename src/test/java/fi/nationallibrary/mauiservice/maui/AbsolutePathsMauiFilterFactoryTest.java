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

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import com.entopix.maui.filters.MauiFilter;

import fi.nationallibrary.mauiservice.ini.MauiFilterConfiguration;

public class AbsolutePathsMauiFilterFactoryTest {

	MauiFilterConfiguration config;
	
	@Before
	public void setUp() throws Exception {
		File path = new File("src/test/resources/test-config/");
		
		File model = new File(path, "yso-archaeology.model");
		File vocab = new File(path, "yso-archaeology.rdf");
		
		config = new MauiFilterConfiguration();
		config.setConfigurationDirectory(path);
		config.setLanguage("fi");
		config.setModel(model.getAbsolutePath());
		config.setStemmer("FinnishStemmer");
		config.setStopwords("StopwordsFinnish");
		config.setVocab(vocab.getAbsolutePath());
		config.setVocabFormat("skos");
	}

	@Test
	public void testMauiFilterFactoryImpl() throws MauiFilterInitializationException {
		MauiFilterFactory factory = new MauiFilterFactoryImpl();
		
		MauiFilter f = factory.createFilter(config);
		assertNotNull(f);
	}

	@Test
	public void testSharedVobabularyMauiFilterFactoryImpl() throws MauiFilterInitializationException {
		MauiFilterFactory factory = new SharedVocabularyMauiFilterFactoryImpl();
		
		MauiFilter f = factory.createFilter(config);
		assertNotNull(f);
	}

}
