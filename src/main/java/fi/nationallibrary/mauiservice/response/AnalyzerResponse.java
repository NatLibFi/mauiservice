package fi.nationallibrary.mauiservice.response;

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

import java.util.ArrayList;
import java.util.List;

public class AnalyzerResponse {
	public List<AnalyzerResult> results = new ArrayList<>();
	
	public void setResults(List<AnalyzerResult> results) {
		this.results = results;
	}
	
	public List<AnalyzerResult> getResults() {
		return results;
	}
	
	public static class AnalyzerResult {
		private String uri;
		private String label;
		private Double score;
		
		public void setLabel(String label) {
			this.label = label;
		}
		
		public String getLabel() {
			return label;
		}
		
		public void setScore(Double score) {
			this.score = score;
		}
		
		public Double getScore() {
			return score;
		}
		
		public void setUri(String uri) {
			this.uri = uri;
		}
		
		public String getUri() {
			return uri;
		}
	}
}
