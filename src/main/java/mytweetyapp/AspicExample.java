package mytweetyapp;

import java.util.ArrayList;

import org.tweetyproject.arg.aspic.ruleformulagenerator.PlFormulaGenerator;
import org.tweetyproject.arg.aspic.syntax.AspicArgumentationTheory;
import org.tweetyproject.arg.aspic.syntax.DefeasibleInferenceRule;
import org.tweetyproject.arg.dung.syntax.Argument;
import org.tweetyproject.arg.dung.syntax.Attack;
import org.tweetyproject.arg.dung.syntax.DungTheory;
import org.tweetyproject.logics.pl.syntax.Negation;
import org.tweetyproject.logics.pl.syntax.Proposition;
import org.tweetyproject.logics.pl.syntax.PlFormula;

/**
 * ASPIC example code that shows how to construct an ASPIC theory programmatically.
 * 
 * @author Matthias Thimm
 *
 */
public class AspicExample {
	public static void main(String[] args){
		Proposition a = new Proposition("a");
		Proposition b = new Proposition("b");
		Proposition c = new Proposition("c");
		Proposition d = new Proposition("d");
		
		AspicArgumentationTheory<PlFormula> t = new AspicArgumentationTheory<PlFormula>(new PlFormulaGenerator());
		t.setRuleFormulaGenerator(new PlFormulaGenerator());
		
		DefeasibleInferenceRule<PlFormula> r1 = new DefeasibleInferenceRule<PlFormula>();
		r1.setConclusion(a);
		r1.addPremise(b);
		r1.addPremise(c);
		t.addRule(r1);
		
		r1 = new DefeasibleInferenceRule<PlFormula>();
		r1.setConclusion(d);
		r1.addPremise(b);
		t.addRule(r1);
		
		r1 = new DefeasibleInferenceRule<PlFormula>();
		r1.setConclusion(new Negation(d));
		r1.addPremise(a);
		t.addRule(r1);
		
		t.addAxiom(b);
		t.addAxiom(c);
		
		
		System.out.println(t);
		System.out.println();
		
		DungTheory aaf = t.asDungTheory();
		
		ArrayList<Argument> argList = new ArrayList<Argument>();
		for(Argument arg: aaf) {
			//System.out.println(arg.toString());
			argList.add(arg);
		}
		
		System.out.println(argList.get(0).toString());
		
		
		System.out.println();
		
		for(Attack att: aaf.getAttacks())
			System.out.println(att);	
		
	}
}