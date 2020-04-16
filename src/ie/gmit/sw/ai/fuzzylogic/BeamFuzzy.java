package ie.gmit.sw.ai.fuzzylogic;

import java.util.Queue;

import ie.gmit.sw.ServiceHandler;
import ie.gmit.sw.ai.search.Node;
import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.rule.Variable;

public class BeamFuzzy {
	/* Scores the url based on how relevant it is */
	public static double UrlRelevance(Node child, Queue<Node> queue, String titleData, String headingsData,
			String paragraphData, String query) {

		final int URL_WEIGHT = 150;
		final int TITLE_WEIGHT = 85;
		final int HEADING_WEIGHT = 45;
		final int PARAGRAPH_WEIGHT = 10;

		/* Scored at a class level with different weights */
		double occuranceScore = 0;
		/* By default parent strength is strong */
		int parentStrength = 2;

		/* If queue isn't empty */
		if (queue.size() > 0) {
			/* If the lowest scoring node in queue is the parent of the child node */
			if (queue.peek().getUrl() == child.getParent().getUrl()) {
				/* Then parent strength is weak */
				parentStrength = 1;
			}
		}

		/* Class level scoring is the same as BFS, felt it gave pretty nice results */

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
		//FIS fis = FIS.load(ServiceHandler.FuzzyBeamFcl.getAbsolutePath(), true);
		FIS fis = FIS.load("res/Beam_Fuzzy.fcl", true);
		FunctionBlock fb = fis.getFunctionBlock("urlrelevance");

		/* Set fuzzy variables */
		fis.setVariable("occurance", Math.log(occuranceScore));
		fis.setVariable("parent", parentStrength);
		fis.setVariable("depth", child.getDepth());

		/* Evaluate function */
		fis.evaluate();

		/* Get the relevance from fuzzy */
		Variable relevance = fb.getVariable("relevance");

		/* Return back the defuzzified value */
		return relevance.getLatestDefuzzifiedValue();
	}
}
