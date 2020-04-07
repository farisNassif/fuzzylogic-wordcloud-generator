package fuzzytest;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;
import net.sourceforge.jFuzzyLogic.rule.Variable;

public class fuzzytest {

	public static void main(String[] args) {
		double highlyRelevant = 305;
		double somewhatRelevant = 214;
		double barelyRelevant = 213;

		FIS fis = FIS.load("./res/tipper.fcl", true);
		FunctionBlock fb = fis.getFunctionBlock("urlrelevance");

        JFuzzyChart.get().chart(fb);

        // Set inputs
        fis.setVariable("title",10);
        fis.setVariable("heading", 9);
        
		fis.evaluate();
        // Show output variable's chart
        Variable relevance = fb.getVariable("relevance");
        JFuzzyChart.get().chart(relevance, relevance.getDefuzzifier(), true);

        // Print ruleSet
        System.out.println(fis);
        
		
		//Variable relevance = fb.getVariable("tipper");
		
		// System.out.println(relevance.getLatestDefuzzifiedValue());
		System.out.println("Real: " + highlyRelevant + " Log: " + Math.log10(highlyRelevant));
		System.out.println("Real: " + somewhatRelevant + " Log: " + Math.log10(somewhatRelevant));
		System.out.println("Real: " + barelyRelevant + " Log: " + Math.log10(barelyRelevant));
	}
}
