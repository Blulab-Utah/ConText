package edu.utah.bioinformatics.context.pipeline;

import edu.utah.bioinformatics.context.ae.ConTextAnnotator;
import gov.va.vinci.leo.context.types.Context;
import gov.va.vinci.leo.descriptors.LeoAEDescriptor;
import gov.va.vinci.leo.descriptors.LeoTypeSystemDescription;
import gov.va.vinci.leo.regex.ae.RegexAnnotator;
import gov.va.vinci.leo.regex.types.RegularExpressionType;
import gov.va.vinci.leo.window.ae.WindowAnnotator;
import gov.va.vinci.leo.window.types.Window;

/**
 * User: Thomas Ginter
 * Date: 6/17/15
 * Time: 17:05
 */
public class ContextPipeline implements PipelineInterface {
    @Override
    public LeoAEDescriptor getPipeline() {
        LeoTypeSystemDescription typeSystemDescription = getLeoTypeSystemDescription();
        LeoAEDescriptor aggregate = new LeoAEDescriptor();
        try {
            aggregate.addDelegate(new RegexAnnotator().getLeoAEDescriptor()
                                    .setParameterSetting(RegexAnnotator.Param.RESOURCE.getName(), "src/test/resources/concepts.regex")
                                    .setParameterSetting(RegexAnnotator.Param.OUTPUT_TYPE.getName(), RegularExpressionType.class.getCanonicalName())
                                    .addTypeSystemDescription(typeSystemDescription))
                     .addDelegate(new WindowAnnotator().getLeoAEDescriptor()
                                    .setParameterSetting(WindowAnnotator.Param.INPUT_TYPE.getName(), new String[]{RegularExpressionType.class.getCanonicalName()})
                                    .setParameterSetting(WindowAnnotator.Param.OUTPUT_TYPE.getName(), Window.class.getCanonicalName())
                                    .setParameterSetting(WindowAnnotator.Param.WINDOW_SIZE.getName(), 10)
                                    .setParameterSetting(WindowAnnotator.Param.ANCHOR_FEATURE.getName(), "Anchor"))
                                    .addTypeSystemDescription(typeSystemDescription)
                     .addDelegate(new ConTextAnnotator().getLeoAEDescriptor()
                                    .setParameterSetting(ConTextAnnotator.Param.INPUT_TYPE.getName(), new String[]{Window.class.getCanonicalName()})
                                    .setParameterSetting(ConTextAnnotator.Param.OUTPUT_TYPE.getName(), Context.class.getCanonicalName())
                                    .setParameterSetting(ConTextAnnotator.Param.CONCEPT_FEATURE_NAME.getName(), "Anchor")
                                    .addTypeSystemDescription(typeSystemDescription));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return aggregate;
    }

    @Override
    public LeoTypeSystemDescription getLeoTypeSystemDescription() {
        return new LeoTypeSystemDescription()
            .addTypeSystemDescription(new RegexAnnotator().getLeoTypeSystemDescription())
            .addTypeSystemDescription(new WindowAnnotator().getLeoTypeSystemDescription())
            .addTypeSystemDescription(new ConTextAnnotator().getLeoTypeSystemDescription());
    }
}