package mageGuild;

import java.util.*;

public class StatLine extends HashMap<String, Float>{
	static final long serialVersionUID = 1L;

	//for getting stats:
	public Float getStat(String statSought){
		if(this.containsKey(statSought)){
			return(this.get(statSought));
		} else {
			//note, returns -1 for non-existing stats, to serve as an error 
			//message
			return(new Float(-1)); 
		}
	}
	
	
	//for changing stats (note, will also create new stats if the
	//name isn't already in the list:
	public void setStat(String statSought, Float newValue){
		this.put(statSought, newValue);
	}
	
	//for changing stats by a given value (ie, adding to it):
	public Float changeStatBy(String statSought, Float incValue){
		if(this.containsKey(statSought)){
			Float newValue = this.get(statSought) + incValue;
			this.put(statSought, newValue);
			return(newValue);
		} else {
			//note, returns -1 for non-existing stats, to serve as an error 
			//message
			return(new Float(-1)); 
		}
	}
	
	
	
	//constructors:
	public StatLine(){
		
	}
	public StatLine(String[] names, Float[] values){
		if(names.length == values.length){
			for(int i=0; i < values.length; i++){
				this.put(names[i], values[i]);
			}
		}
	}
	
	
	
}
