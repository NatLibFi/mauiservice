package fi.nationallibrary.mauiservice.ini;

import static org.junit.Assert.*;

import java.io.FileReader;
import java.io.Reader;

import org.junit.Test;

public class INI4JMauiConfigurationFactoryImplTest {

	@Test
	public void test() throws Exception {
		Reader reader = new FileReader("src/test/resources/example-configuration.ini");
		INI4JMauiConfigurationFactoryImpl factory = new INI4JMauiConfigurationFactoryImpl();
		
		
		MauiConfiguration config = factory.readConfig(reader);
		
		assertEquals(2, config.getConfigurations().size());
		
		assertConfig(config.getConfigurations().get("foo"),    "fi", "foo", "bar",  "gah",  "goo");
		assertConfig(config.getConfigurations().get("foo-sv"), "sv", "lol", "zonk", "barg", "42");
		
	}

	private void assertConfig(MauiModelConfiguration m, String language, String model,
			String stemmer, String stopwords, String vocab) {
		assertNotNull(m);
		
		assertEquals(language,  m.getLanguage());
		assertEquals(model,     m.getModel());
		assertEquals(stemmer,   m.getStemmer());
		assertEquals(stopwords, m.getStopwords());
		assertEquals(vocab,     m.getVocab());
	}

}
