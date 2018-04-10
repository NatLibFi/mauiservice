package fi.nationallibrary.mauiservice;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.entopix.maui.filters.MauiFilter;
import com.entopix.maui.filters.MauiFilter.MauiFilterException;
import com.entopix.maui.util.Topic;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class Analyzer {
	private static Logger logger = LoggerFactory.getLogger(Analyzer.class);
	
	public String analyze(MauiFilter filter, String input) {
		FastVector atts = new FastVector(3);
        atts.addElement(new Attribute("filename", (FastVector) null));
        atts.addElement(new Attribute("doc", (FastVector) null));
        atts.addElement(new Attribute("keyphrases", (FastVector) null));
        Instances data = new Instances("keyphrase_training_data", atts, 0);

        double[] newInst = new double[3];

        newInst[0] = Instance.missingValue();
        //newInst[0] = data.attribute(0).addStringValue(document.getFileName());

        // Adding the text of the document to the instance
        newInst[1] = data.attribute(1).addStringValue(input);
                        
        newInst[2] = Instance.missingValue();

        data.add(new Instance(1.0, newInst));

        try {
                filter.input(data.instance(0));
        } catch(MauiFilterException mfe) {
                throw new IllegalArgumentException("Unable to process data", mfe);
        }

        data = data.stringFreeStructure();
        //logger.info("-- Processing document: " + document.getFileName());


        //Instance[] topRankedInstances = new Instance[topicsPerDocument];
        
        //MauiTopics documentTopics = new MauiTopics(document.getFilePath());

        //documentTopics.setPossibleCorrect(document.getTopicsString().split("\n").length);

        Instance inst;
        //int index = 0;
        double probability;
        Topic topic;
        String title, id;

        logger.trace("-- Keyphrases and feature values:");

        List<String> ret = new ArrayList<>();
        // Iterating over all extracted topic instances
        while ((inst = filter.output()) != null) {
                probability = inst.value(filter.getProbabilityIndex());
                //if (index < getTopicsPerDocument()) {
                        //if (probability > getCutOffTopicProbability()) {
                                //topRankedInstances[index] = inst;
                                title = inst.stringValue(filter.getOutputFormIndex());
                                id = "1"; // topRankedInstances[index].
                                //stringValue(mauiFilter.getOutputFormIndex() + 1); // TODO: Check
                                topic = new Topic(title,  id,  probability);

                                if ((int)inst.value(inst.numAttributes() - 1) == 1) {
                                        topic.setCorrectness(true);
                                } else {
                                        topic.setCorrectness(false);
                                }
                                
                                if (logger.isTraceEnabled()) {
                                        logger.trace("Topic " + title + " " + id + " " + probability + " > " + topic.isCorrect());
                                }
                                ret.add(inst.stringValue(0));
                                //index++;
                        //}
               // }
        }
        
        
        return StringUtils.join(ret, ", ");
	}
	
}
