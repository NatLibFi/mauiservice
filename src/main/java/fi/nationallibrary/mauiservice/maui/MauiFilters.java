package fi.nationallibrary.mauiservice.maui;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

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
