package mytweetyapp;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

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
import org.tweetyproject.logics.pl.syntax.PlFormula;

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
				"type(misbehaves(Person))\n"+
				"type(aaa(Person))");
		
		folparser.setSignature(sig);
		AspicParser<FolFormula> parser2 = new AspicParser<FolFormula>(folparser, new FolFormulaGenerator());
		SimpleAspicOrder<FolFormula> order = parser2.parseSimpleOrder("d1<d2<d3");
		
		parser2.setSymbolComma(";");
		
		AspicArgumentationTheory<FolFormula> at = parser2.parseBeliefBaseFromFile("C:\\Users\\dell\\Desktop\\AspicTest.aspic");		
		SimpleAspicReasoner<FolFormula> ar = new SimpleAspicReasoner<FolFormula>(AbstractExtensionReasoner.getSimpleReasonerForSemantics(Semantics.CONFLICTFREE_SEMANTICS));
		FolFormula pf = (FolFormula)folparser.parseFormula("accessAllowed(bob)");
		
		Collection<AspicArgument<FolFormula>> arguments = at.getArguments();
		ArrayList<AspicArgument<FolFormula>> argList1 = new ArrayList<>();
		
		
		
		System.out.println(" All Arguments of this argumentation system:");
		for(AspicArgument<FolFormula> arg: at.getArguments()) {
			argList1.add(arg);
			
		}
//		System.out.println("111");
//		
//		System.out.println(argList1.get(1));
		
//		Comparator<AspicArgument<FolFormula>> od = at.getOrder();
//		
//		for(int i =0;i<argList1.size();i++) {
//			for(int j = i+1;j<argList1.size()-1;j++) {
//				System.out.println(order.compare(argList1.get(i), argList1.get(j)));
//			}
//		}
//		
		
		System.out.println(pf + "\t" + ar.query(at,pf,InferenceMode.CREDULOUS));
		
		DungTheory aaf = at.asDungTheory();
        
        ArrayList<Argument> argList = new ArrayList<>();
        Extension<DungTheory> ex = new Extension<DungTheory>(arguments);
        
        for (Argument arg: aaf) {
            System.out.println(arg);
            argList.add(arg);
            
            
        }
        System.out.println("The attacks in the system are:");
        
        
        for (Attack att: aaf.getAttacks()) {
            System.out.println(att);
            
        }
   
        System.out.println(aaf.isAcceptable(argList.get(4), ex));
	}
}