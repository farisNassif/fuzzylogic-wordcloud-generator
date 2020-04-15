package ie.gmit.sw.ai.fuzzylogic;

import ie.gmit.sw.ai.search.Node;
import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.rule.Variable;

public class BestFirstFuzzy {
	/* Scores the url based on how relevant it is */
	public static double UrlRelevance(Node child, String titleData, String headingsData, String paragraphData,
			String query) {
		double occuranceScore = 0;
		final int URL_WEIGHT = 150;
		final int TITLE_WEIGHT = 85;
		final int HEADING_WEIGHT = 45;
		final int PARAGRAPH_WEIGHT = 10;

		int depth = 0;

		/* For BFS, anything with a depth over 3 is abyssmal */
		if (child.getDepth() > 3) {
			depth = 3;
		} else {
			depth = child.getDepth();
		}

		/* Aware this will clog everything, will optimize if time towards end if time */

		/* If the URL itself contains the query word */
		if (child.getUrl().contains(query)) {
			occuranceScore += URL_WEIGHT;
		}

		String[] iterable_contents = titleData.split(" ");
		/* Score Title Text */
		for (String word : iterable_contents) {
			if (word.contains(query)) {
				occuranceScore += TITLE_WEIGHT;
			}
		}

		iterable_contents = headingsData.split(" ");
		/* Score Heading Text */
		for (String word : iterable_contents) {
			if (word.contains(query)) {
				occuranceScore += HEADING_WEIGHT;
			}
		}

		iterable_contents = paragraphData.split(" ");
		/* Score Paragraph Text */
		for (String word : iterable_contents) {
			if (word.contains(query)) {
				occuranceScore += PARAGRAPH_WEIGHT;
			}
		}

		/* Get a handle on the fcl file */
		FIS fis = FIS.load("res/BFS_Fuzzy.fcl", true);
		FunctionBlock fb = fis.getFunctionBlock("urlrelevance");

		/* Set fuzzy variables */
		fis.setVariable("occurance", Math.log(occuranceScore));
		fis.setVariable("depth", depth);

		/* Evaluate function */
		fis.evaluate();

		/* Get the relevance from fuzzy */
		Variable relevance = fb.getVariable("relevance");

		/* Return back the defuzzified value */
		return relevance.getLatestDefuzzifiedValue();
	}
}
