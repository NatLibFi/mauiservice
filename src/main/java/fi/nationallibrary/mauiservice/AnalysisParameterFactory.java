package fi.nationallibrary.mauiservice;

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
	public AnalysisParameters createParameters(Object o) {
		AnalysisParameters ret = new AnalysisParameters();
		
		if (o != null) {
			if (o instanceof Map) {
				@SuppressWarnings("unchecked")
				Map<String, Object> map = (Map<String, Object>)o;
				
				Object limit = map.get("limit");
				if (limit != null) {
					if (!(limit instanceof Number)) {
						throw new IllegalArgumentException("Value for key \"limit\" must be a Number");
					}
					
					ret.setLimitNumberOfResults(((Number)limit).intValue());
				}
				
			}
		}
		
		return ret;
	}
}
