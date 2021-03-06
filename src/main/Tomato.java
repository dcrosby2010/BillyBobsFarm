package main;

/**
 * @author Dan Crosby & Leanne Kendrick
 * Custom class for Carrots which has an "Is-A" relationship to a Plant.
 */
public class Tomato extends Plant {
	//plantIndex is a number from 0 to 4 that identifies the plant.
	final private static int plantIndex=1;
	//plantName is a String identifies the plant.
	final private static String plantName="Tomato";

	/**
	 * Create an instance of the Tomato class 
	 * @param int quantity - The number of tomato plants
	 */  
	Tomato(int quantity) {
			//Any method which calls this constructor must trap any exceptions that result
			super(plantIndex,quantity);
			//Recording the round when these plant(s) were purchased for posterity sake.
			super.purchaseRound=Main.currentRound;
			//Recording when these plants will be available for harvest.
			super.maturityRound=Main.currentRound+Main.maturityRounds[plantIndex]-1;
			//Recording until which round these plants will continue to yield fruit
			super.maxHarvestRound=maturityRound+Main.harvestRounds[plantIndex]-1;	
			System.out.format("Thank you for purchasing %d Tomato seed(s), which are now growing (1/2).\n", quantity);
			}
	
	/**
	 * The getType method 
	 * Purpose:  Return the type of the class, which overrides the Plant class 
	 * @return The String name of the plant
	 */
	@Override
	String getType() {
		return plantName;
	}

	/**
	 * The getIndex method
	 * Purpose: Return the type of the class, which overrides the Plant class 
	 * @return The integer ID of the plant
	 */
	@Override
	int getIndex() {
		return plantIndex;
	}
	
	/**
	 * The checkPlantProgress method
	 * Purpose: At the end of each round, this method will be called on each Tomato purchase
	 * If a harvest occurs, increase cash
	 * If a disaster occurs, decrease plant quantity
	 * If maxHarvestRounds exceeded, decrease plant quantity to zero
	 */
	@Override
	public void checkPlantProgress()
	{
		//System.out.format("Debug %s purchased round %d maturity round %d max harvest round %d\n",plantName,super.purchaseRound,super.maturityRound,super.maxHarvestRound);
		if (super.plantQuantity<1) return;
		if (super.maxHarvestRound<Main.currentRound) {
			int plantsDestroyed=super.plantQuantity;
			System.out.format("Your %d %s plant(s) from round %d are no longer productive and are being retired.", plantsDestroyed, plantName, super.purchaseRound);
			//Decrease the amount of space used based on plants destroyed
			Main.farmList.get(Main.currentFarm).changeSpace(-1*plantsDestroyed*Main.squareFootage[plantIndex]);
			super.plantQuantity=0;
		}

		// Check to see if the tomato patch has tomato bugs
		//If yes, reduce the plantQuantity for any plants killed as appropriate
		if ((Math.random()*100)>75 && super.plantQuantity>0) {
			//If a tractor occurs, there will be an evenly weighted chance for the number of plants destroyed.
			int plantsDestroyed=(int)(super.plantQuantity*Math.random())+1;
			//check for a portion of plants destroyed
			try {
				Main.textToConsole("img/tomatobug.txt");
			}
			catch (Exception ex) {
				//not important if this fails so not doing anything
			}
			System.out.format("Tomato bugs have booked AirB&B in your garden, and %d of %d plants are destroyed!", plantsDestroyed,super.plantQuantity);
			super.plantQuantity-=plantsDestroyed;
			//Decrease the amount of space used based on plants destroyed
			Main.farmList.get(Main.currentFarm).changeSpace(-1*plantsDestroyed*Main.squareFootage[plantIndex]);
		}

		//If there are still any plants left at this point, then go ahead and call the checkPlantProgress of the base Plant class.
		if (Main.currentRound>=super.maturityRound && Main.currentRound<=super.maxHarvestRound && super.plantQuantity>0) {
			//System.out.format("%d tomato plant(s) are ready for harvest! (2/2).", super.plantQuantity);
			super.checkPlantProgress();
			return;
		}

		//Display progress messages on any plants not yet mature.
		if (super.maturityRound==Main.currentRound+1 && super.plantQuantity>0) {
			System.out.format("%d tomato plant(s) are producing tiny, green tomatoes (1/2).", super.plantQuantity);
		}
	}


}


