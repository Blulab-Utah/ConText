package gov.va.vinci.leo.context.tools;

import gov.va.vinci.leo.tools.LeoUtils;
import groovy.util.ConfigObject;
import groovy.util.ConfigSlurper;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Stores one or more configurations to be used by an annotator, including configurations pulled from a groovy config file.
 *
 * User: Thomas Ginter
 * Date: 6/16/15
 * Time: 11:54
 */
public class AnnotatorConfiguration {
    /**
     * Name of the annotator set by the configuration file.
     */
    private String annotatorName = null;
    /**
     * Default configuration parameters.
     */
    private ConfigObject defaults;
    /**
     * Map of configuration names to a map of parameters for the annotator.
     */
    private Map<String, Map> configurationNameParametersMap = new LinkedHashMap<String, Map>();

    /**
     * Default constructor.
     */
    public AnnotatorConfiguration() {
        this.defaults = new ConfigObject();
        this.annotatorName = LeoUtils.getUUID();
    }

    /**
     * Initialize one or more configurations from a groovy configuration file.
     *
     * @param configurationFile
     * @throws IOException
     */
    public AnnotatorConfiguration(File configurationFile) throws IOException {
        parseConfiguration(configurationFile);
    }

    /**
     * Parse the groovy configuration file to retrieve the parameters for the annotator.
     *
     * @param configurationFile
     * @throws IOException
     */
    protected void parseConfiguration(File configurationFile) throws IOException {
        ConfigSlurper configSlurper = new ConfigSlurper();
        ConfigObject configObject = new ConfigSlurper().parse(FileUtils.readFileToString(configurationFile));

        //Get the name and base configuration
        annotatorName =(String)configObject.get("name");
        ConfigObject configuration = (ConfigObject) configObject.get("configuration");

        //Get the default parameters if specified
        if(configuration.get("defaults") != null) {
            this.defaults = (ConfigObject) configuration.get("defaults");
        }

        // Create the configuration maps and merge in any defaults. .
        for (Object configurationKey: configuration.keySet()) {
            String configurationName = (String)configurationKey;

            // Defaults are handled outside of this loop.
            if ("defaults".equals(configurationName)) {
                continue;
            }

            if (configurationNameParametersMap.containsKey(configurationName)) {
                throw new IllegalArgumentException("ConText configuration file has multiple configurations of the same name: '" + configurationName + "'.");
            }
            ConfigObject contextConfigObject = (ConfigObject) configuration.get(configurationName);
            ConfigObject mergedConfigObject = contextConfigObject.clone();
            mergeDefaults(mergedConfigObject, this.defaults);
            configurationNameParametersMap.put(configurationName, mergedConfigObject);
        }
    }

    /**
     * Merge default parameter values for parameters that have not been set on the configuration.
     *
     * @param config configuration to merge
     * @param defaults default parameters to merge
     */
    protected void mergeDefaults(ConfigObject config, ConfigObject defaults) {
        for(Object defaultKey : defaults.keySet()) {
            if(!config.containsKey(defaultKey)) {
                config.put(defaultKey, defaults.get(defaultKey));
            }
        }
    }

    /**
     * Add a parameter and associated value to the configuration named for the annotatorName. Typically used when there
     * is only one configuration for the annotator.
     *
     * @param parameter Name of the parameter to add
     * @param value Value of the parameter to add
     * @param overwrite if true then the parameter will be overwritten if it exists
     * @return
     */
    public AnnotatorConfiguration addParameter(String parameter, Object value, boolean overwrite) {
        return this.addParameter(this.annotatorName, parameter, value, overwrite);
    }

    /**
     * Add a parameter to the configuration with the name specified.
     *
     * @param configurationName
     * @param parameter
     * @param value
     * @param overwrite
     * @return
     */
    public AnnotatorConfiguration addParameter(String configurationName, String parameter, Object value, boolean overwrite) {
        ConfigObject configuration = (ConfigObject) configurationNameParametersMap.get(configurationName);
        if(configuration == null) {
            configuration = new ConfigObject();
            mergeDefaults(configuration, this.defaults);
        }
        if(configuration.get(parameter) == null || overwrite) {
            configuration.put(parameter, value);
        }
        return this;
    }

    /**
     * Get the list of configuration names.
     *
     * @return a list of configuration names.
     */
    public Set<String> getConfigNames() {
        return configurationNameParametersMap.keySet();
    }

    /**
     * Retrieve the parameters for the configuration with the annotatorName label.
     *
     * @return Map of parameters with String key to Object value
     */
    public Map getParameters() {
        return this.getParameters(this.annotatorName);
    }

    /**
     * Return a map of parameters for the named configuration.
     *
     * @param configurationName Name of the configuration whose parameters will be retrieved
     * @return Map of parameters for the configuration with String key to Object value
     */
    public Map getParameters(String configurationName) {
        return configurationNameParametersMap.get(configurationName);
    }

}
