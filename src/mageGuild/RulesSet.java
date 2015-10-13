package mageGuild;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Random;

public abstract class RulesSet {
/**
 * this class will contain game-specific ideas, so that it should be relatively easy to 
 * create a new game with a different theme/rulesbase, just by changing this file (and
 * making new events).  Note, class is abstract, to enforce that no state is kept here.
 * Only make static methods; these are used to tell the game how to work, often based on 
 * the current GameState (supplied as a parameter).  
 */
	
	
	public static ArrayList<String> getListOfMagics(){
		//returns an ArrayList containing the names of the schools of
		//magic in the game
		String [] temp = new String[]{
				"Aeromancy", "Pyromancy", "Street Magic", "Healing", 
				"Conjuration", "Necromancy", "Golemancy", "Summoning",
				"Geomancy", "Scrying", "Enchantment", "Shapeshifting",
				"Illusion", "Blood Magic", "Geomancy"
		};
		ArrayList<String> magic = new ArrayList<String>(Arrays.asList(temp));
		return(magic);
	}
	
	public static ArrayList<String> getListOfSkills(){
		//returns an ArrayList of the names of Skills available in the game
		String [] temp = new String[]{
				"Mystique", "Scuffle", "Skulduggery", "Lore"		
		};
		ArrayList<String> skills = new ArrayList<String>(Arrays.asList(temp));
		return(skills);
	}
	
	public static ArrayList<String> getListOfAttributes(){
		//returns an ArrayList of the non-magic, non-skill stats 
		//available in the game
		String [] temp = new String[]{
				"Health", "Stress", "Gold", 
				"Physique", "Willpower", "Finesse", "Intelligence"			
		};
		ArrayList<String> attributes = new ArrayList<String>(Arrays.asList(temp));
		return(attributes);
	}
	
	//StatLines for the game (ie, telling the game which stats exist):
	public static StatLine genericStatLine(){
		
		//create the array of stats
		ArrayList<String> allStats = getListOfMagics();
		allStats.addAll(getListOfSkills());
		allStats.addAll(getListOfAttributes());
		String[] names = allStats.toArray(new String[allStats.size()]);
		
		//create the array of stats
		/*
		String[] names = new String[] {"Health", "Stress", "Gold", 
				"Physique", "Willpower", "Finesse", "Intelligence",
				"Mystique", "Scuffle", "Skulduggery", "Lore", 
				"Aeromancy", "Pyromancy", "Street Magic", "Healing", 
				"Conjuration", "Necromancy", "Golemancy", "Summoning",
				"Geomancy", "Scrying", "Enchantment", "Shapeshifting",
				"Illusion", "Blood Magic", "Geomancy"};
		*/
		
		
		//create the integer array, and fill it with zeros
		Float[] values = new Float[names.length];
		for(int i=0; i< values.length; i++){
			values[i] = new Float(0);
		}
		
		return new StatLine(names, values);
	}
	
	//method for creating a sample character
	public static Character sampleCharacter(){
		Character tempChar = new Character("Cutter", Character.Gender.MALE);
		tempChar.addFlag(new String("canDoBartending"), new String(""));
		tempChar.addFlag(new String("canDoScribing"), new String(""));
		tempChar.addFlag(new String("canDoStreet Tricks"), new String(""));
		tempChar.addFlag(new String("canStudyAeromancy"), new String(""));
		tempChar.addFlag(new String("canStudyNecromancy"), new String(""));
		tempChar.addFlag(new String("canStudyHealing"), new String(""));
		tempChar.addFlag(new String("canStudyConjuration"), new String(""));
		tempChar.addFlag(new String("canStudyStreet Magic"), new String(""));
		tempChar.addFlag(new String("canStudyPyromancy"), new String(""));
		tempChar.addFlag(new String("canStudyBlood Magic"), new String(""));
		tempChar.addFlag(new String("canStudyGolemancy"), new String(""));
		tempChar.addFlag(new String("canPracticeLore"), new String(""));
		tempChar.addFlag(new String("canPracticeScuffle"), new String(""));
		tempChar.addFlag(new String("canPracticeMystique"), new String(""));
		tempChar.addFlag(new String("canPracticeSkulduggery"), new String(""));
		return tempChar;
	}
	
	//method for creating a sample event (mostly for testing purposes)
	public static Event sampleEvent(GameState aGameState){
		Event theEvent = new Event(aGameState, 
				"This is the event text.  It describes the event.  Below are "
				+ "option for how you can respond to the event.", 
				(p) -> true);
		theEvent.addOption("This option is available.", (g) -> true, (g) -> {});
		theEvent.addOption("This one is not.", (g) -> false, (g) -> {});
		theEvent.addOption("This one adds 1 gold", (g) -> true, 
				(g) -> {g.getPlayerCharacter().changeStatBy("Gold", new Float(1));}
				);
		
		
		return theEvent;
	}
	
	//use this to set a message that on the main gui border
	public static String getBorderString(GameState state){
		Integer month = state.getMonth();
		Integer phase = state.getPhase();
		return("Month: " + month + " Phase: " + phase);
	}
	
	
	public static String getPhaseType(GameState state) {
		/*
		 * the gui will ask the rules which phase type to display next.  this
		 * function provides the answer, based on the game state.  
		 */
		int phase = state.getPhase();
		if(phase == 1){
			return "spend";
		} else if(phase == 2){
			return "select";
		} else if (phase == 3){
			return "event";
		} else if (phase == 4){
			return "event";
		} else {
			return "error--unknown phase number";
		}
	}
	
	public static void submitSpendPhase(LinkedHashMap<TimeSpendOption, Integer> activitiesAndTimes,
			GameState state){
		/*
		 * this method takes the choices made during a spend phase and updates
		 * the game state based on them.  Mostly it just calls the lambdas for the
		 * various options.
		 */
		
		//System.out.println(activitiesAndTimes);
		
		TimeSpendOption rest = RulesSet.getRestTimeSpendOption();
		Integer restTime=0;
		
		//first do non rest options:
		activitiesAndTimes.forEach((activity, time) -> {
			activity.spendTime(time, state);
			//System.out.println("activity: "+ activity + " time: " + time);
		});
			
		//now do the rest and relaxation option:
		rest.spendTime(restTime, state);
			
		
		//now check if there are any functions to be called in the game state, and call them:
		// (note, this is how you check to see if any any activity wasn't chosen)
		state.afterTimeSpentFunctions.entrySet().stream().forEach(
				entry -> entry.getValue().accept(state, activitiesAndTimes));
		
		
		//state.nextPhase();
	}
	
	public static void goBackOnePhase(GameState state){
		/*
		 * this method is called by the gui when the user pushes a "back" button.  update
		 * the gamestate data accordingly.
		 */
		state.previousPhase();
	}
	
	public static Integer getMaxTimeAllowed(){
		/*
		 * use this to set the max amount of time player can 
		 * spend on one activity per turn:
		 */
		return 10;
	}
	
	public static Integer getMinTimeAllowed(){
		/*
		 * use this to set the minimum amount of time the player can
		 * spend on rest and relaxation (note, can be negative)
		 */
		return -5;
	}
	
	public static Integer getFullTimeAllowed(){
		/*
		 * use this to return the maximum time available in each round:
		 */
		return 10;
	}
	
	public static Float timeToSkill(Float currentLevel, Integer time, Float scale){ 
			
		/*
		 * this takes a current skill value, and time spent improving it,
		 * and returns the *change* in that skill (note--it doesn't apply
		 * the change, just calculates it, so that you can use this to
		 * estimate changes without causing them)
		 */
		Float currentValueHours = skillToTime(currentLevel, scale);
		Float max = new Float(100);
		Float fTime = new Float(time);
		Float offset = new Float(Math.log(max - 1)/(scale));
		
		
		return  max/(1 + new Float(Math.exp(-scale*(fTime + currentValueHours - offset))))
				- currentLevel;	
		
	}
	public static Float skillToTime(Float currentLevel,  Float scale){
		/*
		 * a function for calculating the number of time units applied to learning
		 * a skill so far, based on the current skill level.  Expected to be used
		 * with the timeToSkill function
		 */
		Float max = new Float(100);
		Float offset = new Float(Math.log(max - 1)/(scale));
		if(currentLevel <=0){
			return new Float(1); //need this in there b/c we start with 0 skill, but that blows up
		} else {
			//System.out.println(offset - (1/scale)*(new Float(Math.log(max/currentLevel - 1))));
			return  offset - (1/scale)*(new Float(Math.log(max/currentLevel - 1)));
		}
	}
	
	public static void studyMagicWrapper(GameState state, String skill, 
			Integer time, Float scale){
		/*
		 * this is a wrapper function for spending time studying a given magic.  It
		 * applies the timeToSkill function and updates the player's stats.
		 */
		//this is what happens when they study
		time = new Integer((int)Math.floor(time 
					- state.getPlayerCharacter().getStat("Stress")/10));
		if(time > 0){
			Float currentSkill = ((GameState)state).getPlayerCharacter().getStat(skill);
			Float change = timeToSkill(currentSkill, time, scale);
			state.getPlayerCharacter().changeStatBy(skill, change);	
		}
	}
	
	public static void otherStudyEffectsWrapper(Character character, String magicName, 
			Float stressFactor, Float minLearnValue, Float learnChanceOffset, int time){
		/*
		 * 	convenience function for dealing with stress and learning chances when
		 *  studying magic 
		 */
		character.changeStatBy("Stress", new Float(stressFactor*time));
		if(character.getStat(magicName) >= minLearnValue){
			testToLearnMagic(character, magicName, 
					new Float(0), "casts"+magicName, "true");
		}
	}
	
	
	
	
	public static void testToLearnMagic(Character character, String magicName, 
			Float numToBeat, String flagName, String flagInfo){
		/*
		 * a function for testing if the character learns a given magic
		 * at this time, based on their current score and a number
		 * to beat.  if successful, the flag flagName is set along with
		 * the extra flagInfo.
		 */
		
		Float currentValue = character.getStat(magicName);
		Random randomGenerator = new Random();
		Float randomFloat = 100*randomGenerator.nextFloat();
		if(randomFloat <= currentValue - numToBeat - character.getStat("Stress")){
			character.addFlag(flagName, flagInfo);
			System.out.println("Success! Learned " + flagName);
		} 
	}
	
	
	public static ArrayList<TimeSpendOption> getTimeSpendOptions() {
		/*
		 * This method returns all the TimeSpendOptions used in the game, all put into
		 * an ArrayList.  NOTE: this isn't the currently available options based on 
		 * the game state. 
		 */
		
		//create the return variable:
		ArrayList<TimeSpendOption> theList = new ArrayList<TimeSpendOption>();
		//now add all the elements:
		//studying magic options:
		
		/* numbers came from running tests in R, results used are here:
		  #      .2  gives 35 skill at 20, 7  at 10, and 80 at 30  **20**
		  #note, .15 gives 47 skill at 30, 17 at 20, and 80 at 40  **30**
		  #      .12 gives 55 skill at 40, 27 at 30, and 80 at 50  **40**
		  #     .09 gives 48 skill at 50, 27 at 40 and 69 at 60    **50**
		  #     .06 gives 40 skill at 70, 55 at 80 and 69 at 90    **80**
		*/
		
		theList.add(new TimeSpendOption("Aeromancy", "Study", 
				(state) -> {return state.getPlayerCharacter().getFlags().containsKey("canStudyAeromancy");},
				10, 
				(time, state) -> {
					studyMagicWrapper(state, "Aeromancy", time, new Float(.15));
					otherStudyEffectsWrapper(state.getPlayerCharacter(), "Aeromancy",
							new Float(.2), new Float (25), new Float(0), time);
				}));
		
		theList.add(new TimeSpendOption("Necromancy", "Study", 
				(state) -> {return state.getPlayerCharacter().getFlags().containsKey("canStudyNecromancy");},
				10, 
				(time, state) -> {
					studyMagicWrapper(state, "Necromancy", time, new Float(.06));
					otherStudyEffectsWrapper(state.getPlayerCharacter(), "Necromancy",
							new Float(1), new Float (40), new Float(0), time);
							
				}));
		
		theList.add(new TimeSpendOption("Healing", "Study", 
				(state) -> {return state.getPlayerCharacter().getFlags().containsKey("canStudyHealing");},
				10, (time, state) -> {
					studyMagicWrapper(state, "Healing", time,  new Float(.09));	
					otherStudyEffectsWrapper(state.getPlayerCharacter(), "Healing",
							new Float(.6), new Float (27), new Float(0), time);
					
				}));
		
		theList.add(new TimeSpendOption("Conjuration", "Study", 
				(state) -> {return state.getPlayerCharacter().getFlags().containsKey("canStudyConjuration");},
				10, (time, state) -> {
					studyMagicWrapper(state, "Conjuration", time,new Float(.12));	
					otherStudyEffectsWrapper(state.getPlayerCharacter(), "Conjuration",
							new Float(.4), new Float (27), new Float(0), time);
				}));
				
		theList.add(new TimeSpendOption("Street Magic", "Study", 
				(state) -> {return state.getPlayerCharacter().getFlags().containsKey("canStudyStreet Magic");},
				10, (time, state) -> {
					studyMagicWrapper(state, "Street Magic", time, new Float(.2));	
					otherStudyEffectsWrapper(state.getPlayerCharacter(), "Street Magic",
							new Float(.2), new Float (10), new Float(0), time);
				}));
		
		theList.add(new TimeSpendOption("Pyromancy", "Study", 
				(state) -> {return state.getPlayerCharacter().getFlags().containsKey("canStudyPyromancy");},
				10, (time, state) -> {
					studyMagicWrapper(state, "Pyromancy", time,new Float(.12));	
					otherStudyEffectsWrapper(state.getPlayerCharacter(), "Pyromancy",
							new Float(.6), new Float (27), new Float(0), time);
				}));
		
		theList.add(new TimeSpendOption("Golemancy", "Study", 
				(state) -> {return state.getPlayerCharacter().getFlags().containsKey("canStudyGolemancy");},
				10, (time, state) -> {
					studyMagicWrapper(state, "Golemancy", time, new Float(.06));	
					otherStudyEffectsWrapper(state.getPlayerCharacter(), "Golemancy",
							new Float(.8), new Float (40), new Float(0), time);
				}));
		
		theList.add(new TimeSpendOption("Summoning", "Study", 
				(state) -> {return state.getPlayerCharacter().getFlags().containsKey("canStudySummoning");},
				10, (time, state) -> {
					studyMagicWrapper(state, "Summoning", time, new Float(.06));	
					otherStudyEffectsWrapper(state.getPlayerCharacter(), "Summoning",
							new Float(1.2), new Float (40), new Float(0), time);
				}));
		
		theList.add(new TimeSpendOption("Scrying", "Study", 
				(state) -> {return state.getPlayerCharacter().getFlags().containsKey("canStudyScrying");},
				10, (time, state) -> {
					studyMagicWrapper(state, "Scrying", time, new Float(.12));
					otherStudyEffectsWrapper(state.getPlayerCharacter(), "Scrying",
							new Float(.6), new Float (27), new Float(0), time);
				}));
		
		theList.add(new TimeSpendOption("Enchantment", "Study", 
				(state) -> {return state.getPlayerCharacter().getFlags().containsKey("canStudyEnchantment");},
				10, (time, state) -> {
					studyMagicWrapper(state, "Enchantment", time, new Float(.09));
					otherStudyEffectsWrapper(state.getPlayerCharacter(), "Enchantment",
							new Float(.8), new Float (27), new Float(0), time);
				}));
		
		theList.add(new TimeSpendOption("Shapeshifting", "Study", 
				(state) -> {return state.getPlayerCharacter().getFlags().containsKey("canStudyShapeshifting");},
				10, (time, state) -> {
					studyMagicWrapper(state, "Shapeshifting", time, new Float(.09));
					otherStudyEffectsWrapper(state.getPlayerCharacter(), "Shapeshifting",
							new Float(.8), new Float (27), new Float(0), time);
				}));
		
		theList.add(new TimeSpendOption("Illusion", "Study", 
				(state) -> {return state.getPlayerCharacter().getFlags().containsKey("canStudyIllusion");},
				10, (time, state) -> {
					studyMagicWrapper(state, "Illusion", time, new Float(.15) );	
					otherStudyEffectsWrapper(state.getPlayerCharacter(), "Illusion",
							new Float(.4), new Float (17), new Float(0), time);
				}));
		
		theList.add(new TimeSpendOption("Geomancy", "Study", 
				(state) -> {return state.getPlayerCharacter().getFlags().containsKey("canStudyGeomancy");},
				10, (time, state) -> {
					studyMagicWrapper(state, "Geomancy", time, new Float(.09) );	
					otherStudyEffectsWrapper(state.getPlayerCharacter(), "Geomancy",
							new Float(.4), new Float (27), new Float(0), time);
				}));
		
		theList.add(new TimeSpendOption("Blood Magic", "Study", 
				(state) -> {return state.getPlayerCharacter().getFlags().containsKey("canStudyBlood Magic");},
				10, (time, state) -> {
					studyMagicWrapper(state, "Blood Magic", time, new Float(.12) );	
					otherStudyEffectsWrapper(state.getPlayerCharacter(), "Blood Magic",
							new Float(1.0), new Float (27), new Float(0), time);
				}));
		
		//practice mundane skills options:
		theList.add(new TimeSpendOption("Mystique", "Practice", 
				(state) -> {return state.getPlayerCharacter().getFlags().containsKey("canPracticeMystique");},
				10, (time, state) -> {
					studyMagicWrapper(state, "Mystique", time, new Float(.1));	
				}));
		theList.add(new TimeSpendOption("Skulduggery", "Practice", 
				(state) -> {return state.getPlayerCharacter().getFlags().containsKey("canPracticeSkulduggery");},
				10, (time, state) -> {
					studyMagicWrapper(state, "Skulduggery", time, new Float(.1));	
				}));
		theList.add(new TimeSpendOption("Scuffle", "Practice", 
				(state) -> {return state.getPlayerCharacter().getFlags().containsKey("canPracticeScuffle");},
				10, (time, state) -> {
					studyMagicWrapper(state, "Scuffle", time, new Float(.1));	
				}));
		theList.add(new TimeSpendOption("Lore", "Practice", 
				(state) -> {return state.getPlayerCharacter().getFlags().containsKey("canPracticeLore");},
				10, (time, state) -> {
					studyMagicWrapper(state, "Lore", time, new Float(.1));	
				}));
		
		
		
		
		
		//working options
		theList.add(new TimeSpendOption("Bartending", "Work", 
				(state) -> {return state.getPlayerCharacter().getFlags().containsKey("canDoBartending");},
				10, (time, state) -> {
					state.getPlayerCharacter().changeStatBy("Gold", new Float(time));
				}));
		theList.add(new TimeSpendOption("Scribing", "Work", 
				(state) -> {return state.getPlayerCharacter().getFlags().containsKey("canDoScribing");},
				10, (time, state) -> {
					state.getPlayerCharacter().changeStatBy("Gold", new Float(time));
					}));
		theList.add(new TimeSpendOption("Street Cons", "Work", 
				(state) -> {return state.getPlayerCharacter().getFlags().containsKey("canDoStreet Cons");},
				10, (time, state) -> {
					state.getPlayerCharacter().changeStatBy("Gold", new Float(time));
					}));
		theList.add(new TimeSpendOption("Thievery", "Work", 
				(state) -> {return state.getPlayerCharacter().getFlags().containsKey("canDoThievery");},
				10, (time, state) -> {
					state.getPlayerCharacter().changeStatBy("Gold", new Float(time));
					}));
		theList.add(new TimeSpendOption("Street Tricks", "Work", 
				(state) -> {return state.getPlayerCharacter().getFlags().containsKey("canDoStreet Tricks");},
				10, (time, state) -> {
					state.getPlayerCharacter().changeStatBy("Gold", new Float(time));
					}));
		theList.add(new TimeSpendOption("Shop Assistant", "Work", 
				(state) -> {return state.getPlayerCharacter().getFlags().containsKey("canDoShop Assistant");},
				10, (time, state) -> {
					state.getPlayerCharacter().changeStatBy("Gold", new Float(time));
					}));
		theList.add(new TimeSpendOption("Wizard for Hire", "Work", 
				(state) -> {return state.getPlayerCharacter().getFlags().containsKey("canDoWizard for Hire");},
				10, (time, state) -> {
					state.getPlayerCharacter().changeStatBy("Gold", new Float(time));
					}));
		theList.add(new TimeSpendOption("Courier", "Work", 
				(state) -> {return state.getPlayerCharacter().getFlags().containsKey("canDoCourier");},
				10, (time, state) -> {
					state.getPlayerCharacter().changeStatBy("Gold", new Float(time));
					}));
		
		
		
		return theList;
	}
	
	public static TimeSpendOption getRestTimeSpendOption(){
		//This returns the rest option as a TimeSpendOption
		return new TimeSpendOption("Rest and Relaxation", "Rest", (state) -> {return true;},
				100, (time, state) -> {
					Float stress = state.getPlayerCharacter().getStat("Stress");
					//note: time may be negative here, so this may increase stress levels
					stress = Math.max(new Float(0), stress - time);
					state.getPlayerCharacter().setStat("Stress", stress);
				});
	}
	
	public static HashMap<String, ArrayList<TimeSpendOption>> getSpendActivities(GameState state){
		/*
		 * this method is used to grab the currently-available TimeSpendOptions, grouped by type.
		 * It returns a hashmap, where the key is a String indicating type, and the value is
		 * an arrayList of the available TimeSpendOptions which have that group.
		 */
		
		HashMap<String, ArrayList<TimeSpendOption>> outputHashMap 
			= new HashMap<String, ArrayList<TimeSpendOption>>();
		
		ArrayList<TimeSpendOption> allOptions = getTimeSpendOptions();
		
		//loop over all the options, creating a new key if needed, or adding to existing list if not
		for(TimeSpendOption option : allOptions){
			if(option.isAvailable(state)){
				if(outputHashMap.containsKey(option.getType())){
					outputHashMap.get(option.getType()).add(option);
				} else {
					ArrayList<TimeSpendOption> tempList = new ArrayList<TimeSpendOption>();
					tempList.add(option);
					outputHashMap.put(option.getType(), tempList);
				}
			}
		}
		
		
		return outputHashMap;
	}
	
	
}
