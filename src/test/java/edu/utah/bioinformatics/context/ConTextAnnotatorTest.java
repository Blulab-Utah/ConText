package edu.utah.bioinformatics.context;

import edu.utah.bioinformatics.context.pipeline.ContextPipeline;
import gov.va.vinci.leo.AnnotationLibrarian;
import gov.va.vinci.leo.context.types.Context;
import gov.va.vinci.leo.descriptors.LeoAEDescriptor;
import org.apache.commons.io.FileUtils;
import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.jcas.JCas;
import org.junit.Test;

import java.io.File;
import java.util.Collection;

/**
 * User: Thomas Ginter
 * Date: 6/17/15
 * Time: 17:04
 */
public class ConTextAnnotatorTest {

    LeoAEDescriptor conTextPipeline = new ContextPipeline().getPipeline();

    @Test
    public void testConTextAnnotator() throws Exception {
        //Get the UIMA analysis engine
        AnalysisEngine ae = UIMAFramework.produceAnalysisEngine(conTextPipeline.getAnalysisEngineDescription());

        //Setup the CAS and process the document text
        String testDoc = FileUtils.readFileToString(new File("src/test/resources/data-001.txt"));
        JCas jcas = ae.newJCas();
        jcas.setDocumentText(testDoc);
        ae.process(jcas);

        Collection<Context> conTexts = AnnotationLibrarian.getAllAnnotationsOfType(jcas, Context.type);
        /**
         * TODO CREATE ASSESSMENTS ON FEATURE VALUES AND NUMBER OF ANNOTATIONS
         */
        for(Context context : conTexts) {
            System.out.println(context.getCoveredText());
        }
    }
}
