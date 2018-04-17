package fi.nationallibrary.mauiservice;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fi.nationallibrary.mauiservice.ini.INI4JMauiConfigurationFactoryImpl;
import fi.nationallibrary.mauiservice.ini.MauiConfiguration;
import fi.nationallibrary.mauiservice.ini.MauiConfigurationFactory;
import fi.nationallibrary.mauiservice.maui.MauiFilterFactory;
import fi.nationallibrary.mauiservice.maui.MauiFilterFactoryImpl;
import fi.nationallibrary.mauiservice.maui.MauiFilterInitializationException;
import fi.nationallibrary.mauiservice.maui.MauiFilters;

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

@Configuration
public class ApplicationConfiguration {

	public static final String DEFAULT_CONFIGURATION_FILE = "./mauiservice.ini";
	
	private static Logger logger = LoggerFactory.getLogger(ApplicationConfiguration.class);
	
	@Bean
	public MauiConfigurationFactory mauiConfigurationFactory() {
		return new INI4JMauiConfigurationFactoryImpl();
	}
	
	@Bean
	@Qualifier("configFileName")
	public String configFileName(ApplicationArguments arg) {
		String configFile = DEFAULT_CONFIGURATION_FILE;
		
		if (arg.containsOption("configuration")) {
			List<String> tmp = arg.getOptionValues("configuration");
			if (tmp.size() == 0) {
				throw new IllegalArgumentException("You need to specify a value: --configuration=file.ini");
			}
			if (tmp.size() > 1) {
				throw new IllegalArgumentException("Please specify only one value for --configuration=file.ini");
			}
			configFile = tmp.get(0);
		} else {
			String property = System.getProperty("MAUISERVICE_CONFIGURATION");
			if (property != null) {
				logger.info("Reading configuration file from system property MAUISERVICE_CONFIGURATION");
				configFile = property;
			}
		}
		return configFile;
	}
	
	@Bean
	public MauiConfiguration mauiConfigration(MauiConfigurationFactory factory, @Qualifier("configFileName") String configFile) throws IOException
	{
		if (DEFAULT_CONFIGURATION_FILE.equals(configFile)) {
			logger.info("Loading INI file with default file name ("+configFile+"), you can specify another location via --configuration=path/to/file.ini");
		} else {
			logger.info("Loading INI file "+configFile);
		}
		
		File iniFile = new File(configFile);
		try (FileReader reader = new FileReader(iniFile)) {
			return factory.readConfig(iniFile.getParentFile(), reader);
		}
	}
	
	@Bean
	public MauiFilterFactory mauiFilterFactory() {
		return new MauiFilterFactoryImpl();
	}
	
	@Bean
	public MauiFilters mauiFilters(MauiFilterFactory factory, MauiConfiguration config) throws MauiFilterInitializationException {
		MauiFilters ret = new MauiFilters();
		ret.setFilterFactory(factory);
		ret.setConfiguration(config);
		ret.init();
		
		return ret;
	}
	
	@Bean
	public Analyzer analyzer() {
		return new Analyzer();
	}
	
	@Bean
	public AnalysisParameterFactory analysisParameterFactory() {
		return new AnalysisParameterFactory();
	}
}
