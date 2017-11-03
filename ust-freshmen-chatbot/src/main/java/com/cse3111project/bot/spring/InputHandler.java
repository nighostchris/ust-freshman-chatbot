/*
		location = 1 for searching keywords
    	location, course_website, academic_advice, timetable_assistant, 
    	restaurant_recommendation, booking, transportation, hall_and_society,
    	teaching_faculty_information, waitlist_helper, credit_transfer;
*/

public static class InputHandler {
	private String input;
	private String[] inputComponent;
	public String result;
	
	//constructor
	public InputHandler(String userInput)
	{
		input = userInput;
		//split the input into array and remove all the puntuation
		inputComponent = input.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");
		result = "";
	}
	//searching keywords   0=>no hitting features
	public int matched_feature()
	{
		int keywordHit[] = new int[keywords.size()];
		for(int i=0;i<keywords.size();i++)
            keywordHit[i]=0;
		for(String text : inputComponent)
		{
			for(int i = 0;i<keywords.size();++i)
			{
				for(int j=0;j<keywords.get(i).size();++j)
				{
					if(text.contains(keywords.get(i).get(j)))
						keywordHit[i]++;
				}
			}
		}
		
		int feature = 0;
		for(int i = 0;i<keywords.size();++i)
		{
			if (keywordHit[i]>feature)
				feature = i+1;
		}
		return feature;
	}
		
	
};
