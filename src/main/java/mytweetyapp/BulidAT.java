package mytweetyapp;

import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.tweetyproject.arg.aspic.order.SimpleAspicOrder;
import org.tweetyproject.arg.aspic.parser.AspicParser;
import org.tweetyproject.arg.aspic.ruleformulagenerator.PlFormulaGenerator;
import org.tweetyproject.arg.aspic.syntax.AspicArgument;
import org.tweetyproject.arg.aspic.syntax.AspicArgumentationTheory;
import org.tweetyproject.arg.aspic.syntax.DefeasibleInferenceRule;
import org.tweetyproject.arg.aspic.syntax.StrictInferenceRule;
import org.tweetyproject.logics.pl.parser.PlParser;
import org.tweetyproject.logics.pl.syntax.Negation;
import org.tweetyproject.logics.pl.syntax.PlFormula;
import org.tweetyproject.logics.pl.syntax.Proposition;

/**
 * This class implements the ASPIC parser:
 * 1. Parsing the premise.
 * 2. Parsing the inference rules.
 * 3. Parsing the orders between the inference rules.
 * 
 * @author Jiang Aiwei
 *
 */

public class BulidAT {
    public BulidAT() {

    }

    public AspicArgumentationTheory < PlFormula > buildArgT() throws IOException {
        // initialize the inputStream and bufferedReader
        FileInputStream inputStream = new FileInputStream("C:\\Users\\dell\\Desktop\\aspictest.txt");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        // initialize the parser
        final PlFormulaGenerator pfg = new PlFormulaGenerator();
        AspicParser < PlFormula > parser = new AspicParser < > (new PlParser(), pfg);

        // Define the nouns that can be used in the knowledge base
        ArrayList < Proposition > proList = new ArrayList < > ();
        proList.add(new Proposition("snores"));
        proList.add(new Proposition("professor"));
        proList.add(new Proposition("misbehaves"));
        proList.add(new Proposition("accessDenied"));
        proList.add(new Proposition("accessAllowed"));
        proList.add(new Proposition("aaa"));
        proList.add(new Proposition("bbb"));
        // symptoms
        proList.add(new Proposition("wheezing"));
        proList.add(new Proposition("cough"));
        proList.add(new Proposition("breathlessness"));
        proList.add(new Proposition("nocturnalSymptoms"));
        proList.add(new Proposition("seasonalVariations "));
        proList.add(new Proposition("chestInfection"));
        proList.add(new Proposition("reducedOxygenSaturation"));
        proList.add(new Proposition("BlueLips"));
        proList.add(new Proposition("coldAndPaleSkin"));
        proList.add(new Proposition("lostOfTaste"));
        proList.add(new Proposition("reducedUrine"));
        proList.add(new Proposition("testPositive"));
        proList.add(new Proposition("testNegative"));
        proList.add(new Proposition("recurrentInfection"));
        proList.add(new Proposition("falteringGrowth"));
        proList.add(new Proposition("malabsorption"));
        proList.add(new Proposition("azonospermia"));
        proList.add(new Proposition("acutePancreatitis"));
        proList.add(new Proposition("meconiumIleus"));
        proList.add(new Proposition("fatigue"));
        proList.add(new Proposition("fever"));
        proList.add(new Proposition("expectoration"));
        proList.add(new Proposition("rhinorrhea"));
        proList.add(new Proposition("hemoptysis"));
        proList.add(new Proposition("chestPain"));
        proList.add(new Proposition("weightLoss"));
        proList.add(new Proposition("inappetence"));
        proList.add(new Proposition("fingerChanges"));
        proList.add(new Proposition("swallowingPain"));
        proList.add(new Proposition("hoarseVoice"));
        proList.add(new Proposition("swellingFace"));
        proList.add(new Proposition("PersistentChest"));
        proList.add(new Proposition("fatigus"));
        proList.add(new Proposition("clubbedFingertrips"));
        proList.add(new Proposition("heartBeat"));
        // Disease
        proList.add(new Proposition("asthma"));
        proList.add(new Proposition("obstructivePulmonary"));
        proList.add(new Proposition("COVID"));
        proList.add(new Proposition("cysticFibrosis"));
        proList.add(new Proposition("pulmonaryFibrosis"));
        proList.add(new Proposition("respiratoryInfection"));
        proList.add(new Proposition("Tuberculosis"));
        proList.add(new Proposition("lungCancer"));
        proList.add(new Proposition("mesothelioma"));
        proList.add(new Proposition("pneumonia"));
        // medicine
        proList.add(new Proposition("corticosteroidHormone"));
        proList.add(new Proposition("albuterol"));
        proList.add(new Proposition("inhaledBeta-2Agonists"));
        proList.add(new Proposition("antimuscarinic"));
        proList.add(new Proposition("steroidInhalers"));
        proList.add(new Proposition("bronchodilators"));
        proList.add(new Proposition("codeineLinctus"));
        proList.add(new Proposition("codeinePhosphate"));
        proList.add(new Proposition("morphineSulfateOralSolution"));
        proList.add(new Proposition("paracetamol"));
        proList.add(new Proposition("Ibuprofen"));
        proList.add(new Proposition("antibioticSpray"));
        proList.add(new Proposition("digestiveEnzyme"));
        proList.add(new Proposition("lungTransplantation"));
        proList.add(new Proposition("Pirefenidone"));
        proList.add(new Proposition("nintedanib"));
        proList.add(new Proposition("oxygenTherapy"));
        proList.add(new Proposition("antibiotics"));
        proList.add(new Proposition("moistOxygen"));
        proList.add(new Proposition("tylenol"));
        proList.add(new Proposition("isoniazid"));
        proList.add(new Proposition("rifampin"));
        proList.add(new Proposition("pyrazizamide"));
        proList.add(new Proposition("ethambutol"));
        proList.add(new Proposition("radiotherapy"));
        proList.add(new Proposition("chemotherapy"));
        proList.add(new Proposition("immunotherapy"));
        proList.add(new Proposition("painkiller"));
        proList.add(new Proposition("drinkplenty"));
        
        AspicArgumentationTheory < PlFormula > t = new AspicArgumentationTheory < PlFormula > (new PlFormulaGenerator());
        t.setRuleFormulaGenerator(new PlFormulaGenerator());

        // read lines from the file and stored into a arrayList
        ArrayList < String > Kbs = new ArrayList < > ();
        String str = null;
        while ((str = bufferedReader.readLine()) != null) {
            Kbs.add(str);
        }
        for (String kb: Kbs) {
            String[] arr = kb.split("\\s+");
            // Parse axtom into the knowledge base
            if (arr[0].equals("=>")) {
                for (Proposition pro: proList) {
                    if (arr[1].equals(pro.toString())) {
                        t.addOrdinaryPremise(pro);
                    }
                }
            }
            // Parse defeasible rules into the knowledge base
            if ((arr[0].toCharArray()[0] == 'd') && (!arr[1].equals("<"))) {
                // Define the defeasible inference rule
                DefeasibleInferenceRule < PlFormula > r1 = new DefeasibleInferenceRule < PlFormula > ();
                String name = "";
                for (int i = 0; i < arr[0].length(); i++) {
                    if (arr[0].charAt(i) != ':') {
                        name += arr[0].charAt(i);
                    }
                }
                r1.setName(name);
                int flag = 0;
                for (int i = 0; i < arr.length - 1; i++) {
                    if (!arr[i].equals("=>")) {
                        flag++;
                    }
                }

                // add the premise of rules
                String[] premiseList = arr[1].split(";");
                for (String premise: premiseList) {
                    for (Proposition pro: proList) {
                        if (premise.equals(pro.toString())) {
                            r1.addPremise(pro);
                        }
                    }
                }

                // add conclusions of rules
                for (int i = flag + 1; i < arr.length; i++) {
                    String newStr = "";
                    // if the conclusion is negation
                    if (arr[i].toCharArray()[0] == '!') {
                        for (int j = 0; j < arr[i].length(); j++) {
                            if (arr[i].charAt(j) != '!') {
                                newStr += arr[i].charAt(j);
                            }
                        }
                        for (Proposition pro: proList) {
                            if (newStr.equals(pro.toString())) {
                                r1.setConclusion(new Negation(pro));
                            }
                        }
                    } else {
                        for (Proposition pro: proList) {
                            if (arr[i].equals(pro.toString())) {
                                r1.setConclusion(pro);
                            }
                        }
                    }

                }
                t.add(r1);
            }
            // Parse strict rules into the knowledge base
            if (arr[0].toCharArray()[0] == 's') {
                // Define the Strict Inference Rule
                StrictInferenceRule < PlFormula > r2 = new StrictInferenceRule < PlFormula > ();
                String name = "";
                for (int i = 0; i < arr[0].length(); i++) {
                    if (arr[0].charAt(i) != ':') {
                        name += arr[0].charAt(i);
                    }
                }
                r2.setName(name);
                int flag = 0;
                for (int i = 0; i < arr.length - 1; i++) {
                    if (!arr[i].equals("->")) {
                        flag++;
                    }
                }
                // add the premises of rules

                String[] premiseList = arr[1].split(";");
                for (String premise: premiseList) {
                    for (Proposition pro: proList) {
                        if (premise.equals(pro.toString())) {
                            r2.addPremise(pro);
                        }
                    }
                }

                // add conclusions of rules
                for (int i = flag + 1; i < arr.length; i++) {
                    String newStr = "";
                    if (arr[i].toCharArray()[0] == '!') {
                        for (int j = 0; j < arr[i].length(); j++) {
                            if (arr[i].charAt(j) != '!') {
                                newStr += arr[i].charAt(j);
                            }
                        }
                        for (Proposition pro: proList) {
                            if (newStr.equals(pro.toString())) {
                                r2.setConclusion(new Negation(pro));
                            }
                        }
                    } else {
                        for (Proposition pro: proList) {
                            if (arr[i].equals(pro.toString())) {
                                r2.setConclusion(pro);
                            }
                        }
                    }

                }
                t.add(r2);
            }
            // Parse rule order into the knowledge base
            if (arr[1].equals("<")) {
                List < String > rule = new ArrayList < > ();
                for (int i = 0; i < arr.length; i++) {
                    if (!arr[i].equals("<")) {
                        rule.add(arr[i]);
                    }
                }
                Comparator < AspicArgument < PlFormula >> order = new SimpleAspicOrder(rule);
                Comparator < AspicArgument < PlFormula >> or = order;
                t.setOrder(or);
            }
        }
        bufferedReader.close();
        return t;
    }

}