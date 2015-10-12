package mageGuild;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class TimeSpendOption {
	
	private String name;
	private String type;
	private Predicate<GameState> isAvailable;
	private Integer maxTime;
	private BiConsumer<Integer, GameState> resultOfTimeSpent;
	
	//constructor
	public TimeSpendOption(String setName, String setType, 
			Predicate<GameState> setIsAvailable, 
			Integer setMaxTime, 
			BiConsumer<Integer, GameState> setResultOfTimeSpent){
		name = setName;
		type =setType;
		isAvailable = setIsAvailable;
		maxTime = setMaxTime;
		resultOfTimeSpent = setResultOfTimeSpent;
	}
	
	//getters
	public String getName(){
		return name;
	}
	public String getType(){
		return type;
	}
	public Integer getMaxTime(){
		return maxTime;
	}
	public boolean isAvailable(GameState state){
		return isAvailable.test(state);
	}
	
	//take in a game state and time spent, and do what needs to be done:
	public void spendTime(Integer timeSpent, GameState state){
		resultOfTimeSpent.accept(timeSpent, state);
	}
	
	//overriding toString so that we can put these inside comboboxes and the like, and only see
	//the name
	@Override
	public String toString(){
		return name;
	}
	
}
