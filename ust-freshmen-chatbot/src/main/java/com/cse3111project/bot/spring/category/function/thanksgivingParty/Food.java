
public class Food {
	private String foodName;
	
	public Food(String n) {
		foodName = n;
	}
	
	public String getFoodName() {
		return foodName;
	}
	
	public boolean compareFoodName(String thatFood) {
		String foodA = thatFood.replaceAll("[^a-zA-Z ]", "").toLowerCase();
		String foodB = this.foodName.replaceAll("[^a-zA-Z ]", "").toLowerCase();
		return foodA.equals(foodB);
	}
	
}

