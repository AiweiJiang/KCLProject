package mytweetyapp;

import java.util.ArrayList;
import java.util.Collections;
import org.tweetyproject.arg.dung.syntax.Argument;

/**
 * This class mainly implements sorting by position, which means the optimal solution can be obtained by
 * combining different perferpences into a common perference.
 * 
 * @author Jiang Aiwei
 *
 */

public class RankbasedOnPosition {
	ArrayList<Argument> argList = new ArrayList<>();
	
	
	public RankbasedOnPosition(ArrayList<Argument> inargList) {
		argList = inargList;
	}
	
	public Medicine bestMedicine() {
		// create objects of medcines and add them to an arralist
		ArrayList<Medicine> medcineList = new ArrayList<>();
		medcineList.add(new Medicine("corticosteroidHormone", 8, 900));
		medcineList.add(new Medicine("albuterol", 6, 1000));
		medcineList.add(new Medicine("inhaledBeta-2Agonists", 7, 400));
		medcineList.add(new Medicine("antimuscarinic", 7, 100));
		medcineList.add(new Medicine("steroidInhalers", 8, 800));
		medcineList.add(new Medicine("bronchodilators", 2, 145));
		medcineList.add(new Medicine("codeineLinctus", 3, 500));
		medcineList.add(new Medicine("codeinePhosphate", 6, 1500));
		medcineList.add(new Medicine("morphineSulfateOralSolution", 9, 2000));
		medcineList.add(new Medicine("paracetamol", 7, 250));
		medcineList.add(new Medicine("Ibuprofen", 8, 225));
		medcineList.add(new Medicine("antibioticSpray", 5, 350));
		medcineList.add(new Medicine("digestiveEnzyme", 4, 290));
		medcineList.add(new Medicine("lungTransplantation", 10, 100000));
		medcineList.add(new Medicine("Pirefenidone", 5, 120));
		medcineList.add(new Medicine("nintedanib", 2, 532));
		medcineList.add(new Medicine("oxygenTherapy", 6, 999));
		medcineList.add(new Medicine("antibiotics", 1, 322));
		medcineList.add(new Medicine("moistOxygen", 6, 352));
		medcineList.add(new Medicine("tylenol", 9, 654));
		medcineList.add(new Medicine("isoniazid", 3, 807));
		medcineList.add(new Medicine("rifampin", 6, 235));
		medcineList.add(new Medicine("pyrazizamide", 7, 549));
		medcineList.add(new Medicine("ethambutol", 3, 293));
		medcineList.add(new Medicine("radiotherapy", 6, 526));
		medcineList.add(new Medicine("chemotherapy", 3, 937));
		medcineList.add(new Medicine("immunotherapy", 7, 829));
		medcineList.add(new Medicine("painkiller", 2, 550));
		medcineList.add(new Medicine("drinkplenty", 3, 203));
			
		ArrayList<Integer> mperformance = new ArrayList<>();
		ArrayList<Integer> mprice = new ArrayList<>();
		ArrayList<Medicine> accMedcines = new ArrayList<>();
		ArrayList<Integer> finalMark = new ArrayList<>();
		
		for(Argument arg:argList) {
			String[] argSplit = arg.toString().split("\\s+");
			int flag = 0;
			for(String str:argSplit) {
				if(!str.equals("=>")) {
					flag++;
				}else {
					break;
				}
			}

			for(Medicine med:medcineList) {
				if(argSplit[flag + 1].equals(med.getName())) {
					mperformance.add(med.getPerformance());
					mprice.add(med.getPrice());
					accMedcines.add(med);
				}
			}
		}
		Collections.sort(mperformance);
		Collections.sort(mprice);
		for(int i = 0;i<mperformance.size();i++) {
			for(Medicine med: accMedcines) {
				if(mperformance.get(i) == med.performance) {
					med.finalMark = med.finalMark + i;
				}
				if(mprice.get(i) == med.price) {
					med.finalMark = med.finalMark - i;
				}
			}
		}
		
		for(Medicine med: accMedcines) {
			finalMark.add(med.finalMark);
		}
		Collections.sort(finalMark);
		Medicine finalAnswer = null;
		for(Medicine med: accMedcines) {
			if(finalMark.get(finalMark.size() - 1) == med.finalMark) {
				finalAnswer = med;
			}
		}
		
		return finalAnswer;
	}
	

}
