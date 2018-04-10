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

public class MauiModelConfiguration {
	private String model;
	private String vocab;
	private String language;
	private String stopwords;
	private String stemmer;
	
	public String getModel() {
		return model;
	}
	
	public void setModel(String model) {
		this.model = model;
	}
	
	public String getVocab() {
		return vocab;
	}
	
	public void setVocab(String vocab) {
		this.vocab = vocab;
	}
	
	public String getLanguage() {
		return language;
	}
	
	public void setLanguage(String language) {
		this.language = language;
	}
	
	public String getStopwords() {
		return stopwords;
	}
	
	public void setStopwords(String stopwords) {
		this.stopwords = stopwords;
	}
	
	public String getStemmer() {
		return stemmer;
	}
	
	public void setStemmer(String stemmer) {
		this.stemmer = stemmer;
	}
	
}
