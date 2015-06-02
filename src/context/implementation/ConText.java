package context.implementation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Class used to analyze concept context (based on the 'ConText' algorithm by Chapman et al.)
 * @author Julien Thibault, Stephane Meystre, Oscar Ferrandez-Escamez
 * Department of Biomedical Informatics, University of Utah, 2011
 */
public class ConText {

	public enum NegationContext{
		Affirmed, Negated, Possible;
	}
	public enum TemporalityContext{
		Recent, Historical, Hypothetical;
	}
	
	private static final int MAX_WINDOW = 15;
	
	String[] regexes = new String[]{"absence of ,pre,neg","adequate to rule her out ,pre,neg","adequate to rule him out ,pre,neg","adequate to rule out ,pre,neg","adequate to rule the patient out ,pre,neg","although ,termin,neg","any other,pre,neg","apart from ,termin,neg","are ruled out ,post,neg","as a cause for ,termin,neg","as a cause of ,termin,neg","as a etiology for ,termin,neg","as a etiology of ,termin,neg","as a reason for ,termin,neg","as a reason of ,termin,neg","as a secondary cause for ,termin,neg","as a secondary cause of ,termin,neg","as a secondary etiology for ,termin,neg","as a secondary etiology of ,termin,neg","as a secondary origin for ,termin,neg","as a secondary origin of ,termin,neg","as a secondary reason for ,termin,neg","as a secondary reason of ,termin,neg","as a secondary source for ,termin,neg","as a secondary source of ,termin,neg","as a source for ,termin,neg","as a source of ,termin,neg","as an cause for ,termin,neg","as an cause of ,termin,neg","as an etiology for ,termin,neg","as an etiology of ,termin,neg","as an origin for ,termin,neg","as an origin of ,termin,neg","as an reason for ,termin,neg","as an reason of ,termin,neg","as an secondary cause for ,termin,neg","as an secondary cause of ,termin,neg","as an secondary etiology for ,termin,neg","as an secondary etiology of ,termin,neg","as an secondary origin for ,termin,neg","as an secondary origin of ,termin,neg","as an secondary reason for ,termin,neg","as an secondary reason of ,termin,neg","as an secondary source for ,termin,neg","as an secondary source of ,termin,neg","as an source for ,termin,neg","as an source of ,termin,neg","as has,termin,neg","as needed,pre,hypo","as the cause for ,termin,neg","as the cause of ,termin,neg","as the etiology for ,termin,neg","as the etiology of ,termin,neg","as the origin for ,termin,neg","as the origin of ,termin,neg","as the reason for ,termin,neg","as the reason of ,termin,neg","as the secondary cause for ,termin,neg","as the secondary cause of ,termin,neg","as the secondary etiology for ,termin,neg","as the secondary etiology of ,termin,neg","as the secondary origin for ,termin,neg","as the secondary origin of ,termin,neg","as the secondary reason for ,termin,neg","as the secondary reason of ,termin,neg","as the secondary source for ,termin,neg","as the secondary source of ,termin,neg","as the source for ,termin,neg","as the source of ,termin,neg","as well as any,pre,neg","aside from ,termin,neg","aunt,pre,exp","aunt's,pre,exp","be ruled out ,post,poss","be ruled out for ,pre,poss","because,termin,hypo","being ruled out ,post,poss","brother,pre,exp","brother's,pre,exp","but ,termin,neg","can be ruled out ,post,poss","can be ruled out for ,pre,poss","can rule her out ,pre,neg","can rule her out against ,pre,neg","can rule her out for ,pre,neg","can rule him out ,pre,neg","can rule him out against ,pre,neg","can rule him out for ,pre,neg","can rule out ,pre,neg","can rule out against ,pre,neg","can rule out for ,pre,neg","can rule the patient out ,pre,neg","can rule the patinet out against ,pre,neg","can rule the patinet out for ,pre,neg","cannot ,pre,neg","cause for ,termin,neg","cause of ,termin,neg","causes for ,termin,neg","causes of ,termin,neg","checked for ,pre,neg","clear of,pre,neg","come back for,pre,hypo","come back to,pre,hypo","complains,termin,histexp","could be ruled out ,post,poss","could be ruled out for ,pre,poss","currently,termin,histexp","dad,pre,exp","dad's,pre,exp","declined ,pre,neg","declines ,pre,neg","denied ,pre,neg","denies ,pre,neg","denying ,pre,neg","did not rule out ,post,poss","did rule her out ,pre,neg","did rule her out against ,pre,neg","did rule her out for ,pre,neg","did rule him out ,pre,neg","did rule him out against ,pre,neg","did rule him out for ,pre,neg","did rule out ,pre,neg","did rule out against ,pre,neg","did rule out for ,pre,neg","did rule the patient out ,pre,neg","did rule the patient out against ,pre,neg","did rule the patient out for ,pre,neg","doesn't look like,pre,neg","ED,termin,hist","emergency department,termin,hist","etiology for ,termin,neg","etiology of ,termin,neg","evaluate for ,pre,neg","except ,termin,neg","fails to reveal ,pre,neg",
			"family,pre,exp","fam hx,pre,exp","father,pre,exp","father's,pre,exp","free ,post,neg","free of ,pre,neg","gram negative ,pseudo,neg","grandfather,pre,exp","grandfather's,pre,exp","grandmother,pre,exp","grandmother's,pre,exp","has been negative,post,neg","has been ruled out ,post,neg","have been ruled out ,post,neg","her,termin,hypoexp","his,termin,hypoexp",
			"history,pre,hist","history and,pseudo,hist","history and examination,pseudo,hist","history and physical,pseudo,hist","history for,pseudo,hist","history of chief complaint,pseudo,hist","history of present illness,pseudo,hist","history taking,pseudo,hist","\"history, physical\",pseudo,hist","however ,termin,neg","if,pre,hypo",
	                              "if negative,pseudo,hypo",
	                              "inconsistent with,pre,neg",
	                              "is not,pre,neg",
	                              "is ruled out ,post,neg",
	                              "is to be ruled out ,post,poss",
	                              "is to be ruled out for ,pre,poss",
	                              "isn't,pre,neg",
	                              "lack of,pre,neg",
	                              "lacked,pre,neg",
	                              "may be ruled out ,post,poss",
	                              "may be ruled out for ,pre,poss",
	                              "might be ruled out ,post,poss",
	                              "might be ruled out for ,pre,poss",
	                              "mom,pre,exp",
	                              "mom's,pre,exp",
	                              "mother,pre,exp",
	                              "mother's,pre,exp",
	                              "must be ruled out ,post,poss",
	                              "must be ruled out for ,pre,poss",
	                              "negative for ,pre,neg",
	                              "never developed ,pre,neg",
	                              "never had ,pre,neg",
	                              "nevertheless ,termin,neg",
	                              "no ,pre,neg",
	                              "no abnormal ,pre,neg",
	                              "no cause of ,pre,neg",
	                              "no change ,pseudo,neg",
	                              "no complaints of ,pre,neg",
	                              "no definite change ,pseudo,neg",
	                              "no evidence ,pre,neg",
	                              "no evidence to suggest ,pre,neg",
	                              "no findings of ,pre,neg",
	                              "no findings to indicate ,pre,neg",
	                              "no history of,pre,neg",
	                              "no increase ,pseudo,neg",
	                              "no interval change ,pseudo,neg",
	                              "no longer present,post,neg",
	                              "no mammographic evidence of ,pre,neg",
	                              "no new ,pre,neg",
	                              "no new evidence ,pre,neg",
	                              "no other evidence ,pre,neg",
	                              "no radiographic evidence of ,pre,neg",
	                              "no sign of ,pre,neg",
	                              "no significant ,pre,neg",
	                              "no significant change ,pseudo,neg",
	                              "no significant interval change ,pseudo,neg",
	                              "no signs of ,pre,neg",
	                              "no suggestion of ,pre,neg",
	                              "no suspicious ,pre,neg",
	                              "no suspicious change ,pseudo,neg",
	                              "non diagnostic,post,neg",
	                              "not ,pre,neg",
	                              "not appear ,pre,neg",
	                              "not appreciate ,pre,neg",
	                              "not associated with ,pre,neg",
	                              "not been ruled out ,post,poss",
	                              "not cause ,pseudo,neg",
	                              "not certain if ,pseudo,neg",
	                              "not certain whether ,pseudo,neg",
	                              "not complain of ,pre,neg",
	                              "not demonstrate ,pre,neg",
	                              "not drain ,pseudo,neg",
	                              "not exhibit ,pre,neg",
	                              "not extend ,pseudo,neg",
	                              "not feel ,pre,neg",
	                              "not had ,pre,neg",
	                              "not have ,pre,neg",
	                              "not have evidence of,pre,neg",
	                              "not know of ,pre,neg",
	                              "not known to have ,pre,neg",
	                              "not necessarily ,pseudo,neg",
	                              "not on,pseudo,neg",
	                              "not only ,pseudo,neg",
	                              "not reveal ,pre,neg",
	                              "not ruled out ,post,poss",
	                              "not see ,pre,neg",
	                              "not to be ,pre,neg",
	                              "noted,termin,histexp",
	                              "now resolved,post,neg",
	                              "origin for ,termin,neg",
	                              "origin of ,termin,neg",
	                              "origins for ,termin,neg",
	                              "origins of ,termin,neg",
	                              "other possibilities of ,termin,neg",
	                              "ought to be ruled out ,post,poss",
	                              "ought to be ruled out for ,pre,poss",
	                              "past history,pre,hist",
	                              "past medical history,pre,hist",
	                              "patient,termin,hypoexp",
	                              "patient was not ,pre,neg",
	                              "patient's,termin,hypoexp",
	                              "poor history,pseudo,hist",
	                              "presenting,termin,histexp",
	                              "presents,termin,histexp",
	                              "previous,one,hist",
	                              "prophylaxis,post,neg",
	                              "r/o ,pre,poss",
	                              "rather than ,pre,neg",
	                              "reason for ,termin,neg",
	                              "reason of ,termin,neg",
	                              "reasons for ,termin,neg",
	                              "reasons of ,termin,neg",
	                              "reported,termin,histexp",
	                              "reports,termin,histexp",
	                              "resolved ,pre,neg",
	                              "return,pre,hypo",
	                              "ro ,pre,poss",
	                              "rule her out ,pre,poss",
	                              "rule her out for ,pre,poss",
	                              "rule him out ,pre,poss",
	                              "rule him out for ,pre,poss",
	                              "rule out ,pre,poss",
	                              "rule out for ,pre,poss",
	                              "rule the patient out ,pre,poss",
	                              "rule the patinet out for ,pre,poss",
	                              "ruled her out ,pre,neg",
	                              "ruled her out against ,pre,neg",
	                              "ruled her out for ,pre,neg",
	                              "ruled him out ,pre,neg",
	                              "ruled him out against ,pre,neg",
	                              "ruled him out for ,pre,neg",
	                              "ruled out ,pre,neg",
	                              "ruled out against ,pre,neg",
	                              "ruled out for ,pre,neg",
	                              "ruled the patient out ,pre,neg",
	                              "ruled the patient out against ,pre,neg",
	                              "ruled the patient out for ,pre,neg",
	                              "rules her out ,pre,neg",
	                              "rules her out for ,pre,neg",
	                              "rules him out ,pre,neg",
	                              "rules him out for ,pre,neg",
	                              "rules out ,pre,neg",
	                              "rules out for ,pre,neg",
	                              "rules the patient out ,pre,neg",
	                              "rules the patient out for ,pre,neg",
	                              "secondary,termin,neg",
	                              "secondary to,termin,neg",
	                              "should be ruled out ,post,poss",
	                              "should be ruled out for ,pre,poss",
	                              "should he,pre,hypo",
	                              "should she,pre,hypo",
	                              "should the patient,pre,hypo",
	                              "should there,pre,hypo",
	                              "since,termin,hypo",
	                              "sister,pre,exp",
	                              "sister's,pre,exp",
	                              "social history,pseudo,hist",
	                              "source for ,termin,neg",
	                              "source of ,termin,neg",
	                              "sources for ,termin,neg",
	                              "sources of ,termin,neg",
	                              "states,termin,histexp",
	                              "still ,termin,neg",
	                              "sudden onset of,pseudo,hist",
	                              "sufficient to rule her out ,pre,neg",
	                              "sufficient to rule her out against ,pre,neg",
	                              "sufficient to rule her out for ,pre,neg",
	                              "sufficient to rule him out ,pre,neg",
	                              "sufficient to rule him out against ,pre,neg",
	                              "sufficient to rule him out for ,pre,neg",
	                              "sufficient to rule out ,pre,neg",
	                              "sufficient to rule out against ,pre,neg",
	                              "sufficient to rule out for ,pre,neg",
	                              "sufficient to rule the patient out ,pre,neg",
	                              "sufficient to rule the patient out against ,pre,neg",
	                              "sufficient to rule the patient out for ,pre,neg",
	                              "test for ,pre,neg",
	                              "though ,termin,neg",
	                              "to exclude ,pre,neg",
	                              "today,termin,histexp",
	                              "trigger event for ,termin,neg",
	                              "uncle,pre,exp",
	                              "uncle's,pre,exp",
	                              "unlikely ,post,neg",
	                              "unremarkable for ,pre,neg",
	                              "was found,termin,histexp",
	                              "was negative,post,neg",
	                              "was not,pre,neg",
	                              "was ruled out ,post,neg",
	                              "wasn't,pre,neg",
	                              "what must be ruled out is ,pre,poss",
	                              "which,termin,exp",
	                              "who,termin,hypoexp",
	                              "will be ruled out ,post,poss",
	                              "will be ruled out for ,pre,poss",
	                              "with no ,pre,neg",
	                              "without ,pre,neg",
	                              "without any evidence of ,pre,neg",
	                              "without difficulty ,pseudo,neg",
	                              "without evidence ,pre,neg",
	                              "without indication of ,pre,neg",
	                              "without sign of ,pre,neg",
	                              "yet ,termin,neg"};
	
	private Pattern regexPseudo;
	
	private Pattern regexNegPre;
	private Pattern regexNegPost;
	private Pattern regexPossPre;
	private Pattern regexPossPost;
	private Pattern regexNegEnd;
	
	private Pattern regexExpPre;
	private Pattern regexExpEnd;
	
	private Pattern regexHypoPre;
	private Pattern regexHypoEnd;
	private Pattern regexHypoExpEnd;
	
	private Pattern regexHistPre;
	private Pattern regexHist1w;
	private Pattern regexHistEnd;
	private Pattern regexHistExpEnd;
	
	private Pattern regexTime;
	private Pattern regexTimeFor;
	private Pattern regexTimeSince;
	
	//originally this pattern recognized UMLS concepts, but for this application
	//it will recognize the input concepts
	private static final String regExUmlsTag = "\\[\\d+\\]"; 

	
	/**
	 * Initialization regex (load parameters)
	 * @throws IOException 
	 */
	public ConText() 
	{	
		
		String regex_PSEUDO = "";
		String regex_NEG_PRE = "";
		String regex_NEG_POST = "";
		String regex_POSS_PRE = "";
		String regex_POSS_POST = "";
		String regex_NEG_END = "";
		
		String regex_EXP_PRE = "";
		String regex_EXP_END = "";
		
		String regex_HYPO_PRE = "";
		String regex_HIST_PRE = "";
		String regex_HIST_1W = "";
		
		String regex_HYPO_END = "";
		String regex_HIST_END = "";
		String regex_HIST_EXP_END = "";
		String regex_HYPO_EXP_END = "";
		
		for (int i=0; i < regexes.length; i++)
		{
			int attrIndex = regexes[i].indexOf(',');
			int attrIndex2 = regexes[i].lastIndexOf(',');
			
			String phrase = regexes[i].substring(0,attrIndex).replaceAll(" ", "[\\\\s\\\\-]");
			String position = regexes[i].substring(attrIndex+1, attrIndex2);
			String contextType = regexes[i].substring(attrIndex2+1);
			
			if (position.compareTo("pseudo")==0)
			{
				regex_PSEUDO = regex_PSEUDO + "|" + phrase;
			}
			else if (position.compareTo("termin")==0)
			{
				if (contextType.compareTo("neg")==0)
					regex_NEG_END = regex_NEG_END + "|[\\s\\.]+" + phrase + "[\\s\\.\\:;\\,]+";
				else if (contextType.compareTo("hypo")==0)
					regex_HYPO_END = regex_HYPO_END + "|[\\s\\.]+" + phrase + "[\\s\\.\\:;\\,]+";
				else if (contextType.compareTo("hist")==0)
					regex_HIST_END = regex_HIST_END + "|[\\s\\.]+" + phrase + "[\\s\\.\\:;\\,]+";
				else if (contextType.compareTo("histexp")==0)
					regex_HIST_EXP_END = regex_HIST_EXP_END + "|[\\s\\.]+" + phrase + "[\\s\\.\\:;\\,]+";
				else if (contextType.compareTo("hypoexp")==0)
					regex_HYPO_EXP_END = regex_HYPO_EXP_END + "|[\\s\\.]+" + phrase + "[\\s\\.\\:;\\,]+";
				else if (contextType.compareTo("exp")==0)
					regex_EXP_END = regex_EXP_END + "|[\\s\\.]+" + phrase + "[\\s\\.\\:;\\,]+";
			}
			else if (position.compareTo("pre")==0)
			{
				if (contextType.compareTo("neg")==0)
					regex_NEG_PRE = regex_NEG_PRE + "|[\\s\\.]+" + phrase + "[\\s\\.\\:;\\,]+";
				else if (contextType.compareTo("poss")==0)
					regex_POSS_PRE = regex_POSS_PRE + "|[\\s\\.]+" + phrase + "[\\s\\.\\:;\\,]+";
				else if (contextType.compareTo("hypo")==0)
					regex_HYPO_PRE = regex_HYPO_PRE + "|[\\s\\.]+" + phrase + "[\\s\\.\\:;\\,]+";
				else if (contextType.compareTo("exp")==0)
					regex_EXP_PRE = regex_EXP_PRE + "|[\\s\\.]+" + phrase + "[\\s\\.\\:;\\,]+";
				else if (contextType.compareTo("hist")==0)
					regex_HIST_PRE = regex_HIST_PRE + "|[\\s\\.]+" + phrase + "[\\s\\.\\:;\\,]+";
				else if (contextType.compareTo("hist")==0)
					regex_HIST_1W = regex_HIST_1W + "|[\\s\\.]+" + phrase + "[\\s\\.\\:;\\,]+";
			}
			else if (position.compareTo("post")==0)
			{
				if (contextType.compareTo("neg")==0)
					regex_NEG_POST = regex_NEG_POST + "|[\\s\\.]+" + phrase + "[\\s\\.\\:;\\,]+";
				else if (contextType.compareTo("poss")==0)
					regex_POSS_POST = regex_POSS_POST + "|[\\s\\.]+" + phrase + "[\\s\\.\\:;\\,]+";
			}
			
		}
		if (regex_PSEUDO.length()>0)
			regexPseudo = Pattern.compile(regex_PSEUDO.substring(2));
		
		//negation context
		if (regex_NEG_PRE.length()>0)
			regexNegPre = Pattern.compile(regex_NEG_PRE.substring(1));
		if (regex_NEG_POST.length()>0)
			regexNegPost = Pattern.compile(regex_NEG_POST.substring(1));
		if (regex_NEG_END.length()>0)
			regexNegEnd = Pattern.compile(regex_NEG_END.substring(1));
		if (regex_POSS_PRE.length()>0)
			regexPossPre = Pattern.compile(regex_POSS_PRE.substring(1));
		if (regex_POSS_POST.length()>0)
			regexPossPost = Pattern.compile(regex_POSS_POST.substring(1));
		
		//temporality context
		if (regex_HIST_PRE.length()>0)
			regexHistPre = Pattern.compile(regex_HIST_PRE.substring(1));
		if (regex_HYPO_PRE.length()>0)
			regexHypoPre = Pattern.compile(regex_HYPO_PRE.substring(1));
		if (regex_HIST_1W.length()>0)
			regexHist1w = Pattern.compile(regex_HIST_1W.substring(1));
		if (regex_HIST_END.length()>0)
			regexHistEnd = Pattern.compile(regex_HIST_END.substring(1));
		if (regex_HYPO_END.length()>0)
			regexHypoEnd = Pattern.compile(regex_HYPO_END.substring(1));
		
		//experiencer and mixed
		if (regex_EXP_PRE.length()>0)
			regexExpPre = Pattern.compile(regex_EXP_PRE.substring(1));
		if (regex_EXP_END.length()>0)
			regexExpEnd = Pattern.compile(regex_EXP_END.substring(1));
		if (regex_HYPO_EXP_END.length()>0)
			regexHypoExpEnd = Pattern.compile(regex_HYPO_EXP_END.substring(1));
		if (regex_HIST_EXP_END.length()>0)
			regexHistExpEnd = Pattern.compile(regex_HIST_EXP_END.substring(1));
		
		
		/**********
		System.out.println("-----------------------------------------------------");
		System.out.println("NEGEX CONFIG: ");
		System.out.println("\tMax window: " + MAX_WINDOW + " words");
		System.out.println("\tPRENEG:   " + regex_NEG_PRE.substring(1));
		System.out.println("\tPOSTNEG:  " + regex_NEG_POST.substring(1));
		System.out.println("\tPREPOSS:  " + regex_POSS_PRE.substring(1));
		//System.out.println("POSTPOSS: " + regex_POSS_POST.substring(1));
		System.out.println("\tPSEUDO:   " + regex_PSEUDO.substring(1));
		System.out.println("\tNEGTERM:  " + regex_NEG_END.substring(1));
		System.out.println("\n");
		**********/
	}
	
	/**
	 * Pre-processing on the sentence (replace concepts and negation terms by keywords)
	 * @param sent
	 * @return Tagged sentence (concepts and context base terms)
	 * @throws Exception 
	 */
	private String preProcessSentence(String sent, String concept) throws Exception
	{
		String sentenceTagged = " " + sent.replaceAll("\\s+", " ").toLowerCase();
		
		int lastOffset = 0;
		int charOffset=0;
		
		String tag="";
		String umlsConcept = concept.replaceAll("\\s+", " ").toLowerCase();
		tag = " [0] ";
		
		int conceptIndex = sentenceTagged.indexOf(umlsConcept);
		if (conceptIndex != -1)
		{
			charOffset = conceptIndex;
			sentenceTagged = sentenceTagged.substring(0,charOffset) + tag + sentenceTagged.substring(charOffset+umlsConcept.length());
			lastOffset = charOffset + tag.length();
		}
		else
			return null;
		
		
		//replacing negation phrases with corresponding tags
		
		//negation phrases
		if (regexPseudo != null){
			Matcher m0 = regexPseudo.matcher(sentenceTagged);
			sentenceTagged = m0.replaceAll(" <NEG_PSEUDO> ");
		}
		if (regexNegPre != null){
			Matcher m1 = regexNegPre.matcher(sentenceTagged);
			sentenceTagged = m1.replaceAll(" <NEG_PRE> ");
		}
		if (regexPossPre != null){
			Matcher m2 = regexPossPre.matcher(sentenceTagged);
			sentenceTagged = m2.replaceAll(" <POSS_PRE> ");
		}
		if (regexNegPost != null){
			Matcher m3 = regexNegPost.matcher(sentenceTagged);
			sentenceTagged = m3.replaceAll(" <NEG_POST> ");
		}
		if (regexPossPost != null){
			Matcher m4 = regexPossPost.matcher(sentenceTagged);
			sentenceTagged = m4.replaceAll(" <POSS_POST> ");
		}
		if (regexNegEnd != null){
			Matcher m5 = regexNegEnd.matcher(sentenceTagged);
			sentenceTagged = m5.replaceAll(" <NEG_END> ");
		}
		
		//experiencer phrases
		if (regexExpPre != null){
			Matcher m6 = regexExpPre.matcher(sentenceTagged);
			sentenceTagged = m6.replaceAll(" <EXP_PRE> ");
		}
		if (regexExpEnd != null){
			Matcher m14 = regexExpEnd.matcher(sentenceTagged);
			sentenceTagged = m14.replaceAll(" <EXP_END> ");
		}
		
		//hypothesis
		if (regexHypoPre != null){
			Matcher m7 = regexHypoPre.matcher(sentenceTagged);
			sentenceTagged = m7.replaceAll(" <HYPO_PRE> ");
		}
		if (regexHypoEnd != null){
			Matcher m10 = regexHypoEnd.matcher(sentenceTagged);
			sentenceTagged = m10.replaceAll(" <HYPO_END> ");
		}
		
		//temporality
		if (regexHistPre != null){
			Matcher m8 = regexHistPre.matcher(sentenceTagged);
			sentenceTagged = m8.replaceAll(" <HIST_PRE> ");
		}
		if (regexHist1w != null){
			Matcher m9 = regexHist1w.matcher(sentenceTagged);
			sentenceTagged = m9.replaceAll(" <HIST_1W> ");
		}
		if (regexHistEnd != null){
			Matcher m12 = regexHistEnd.matcher(sentenceTagged);
			sentenceTagged = m12.replaceAll(" <HIST_END> ");
		}
		
		// mixed
		if (regexHypoExpEnd != null){
			Matcher m11 = regexHypoExpEnd.matcher(sentenceTagged);
			sentenceTagged = m11.replaceAll(" <HYPO_EXP_END> ");
		}
		if (regexHistExpEnd != null){
			Matcher m13 = regexHistExpEnd.matcher(sentenceTagged);
			sentenceTagged = m13.replaceAll(" <HIST_EXP_END> ");
		}
		
		//time 
		regexTime = Pattern.compile("((1[4-9]|[1-9]?[2-9][0-9])[ |-][day|days] of)|" +
				"(([2-9]|[1-9][0-9])[ |-][week|weeks] of)|" +
				"(([1-9]?[0-9])[ |-][month|months|year|years] of)");//pattern to recognize expressions of >14 days
		regexTimeFor = Pattern.compile("[for|over] the [last|past] (((1[4-9]|[1-9]?[2-9][0-9])[ |-][day|days] of)|" +
				"(([2-9]|[1-9][0-9])[ |-][week|weeks] of)|" +
				"(([1-9]?[0-9])[ |-][month|months|year|years] of))");//other pattern to recognize expressions of >14 days
		regexTimeSince = Pattern.compile("since [last|the last]? ((([2-9]|[1-9][0-9]) weeks ago)|" +
				"(([1-9]?[0-9])? [month|months|year|years] ago)|" +
				"([january|february|march|april|may|june|july|august|september|october|november|december|spring|summer|fall|winter]))");
		Matcher mTime = regexTimeFor.matcher(sentenceTagged);
		sentenceTagged = mTime.replaceAll(" <TIME_PRE> ");
		mTime = regexTime.matcher(sentenceTagged);
		sentenceTagged = mTime.replaceAll(" <TIME_PRE> ");
		mTime = regexTimeSince.matcher(sentenceTagged);
		sentenceTagged = mTime.replaceAll(" <TIME_POST> ");
		
		return sentenceTagged;
	}
	
	/**
	 * Context analysis on the given sentence.
	 * @param a concept in the sentence
	 * @param sent Sentence to analyze
	 */
	public ArrayList<String> applyContext(String concept, String sentence) throws Exception
	{
		if(concept.equals("") || sentence.equals(""))
			return null;
		
		String tagged = preProcessSentence(sentence, concept);
		
		if(tagged==null)
			return null;
		
		//tokenizing the sentence in words
		String[] words =  tagged.split("[,;\\s]+");
		
		String ne = applyNegEx(words);
		String tmp = applyTemporality(words);
		String subj = applyExperiencer(words);
		
		ArrayList result = new ArrayList();
		result.add(concept); result.add(sentence);
		result.add(ne);result.add(tmp);result.add(subj);
		
		return result;
	}
	
	/**
	 * Apply NegEx algorithm to find negation context of the concepts found in the sentence
	 * @return
	 */
	public String applyNegEx(String[] words) throws Exception
	{
		//Going from one negation to another, and creating the appropriate window
		int m = 0;
		List<String> window = new ArrayList<String>();
		
		//for each word in the sentence
		while (m < words.length)
		{
			//IF word is a pseudo-negation, skips to the next word
			if(words[m].equals("<NEG_PSEUDO>"))
			{
				m++;
			}
			//IF word is a pre- concept negation or possible...
			else if(words[m].matches("<NEG_PRE>|<PREP>"))
			{
				//find window (default is six words after the negation phrase)
				int maxWindow = MAX_WINDOW;
				if (words.length < m + maxWindow) maxWindow = words.length - m;
				for(int o=1; o < maxWindow; o++)
				{
					if(words[m+o].matches("<NEG_PRE>|<PREP>|<NEG_POST>|<POSS_POST>|<NEG_END>"))
						break;
					else window.add(words[m+o]);
				}
				
				//get type of Negation
				NegationContext currentNegationContext = NegationContext.Affirmed;
				if (words[m].equals("<NEG_PRE>")) {
					currentNegationContext = NegationContext.Negated;
				}
				else if(words[m].equals("<POSS_PRE>")) 
					currentNegationContext = NegationContext.Possible;
				
				//check if there are concepts in the window
				for(int w=0; w<window.size(); w++) {
					if(window.get(w).matches(regExUmlsTag)){
						String umlsWord = window.get(w);
						//int index = Integer.parseInt(umlsWord.replaceAll("\\[|\\]",""));
						//mappingResults.get(index).setNegationContext(currentNegationContext.name());
						return currentNegationContext.name();
					}
				}
				window.clear();
				m++;
			}
			//IF word a post- concept negation or possible
			else if(words[m].matches("<NEG_POST>|<POSS_POST>"))
			{
				//find window (default is six words before the negation phrase)
				int maxWindow = MAX_WINDOW;
				if (m < maxWindow) maxWindow = m;
				for(int o=1; o < maxWindow; o++) {
					if(words[m-o].matches("<NEG_PRE>|<POSS_PRE>|<NEG_POST>|<POSS_POST>|<NEG_END>"))
						break;
					else
						window.add(words[m-o]);
				}
				
				//get type of Negation
				NegationContext currentNegationContext = NegationContext.Affirmed;
				if (words[m].equals("<NEG_POST>")){
					currentNegationContext = NegationContext.Negated;
				}
				else if(words[m].equals("<POSS_POST>")) 
					currentNegationContext = NegationContext.Possible;
					
				//check if there are concepts in the window
				for(int w=0; w<window.size(); w++) {
					if(window.get(w).matches(regExUmlsTag)){
						String umlsWord = window.get(w);
						//int index = Integer.parseInt(umlsWord.replaceAll("\\[|\\]",""));
						//mappingResults.get(index).setNegationContext(currentNegationContext.name());
						return currentNegationContext.name();
					}
				}
				window.clear();
				m++;
			}
			//IF word not a negation or conjunction skip
			else{
				m++;
			}
		}
		return NegationContext.Affirmed.name();
	}
	
	/**
	 * Temporality analysis
	 * @return
	 */
	public String applyTemporality(String[] words) throws Exception
	{

		List<String> window = new ArrayList<String>();
		
		
	
		//Going from one temporality term to another, and creating the appropriate window
		int mm = 0;
		while(mm<words.length)
		{
			//IF word is a pseudo-negation, skips to the next word
			if(words[mm].equals("<NEG_PSEUDO>")) mm++;
	
			//IF word is a pre- hypothetical trigger term
			else if(words[mm].equals("<HYPO_PRE>")){

				//expands window until end of sentence, termination term, or other negation/possible trigger term
				for(int o=1; (mm+o)<words.length; o++) {
					if(words[mm+o].equals("<HYPO_END>|<HYPO_EXP_END>|<HYPO_PRE>")) {
						break;//window decreased to right before other negation or conjunction
					}
					else 
						window.add(words[mm+o]);
				}
				//check if there are concepts in the window
				for(int w=0; w<window.size(); w++) {
					if(window.get(w).matches(regExUmlsTag)){
						String umlsWord = window.get(w);
						//int index = Integer.parseInt(umlsWord.replaceAll("\\[|\\]",""));
						//mappingResults.get(index).setTemporalContext(TemporalityContext.Hypothetical.name());
						return TemporalityContext.Hypothetical.name();
					}
				}
				window.clear();
				mm++;
			}
			//IF word a pre- historical trigger term
			else if(words[mm].matches("<HIST_PRE>|<TIME_PRE>")){

				//expands window until end of sentence, termination term, or other negation/possible trigger term
				for(int o=1; (mm+o)<words.length; o++) {
					if(words[mm+o].matches("<HIST_END>|<HIST_EXP_END>|<HIST_PRE>|<HIST_1W>")) {
						break;//window decreased to right after other negation or conjunction
					}
					else window.add(words[mm+o]);
				}
				//check if there are concepts in the window
				for(int w=0; w<window.size(); w++) {
					if(window.get(w).matches(regExUmlsTag)){
						String umlsWord = window.get(w);
						//int index = Integer.parseInt(umlsWord.replaceAll("\\[|\\]",""));
						//mappingResults.get(index).setTemporalContext(TemporalityContext.Historical.name());
						return TemporalityContext.Historical.name();
					}
				}
				window.clear();
				mm++;
			}
			//IF word a post- historical trigger term
			else if(words[mm].equals("<TIME_POST>")){

				//expands window until end of sentence, termination term, or other negation/possible trigger term
				for(int o=1; (mm-o)>=0; o++) {
					if(words[mm-o].matches("<HIST_END>|<HIST_EXP_END>|<HIST_PRE>|<HIST_1W>")) {
						break;//window decreased to right after other negation or conjunction
					}
					else window.add(words[mm-o]);
				}
				//check if there are concepts in the window
				for(int w=0; w<window.size(); w++) {
					if(window.get(w).matches(regExUmlsTag)){
						String umlsWord = window.get(w);
						//int index = Integer.parseInt(umlsWord.replaceAll("\\[|\\]",""));
						//mappingResults.get(index).setTemporalContext(TemporalityContext.Historical.name());
						return TemporalityContext.Historical.name();
					}
				}
				window.clear();
				mm++;
			}
			else mm++;
		}
		return TemporalityContext.Recent.name();
	}
	
	/**
	 * Experiencer analysis
	 * @return
	 */
	public String applyExperiencer(String[] words) throws Exception
	{
		List<String> window = new ArrayList<String>();
		
		//Going from one experiencer term to another, and creating the appropriate window
		int mm = 0;
		while(mm<words.length){
			//IF word is a pseudo-negation, skips to the next word
			if(words[mm].equals("<NEG_PSEUDO>")) mm++;
	
			//IF word is a pre- experiencer trigger term
			else if(words[mm].equals("<EXP_PRE>"))
			{
				//expands window until end of sentence, termination term, or other negation/possible trigger term
				for(int o=1; (mm+o)<words.length; o++) {
					if(words[mm+o].equals("<EXP_END>|<HIST_EXP_END>|<HYPO_EXP_END>|<EXP_PRE>")) {
						break;//window decreased to right before other negation or conjunction
					}
					else window.add(words[mm+o]);
				}
				for(int w=0; w<window.size(); w++) {
					if(window.get(w).matches(regExUmlsTag)){
						String umlsWord = window.get(w); 
						//int index = Integer.parseInt(umlsWord.replaceAll("\\[|\\]",""));
						//mappingResults.get(index).setIsExperiencer(false);
						return "Other";
					}
				}
				window.clear();
				mm++;
			}
			else mm++;
		}
		return "Patient";
	}
	


}
