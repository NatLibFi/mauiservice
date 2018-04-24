package fi.nationallibrary.mauiservice.maui;

import java.util.Collections;

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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.entopix.maui.filters.MauiFilter;

import fi.nationallibrary.mauiservice.ini.MauiConfiguration;
import fi.nationallibrary.mauiservice.ini.MauiFilterConfiguration;

public class MauiFilters {
	private static Logger logger = LoggerFactory.getLogger(MauiFilters.class);
	
	// Injected by spring
	private MauiFilterFactory filterFactory;
	private MauiConfiguration configuration;
	
	// Set up in init()
	private Map<String, MauiFilter> filters;
	
	
	public MauiFilterFactory getFilterFactory() {
		return filterFactory;
	}
	
	public void setFilterFactory(MauiFilterFactory filterFactory) {
		this.filterFactory = filterFactory;
	}
	
	public MauiConfiguration getConfiguration() {
		return configuration;
	}
	
	public void setConfiguration(MauiConfiguration configuration) {
		this.configuration = configuration;
	}
	
	public MauiFilter getFilter(String id) {
		if (filters == null) {
			throw new IllegalStateException("Call init() before getFilter()");
		}
		return filters.get(id);
	}
	
	public Set<String> getFilterNames() {
		return Collections.unmodifiableSet(filters.keySet());
	}

	public void init() throws MauiFilterInitializationException {
		// Double-gate to protect creating the filters multiple times over
		if (filters == null) {
			synchronized(this) {
				if (filters == null) {
					filters = createFilters();
				}
			}
		}
	}
	
	private Map<String, MauiFilter> createFilters() throws MauiFilterInitializationException {
		Map<String, MauiFilter> ret = new HashMap<>();
		
		for (Entry<String, MauiFilterConfiguration> e : getConfiguration().getConfigurations().entrySet()) {
			logger.info("Creating MauiFilter "+e.getKey());
			MauiFilter tmp = getFilterFactory().createFilter(e.getValue());
			ret.put(e.getKey(), tmp);
		}
		
		return ret;
	}
}
