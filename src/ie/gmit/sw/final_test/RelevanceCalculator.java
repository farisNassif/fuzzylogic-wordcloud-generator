package ie.gmit.sw.final_test;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.rule.Variable;

public class RelevanceCalculator {

	/* Scores the url based on how relevant it is */
	public static void UrlRelevance(String child, String title, String headings, String paragraph, String query) {
		double highlyRelevant = 0;
		double somewhatRelevant = 0;
		double barelyRelevant = 0;

		/* If the URL itself contains the query word */
		if (child.contains(query)) {
			highlyRelevant += 100;
		}

		String[] iterable_contents = title.split(" ");
		for (String word : iterable_contents) {
			highlyRelevant += 50;
		}

		iterable_contents = headings.split(" ");
		for (String word : iterable_contents) {
			somewhatRelevant += 25;
		}

		iterable_contents = paragraph.split(" ");
		for (String word : iterable_contents) {
			barelyRelevant += 3;
		}

		FIS fis = FIS.load("./res/UrlRelevance.fcl", true);
		FunctionBlock fb = fis.getFunctionBlock("relevance");
		
	    fis.setVariable("title",highlyRelevant);
	    fis.setVariable("heading",somewhatRelevant);
	    fis.setVariable("paragraph",barelyRelevant);
	    fis.evaluate();
	    Variable relevance = fb.getVariable("relevance");
	    //System.out.println(relevance.getLatestDefuzzifiedValue());
	    System.out.println("Real: " + highlyRelevant + " Log: " + Math.log10(highlyRelevant));
	    System.out.println("Real: " + somewhatRelevant + " Log: " + Math.log10(somewhatRelevant));
	    System.out.println("Real: " + barelyRelevant + " Log: " + Math.log10(barelyRelevant));
	    
		//System.out.println("high: " + highlyRelevant + " med: " + somewhatRelevant + " low: " + barelyRelevant);
	}
}
