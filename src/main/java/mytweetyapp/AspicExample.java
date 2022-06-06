package mytweetyapp;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic;
import org.tweetyproject.arg.aspic.order.SimpleAspicOrder;
import org.tweetyproject.arg.aspic.parser.AspicParser;
import org.tweetyproject.arg.aspic.ruleformulagenerator.PlFormulaGenerator;
import org.tweetyproject.arg.aspic.syntax.AspicArgument;
import org.tweetyproject.arg.aspic.syntax.AspicArgumentationTheory;
import org.tweetyproject.arg.aspic.syntax.DefeasibleInferenceRule;
import org.tweetyproject.arg.aspic.syntax.StrictInferenceRule;
import org.tweetyproject.arg.dung.semantics.Extension;
import org.tweetyproject.arg.dung.syntax.Argument;
import org.tweetyproject.arg.dung.syntax.Attack;
import org.tweetyproject.arg.dung.syntax.DungTheory;
import org.tweetyproject.commons.InferenceMode;
import org.tweetyproject.logics.fol.syntax.FolFormula;
import org.tweetyproject.logics.pl.parser.PlParser;
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
		final PlFormulaGenerator pfg = new PlFormulaGenerator();
		AspicParser<PlFormula> parser = new AspicParser<>(new PlParser(), pfg);
		Proposition a = new Proposition("a");
		Proposition b = new Proposition("b");
		Proposition c = new Proposition("c");
		Proposition d = new Proposition("d");
		Proposition e = new Proposition("e");
		
		AspicArgumentationTheory<PlFormula> t = new AspicArgumentationTheory<PlFormula>(new PlFormulaGenerator());
		t.setRuleFormulaGenerator(new PlFormulaGenerator());
		
		DefeasibleInferenceRule<PlFormula> r2 = new DefeasibleInferenceRule<PlFormula>();
		//StrictInferenceRule<PlFormula> r2 = new StrictInferenceRule<PlFormula>(); 
		r2.setConclusion(new Negation(d));
		r2.addPremise(a);
		r2.setName("d3");
		t.addRule(r2);
		
		
		DefeasibleInferenceRule<PlFormula> r1 = new DefeasibleInferenceRule<PlFormula>();
		r1.setConclusion(a);
		r1.addPremise(b);
		r1.addPremise(c);
		r1.setName("d1");
		t.addRule(r1);
		
		r1 = new DefeasibleInferenceRule<PlFormula>();
		r1.setConclusion(d);
		r1.addPremise(b);
		r1.setName("d2");
		t.addRule(r1);
		
//		r1 = new DefeasibleInferenceRule<PlFormula>();
//		r1.setConclusion(new Negation(b));
//		r1.addPremise(e);
//		r1.setName("attack");
//		t.addRule(r1);
		
		t.addOrdinaryPremise(b);
		t.addOrdinaryPremise(c);
//		t.addOrdinaryPremise(e);
//		t.addAxiom(b);
//		t.addAxiom(c);
//		t.addAxiom(e);
		
		List<String> rule = new ArrayList<>();
		rule.add("d1");
		rule.add("d2");
		rule.add("d3");
		
		Comparator<AspicArgument<PlFormula>> order = new SimpleAspicOrder(rule);
		Comparator<AspicArgument<PlFormula>> or = order;
		t.setOrder(or);
		ArrayList<AspicArgument<FolFormula>> argList1 = new ArrayList<>();
		for(AspicArgument arg:t.getArguments()) {
			//System.out.println(arg.toString());
			argList1.add(arg);
		}
		
		//System.out.println(order.compare(argList1.get(3), argList1.get(4)));
		DungTheory aaf = t.asDungTheory();
		
		Extension<DungTheory> Ex1 = new Extension<DungTheory>();
		ArrayList<Argument> argList = new ArrayList<Argument>();
		for(Argument arg: aaf) {
			System.out.println(arg.toString());
			argList.add(arg);
			Ex1.add(arg);
		}
		
		
		System.out.println(aaf.isAcceptable(argList.get(3),Ex1));
		
		System.out.println(argList1.get(3).getConclusion().getClass());
		
//		Argument a1 = new Argument("attack");
//		
//		aaf.add(a1);
//		
//		Attack at = new Attack(a1, argList.get(0));
//		aaf.add(at);
		
		
		for(Attack att: aaf.getAttacks())
			System.out.println(att);	
		
		
	}
}