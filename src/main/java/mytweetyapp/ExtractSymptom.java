package mytweetyapp;

import java.util.ArrayList;

public class ExtractSymptom {
	String sentence = null;
	String[] symtomsArray = {"wheezing","cough","breathlessness",
			"nocturnalSymptoms","seasonalVariations","chestInfection",
			"reducedOxygenSaturation","BlueLips","coldAndPaleSkin",
			"lostOfTaste","reducedUrine","testPositive","testNegative",
			"recurrentInfection","falteringGrowth","malabsorption",
			"azonospermia","acutePancreatitis","meconiumIleus","fatigue",
			"fever","expectoration","rhinorrhea","hemoptysis","chestPain",
			"weightLoss","inappetence","fingerChanges","swallowingPain",
			"hoarseVoice","swellingFace","PersistentChest","fatigus",
			"clubbedFingertrips","heartBeat"};
	
	public ExtractSymptom(String sentence) {
		this.sentence = sentence;
	}
	
	public ArrayList<String> extract(){
		ArrayList<String> symtoms = new ArrayList<>();
		String PureSentence = sentence.replaceAll("\\p{Punct}", "");
		String[] sentenceSplit = PureSentence.split("\\s+");
		for (String str:sentenceSplit) {
			for (String symptom: symtomsArray) {
				if (str.trim().equals(symptom)) {
					symtoms.add("=> "+str);				}
			}
		}
		return symtoms;
		
	}
	
	public static void main(String[] args) {
        ExtractSymptom Es = new ExtractSymptom("I feel that I have wheezing, chestInfection, BlueLips and cough. Oh! I also feel breathlessness and reducedOxygenSaturation");
        ArrayList<String> resultArrayList = new ArrayList<>();
        resultArrayList = Es.extract();
        for (String str: resultArrayList) {
        	System.out.println(str);
        }
    }
	
}
