package mytweetyapp;

import java.io.IOException;

import org.tweetyproject.agents.dialogues.ExecutableDungTheory;
import org.tweetyproject.arg.aspic.parser.AspicParser;
import org.tweetyproject.arg.aspic.reasoner.SimpleAspicReasoner;
import org.tweetyproject.arg.aspic.ruleformulagenerator.FolFormulaGenerator;
import org.tweetyproject.arg.aspic.ruleformulagenerator.PlFormulaGenerator;
import org.tweetyproject.arg.aspic.syntax.AspicArgumentationTheory;
import org.tweetyproject.arg.aspic.syntax.DefeasibleInferenceRule;
import org.tweetyproject.arg.aspic.syntax.InferenceRule;
import org.tweetyproject.arg.dung.reasoner.AbstractExtensionReasoner;
import org.tweetyproject.arg.dung.semantics.Extension;
import org.tweetyproject.arg.dung.semantics.Semantics;
import org.tweetyproject.arg.dung.syntax.Argument;
import org.tweetyproject.arg.dung.syntax.Attack;
import org.tweetyproject.arg.dung.syntax.DungTheory;
import org.tweetyproject.commons.InferenceMode;
import org.tweetyproject.commons.ParserException;
import org.tweetyproject.logics.fol.parser.FolParser;
import org.tweetyproject.logics.fol.syntax.FolFormula;
import org.tweetyproject.logics.fol.syntax.FolSignature;
import org.tweetyproject.logics.pl.syntax.PlFormula;
import org.tweetyproject.logics.pl.syntax.Proposition;

/**
 * Example code for using ASPIC with first-order-logic formulas.
 * 
 * @author Anna Gessler
 *
 */
public class AspicExampleFol {
    public static void main(String[] args) throws ParserException, IOException {
        // FOL Example
        FolParser folparser = new FolParser();
        FolSignature sig = folparser.parseSignature("Person = {alice,bob}\n" +
            "type(rhinorrhea(Person))\n" +
            "type(hyperthermia(Person))\n" +
            "type(cough(Person))\n" +
            "type(lostOfTaste(Person))\n" +
            "type(covidPositeve(Person))\n" +
            "type(covidNegative(Person))\n" +
            "type(muscularStiffness(Person))\n" +
            "type(headache(Person))\n" +
            "type(inappetence(Person))\n" +
            "type(rhinobyon(Person))\n" +
            "type(influenza(Person))\n" +
            "type(sinusitis(Person))\n" +
            "type(fever(Person))\n" +
            "type(covid(Person))\n");
        folparser.setSignature(sig);
        AspicParser < FolFormula > parser2 = new AspicParser < FolFormula > (folparser, new FolFormulaGenerator());
        parser2.setSymbolComma(",");
        //at返回的是可以从defeasible rule推导出来的所有论点
        // "C:\\Users\\dell\\.m2\\repository\\org\\tweetyproject\\arg\\aspic\\1.21\\ex5_fol.aspic"
        AspicArgumentationTheory < FolFormula > at = parser2.parseBeliefBaseFromFile("C:\\Users\\dell\\Desktop\\AspicTest.aspic");
        SimpleAspicReasoner < FolFormula > ar = new SimpleAspicReasoner < FolFormula > (AbstractExtensionReasoner.getSimpleReasonerForSemantics(Semantics.PREFERRED_SEMANTICS));
        FolFormula pf = (FolFormula) folparser.parseFormula("covid(bob)");

        DungTheory aaf = at.asDungTheory();
        System.out.println(aaf);
        // 检测在credulous环境中，pf是否为真
        System.out.println(pf + "\t" + ar.query(at, pf, InferenceMode.CREDULOUS));
        
        
        
        for (Argument arg: aaf)
            System.out.println(arg);
        System.out.println("The attacks in the system are:");
        for (Attack att: aaf.getAttacks()) {
            System.out.println(att);
        }
    }
}