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

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class AnalysisParameterFactoryTest {

	private AnalysisParameterFactory factory;
	
	@Before
	public void setUp() throws Exception {
		factory = new AnalysisParameterFactory();
	}

	@Test
	public void testNoConfiguration() {
		Map<String, Object> obj = new HashMap<>();
		
		AnalysisParameters params = factory.createParameters(obj);
		
		assertNotNull(params);
		assertNull(params.getLimitNumberOfResults());
	}
	
	@Test
	public void testLimitSet() {
		Map<String, Object> obj = new HashMap<>();
		obj.put("limit", new Integer(5));
		
		AnalysisParameters params = factory.createParameters(obj);
		
		assertNotNull(params);
		assertEquals((Integer)5, params.getLimitNumberOfResults());
	}

}
