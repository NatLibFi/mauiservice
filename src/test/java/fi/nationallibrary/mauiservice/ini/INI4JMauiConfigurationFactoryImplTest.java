package fi.nationallibrary.mauiservice.ini;

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
import java.io.FileReader;
import java.io.Reader;

import org.junit.Test;

public class INI4JMauiConfigurationFactoryImplTest {

	@Test
	public void test() throws Exception {
		File f = new File("src/test/resources/example-configuration.ini");
		Reader reader = new FileReader(f);
		INI4JMauiConfigurationFactoryImpl factory = new INI4JMauiConfigurationFactoryImpl();
		
		
		MauiConfiguration config = factory.readConfig(f.getParentFile(), reader);
		
		assertEquals(2, config.getConfigurations().size());
		
		assertConfig(config.getConfigurations().get("foo"),    "fi", "foo", "bar",  "gah",  "goo", "zah");
		assertConfig(config.getConfigurations().get("foo-sv"), "sv", "lol", "zonk", "barg", "42",  "trol");
		
		File expectedConfigPath = new File("src/test/resources/");
		assertEquals(expectedConfigPath, config.getConfigurations().get("foo").getConfigurationDirectory());
	}

	private void assertConfig(MauiFilterConfiguration m, String language, String model,
			String stemmer, String stopwords, String vocab, String vocabFormat) {
		assertNotNull(m);
		
		assertEquals(language,    m.getLanguage());
		assertEquals(model,       m.getModel());
		assertEquals(stemmer,     m.getStemmer());
		assertEquals(stopwords,   m.getStopwords());
		assertEquals(vocab,       m.getVocab());
		assertEquals(vocabFormat, m.getVocabFormat());
	}

}
