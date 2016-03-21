package mageGuild;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class TextParser {

	private CodeWordCollection codeWordLibrary;
	
	//constructor
	public TextParser(){
		codeWordLibrary = new CodeWordCollection();
		
		//Below this point the specific code words are created and added
		//to the library:
		
		//getting the name of a character object
		codeWordLibrary.addCodeWord(new CodeWord(
				"nameOf", 
				(listOfStrings, library) -> {
					if(listOfStrings.isEmpty()){
						throw new UnsupportedOperationException("Character expected by nameOf");
					}
					if(!"Character".equals(codeWordLibrary.getWord(listOfStrings.get(0)).getProduces())){
						throw new UnsupportedOperationException("Character expected by nameOf");
					}
					//has passed the checks, so return the function:
					return(gamestate -> {
						Character tempCharacter = 
							(Character)((library.applyFirstWord(listOfStrings)).apply(gamestate));
						return(tempCharacter.getName());
						//return("testing");
					});	
				},  "Character", "String", "nameOf <Character>"
		));
		
		//getting a stat (number) form a character
		codeWordLibrary.addCodeWord(new CodeWord(
				"statCalled_FromCharacter", 
				(listOfStrings, library) -> {
					if(listOfStrings.size() < 2){
						throw new UnsupportedOperationException("stat name and character expected");
					}
					String statName = listOfStrings.get(0).trim();
					listOfStrings.remove(0);
					if(!"Character".equals(codeWordLibrary.getWord(listOfStrings.get(0)).getProduces())){
						throw new UnsupportedOperationException("stat name and character expected");
					}
					//has passed the checks, so return the function:
					return(gamestate -> {
						Character tempCharacter = 
								(Character)((library.applyFirstWord(listOfStrings)).apply(gamestate));
						return(tempCharacter.getStat(statName));
					});	
				},  "Character", "Number", "statCalled_FromCharacter <stat> <Character>"
		));
		
		//getting a flag from a character
		codeWordLibrary.addCodeWord(new CodeWord(
				"flagCalled_FromCharacter", 
				(listOfStrings, library) -> {
					if(listOfStrings.size() < 2){
						throw new UnsupportedOperationException("flag name and character expected");
					}
					String flagName = listOfStrings.get(0).trim();
					listOfStrings.remove(0);
					if(!"Character".equals(codeWordLibrary.getWord(listOfStrings.get(0)).getProduces())){
						throw new UnsupportedOperationException("flag name and character expected");
					}
					//has passed the checks, so return the function:
					return(gamestate -> {	
						Character tempCharacter = 
								(Character)((library.applyFirstWord(listOfStrings)).apply(gamestate));
						return(tempCharacter.getFlag(flagName));
					});	
				},  "Character", "String", "flagCalled_FromCharacter <flagname> <Chracter>"
		));
	
		//getting a flag from the gamestate
		codeWordLibrary.addCodeWord(new CodeWord(
				"flagCalled_FromState", 
				(listOfStrings, library) -> {
					if(listOfStrings.size() < 1){
						throw new UnsupportedOperationException("flag name expected");
					}
					return(gamestate -> {
						String flagName = listOfStrings.get(0).trim();
						//listOfStrings.remove(0);
						return(gamestate.getFlag(flagName));
					});	
				},  "none", "String", "flagCalled_FromState <flagname>"
		));
		
		//get the player character
		codeWordLibrary.addCodeWord(new CodeWord(
				"playerCharacter", 
				(listOfStrings, library) -> {
					return(gamestate -> {
						return(gamestate.getPlayerCharacter());
					});	
				},  "none", "Character", "playerCharacter"
		));
		
		//get a non-player-character by name 
		codeWordLibrary.addCodeWord(new CodeWord(
				"NPCnamed", 
				(listOfStrings, library) -> {
					if(listOfStrings.size() < 1){
						throw new UnsupportedOperationException("NPC name expected");
					}
					return(gamestate -> {
						String NPCName = listOfStrings.get(0).trim();
						//listOfStrings.remove(0);
						return(gamestate.getNPC(NPCName));
					});	
				},  "String", "Character", "NPCnamed <NPCname>"
		));
		
		//get the his/hers/its string for a character
		codeWordLibrary.addCodeWord(new CodeWord(
				"hisHersOf", 
				(listOfStrings, library) -> {
					if(listOfStrings.size() < 1){
						throw new UnsupportedOperationException("character expected");
					}
					if(!"Character".equals(codeWordLibrary.getWord(listOfStrings.get(0)).getProduces())){
						throw new UnsupportedOperationException("character expected");
					}
					return(gamestate -> {
						Character tempCharacter = 
								(Character)((library.applyFirstWord(listOfStrings)).apply(gamestate));
						return(tempCharacter.hishers());
					});	
				},  "Character", "String", "hisHersOf <Character>"
		));
	
		//get the him/her/it string for a character
		codeWordLibrary.addCodeWord(new CodeWord(
				"himHerOf", 
				(listOfStrings, library) -> {
					if(listOfStrings.size() < 1){
						throw new UnsupportedOperationException("character expected");
					}
					if(!"Character".equals(codeWordLibrary.getWord(listOfStrings.get(0)).getProduces())){
						throw new UnsupportedOperationException("character expected");
					}
					return(gamestate -> {
						Character tempCharacter = 
								(Character)((library.applyFirstWord(listOfStrings)).apply(gamestate));
						return(tempCharacter.himher());
					});	
				},  "Character", "String", "himHerOf <Character>"
		));
		
		//get the he/she/it string for a character
		codeWordLibrary.addCodeWord(new CodeWord(
				"heSheOf", 
				(listOfStrings, library) -> {
					if(listOfStrings.size() < 1){
						throw new UnsupportedOperationException("character expected");
					}
					if(!"Character".equals(codeWordLibrary.getWord(listOfStrings.get(0)).getProduces())){
						throw new UnsupportedOperationException("character expected");
					}
					return(gamestate -> {
						Character tempCharacter = 
								(Character)((library.applyFirstWord(listOfStrings)).apply(gamestate));
						return(tempCharacter.heshe());
					});	
				},  "Character", "String", "heSheOf <Character>"
		));

		
		//get the man/Woman/being string for a character
		codeWordLibrary.addCodeWord(new CodeWord(
				"manWomanOf", 
				(listOfStrings, library) -> {
					if(listOfStrings.size() < 1){
						throw new UnsupportedOperationException("character expected");
					}
					if(!"Character".equals(codeWordLibrary.getWord(listOfStrings.get(0)).getProduces())){
						throw new UnsupportedOperationException("character expected");
					}
					return(gamestate -> {
						Character tempCharacter = 
								(Character)((library.applyFirstWord(listOfStrings)).apply(gamestate));
						return(tempCharacter.manwoman());
					});	
				},  "Character", "String", "manWomanOf <Character>"
		));		
		
		
		//get the men/women/beings string for a character
		codeWordLibrary.addCodeWord(new CodeWord(
				"menWomenOf", 
				(listOfStrings, library) -> {
					if(listOfStrings.size() < 1){
						throw new UnsupportedOperationException("character expected");
					}
					if(!"Character".equals(codeWordLibrary.getWord(listOfStrings.get(0)).getProduces())){
						throw new UnsupportedOperationException("character expected");
					}
					return(gamestate -> {
						Character tempCharacter = 
								(Character)((library.applyFirstWord(listOfStrings)).apply(gamestate));
						return(tempCharacter.menwomen());
					});	
				},  "Character", "String", "menWomenOf <Character>"
		));
			

	} //closes constructor
	
	
	//method for returning the code words
	public CodeWordCollection getCodeWords(){
		return(codeWordLibrary);
	}
	
	
	public Function<GameState, String> parseFullText(String theText) 
			throws UnsupportedOperationException {
	/*
	 * This method takes in a string, which may contain code sections indicated
	 * by leading and trailing **'s, and returns lambda.  The lambda takes in
	 * the gamestate, and returns the text string for the event text.  As an
	 * example, if the string "££nameOf NPCnamed flagCalled_FromState badguy££
	 * shouts stop!", this method will return a lambda, which when given the
	 * gamestate as input, will return the string "Bob shouts stop!" (if Bob
	 * is the name of the badguy in gamestate).  
	 */
		
		boolean codeFirst = theText.startsWith("££");
		if(codeFirst){ //if starts with code, strip off leading ££ before splitting
			theText = theText.substring(2);
		}
		String [] pieces = theText.split("££");
		//System.out.println(Arrays.toString(pieces));
		
		return((gamestate) -> {
			String outString = "";
			boolean isCode = codeFirst;
			
			for(String piece : pieces){
				/*
				if(piece.equals("")){
					System.out.println("test " + isCode);
					//checks if an empty string has ended up as an element
					//if so, we just want to skip it.
					continue;
				}
				*/
				if(isCode){
					//turn the code into arraylist, and strip off unnecessary 
					//white space.
					ArrayList<String> tempArray = 
						Arrays.asList(piece.split(" "))
							.stream()
							.map(String::trim)
							.filter(string -> !string.equals(""))
							.collect(Collectors.toCollection(ArrayList::new));
					
					if(!tempArray.isEmpty()){
						try {
							//make sure return type of first word is string:
							if(!codeWordLibrary.getMap().get(tempArray.get(0)).getProduces().equals("String")){
								throw new UnsupportedOperationException("Code sections must return Strings: " 
										+ tempArray.get(0) + " returns " +
										codeWordLibrary.getMap().get(tempArray.get(0)).getProduces());
							}
							//add the output of the code to the string		
							outString = outString + 
								parseText(tempArray).apply(gamestate);
						} catch(NullPointerException e) {
							throw new UnsupportedOperationException("Unrecognized code: " + tempArray);
						}
					} 
											
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
	
	
	public  Function<GameState, Object> parseText(List<String> text) 
			throws UnsupportedOperationException {
		
		if(text.isEmpty()){
			return((gamestate) -> {return("");});
		} else {
			
			return(codeWordLibrary.applyFirstWord(text));
			
			/* old version of code commented out
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
				if(!(text.size() >= 1)){
					System.out.println("name not given");
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
			*/
			
		} //closes if else statement
		
		
	} //closes text parser method
	
	
	
	
	
	
	
} //closes class
