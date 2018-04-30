package fi.nationallibrary.mauiservice.maui;

import java.io.File;

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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.entopix.maui.filters.MauiFilter;
import com.entopix.maui.stemmers.Stemmer;
import com.entopix.maui.stopwords.Stopwords;
import com.entopix.maui.vocab.Vocabulary;

import fi.nationallibrary.mauiservice.ini.MauiFilterConfiguration;

/**
 * This factory caches vocabularies to avoid loading the same vocabulary multiple times.
 * In practice it will use the same vocabulary between configurations that use the exact
 * same vocabulary file and stemmer, stopwords and language configurations.
 * 
 * @author v2
 *
 */
public class SharedVocabularyMauiFilterFactoryImpl implements MauiFilterFactory {
	private static Logger logger = LoggerFactory.getLogger(SharedVocabularyMauiFilterFactoryImpl.class);
	
	private Map<String, Vocabulary> vocabularyCache = new HashMap<>();
	
	@Override
	public MauiFilter createFilter(MauiFilterConfiguration config) throws MauiFilterInitializationException {
		MauiFilter ret;

		File modelFile = MauiFilterFactoryImpl.getFilePossiblyInRelativePath(config.getConfigurationDirectory(), config.getModel());
		File vocabFile = MauiFilterFactoryImpl.getFilePossiblyInRelativePath(config.getConfigurationDirectory(), config.getVocab());

		testFile("Model", modelFile);
		testFile("Vocabulary", vocabFile);

		ret = readModel(config, modelFile);

		ret.setVocabularyName(config.getVocab());
		ret.setVocabularyFormat(config.getVocabFormat());
		ret.setDocumentLanguage(config.getLanguage());
		
		boolean cachedVocabulary = false;
		
		Vocabulary vocabulary = getCachedVocabulary(vocabFile, config.getStemmer(), config.getStopwords(), config.getLanguage());
		if (vocabulary != null) {
			logger.info("Using previously cached vocabulary "+vocabFile+" (stemmer: "+config.getStemmer()+", stopwords: "+config.getStopwords()+", language: "+config.getLanguage()+")");
			cachedVocabulary = true;
		}
		
		Stemmer stemmer = loadStemmer(config);
		ret.setStemmer(stemmer);

		if (!cachedVocabulary) {
			vocabulary = createVocabulary(config, vocabFile, stemmer);
			
			logger.info("Caching vocabulary "+vocabFile+" (stemmer: "+config.getStemmer()+", stopwords: "+config.getStopwords()+", language: "+config.getLanguage()+")");
			cacheVocabulary(vocabFile, config.getStemmer(), config.getStopwords(), config.getLanguage(), vocabulary);
		}
		
		ret.setVocabulary(vocabulary);

		return ret;
	}

	private String createVocabularyKey(File file, String stemmerName, String stopWords, String language) throws MauiFilterInitializationException {
		try {
			return file.getCanonicalPath() + "///" + stemmerName + "///" + stopWords + "///" + language;
		} catch(IOException ie) {
			throw new MauiFilterInitializationException("Cannot determine canonical file name for vocabulary", ie);
		}
	}
	
	Vocabulary getCachedVocabulary(File file, String stemmerName, String stopWords, String language) throws MauiFilterInitializationException {
		String key = createVocabularyKey(file, stemmerName, stopWords, language);
		return vocabularyCache.get(key);
	}
	
	void cacheVocabulary(File file, String stemmerName, String stopWords, String language, Vocabulary vocabulary) throws MauiFilterInitializationException {
		String key = createVocabularyKey(file, stemmerName, stopWords, language);
		vocabularyCache.put(key, vocabulary);
	}
	
	MauiFilter readModel(MauiFilterConfiguration config, File modelFile)
			throws MauiFilterInitializationException {
		try (InputStream is = new FileInputStream(modelFile)) {
			ObjectInputStream in = new ObjectInputStream(is);
			return (MauiFilter) in.readObject();

		} catch (IOException e) {
			throw new MauiFilterInitializationException("Error while loading extraction model from file " + config.getModel() + "!\n", e);
		} catch (ClassNotFoundException e) {
			throw new MauiFilterInitializationException("Class mismatch when loading maui model from file " + config.getModel() + "!\n", e);
		}
	}

	Vocabulary createVocabulary(MauiFilterConfiguration config, File vocabFile, Stemmer stemmer)
			throws MauiFilterInitializationException
	{
		Vocabulary vocabulary;
		vocabulary = new Vocabulary();
		vocabulary.setStemmer(stemmer);
		
		try {
			String stopwordsString = "com.entopix.maui.stopwords.".concat(config.getStopwords());
		    Stopwords stopwords = (Stopwords) Class.forName(stopwordsString).newInstance();
		    
		   	vocabulary.setStopwords(stopwords);
		} catch(ClassNotFoundException | IllegalAccessException | InstantiationException e) {
			throw new MauiFilterInitializationException("Unable to load stopwords "+config.getStopwords(), e);
		}
		
		vocabulary.setLanguage(config.getLanguage());
		vocabulary.setSerialize(false);

		vocabulary.initializeVocabulary(vocabFile.getAbsolutePath(), config.getVocabFormat());
		return vocabulary;
	}

	Stemmer loadStemmer(MauiFilterConfiguration config) throws MauiFilterInitializationException {
		try {
			String stemmerString = "com.entopix.maui.stemmers.".concat(config.getStemmer());
	        return (Stemmer) Class.forName(stemmerString).newInstance();
		} catch(ClassNotFoundException | IllegalAccessException | InstantiationException e) {
			throw new MauiFilterInitializationException("Unable to load stemmer "+config.getStemmer(), e);
		}
	}

	void testFile(String type, File file) throws MauiFilterInitializationException {
		if (!file.exists()) {
			throw new MauiFilterInitializationException(type+" file "+file+" does not exist!");
		}
		
		if (file.isDirectory()) {
			throw new MauiFilterInitializationException(type+" file "+file+" is a directory, expecting a file!");
		}
		
		if (!file.canRead()) {
			throw new MauiFilterInitializationException("Cannot read "+type+" file "+file+"!");
		}
	}
}
