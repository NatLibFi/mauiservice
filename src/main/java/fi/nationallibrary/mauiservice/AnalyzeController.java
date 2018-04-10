package fi.nationallibrary.mauiservice;

import javax.servlet.http.HttpServletResponse;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.entopix.maui.filters.MauiFilter;

import fi.nationallibrary.mauiservice.maui.MauiFilters;

@RestController
public class AnalyzeController {
	
	@Autowired
	private MauiFilters filters;

	@RequestMapping("/maui/{id}/analyze")
	public String analyze(@PathVariable("id") String configurationId, HttpServletResponse response) {
		MauiFilter filter = filters.getFilter(configurationId);
		
		if (filter == null) {
			response.setStatus(404);
			return null;
		}
		return "Hello world: "+configurationId;
	}
}
