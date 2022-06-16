package mytweetyapp;

/**
 * This class describes the different properties of a medicine.
 * One of medicines has the following properities:
 * 1. name.
 * 2. performance which stand for the potency of this medicine and can be used for sepuencing.
 * 3. price which can be used for price ordering.
 * 4. finalMark which stand for the total score of performance and price.
 * 
 * @author Jiang Aiwei
 *
 */

public class Medicine {
	String name;
	int performance;
	int price;
	int finalMark;
	
	public Medicine(String inputname,int inperformance,int inprice) {
		name = inputname;
		performance = inperformance;
		price = inprice;
	}
	
	public String getName() {
		return name;
	}
	
	public int getPerformance() {
		return performance;
	}
	
	public int getPrice() {
		return price;
	}
	
}
