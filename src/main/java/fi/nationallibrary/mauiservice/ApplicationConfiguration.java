package fi.nationallibrary.mauiservice;

import java.io.FileReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fi.nationallibrary.mauiservice.ini.INI4JMauiConfigurationFactoryImpl;
import fi.nationallibrary.mauiservice.ini.MauiConfiguration;
import fi.nationallibrary.mauiservice.ini.MauiConfigurationFactory;

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
	public MauiConfiguration mauiConfigration(MauiConfigurationFactory factory, ApplicationArguments arg) throws IOException
	{
		String configFile = DEFAULT_CONFIGURATION_FILE;
		if (arg.containsOption("configuration")) {
			configFile = arg.getOptionValues("configuration").get(0);
		}
		
		logger.info("Loading INI file "+configFile);
		
		try (FileReader reader = new FileReader(configFile)) {
			return factory.readConfig(reader);
		}
	}
}
