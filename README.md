# ConText

The ConText project contains original ConText algorithm code.  Attributions and more information can be found in
src/main/java/edu/utah/bioinformatics/context/README.

A base Leo annotator has also been created to allow users to implement UIMA annotators that utilize their own algorithms 
for ConText rules.
  
## Leo

Leo provides a framework for the creation and configuration of UIMA-AS pipelines and clients.  The BaseConTextAnnotator
is a Leo annotator that provides the base configuration, annotation retrieval, and output annotation creation functionality
for extending classes.  In addition to the usual Leo configuration capability the AnnotatorConfiguration object was also
added to allow the user to utilize groovy config files in order to specify multiple tasks/sections in one annotator.

## BaseConTextAnnotator

BaseConTextAnnotator retreives and manages the configuration of the annotator.  It also implements the process method 
which is called for processing of each CAS handed to the annotator for processing.  The input annotations are then
retrieved for every configuration and passed to the applyContext abstract method for processing.

### Parameters

Parameters are declared in the static inner class called Param.  By extending the parent static class the implementing
annotator can make use of the parameters declared in the parent.  BaseContextAnnotator declares the following parameters
which are also available as parameters in the groovy config file format:

inputTypes - String array of annotation types that represtent the context of the concept to be processed.  This can be 
one or more window types with a concept anchor or sentence types with a concept name or anchor type.

outputType - Name of the output annotation type to be created.

conceptFeatureName - Name of the feature in each input annotation type that stores the anchor type or concept name.

groovy_config_file - Path to the groovy config file

Parameters can be set directly or within a groovy configuration file.

### Groovy Config File

The groovy config file allows the user to specify multiple configurations for one annotator using the following format:

    name = "MyAnnotator"  //Name of the annotator
    
    //This section contains all the configurations for the annotator
    configuration {
        
        //Default parameter values can be specified in the "defaults" section.  Default values do not overwrite values
        //in configuration sections but simply fill in values that may not be specified.
        defaults {
            outputType = "gov.va.vinci.types.MyOutputContextType"
            conceptFeatureName = "Anchor"
        }
        
        //Each configuration name is arbitrary but must be a unique name.
        "config1" {
            inputTypes = [
                "gov.va.vinci.types.Window",
                "gov.va.vinci.types.BobsWindow"
            ]
            conceptFeatureName = "Concept"
            outputType = "gov.va.vinci.types.BobContext"
        }
        
        //This configuration uses the default outputType and conceptFeatureName
        "Another configuration" {
            inputTypes = [
                "gov.va.vinci.types.Sentence"
            ]
        }
    }
    
If a groovy config file path is provided the other annotator parameters are ignored as configuration is taken completely
from the file.

## edu.utah.bioinformatics.context.ae.ConTextAnnotator

The ConTextAnnotator is an example implementation of a ConText annotator that extends the BaseConTextAnnotator.  It 
utilizes the existing edu.utah.bioinformatics.context.ConText object to analyze each context annotation instance for
the implemented ConText features of temorality, experiencer, and negation.  This annotator is a good example of how a 
user can extend the BaseConTextAnnotator to create their own annotator using their own rules for the ConText features.