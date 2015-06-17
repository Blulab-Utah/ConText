package gov.va.vinci.leo.context.ae;

import gov.va.vinci.leo.ae.LeoBaseAnnotator;
import gov.va.vinci.leo.tools.ConfigurationParameterImpl;
import org.apache.uima.UimaContext;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.metadata.ConfigurationParameter;

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
     * Annotator Parameter Variables
     */

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
