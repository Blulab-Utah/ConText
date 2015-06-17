
/* First created by JCasGen Fri Oct 03 11:32:50 MDT 2014 */
package gov.va.vinci.leo.context.types;

import org.apache.uima.cas.Feature;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.tcas.Annotation_Type;

/** 
 * Updated by JCasGen Fri Oct 03 11:32:50 MDT 2014
 * @generated */
public class Context_Type extends Annotation_Type {
  /** @generated 
   * @return the generator for this type
   */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (Context_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = Context_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new Context(addr, Context_Type.this);
  			   Context_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new Context(addr, Context_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = Context.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("gov.va.vinci.leo.context.types.Context");
 
  /** @generated */
  final Feature casFeat_Experiencer;
  /** @generated */
  final int     casFeatCode_Experiencer;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getExperiencer(int addr) {
        if (featOkTst && casFeat_Experiencer == null)
      jcas.throwFeatMissing("Experiencer", "gov.va.vinci.leo.context.types.Context");
    return ll_cas.ll_getStringValue(addr, casFeatCode_Experiencer);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setExperiencer(int addr, String v) {
        if (featOkTst && casFeat_Experiencer == null)
      jcas.throwFeatMissing("Experiencer", "gov.va.vinci.leo.context.types.Context");
    ll_cas.ll_setStringValue(addr, casFeatCode_Experiencer, v);}
    
  
 
  /** @generated */
  final Feature casFeat_ExperiencerPattern;
  /** @generated */
  final int     casFeatCode_ExperiencerPattern;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getExperiencerPattern(int addr) {
        if (featOkTst && casFeat_ExperiencerPattern == null)
      jcas.throwFeatMissing("ExperiencerPattern", "gov.va.vinci.leo.context.types.Context");
    return ll_cas.ll_getStringValue(addr, casFeatCode_ExperiencerPattern);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setExperiencerPattern(int addr, String v) {
        if (featOkTst && casFeat_ExperiencerPattern == null)
      jcas.throwFeatMissing("ExperiencerPattern", "gov.va.vinci.leo.context.types.Context");
    ll_cas.ll_setStringValue(addr, casFeatCode_ExperiencerPattern, v);}
    
  
 
  /** @generated */
  final Feature casFeat_Negation;
  /** @generated */
  final int     casFeatCode_Negation;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getNegation(int addr) {
        if (featOkTst && casFeat_Negation == null)
      jcas.throwFeatMissing("Negation", "gov.va.vinci.leo.context.types.Context");
    return ll_cas.ll_getStringValue(addr, casFeatCode_Negation);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setNegation(int addr, String v) {
        if (featOkTst && casFeat_Negation == null)
      jcas.throwFeatMissing("Negation", "gov.va.vinci.leo.context.types.Context");
    ll_cas.ll_setStringValue(addr, casFeatCode_Negation, v);}
    
  
 
  /** @generated */
  final Feature casFeat_NegationPattern;
  /** @generated */
  final int     casFeatCode_NegationPattern;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getNegationPattern(int addr) {
        if (featOkTst && casFeat_NegationPattern == null)
      jcas.throwFeatMissing("NegationPattern", "gov.va.vinci.leo.context.types.Context");
    return ll_cas.ll_getStringValue(addr, casFeatCode_NegationPattern);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setNegationPattern(int addr, String v) {
        if (featOkTst && casFeat_NegationPattern == null)
      jcas.throwFeatMissing("NegationPattern", "gov.va.vinci.leo.context.types.Context");
    ll_cas.ll_setStringValue(addr, casFeatCode_NegationPattern, v);}
    
  
 
  /** @generated */
  final Feature casFeat_Temporality;
  /** @generated */
  final int     casFeatCode_Temporality;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getTemporality(int addr) {
        if (featOkTst && casFeat_Temporality == null)
      jcas.throwFeatMissing("Temporality", "gov.va.vinci.leo.context.types.Context");
    return ll_cas.ll_getStringValue(addr, casFeatCode_Temporality);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setTemporality(int addr, String v) {
        if (featOkTst && casFeat_Temporality == null)
      jcas.throwFeatMissing("Temporality", "gov.va.vinci.leo.context.types.Context");
    ll_cas.ll_setStringValue(addr, casFeatCode_Temporality, v);}
    
  
 
  /** @generated */
  final Feature casFeat_TemporalityPattern;
  /** @generated */
  final int     casFeatCode_TemporalityPattern;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getTemporalityPattern(int addr) {
        if (featOkTst && casFeat_TemporalityPattern == null)
      jcas.throwFeatMissing("TemporalityPattern", "gov.va.vinci.leo.context.types.Context");
    return ll_cas.ll_getStringValue(addr, casFeatCode_TemporalityPattern);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setTemporalityPattern(int addr, String v) {
        if (featOkTst && casFeat_TemporalityPattern == null)
      jcas.throwFeatMissing("TemporalityPattern", "gov.va.vinci.leo.context.types.Context");
    ll_cas.ll_setStringValue(addr, casFeatCode_TemporalityPattern, v);}
    
  
 
  /** @generated */
  final Feature casFeat_Window;
  /** @generated */
  final int     casFeatCode_Window;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public int getWindow(int addr) {
        if (featOkTst && casFeat_Window == null)
      jcas.throwFeatMissing("Window", "gov.va.vinci.leo.context.types.Context");
    return ll_cas.ll_getRefValue(addr, casFeatCode_Window);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setWindow(int addr, int v) {
        if (featOkTst && casFeat_Window == null)
      jcas.throwFeatMissing("Window", "gov.va.vinci.leo.context.types.Context");
    ll_cas.ll_setRefValue(addr, casFeatCode_Window, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public Context_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_Experiencer = jcas.getRequiredFeatureDE(casType, "Experiencer", "uima.cas.String", featOkTst);
    casFeatCode_Experiencer  = (null == casFeat_Experiencer) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Experiencer).getCode();

 
    casFeat_ExperiencerPattern = jcas.getRequiredFeatureDE(casType, "ExperiencerPattern", "uima.cas.String", featOkTst);
    casFeatCode_ExperiencerPattern  = (null == casFeat_ExperiencerPattern) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_ExperiencerPattern).getCode();

 
    casFeat_Negation = jcas.getRequiredFeatureDE(casType, "Negation", "uima.cas.String", featOkTst);
    casFeatCode_Negation  = (null == casFeat_Negation) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Negation).getCode();

 
    casFeat_NegationPattern = jcas.getRequiredFeatureDE(casType, "NegationPattern", "uima.cas.String", featOkTst);
    casFeatCode_NegationPattern  = (null == casFeat_NegationPattern) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_NegationPattern).getCode();

 
    casFeat_Temporality = jcas.getRequiredFeatureDE(casType, "Temporality", "uima.cas.String", featOkTst);
    casFeatCode_Temporality  = (null == casFeat_Temporality) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Temporality).getCode();

 
    casFeat_TemporalityPattern = jcas.getRequiredFeatureDE(casType, "TemporalityPattern", "uima.cas.String", featOkTst);
    casFeatCode_TemporalityPattern  = (null == casFeat_TemporalityPattern) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_TemporalityPattern).getCode();

 
    casFeat_Window = jcas.getRequiredFeatureDE(casType, "Window", "uima.tcas.Annotation", featOkTst);
    casFeatCode_Window  = (null == casFeat_Window) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Window).getCode();

  }
}



    