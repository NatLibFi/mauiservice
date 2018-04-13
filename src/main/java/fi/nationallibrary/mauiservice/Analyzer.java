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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.entopix.maui.filters.MauiFilter;
import com.entopix.maui.filters.MauiFilter.MauiFilterException;

import fi.nationallibrary.mauiservice.response.AnalyzerResponse;
import fi.nationallibrary.mauiservice.response.AnalyzerResponse.AnalyzerResult;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class Analyzer {
	private static Logger logger = LoggerFactory.getLogger(Analyzer.class);

	public AnalyzerResponse analyze(MauiFilter filter, String input) {
		FastVector atts = new FastVector(3);
		atts.addElement(new Attribute("filename", (FastVector) null));
		atts.addElement(new Attribute("doc", (FastVector) null));
		atts.addElement(new Attribute("keyphrases", (FastVector) null));
		Instances data = new Instances("keyphrase_training_data", atts, 0);

		double[] newInst = new double[3];
		newInst[0] = Instance.missingValue();

		// Adding the text of the document to the instance
		newInst[1] = data.attribute(1).addStringValue(input);

		newInst[2] = Instance.missingValue();

		data.add(new Instance(1.0, newInst));

		try {
			filter.input(data.instance(0));
		} catch (MauiFilterException mfe) {
			throw new IllegalArgumentException("Unable to process data", mfe);
		}

		data = data.stringFreeStructure();

		Instance inst;
		logger.trace("-- Keyphrases and feature values:");

		AnalyzerResponse response = new AnalyzerResponse();
		while ((inst = filter.output()) != null) {
			String uri = inst.stringValue(0);
			double score = inst.value(filter.getProbabilityIndex());
			String label = inst.stringValue(filter.getOutputFormIndex());

			AnalyzerResult result = new AnalyzerResult();
			result.setUri(uri);
			result.setScore(score);
			result.setLabel(label);

			if (logger.isTraceEnabled()) {
				logger.trace(" - result " + result.getUri() + " (" + result.getLabel() + "), score = " + result.getScore());
			}

			response.getResults().add(result);
		}

		return response;
	}

}
