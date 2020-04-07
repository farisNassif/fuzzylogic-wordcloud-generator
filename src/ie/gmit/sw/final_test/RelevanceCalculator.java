package ie.gmit.sw.final_test;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.rule.Variable;

public class RelevanceCalculator {

	/* Scores the url based on how relevant it is */
	public static void UrlRelevance(String child, String title, String headings, String paragraph, String query) {
		double occuranceScore = 0;

		/* Well aware this will clog up everything, will optimize if time towards end */

		/* If the URL itself contains the query word */
		if (child.contains(query)) {
			occuranceScore += 100;
		}

		String[] iterable_contents = title.split(" ");
		for (String word : iterable_contents) {
			if (word.contains(query)) {
				occuranceScore += 50;
			}
		}

		iterable_contents = headings.split(" ");
		for (String word : iterable_contents) {
			if (word.contains(query)) {
				occuranceScore += 30;
			}
		}

		iterable_contents = paragraph.split(" ");
		for (String word : iterable_contents) {
			if (word.contains(query)) {
				occuranceScore += 5;
			}
		}

		FIS fis = FIS.load("./res/UrlRelevance.fcl", true);
		FunctionBlock fb = fis.getFunctionBlock("urlrelevance");

		fis.setVariable("occurance",Math.log1p(occuranceScore));
		fis.setVariable("depth",1);

		fis.evaluate();
		// Variable relevance = fb.getVariable("relevance");
		// System.out.println(relevance.getLatestDefuzzifiedValue() + "total = " +
		// occuranceScore);

		System.out.println("total = " + occuranceScore);
	}
}
