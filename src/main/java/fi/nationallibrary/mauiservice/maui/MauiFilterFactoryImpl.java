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

import com.entopix.maui.filters.MauiFilter;
import com.entopix.maui.stemmers.Stemmer;
import com.entopix.maui.stopwords.Stopwords;
import com.entopix.maui.vocab.Vocabulary;

import fi.nationallibrary.mauiservice.ini.MauiFilterConfiguration;

public class MauiFilterFactoryImpl implements MauiFilterFactory {

	@Override
	public MauiFilter createFilter(MauiFilterConfiguration config) throws MauiFilterInitializationException {
		MauiFilter ret;

		File modelFile = new File(config.getConfigurationDirectory(), config.getModel());
		File vocabFile = new File(config.getConfigurationDirectory(), config.getVocab());
		
		testFile("Model", modelFile);
		testFile("Vocabulary", vocabFile);

		try (InputStream is = new FileInputStream(modelFile)) {
			ObjectInputStream in = new ObjectInputStream(is);
			ret = (MauiFilter) in.readObject();

		} catch (IOException e) {
			throw new MauiFilterInitializationException("Error while loading extraction model from file " + config.getModel() + "!\n", e);
		} catch (ClassNotFoundException e) {
			throw new MauiFilterInitializationException("Class mismatch when loading maui model from file " + config.getModel() + "!\n", e);
		}

		ret.setVocabularyName(config.getVocab());
		
		ret.setVocabularyFormat(config.getVocabFormat());
		ret.setDocumentLanguage(config.getLanguage());
		
		Vocabulary vocabulary = new Vocabulary();
		
		try {
			String stemmerString = "com.entopix.maui.stemmers.".concat(config.getStemmer());
	        Stemmer stemmer = (Stemmer) Class.forName(stemmerString).newInstance();
	        
			ret.setStemmer(stemmer);
			vocabulary.setStemmer(stemmer);
		} catch(ClassNotFoundException | IllegalAccessException | InstantiationException e) {
			throw new MauiFilterInitializationException("Unable to load stemmer "+config.getStemmer(), e);
		}

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

		ret.setVocabulary(vocabulary);

		return ret;
	}

	private void testFile(String type, File file) throws MauiFilterInitializationException {
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
