package fi.nationallibrary.mauiservice.maui;

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
