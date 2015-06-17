package gov.va.vinci.leo.context.ae;

import gov.va.vinci.leo.AnnotationLibrarian;
import gov.va.vinci.leo.ae.LeoBaseAnnotator;
import gov.va.vinci.leo.context.tools.AnnotatorConfiguration;
import gov.va.vinci.leo.descriptors.LeoTypeSystemDescription;
import gov.va.vinci.leo.tools.ConfigurationParameterImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CASException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.metadata.ConfigurationParameter;
import org.apache.uima.resource.metadata.TypeDescription;
import org.apache.uima.resource.metadata.impl.TypeDescription_impl;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

/**
 * This is a generic annotator for the Context algorithm that allows the user to specify an input type that represents
 * the context in which an anchor concept is found.  The name of the annotation type feature which contains a pointer to
 * the anchor must also be specified.  Rules are then executed against each concept anchor and context instance to
 * determine the ConText features provided by the implementing class.
 *
 * User: Thomas Ginter
 * Date: 6/16/15
 * Time: 12:25
 */
public abstract class BaseConTextAnnotator extends LeoBaseAnnotator {

    /**
     * Annotation configuration map, enables the use of a groovy config file.
     */
    protected AnnotatorConfiguration annotatorConfiguration = null;

    /**
     * Name of the current configuration being used.
     */
    protected String currentConfigurationName = null;

    /**
     * Name of the default context type for this annotator
     */
    protected static String	CONTEXT_TYPE = "gov.va.vinci.leo.context.types.Context";

    /********************************
     * Annotator Parameter Variables
     ********************************/

    /**
     * Optional, path to the groovy config file. Parameters can be directly specified but will be ignored if a groovy
     * config file path is provided.
     */
    protected String  groovy_config_file    = null;
    /**
     * Optional, Name of the annotation type feature with stores a pointer to the anchor concept annotation within the
     * input type specified.  Defaults to 'Anchor'.
     */
    protected String  conceptFeatureName    = null;

    @Override
    public void initialize(UimaContext aContext) throws ResourceInitializationException {
        super.initialize(aContext);

        if(StringUtils.isNotBlank(groovy_config_file)) {
            try {
                this.annotatorConfiguration = new AnnotatorConfiguration(new File(groovy_config_file));
            } catch (IOException e) {
                throw new ResourceInitializationException(e);
            }
        } else {
            this.annotatorConfiguration = new AnnotatorConfiguration();
            this.annotatorConfiguration.addParameter(Param.INPUT_TYPE.getName(), inputTypes, true);
            this.annotatorConfiguration.addParameter(Param.OUTPUT_TYPE.getName(), outputType, true);
            this.annotatorConfiguration.addParameter(Param.CONCEPT_FEATURE_NAME.getName(), conceptFeatureName, true);
        }
    }

    /**
     * Called once by the UIMA Framework for each document being analyzed (each
     * CAS instance). Acts on the parameters given by <initialize> method. Main
     * method to implement the annotator logic. In the base class, this simply
     * increments to numberOfCASesProcessed
     *
     * @param aJCas the CAS to process
     * @throws AnalysisEngineProcessException if an exception occurs during processing.
     * @see JCasAnnotator_ImplBase#process(JCas)
     */
    @Override
    public void process(JCas aJCas) throws AnalysisEngineProcessException {
        super.process(aJCas);
        for(String configurationName : annotatorConfiguration.getConfigNames()) {
            this.currentConfigurationName = configurationName;
            Map parameters = annotatorConfiguration.getParameters(configurationName);
            this.outputType = (String) parameters.get(Param.OUTPUT_TYPE.getName());
            this.conceptFeatureName = (String) parameters.get(Param.CONCEPT_FEATURE_NAME.getName());
            //For each input type get the instances and pass each one to the method for processing
            for(String type : (String[]) parameters.get(Param.INPUT_TYPE.getName())) {
                try {
                    Collection<Annotation> annotations = AnnotationLibrarian.getAllAnnotationsOfType(aJCas, type);
                    for(Annotation annotation : annotations) {
                        applyContext(annotation);
                    }
                } catch (CASException e) {
                    throw new AnalysisEngineProcessException(e);
                }
            }
        }
    }

    /**
     * Apply the context rules to the annotation.
     *
     * @param contextAnnotation Annotation instance whose context features will be determined
     */
    public abstract void applyContext(Annotation contextAnnotation) throws AnalysisEngineProcessException;

    @Override
    public LeoTypeSystemDescription getLeoTypeSystemDescription() {
        return getLeoTypeSystemDescription(CONTEXT_TYPE);
    }

    /**
     * Create the Annotation Type System for the the Annotator.
     *
     * @param type String that is the Parent type
     * @return LeoTypeSystemDescription object with the specific types.
     */
    public LeoTypeSystemDescription getLeoTypeSystemDescription(String type) {
        TypeDescription parentAnnotation = new TypeDescription_impl(type, "", "uima.tcas.Annotation");
        parentAnnotation.addFeature("Experiencer", "", "uima.cas.String");
        parentAnnotation.addFeature("ExperiencerPattern", "", "uima.cas.String");
        parentAnnotation.addFeature("Negation", "", "uima.cas.String");
        parentAnnotation.addFeature("NegationPattern", "", "uima.cas.String");
        parentAnnotation.addFeature("Temporality", "", "uima.cas.String");
        parentAnnotation.addFeature("TemporalityPattern", "", "uima.cas.String");
        parentAnnotation.addFeature("Window", "", "uima.tcas.Annotation");

        LeoTypeSystemDescription ftsd = new LeoTypeSystemDescription();
        ftsd.addType(parentAnnotation);
        return ftsd;
    }

    /**
     * A set of parameter definitions for this annotator.
     *
     * Each field represents an annotation parameter and utilizes the UIMA ConfigurationParameter as in the declaration:
     * <p>
     * <code>public static ConfigurationParameter PARAMETER_NAME = new ConfigurationParameterImpl("name", "description", "type", isMandatory, isMultivalued, new String[]{});</code>
     */
    public static class Param extends LeoBaseAnnotator.Param {
        /**
         * the path to the groovy configuration file. Either a resource or groovy config file must be used, but not both.
         */
        public static ConfigurationParameter GROOVY_CONFIG_FILE
                = new ConfigurationParameterImpl("groovy_config_file",
                                                 "The groovy config file path",
                                                 "String", false, false, new String[0]);
        /**
         * String name of the feature in the input types that contain the concept annotation. The
         * default feature name is "Anchor"
         */
        public static ConfigurationParameter CONCEPT_FEATURE_NAME
                = new ConfigurationParameterImpl("conceptFeatureName",
                                                 "String name of the feature in the input types that contain the concept annotation. The default feature name is \"Anchor\"",
                                                 "String", false, false, new String[] {});
    }
}
