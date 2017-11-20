import com.cse3111project.bot.spring.category.function.timetable.*;

//import com.cse3111project.bot.spring.category.function.timetable.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.*;

public class ThanksgivingParty extends Activity {
	private ArrayList<Food> foodlist;
	
	public ThanksgivingParty() {
		super("Thanksgiving", new Timeslot(0, 23));
		foodlist = new ArrayList<Food>();
	}
	
	public void addNewFood(String newFood) {
		if(foodlist==null)
			foodlist.add(new Food(newFood));
		else {
			Boolean temp = true;
			for (Food loop : foodlist)
			{
				// same name
				if(loop.compareFoodName(newFood))
				{
					temp = false;
					break;
				}
			}
			if(temp)
			{
				foodlist.add(new Food(newFood));
				System.out.println(newFood+" has been added");
			}
			else
				System.out.println("Someone is bringing that already, can you pick another one?");
		}
	}
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		/**/
		ThanksgivingParty pt = new ThanksgivingParty();
		String snackName = "";
		while(snackName!="q")
		{
			System.out.println("what party snack will you bring? enter q = quit");
			snackName = sc.next();
			pt.addNewFood(snackName);
			
		}
	}
}
