package mageGuild;

import java.io.File;
import java.util.HashMap;

public class Character {

	//declare the attributes, 
	private String name;
	private StatLine stats; //= new StatLine();
	private File characterImage; // = new File("C:/Users/Travis/Pictures/can-stock-photo_csp14845282.jpg");
	private Gender gender;
	private HashMap<String, String> flags;
	
	
	//need items?
	
	//need attributes? (ie, non stat properties)
		
	//need knowledge of others?
		
	//need servants/familiars?
		
	//need flags (note, this may contain all/some of the above?)?
		
	//need relations to other characters (note, does this go here, or somewhere else?)
		
	
	
	//constructor:
	public Character(String newName, Gender newGender){
		name = newName;
		stats = RulesSet.genericStatLine();
		characterImage = new File("images/can-stock-photo_csp14845282.jpg");
		gender = newGender;
		flags = new HashMap<String, String>();
		this.addFlag("defaultFlag", "defaultCharacterFlag");
	}
	
	
	
	//getters and setters, etc
	public void setName(String newName){
		this.name = newName;
	}
	public String getName(){
		return(this.name);
	}
	
	public StatLine getStats(){
		return(this.stats);
	}
	public Float getStat(String statSought){
		//return(this.stats.getStat(statSought));
		if(stats.containsKey(statSought)){
			return(stats.get(statSought));
		} else {
			return(new Float(-999));
			//throw new UnsupportedOperationException("stat "+ statSought + " not found");
		}
	}
	public void setStat(String statSought, Float newValue){
		this.stats.setStat(statSought, newValue);
	}
	public Float changeStatBy(String statSought, Float incValue){
		return(this.stats.changeStatBy(statSought, incValue));
	}
	
	
	public File getCharacterImage(){
		return(characterImage);
	}
	public void setCharacterImage(File theFile){
		characterImage = theFile;
	}
	
	public HashMap<String, String> getFlags(){
		return flags;
	}
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
	
	
	public enum Gender {
		MALE ("his", "him", "he", "man", "men"),
		FEMALE ("hers", "her", "she", "woman", "women" ),
		NEUTRAL("theirs", "them", "they", "person", "people"),
		NONHUMAN("its", "it", "it", "thing", "things");
		private String hishers;
		private String himher;
		private String heshe;
		private String manwoman;
		private String menwomen;
		Gender(String hishers, String himher, String heshe, String manwoman, String menwomen){
			this.hishers = hishers;
			this.himher = himher;
			this.heshe = heshe;
			this.manwoman = manwoman;
			this.menwomen = menwomen;
		}
		public String hishers(){
			return hishers;
		}
		public String himher(){
			return himher;
		}
		public String heshe(){
			return heshe;
		}
		public String manwoman(){
			return manwoman;
		}
		public String menwomen(){
			return menwomen;
		}	
	}
	public Gender getGender(){
		return gender;
	}
	//the methods below just make the above accessible directly from the character, rather than
	//having to type myCharacter.getGender.hishers() you can type myCharacter.hishers()
	public String hishers(){
		return gender.hishers;
	}
	public String himher(){
		return gender.himher;
	}
	public String heshe(){
		return gender.heshe;
	}
	public String manwoman(){
		return gender.manwoman;
	}
	public String menwomen(){
		return gender.menwomen;
	}
	
	
}
