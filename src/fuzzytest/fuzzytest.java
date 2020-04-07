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

		FIS fis = FIS.load("./res/urlrelevance.fcl", true);
		FunctionBlock fb = fis.getFunctionBlock("urlrelevance");

        JFuzzyChart.get().chart(fb);

        // Set inputs
        fis.setVariable("occurance",9);
        fis.setVariable("depth",7);
        
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
		
		
		/* 	IF title IS unpromising AND heading IS inadequate THEN relevance is insubstantial;
	IF title IS soso AND heading IS marginal THEN relevance is borderline;
	IF title IS soso OR heading IS marginal AND 
	
	RULE 1 : IF title IS unpromising AND heading IS inadequate THEN relevance is insubstantial;
	RULE 2 : IF title IS unpromising AND heading IS marginal THEN relevance is insubstantial;
	RULE 3 : IF title IS unpromising AND heading IS adequate THEN relevance is borderline;

	RULE 4 : IF title IS soso AND heading IS inadequate THEN relevance is insubstantial;
	RULE 5 : IF title IS soso AND heading IS marginal THEN relevance is borderline;
	RULE 6 : IF title IS soso AND heading IS adequate THEN relevance is borderline;

	RULE 7 : IF title IS promising AND heading IS inadequate THEN relevance is borderline;
	RULE 8 : IF title IS promising AND heading IS marginal THEN relevance is borderline;
	RULE 9 : IF title IS promising AND heading IS adequate THEN relevance is substantial;*/
		/* 
		 * DEFUZZIFY relevance			
	TERM insubstantial := (0,0) (0,1) (45,0);
	TERM borderline := (15,0) (50,1) (85,0);
	TERM substantial := (55,0) (100, 1) (100,0);

	METHOD : COG;
	DEFAULT := 10;
END_DEFUZZIFY
*/
	}
}