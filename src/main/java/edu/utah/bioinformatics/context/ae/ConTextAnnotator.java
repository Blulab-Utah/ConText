package edu.utah.bioinformatics.context.ae;

import edu.utah.bioinformatics.context.ConText;
import gov.va.vinci.leo.context.ae.BaseConTextAnnotator;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.Feature;
import org.apache.uima.cas.Type;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * User: Thomas Ginter
 * Date: 6/17/15
 * Time: 11:19
 */
public class ConTextAnnotator extends BaseConTextAnnotator {

    protected ConText conText = new ConText();

    /**
     * Apply the context rules to the annotation.
     *
     * @param contextAnnotation Annotation instance whose context features will be determined
     */
    @Override
    public void applyContext(Annotation contextAnnotation) throws AnalysisEngineProcessException {
        Feature conceptFeatureObj = contextAnnotation.getType().getFeatureByBaseName(this.conceptFeatureName);
        Annotation concept = (Annotation) contextAnnotation.getFeatureValue(conceptFeatureObj);

        //Get the context results from the ConText service class
        try {
            JCas jCas = contextAnnotation.getCAS().getJCas();
            Type outputAnnotationType = jCas.getRequiredType(this.outputType);

            //Parse the context annotation and get the results
            ArrayList<String> results = conText.applyContext(concept.getCoveredText(), contextAnnotation.getCoveredText());

            //Set the feature values from the context results
            Map<String, Object> featureValues = new HashMap<String, Object>(3);
            featureValues.put("Negation", results.get(2));
            featureValues.put("Temporality", results.get(3));
            featureValues.put("Experiencer", results.get(4));

            //Create the output annotation
            this.addOutputAnnotation(this.outputType, jCas, contextAnnotation.getBegin(), contextAnnotation.getEnd(), featureValues);
        } catch (Exception e) {
            throw new AnalysisEngineProcessException(e);
        }
    }
}
