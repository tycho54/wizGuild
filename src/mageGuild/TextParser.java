package mageGuild;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TextParser {

	public static Function<GameState, String> parseFullText(String theText) 
			throws UnsupportedOperationException {
	/*
	 * This method takes in a string, which may contain code sections indicated
	 * by leading and trailing **'s, and returns lambda.  The lambda takes in
	 * the gamestate, and returns the text string for the event text.  As an
	 * example, if the string "**nameOf NPCnamed flagCalled_FromState badguy**
	 * shouts stop!", this method will return a lambda, which when given the
	 * gamestate as input, will return the string "Bob shouts stop!" (if Bob
	 * is the name of the badguy in gamestate).  
	 */
		
		String [] pieces = theText.split("££");
		boolean codeFirst = theText.startsWith("££");
		//System.out.println(Arrays.toString(pieces));
		
		return((gamestate) -> {
			String outString = "";
			boolean isCode = codeFirst;
			
			for(String piece : pieces){
				if(piece.equals("")){
					//checks if an empty string has ended up as an element
					//if so, we just want to skip it.
					continue;
				}
				if(isCode){
					//turn the code into arraylist, and strip off unnecessary 
					//white space.
					ArrayList<String> tempArray = 
						Arrays.asList(piece.split(" "))
							.stream()
							.map(String::trim)
							.filter(string -> !string.equals(""))
							.collect(Collectors.toCollection(ArrayList::new));
					
					//add the output of the code to the string		
					outString = outString + 
							parseText(tempArray).apply(gamestate);						
				} else {
					//for strings, just add the text to the existing string
					outString = outString + piece;
				}
				//next piece will be opposite (code/not code) to current piece
				isCode = !isCode;
			}
			
			return(outString);
		});
		
	}
	
	
	public static Function<GameState, Object> parseText(ArrayList<String> text) 
			throws UnsupportedOperationException {
		
		if(text.isEmpty()){
			return((gamestate) -> {return("");});
		} else {
			String currentWord = text.get(0).trim();
			text.remove(0);
			
			switch(currentWord){
			
			case "nameOf":	
				if(text.size() < 1){
					throw new UnsupportedOperationException("character expected");
				}
				return((gamestate)->{
					Character tempCharacter = (Character)parseText(text).apply(gamestate);
					return(tempCharacter.getName());
				});
				//break; (commented out as unreachable)
				
			case "statCalled_FromCharacter":
				if(text.size() < 2){
					throw new UnsupportedOperationException("stat name and character expected");
				}
				return((gamestate)->{
					String statName = text.get(0);
					text.remove(0);
					Character tempCharacter = (Character)parseText(text).apply(gamestate);
					return(tempCharacter.getStat(statName));
				});
				//break; (commented out as unreachable)
				
				
				
			case "flagCalled_FromCharacter":
				if(text.size() < 2){
					throw new UnsupportedOperationException("flag name and character expected");
				}
				return((gamestate)->{
					String flagName = text.get(0);
					text.remove(0);
					Character tempCharacter = (Character)(parseText(text).apply(gamestate));
					return(tempCharacter.getFlag(flagName));
				});
				//break; (commented out as unreachable)
				
				
			case "flagCalled_FromState":
				if(text.size() < 1){
					throw new UnsupportedOperationException("Flag name required");
				}
				return((gamestate)->{
					String flagName = text.get(0);
					text.remove(0);
					//Character tempCharacter = (Character)parseText(text).apply(gamestate);
					return(gamestate.getFlag(flagName));
				});
				//break; (commented out as unreachable)
				
			case "playerCharacter":
				return((gamestate) -> {
					return(gamestate.getPlayerCharacter());
				});
				//break; (commented out as unreachable)
				
			case "NPCnamed":
				if(text.size() != 1){
					throw new UnsupportedOperationException("NPC name not given");
				}
				return((gamestate) -> {
					String name = text.get(0);
					text.remove(0);
					return(gamestate.getNPC(name));
				});
				//break; (commented out as unreachable)
				
				
				//--produce an arrayList from...?
				//arrayList
				
			case "valueAtIndex_In":
				if(text.size() < 2 ){
					throw new UnsupportedOperationException("Index and array required.");
				}
				return((gamestate) ->{
					int index = Integer.parseInt(text.get(0));
					text.remove(0);
					ArrayList<Object> tempArray = (ArrayList<Object>)parseText(text).apply(gamestate);
					return(tempArray.get(index));
				});
				//break; (commented out as unreachable)
				
			case "hisHersOf":
				if(text.size() == 0){
					throw new UnsupportedOperationException("Character expected.");
				}
				return((gamestate)->{
					Character tempCharacter = (Character)parseText(text).apply(gamestate);
					return(tempCharacter.hishers());
				});
				//break; (commented out as unreachable)
				
			case "himHerOf":
				if(text.size() == 0){
					throw new UnsupportedOperationException("Character expected.");
				}
				return((gamestate)->{
					Character tempCharacter = (Character)parseText(text).apply(gamestate);
					return(tempCharacter.himher());
				});
				//break; (commented out as unreachable)
				
			case "heSheOf":
				if(text.size() == 0){
					throw new UnsupportedOperationException("Character expected.");
				}
				return((gamestate)->{
					Character tempCharacter = (Character)parseText(text).apply(gamestate);
					return(tempCharacter.heshe());
				});
				//break; (commented out as unreachable)
			
			case "manWomanOf":
				if(text.size() == 0){
					throw new UnsupportedOperationException("Character expected.");
				}
				return((gamestate)->{
					Character tempCharacter = (Character)parseText(text).apply(gamestate);
					return(tempCharacter.manwoman());
				});
				//break; (commented out as unreachable)	
				

			case "menWomenOf":
				if(text.size() == 0){
					throw new UnsupportedOperationException("Character expected.");
				}
				return((gamestate)->{
					Character tempCharacter = (Character)parseText(text).apply(gamestate);
					return(tempCharacter.menwomen());
				});
				//break; (commented out as unreachable)	
			
			case "": //handle any empty strings that sneak in
				return((gamestate)->{
					return(parseText(text).apply(gamestate));
				});
				//break; (commented out as unreachable)	
				
			default:
				throw new UnsupportedOperationException("Command word not recognized.");
				//return((gamestate) -> {
					//System.out.println("Unrecognized command: " + text);
					//return(parseText(text).apply(gamestate));
				//});
				
			}//closes switch statement
		} //closes if else statement
		
		
	} //closes text parser method
	
} //closes class
