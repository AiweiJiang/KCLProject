package mytweetyapp;

import java.io.IOException;
import java.util.ArrayList;

import org.tweetyproject.arg.aspic.syntax.AspicArgumentationTheory;
import org.tweetyproject.arg.dung.semantics.Extension;
import org.tweetyproject.arg.dung.syntax.Argument;
import org.tweetyproject.arg.dung.syntax.Attack;
import org.tweetyproject.arg.dung.syntax.DungTheory;
import org.tweetyproject.logics.pl.syntax.PlFormula;

public class VauleArgument {
	String action;
	String purpose;
	String value;
	boolean statu;
	boolean isDefeated = false;
	
	public VauleArgument(String action, String purpose, String value, boolean statu) {
		this.action = action;
		this.purpose = purpose;
		this.value = value;
		this.statu = statu;
	}
	
	public String getAction() {
		return action;
	}
	
	public String  getPurpose() {
		return purpose;
	}
	
	public String getValue() {
		return value;
	}
	
	public boolean getStatue() {
		return statu;
	}

	public static void main(String[] args) throws IOException {
		
		ArrayList<Medicine> medcineList = new ArrayList<>();
		medcineList.add(new Medicine("corticosteroidHormone", 8, 900 , 7));
		medcineList.add(new Medicine("albuterol", 6, 1000, 3));
		medcineList.add(new Medicine("inhaledBeta-2Agonists", 7, 400,5));
		medcineList.add(new Medicine("antimuscarinic", 7, 100,1));
		medcineList.add(new Medicine("steroidInhalers", 8, 800,6));
		medcineList.add(new Medicine("bronchodilators", 2, 145,2));
		medcineList.add(new Medicine("codeineLinctus", 3, 500,18));
		medcineList.add(new Medicine("codeinePhosphate", 6, 1500,39));
		medcineList.add(new Medicine("morphineSulfateOralSolution", 9, 2000,27));
		medcineList.add(new Medicine("paracetamol", 7, 250,21));
		medcineList.add(new Medicine("Ibuprofen", 8, 225,41));
		medcineList.add(new Medicine("antibioticSpray", 5, 350,50));
		medcineList.add(new Medicine("digestiveEnzyme", 4, 290,32));
		medcineList.add(new Medicine("lungTransplantation", 10, 100000,89));
		medcineList.add(new Medicine("Pirefenidone", 5, 120,10));
		medcineList.add(new Medicine("nintedanib", 2, 532,24));
		medcineList.add(new Medicine("oxygenTherapy", 6, 999,53));
		medcineList.add(new Medicine("antibiotics", 1, 322,16));
		medcineList.add(new Medicine("moistOxygen", 6, 352,73));
		medcineList.add(new Medicine("tylenol", 9, 654,93));
		medcineList.add(new Medicine("isoniazid", 3, 807,52));
		medcineList.add(new Medicine("rifampin", 6, 235,16));
		medcineList.add(new Medicine("pyrazizamide", 7, 549,73));
		medcineList.add(new Medicine("ethambutol", 3, 293,48));
		medcineList.add(new Medicine("radiotherapy", 6, 526,36));
		medcineList.add(new Medicine("chemotherapy", 3, 937,84));
		medcineList.add(new Medicine("immunotherapy", 7, 829,39));
		medcineList.add(new Medicine("painkiller", 2, 550,38));
		medcineList.add(new Medicine("drinkplenty", 3, 203,64));
		
		Extension<DungTheory> Ex1 = new Extension<DungTheory>();
        // Initialize an instance of BuiltAT that creates argumentation Theory 
        // according to inference rules in the ASPIC document.
        BulidAT bAt = new BulidAT();
        AspicArgumentationTheory < PlFormula > t = bAt.buildArgT();
        DungTheory aaf = t.asDungTheory();
        // Content in argList is acceptable arguments
        ArrayList < Argument > argList = new ArrayList < > ();
        for (Argument arg: aaf) {
        	Ex1.add(arg);
            argList.add(arg);
        }
        ArrayList<Argument> accArgList = new ArrayList<>();
        for(Argument arg:aaf) {
        	if(aaf.isAcceptable(arg,Ex1)) {
        		accArgList.add(arg);
        	}
        }
        
        ArrayList<VauleArgument> VAlist = new ArrayList<>();
        for (Argument accarg: accArgList) {
        	//System.out.println(accarg);
        	String[] argSplit = accarg.toString().split("\\s+");
        	int flag = 0;
			for(String str:argSplit) {
				if(!str.equals("=>")) {
					flag++;
				}else {
					break;
				}
			}
			if (argSplit[flag + 1].trim().equals("cured")) {
				String action = argSplit[1];
				String purpose = argSplit[flag + 1];
				String value = null;
				boolean statu = true;
				
				VauleArgument VA = new VauleArgument(action, purpose, value, statu);
				VAlist.add(VA);
			}
        }
        
        for (Argument accarg: accArgList) {
        	String[] argSplit = accarg.toString().split("\\s+");
        	int flag = 0;
			for(String str:argSplit) {
				if(!str.equals("=>")) {
					flag++;
				}else {
					break;
				}
			}
			for (Medicine med:medcineList) {
				if (argSplit[1].trim().equals(med.name) && !argSplit[flag + 1].trim().equals("cured")) {
					String value = argSplit[flag + 1];
				    for (VauleArgument Varg: VAlist) {
				    	if(Varg.getAction().trim().equals(argSplit[1])) {
				    		Varg.value = value;
				    	}
				    }
				}
			}
        }
        
        for (VauleArgument Varg:VAlist) {
        	System.out.println(Varg.action + " " + Varg.purpose + " " + Varg.value + " " + Varg.isDefeated);
        }
        
        String perference = "sideEffect performance price";
        for (int i = 0; i < VAlist.size(); i++) {
        	for (int j = i+1;j < VAlist.size(); j++) {
        		if(perference.indexOf(VAlist.get(i).value) < perference.indexOf(VAlist.get(j).value)) {
        			VAlist.get(i).isDefeated = true;
        			System.out.println(VAlist.get(j).action + " defeat " + VAlist.get(i).action);
        		}else if (perference.indexOf(VAlist.get(i).value) > perference.indexOf(VAlist.get(j).value)){
        			VAlist.get(j).isDefeated = true;
        			System.out.println(VAlist.get(i).action + " defeat " + VAlist.get(j).action);
        		}
        	}
        }
        
        for (VauleArgument VArg: VAlist) {
        	if(VArg.isDefeated == false) {
        		System.out.println(VArg.action);
        	}
        }
        
        //把这个系统移植到serverhandle类中，然后再设计UI传输过来的preference字符串

	}

}
