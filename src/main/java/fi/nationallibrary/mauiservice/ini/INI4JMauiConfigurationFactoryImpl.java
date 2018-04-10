package fi.nationallibrary.mauiservice.ini;

import java.io.IOException;

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

import java.io.Reader;
import java.util.Map.Entry;

import org.ini4j.InvalidFileFormatException;
import org.ini4j.Profile.Section;
import org.ini4j.Wini;

public class INI4JMauiConfigurationFactoryImpl implements MauiConfigurationFactory {

	@Override
	public MauiConfiguration readConfig(Reader reader) throws IOException {
		try {
			Wini ini = new Wini(reader);
			
			MauiConfiguration ret = new MauiConfiguration();
			
			for (Entry<String, Section> e : ini.entrySet()) {
				MauiModelConfiguration mmc = new MauiModelConfiguration();
				ret.getConfigurations().put(e.getKey(), mmc);
				
				
				mmc.setLanguage  (getFieldOrFail(e, "language"));
				mmc.setModel     (getFieldOrFail(e, "model"));
				mmc.setStemmer   (getFieldOrFail(e, "stemmer"));
				mmc.setStopwords (getFieldOrFail(e, "stopwords"));
				mmc.setVocab     (getFieldOrFail(e, "vocab"));
			}
			
			return ret;
			
		} catch(InvalidFileFormatException e) {
			throw new IOException(e);
		}
	}

	private String getFieldOrFail(Entry<String, Section> e, String key) throws IOException {

		String value = e.getValue().get(key);
		if (value != null) {
			value = value.trim();
		}
		
		if (value == null || value.length() == 0) {
			throw new IOException("Section "+e.getKey()+" is missing value for "+key);
		}
		
		return value;
	}

}
