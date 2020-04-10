package ie.gmit.sw.search.test;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.rule.Variable;

public class BestFirstSearchFuzzy {
	/* Scores the url based on how relevant it is */
	public static double UrlRelevance(Node child, String titleData, String headingsData, String paragraphData,
			String query) {
		double occuranceScore = 0;
		int depth = 0;

		if (child.getDepth() > 3) {
			depth = 3;
		} else {
			depth = child.getDepth();
		}

		/* Well aware this will clog up everything, will optimize if time towards end */

		/* If the URL itself contains the query word */
		if (child.getUrl().contains(query)) {
			occuranceScore += 150;
		}

		String[] iterable_contents = titleData.split(" ");
		/* Score Title Text */
		for (String word : iterable_contents) {
			if (word.contains(query)) {
				occuranceScore += 50;
			}
		}

		iterable_contents = headingsData.split(" ");
		/* Score Heading Text */
		for (String word : iterable_contents) {
			if (word.contains(query)) {
				occuranceScore += 25;
			}
		}

		iterable_contents = paragraphData.split(" ");
		/* Score Paragraph Text */
		for (String word : iterable_contents) {
			if (word.contains(query)) {
				occuranceScore += 5;
			}
		}

		FIS fis = FIS.load("./res/UrlRelevance.fcl", true);
		FunctionBlock fb = fis.getFunctionBlock("urlrelevance");

		fis.setVariable("occurance", Math.log(occuranceScore));
		fis.setVariable("depth", depth);

		fis.evaluate();

		Variable relevance = fb.getVariable("relevance");
		return relevance.getLatestDefuzzifiedValue();

	}
}
