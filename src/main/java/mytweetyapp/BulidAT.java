package mytweetyapp;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.print.attribute.standard.PrinterLocation;

import org.tweetyproject.arg.aspic.order.SimpleAspicOrder;
import org.tweetyproject.arg.aspic.parser.AspicParser;
import org.tweetyproject.arg.aspic.ruleformulagenerator.PlFormulaGenerator;
import org.tweetyproject.arg.aspic.syntax.AspicArgument;
import org.tweetyproject.arg.aspic.syntax.AspicArgumentationTheory;
import org.tweetyproject.arg.aspic.syntax.DefeasibleInferenceRule;
import org.tweetyproject.arg.aspic.syntax.StrictInferenceRule;
import org.tweetyproject.arg.dung.syntax.Argument;
import org.tweetyproject.arg.dung.syntax.Attack;
import org.tweetyproject.arg.dung.syntax.DungTheory;
import org.tweetyproject.logics.pl.parser.PlParser;
import org.tweetyproject.logics.pl.syntax.Negation;
import org.tweetyproject.logics.pl.syntax.PlFormula;
import org.tweetyproject.logics.pl.syntax.Proposition;

public class BulidAT {
	public BulidAT() {
		
	}
	
	public AspicArgumentationTheory<PlFormula> buildArgT() throws IOException{
		// initialize the inputStream and bufferedReader
		FileInputStream inputStream = new FileInputStream("C:\\Users\\dell\\Desktop\\aspictest.txt");
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		
		// initialize the parser
		final PlFormulaGenerator pfg = new PlFormulaGenerator();
		AspicParser<PlFormula> parser = new AspicParser<>(new PlParser(), pfg);
		
		// Define the nouns that can be used in the knowledge base
		Proposition snores = new Proposition("snores");
		Proposition professor = new Proposition("professor");
		Proposition misbehaves = new Proposition("misbehaves");
		Proposition accessDenied = new Proposition("accessDenied");
		Proposition accessAllowed = new Proposition("accessAllowed");
		Proposition aaa = new Proposition("aaa");
		Proposition bbb = new Proposition("bbb");
		
		ArrayList<Proposition> proList = new ArrayList<>();
		proList.add(snores);
		proList.add(professor);
		proList.add(misbehaves);
		proList.add(accessDenied);
		proList.add(accessAllowed);
		proList.add(aaa);
		proList.add(bbb);
		
		
		AspicArgumentationTheory<PlFormula> t = new AspicArgumentationTheory<PlFormula>(new PlFormulaGenerator());
		t.setRuleFormulaGenerator(new PlFormulaGenerator());
		
		
		
		// read lines from the file and stored into a arrayList
		ArrayList<String> Kbs = new ArrayList<>();
		String str = null;
		while ((str = bufferedReader.readLine()) != null ) {
			Kbs.add(str);
		}
		for(String kb:Kbs) {
			String[] arr = kb.split("\\s+");
			// Parse axtom into the knowledge base
			if(arr[0].equals("=>")) {
				for(Proposition pro:proList) {
					if(arr[1].equals(pro.toString())) {
						t.addAxiom(pro);
					}
				}
			}
			// Parse defeasible rules into the knowledge base
			if((arr[0].toCharArray()[0]=='d' ) && (!arr[1].equals("<"))) {
				// Define the defeasible inference rule
				DefeasibleInferenceRule<PlFormula> r1 = new DefeasibleInferenceRule<PlFormula>();
				String name = "";
				for(int i = 0; i<arr[0].length();i++) {
					if(arr[0].charAt(i)!=':') {
						name += arr[0].charAt(i);
					}
				}
				r1.setName(name);
				int flag = 0;
				for(int i = 0;i < arr.length - 1;i++) {
					if(!arr[i].equals("=>")) {
						flag++;
					}
				}
				
				// add the premise of rules
				String[] premiseList = arr[1].split(";"); 
				for(String premise:premiseList) {
					for(Proposition pro:proList) {
						if(premise.equals(pro.toString())) {
							r1.addPremise(pro);
						}	
					}
				}
				
				// add conclusions of rules
				for(int i=flag+1;i<arr.length;i++) {
					String newStr = "";
					// if the conclusion is negation
					if(arr[i].toCharArray()[0]=='!') {
						for(int j =0; j<arr[i].length(); j++) {
							if(arr[i].charAt(j) != '!') {
								newStr += arr[i].charAt(j);
							}
						}
						for(Proposition pro:proList) {
							if(newStr.equals(pro.toString())) {
								r1.setConclusion(new Negation(pro));
							}
						}
					}else {
						for(Proposition pro:proList) {
							if(arr[i].equals(pro.toString())) {
								r1.setConclusion(pro);
							}
						}
					}
					
				}
				t.add(r1);
			}
			// Parse strict rules into the knowledge base
			if(arr[0].toCharArray()[0]=='s') {
				// Define the Strict Inference Rule
				StrictInferenceRule<PlFormula> r2 = new StrictInferenceRule<PlFormula>();
				String name = "";
				for(int i = 0; i<arr[0].length();i++) {
					if(arr[0].charAt(i)!=':') {
						name += arr[0].charAt(i);
					}
				}
				r2.setName(name);
				int flag = 0;
				for(int i = 0;i < arr.length - 1;i++) {
					if(!arr[i].equals("->")) {
						flag++;
					}
				}
				// add the premises of rules
				
				String[] premiseList = arr[1].split(";");
				for(String premise:premiseList) {
					for(Proposition pro:proList) {
						if(premise.equals(pro.toString())) {
							r2.addPremise(pro);
						}	
					}
				}
				
				// add conclusions of rules
				for(int i=flag+1;i<arr.length;i++) {
					String newStr = "";
					if(arr[i].toCharArray()[0]=='!') {
						for(int j =0; j<arr[i].length(); j++) {
							if(arr[i].charAt(j) != '!') {
								newStr += arr[i].charAt(j);
							}
						}
						for(Proposition pro:proList) {
							if(newStr.equals(pro.toString())) {
								r2.setConclusion(new Negation(pro));
							}
						}
					}else {
						for(Proposition pro:proList) {
							if(arr[i].equals(pro.toString())) {
								r2.setConclusion(pro);
							}
						}
					}
					
				}
				t.add(r2);
			}
			// Parse rule order into the knowledge base
			if(arr[1].equals("<")) {
				List<String> rule = new ArrayList<>();
				for(int i=0;i<arr.length;i++) {
					if(!arr[i].equals("<")) {
						rule.add(arr[i]);
					}
				}
				Comparator<AspicArgument<PlFormula>> order = new SimpleAspicOrder(rule);
				Comparator<AspicArgument<PlFormula>> or = order;
				t.setOrder(or);
			}
		}
		return t;
	}



}
