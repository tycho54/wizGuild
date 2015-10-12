package mageGuild;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Event {

	private String eventText;
	private ArrayList<EventOption> options;
	private GameState gameState;

	private Predicate<GameState> isAvailableFunction;
	
	
	//constructor (note--create the event first, then add eventOptions using addOption
	public Event (GameState aGameState, String theEventText, 
			Predicate<GameState> isAvailableLambda){
		
		eventText = theEventText;
		gameState = aGameState;
		isAvailableFunction = isAvailableLambda;
		options = new ArrayList<EventOption>();
	}
	//this is a wrapper for the private class function, since won't be able to get
	//to the eventOption class outside of this class
	public void addOption(String text, Predicate<GameState> optionAvailableLambda, 
			Consumer<GameState> consequenceLambda){
		options.add(new EventOption(text, optionAvailableLambda, consequenceLambda));
	}
	
	public ArrayList<EventOption> getOptions(){
		return options;
	}
	
	public String getText(){
		return eventText;
	}
	
	public boolean isAvailable(){
		return isAvailableFunction.test(gameState);
	}
	
	
	public class EventOption {
		private String optionText;
		private Predicate<GameState> optionAvailableFunction;
		private Consumer<GameState> consequence;
		
		//constructor
		public EventOption(String text, Predicate<GameState> optionAvailableLambda, 
				Consumer<GameState> consequenceLambda){
			optionText = text;
			optionAvailableFunction = optionAvailableLambda;
			consequence = consequenceLambda;
		}
		
		public String getText(){
			return optionText;
		}
		
		public boolean optionAvailable(){
			return optionAvailableFunction.test(gameState);
		}
		
		public void selectOption(){
			consequence.accept(gameState);
		}
		
	}
}
