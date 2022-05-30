package mytweetyapp;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;

import org.tweetyproject.arg.aspic.order.SimpleAspicOrder;
import org.tweetyproject.arg.aspic.parser.AspicParser;
import org.tweetyproject.arg.aspic.reasoner.SimpleAspicReasoner;
import org.tweetyproject.arg.aspic.ruleformulagenerator.FolFormulaGenerator;
import org.tweetyproject.arg.aspic.syntax.AspicArgument;
import org.tweetyproject.arg.aspic.syntax.AspicArgumentationTheory;
import org.tweetyproject.arg.dung.reasoner.AbstractExtensionReasoner;
import org.tweetyproject.arg.dung.semantics.Extension;
import org.tweetyproject.arg.dung.semantics.Semantics;
import org.tweetyproject.arg.dung.syntax.Argument;
import org.tweetyproject.arg.dung.syntax.ArgumentationFramework;
import org.tweetyproject.arg.dung.syntax.Attack;
import org.tweetyproject.arg.dung.syntax.DungTheory;
import org.tweetyproject.commons.InferenceMode;
import org.tweetyproject.commons.ParserException;
import org.tweetyproject.logics.commons.syntax.interfaces.Invertable;
import org.tweetyproject.logics.fol.parser.FolParser;
import org.tweetyproject.logics.fol.syntax.FolFormula;
import org.tweetyproject.logics.fol.syntax.FolSignature;

/**
 * Example code for using ASPIC with first-order-logic formulas.
 * 
 * @author Anna Gessler
 *
 */
public class AspicExampleFol1 {
	public static void main(String[] args) throws ParserException, IOException {
		// FOL Example
		FolParser folparser = new FolParser();
		FolSignature sig = folparser.parseSignature("Person = {alice,bob}\n" + 
				"type(snores(Person))\n" + 
				"type(professor(Person))\n" + 
				"type(accessDenied(Person))\n" + 
				"type(accessAllowed(Person))\n" + 
				"type(misbehaves(Person))");
		folparser.setSignature(sig);
		AspicParser<FolFormula> parser2 = new AspicParser<FolFormula>(folparser, new FolFormulaGenerator());
		parser2.setSymbolComma(";");
		
		AspicArgumentationTheory<FolFormula> at = parser2.parseBeliefBaseFromFile("C:\\Users\\dell\\Desktop\\AspicTest.aspic");		
		SimpleAspicReasoner<FolFormula> ar = new SimpleAspicReasoner<FolFormula>(AbstractExtensionReasoner.getSimpleReasonerForSemantics(Semantics.CONFLICTFREE_SEMANTICS));
		FolFormula pf = (FolFormula)folparser.parseFormula("accessDenied(bob)");
//		SimpleAspicOrder<FolFormula> order = parser2.parseSimpleOrder("C:\\Users\\dell\\Desktop\\AspicTest.aspic");
//		Collection<AspicArgument<FolFormula>> s = at.getArguments();
//		ArrayList<AspicArgument> sarylist = new ArrayList<>();
//		for (AspicArgument aspicarg:at.getArguments()) {
//			sarylist.add(aspicarg);
//		}
//		System.out.println(order.compare(sarylist.get(1), sarylist.get(2)));
		System.out.println(at.getArguments());
		//System.out.println(at.asDungTheory());
		System.out.println(pf + "\t" + ar.query(at,pf,InferenceMode.CREDULOUS));
		DungTheory aaf = at.asDungTheory(false);
        
        ArrayList<Argument> argList = new ArrayList<>();
        for (Argument arg: aaf) {
            System.out.println(arg);
            argList.add(arg);
        }
        System.out.println("The attacks in the system are:");
        for (Attack att: aaf.getAttacks()) {
            System.out.println(att);
        }
        Extension ex = new Extension<>();
        ex.add(argList.get(4));
        
        System.out.println(argList.get(5));
        System.out.println(aaf.isAcceptable(argList.get(2), ex));
	}
}