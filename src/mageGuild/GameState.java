package mageGuild;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.function.BiConsumer;


public class GameState {
/*
 * Objects of this class hold all the info about the state of a game at any
 * point in the game.  
 */
	
	private Character playerCharacter;
	private Integer month;
	private Integer gamePhase;
	//private HashMap<String, Integer> phaseOneActivitiesAndTimes;
	public LinkedHashMap<String, BiConsumer<GameState, 
			LinkedHashMap<TimeSpendOption, Integer>>> afterTimeSpentFunctions;
	private HashMap<String, String> flags;
	
	private LinkedHashMap<String, Character> NPCs;
	
	//constructor
	public GameState(Character theCharacter){
		playerCharacter = theCharacter;
		month =1;
		gamePhase =1;
		afterTimeSpentFunctions = new LinkedHashMap<String, 
				BiConsumer<GameState, LinkedHashMap<TimeSpendOption, Integer>>>();
		flags = new HashMap<String, String>();
		this.addFlag("defaultFlag", "defaultGameStateFlag");
		NPCs = new LinkedHashMap<String, Character>();
		this.addNPC("defaultNPC", new Character("defaultNPC", Character.Gender.NEUTRAL));
	}
	
	public void setPlayerCharacter(Character thePlayer){
		playerCharacter = thePlayer;
	}
	public Character getPlayerCharacter(){
		return(playerCharacter);
	}
	
	public void addNPC(String name, Character npc){
		NPCs.put(name, npc);
	}
	
	public Character getNPC(String name){
		if(NPCs.containsKey(name)){
			return(NPCs.get(name));
		} else {
			return(NPCs.get("defaultNPC"));
			//throw new UnsupportedOperationException("NPC "+ name + " not found");
		}
	}
	
	
	//this is the clock tick of the game.  there are four phases each month.
	//this adds one to the phase, and if it gets to five, moves to the next 
	//month and resets phase to 1.
	public void nextPhase(){
		gamePhase ++;
		if(gamePhase > 2){  //TODO: change back to > 4; put to 2 just for testing
			gamePhase = 1;
			month ++;
		}
	}
	
	//this one undoes the previous.  Note, it probably shouldn't be made available
	//in phase 1, as this just moves time back, doesn't undo the results that will
	//occur at the end of the month.  
	public void previousPhase(){
		gamePhase--;
		if(gamePhase < 1){
			gamePhase = 4;
			month --;
		}
	}
	
	
	public Integer getMonth(){
		return(month);
	}
	public Integer getPhase(){
		return(gamePhase);
	}
	
	//getters and setters for flags
	public void addFlag(String flagName, String info){
		flags.put(flagName, info);
	}
	public void removeFlag(String flagName){
		flags.remove(flagName);
	}
	public void changeFlag(String flagName, String newInfo){
		flags.put(flagName, newInfo);
	}
	public String getFlag(String flagName){
		if(flags.containsKey(flagName)){
			return(flags.get(flagName));
		} else {
			return(flags.get("defaultFlag"));
			//throw new UnsupportedOperationException("flag "+ flagName + " not found");
		}
	}
	
}
