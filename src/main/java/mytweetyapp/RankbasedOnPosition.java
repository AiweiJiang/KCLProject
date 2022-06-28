package mytweetyapp;

import java.util.ArrayList;
import java.util.Collections;

/**
 * This class mainly implements sorting by position, which means the optimal solution can be obtained by
 * combining different perferpences into a common perference.
 * 
 * @author Jiang Aiwei
 *
 */

public class RankbasedOnPosition {
	ArrayList<VauleArgument> argList = new ArrayList<>();
	
	
	public RankbasedOnPosition(ArrayList<VauleArgument> inargList) {
		argList = inargList;
	}
	
	public Medicine bestMedicine() {
		// create objects of medcines and add them to an arralist
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
		
		// Initializes an array of different attributes for later sorting
		ArrayList<Integer> mperformance = new ArrayList<>();
		ArrayList<Integer> mprice = new ArrayList<>();
		ArrayList<Integer> msideEffect = new ArrayList<>();
		ArrayList<Medicine> accMedcines = new ArrayList<>();
		ArrayList<Double> finalMark = new ArrayList<>();
		// Identify the medicines passed in and add the medicines' property to the array
		for (VauleArgument va: argList) {
			for (Medicine med: medcineList) {
				if (va.getAction().equals(med.name)) {
					accMedcines.add(med);
					mperformance.add(med.performance);
					mprice.add(med.price);
					msideEffect.add(med.sideEffect);
				}
			}
		}
		// Sort the Performance array from smallest to largest
		// and the Price and Side Effect arrays from largest to smallest
		Collections.sort(mperformance);
		Collections.sort(mprice);
		Collections.sort(msideEffect);
		Collections.reverse(mprice);
		Collections.reverse(msideEffect);
		// Assignment based on location,The higher the performance value is, the higher the value is assigned; 
		// the higher the price and side effect value is, the lower the value is assigned
		for(int i = 0;i<mperformance.size();i++) {
			for(Medicine med: accMedcines) {
				if(mperformance.get(i) == med.performance) {
					med.finalMark = med.finalMark + i;
				}
				if(mprice.get(i) == med.price) {
					med.finalMark = med.finalMark + i;
				}
				if(msideEffect.get(i) == med.sideEffect) {
					med.finalMark = med.finalMark + i;
				}
			}
		}
		// Returns the medicine with the highest total score
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
