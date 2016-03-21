package mageGuild;

import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class CodeWordCollection {

	private HashMap<String, CodeWord> library;
	
	//constructor 
	public CodeWordCollection(){
		library = new HashMap<String, CodeWord>();
	}
	
	public Function<GameState, Object> applyFirstWord(List<String> listOfStrings){
	//this method takes the first word off the list, looks it up in the library,
	//and carries out it's evaluate method.  Likely the method for that word will
	//call this function again on what's left of the arraylist until it forms a
	//complete function.  
		//if array is empty, just send back blank string
		if(listOfStrings.isEmpty()){
			return((gamestate) -> {return("");});
		}
		//list is not empty, so grab the first word and apply it's lambda if possible
		String currentWord = listOfStrings.get(0).trim();
		if(library.containsKey(currentWord)){
			//listOfStrings.remove(0);
			//ideally should do a bit more checking here to make sure the
			//types match up for the various parts of the list
			return((library.get(currentWord)).evaluate(
					listOfStrings.subList(1,listOfStrings.size()), this));
		} else {
			throw new UnsupportedOperationException("unknown codeword: " + currentWord);
		}
	}
	
	public void addCodeWord(CodeWord aWord){
		//use this method to add a codeword to the library
		if(library.containsKey(aWord.getName())){
			JOptionPane.showMessageDialog(new JFrame(), "CodeWord " + 
					aWord.getName() + " already exists.");
		} else {
			library.put(aWord.getName(), aWord);
		}
	}
	
	public void removeCodeWord(String name){
		//use this to remove a codeword from the library
		if(library.containsKey(name)){
			library.remove(name);
		} else {
			JOptionPane.showMessageDialog(new JFrame(), "CodeWord " + 
					name + " not found.");
		}
	}
	
	//for getting words by name
	public CodeWord getWord(String name){
		if(library.containsKey(name)){
			return(library.get(name));
		} else {
			JOptionPane.showMessageDialog(new JFrame(), "CodeWord " + 
					name + " not found.");
			throw new UnsupportedOperationException("unknown codeword: " + name);
			
		}
	}
	
	//for getting the whole hashmap
	public HashMap<String, CodeWord> getMap(){
		return(library);
	}
	
}
