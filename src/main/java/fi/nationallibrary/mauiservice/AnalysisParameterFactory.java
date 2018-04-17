package fi.nationallibrary.mauiservice;

import java.util.Collection;

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

import java.util.Map;

public class AnalysisParameterFactory {
	
	@SuppressWarnings("rawtypes")
	public AnalysisParameters createParameters(Object o) {
		AnalysisParameters ret = new AnalysisParameters();
		
		if (o != null) {
			if (o instanceof Map) {
				@SuppressWarnings("unchecked")
				Map<String, Object> map = (Map<String, Object>)o;
				
				Object limit = map.get("limit");
				if (limit != null) {
					// Take first value if the value is a collection
					if (limit instanceof Collection) {
						limit = ((Collection)limit).iterator().next();
					}
					
					// If the value is a string, convert it into an integer
					if (limit instanceof String) {
						limit = new Integer((String)limit);
					}
					
					// If it's not a number by now, we don't know what to do
					if (!(limit instanceof Number)) {
						throw new IllegalArgumentException("Value for key \"limit\" must be a Number, but it's a "+limit.getClass());
					}
					
					ret.setLimitNumberOfResults(((Number)limit).intValue());
				}
				
			}
		}
		
		return ret;
	}
}
