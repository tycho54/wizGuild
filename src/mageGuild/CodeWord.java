package mageGuild;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;




public class CodeWord {
	
	private String name;
	private BiFunction<List<String>,CodeWordCollection,
		Function<GameState, Object>> myLambda;
	//private String errorMsg;
	private String expects;
	private String produces;
	private String hoverMsg;
	
	//Constructor
	public CodeWord(String name, 
					BiFunction <List<String>,CodeWordCollection,
								Function<GameState, Object>> aLambda,
					//String errorMsg, 
					String expects, 
					String produces,
					String hoverMsg
					){
		
		this.name = name;
		this.myLambda = aLambda;
		//this.errorMsg = errorMsg;
		this.expects = expects;
		this.produces = produces;
		this.hoverMsg = hoverMsg;
	}
	
	public Function<GameState, Object> evaluate(
			List<String> listOfWords, 
			CodeWordCollection myCollection){
		//this method creates a function for mapping a gamestate
		//to some object.   This can depend on the 
		//remaining words in listOfWords.
		
		return((gamestate) ->{
			return(myLambda.apply(listOfWords, myCollection).apply(gamestate));
		});
		
	}
	
	//this just returns the name of the CodeWord
	public String getName(){
		return(name);
	}
	
	public String getProduces(){
		return(produces);
	}
	
	public String getExpects(){
		return(expects);
	}

	public String getHoverMsg(){
		return(hoverMsg);
	}
	
	public Boolean checkTypes(List<String> aList, 
			CodeWordCollection aCollection){
	/* this function takes in an ArrayList of strings, and a codeword collection,
	 * and checks that the first word in the array list produces the expected
	 * object type that this codeword accepts.  
	 */
		return(expects.equals(aCollection.getWord(aList.get(0)).getProduces()));
	}
	
}
