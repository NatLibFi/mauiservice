package fi.nationallibrary.mauiservice;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

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

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.entopix.maui.filters.MauiFilter;

import fi.nationallibrary.mauiservice.maui.MauiFilters;


@RestController
public class AnalyzeController {
	private Logger logger = LoggerFactory.getLogger(AnalyzeController.class);
	
	@Autowired
	private MauiFilters filters;
	
	@Autowired
	private Analyzer analyzer;

	@RequestMapping(path = "/maui/{id}/analyze", method = RequestMethod.POST)
	public String analyze(
			@PathVariable("id") String configurationId,
			@RequestBody Map<String, Object> parameters,
			HttpServletResponse response)
	{
		if (logger.isTraceEnabled()) {
			logger.trace("Received analysis request for id '"+configurationId+"' with text '"+parameters.get("text")+"'");
		}
		MauiFilter filter = filters.getFilter(configurationId);
		
		if (filter == null) {
			response.setStatus(404);
			return null;
		}
		
		// Filters are not synchronized
		String result;
		synchronized(filter) {
			result = analyzer.analyze(filter, (String)parameters.get("text"));
		}
		
		if (logger.isTraceEnabled()) {
			logger.trace(" - analysis result: '"+result+"'");
		}
		
		return result;
	}

}
