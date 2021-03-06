package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Dan Crosby & Leanne Kendrick
 * Each player will have their own farm.  (The correct farm can be selected using the Main.currentFarm variable)
 * The main class will have an ArrayList of type ArrayList<Farm>
 * To access the appropriate Farm use farmList.get(Main.currentFarm)
 * Farms can contain plants via ArrayList plantList
 * Farms have their own fields for playerCash, playerName, farmSize, and spaceUsed.
 */
public class Farm {
	//Name of the player (each Farm has-a player)
	private String playerName;
	//Size of the farm
	private int farmSize;
	//Current space used on the farm.  This value is checked before new plants can be purchased.
	private float spaceUsed=0;
	//The current amount of cash that the player has for use on this farm.
	private double playerCash;

	//ArrayList of type Plant for storing all plants that have been purchased for this farm.
	//This is will be used with Polymorphism since the actual objects will be on custom plant types that extend Plant.
	protected ArrayList<Plant> plantList = new ArrayList<>();

	/**
	 * The Farm method (constructor)
	 * Purpose: Create an instance of the Farm class 
	 * @param playerName String name of the player
	 * @param farmSize integer size for how much space there will be
	 * @param playerCash - how much money (double) the player will have
	 */
	Farm (String playerName,int farmSize, double playerCash)
	{
		this.playerCash=playerCash;
		this.farmSize=farmSize;
		this.playerName=playerName;
	}

	/**
	 * The getPlayerName method
	 * Purpose: Getter to return the name of the player 
	 * @return name of the player
	 */
	public String getPlayerName() {
		return this.playerName;
	}
	
	/**
	 * The getPlayer cash method
	 * Purpose: Getter to return the amount of cash the player has 
	 * @return current cash amount
	 */
	public double getPlayerCash() {
		return this.playerCash;
	}
	
	/**
	 * The getSpaceAvailable method
	 * Purpose: Getter to return the amount of farm space available
	 * @return current space available
	 */
	public double getSpaceAvailable() {
		return this.farmSize-spaceUsed;
	}
	
	/**
	 * The getFarmSize method
	 * Purpose: Getter to return the total farm size
	 * @return double - current farm size
	 */
	public double getFarmSize() {
		return this.farmSize;
	}
	
	/**
	 * The changeCash method
	 * Purpose: Setter to change the amount of player cash
	 * @param double - change current player cash amount
	 */
	protected void changeCash(double amount) {
		if (amount+this.playerCash<0) {
			throw new NotEnoughMoneyException();
		}
		else
		{
			this.playerCash+=amount;
		}
	}
	
	/**
	 * The changeSpace method
	 * Purpose: Setter to change the amount of available space
	 * @param double - change current farm available space
	 */
	protected void changeSpace(double space) {
		if (space>this.getSpaceAvailable()) {
			throw new NotEnoughLandException();
		}
		else
		{
			this.spaceUsed+=space;
		}
	}
	
	/**
	 * The processRound method
	 * Purpose: At the end of each round, call this method 
	 * Method will loop through each plant use the checkPlantProgress, which is polymorphic
	 */
	public void processRound ()
	{
		//Loop through each plant in the plants arrayList
		for (Plant pl: plantList) {
			pl.checkPlantProgress();
		}
				
		Main.promptEnterKey();
	}

	/**
	 * The getCount method
	 * Purpose: Getter for counting the number of plants of a certain name.
	 * @param String - the name of the plant to check
	 * @return int - the count of plants
	 */
	public int getCount(String plant) {
		//Temporary variable for calculating the quantity of plants with a certain name
		int qty=0;
		for (Plant pl: plantList) {
		   if (pl.getType()==plant) qty+=pl.plantQuantity;
		}
		return qty;
	}

	/**
	 * The addPlant method
	 * Purpose: Method to create custom plant objects for use in polymorphism
	 * @param int - the index of the plant to add
	 * @param int - the count of plants
	 */
	protected void addPlant(int plant, int qty) {
		switch (plant) {
		
		case 0: 
			this.plantList.add(new Carrot (qty));
		    break;
			
		case 1: 
			this.plantList.add(new Tomato (qty));
			break;
			
		case 2: 
			this.plantList.add(new Potato (qty));
			break;
			
		case 3: 
			this.plantList.add(new Corn (qty));
			break;
			
		case 4: 
			this.plantList.add(new Watermelon(qty));
			//System.out.format("Added new watermelon.  New list count=%d.\n",plantList.size());
			break;
		default:
			System.out.format("Error: Plant ID %d not implemented.",plant);
			break;
		}
	}
	
	/** 
	 * The logGame method - 
	 * Purpose:  log the game results to the high scores file
	 * Note that at this point the game is simply going to log all completed games
	 * Rather than trying to determine which is the most worthy of remaining in the log.
	 */
	public void logGame() {
    	//boolean to track if the highscores file needs a header
		boolean needsHeader=false;
    	//pointer to the high scores file object
        File file = new  File("highscores.txt");
        //header to write to the high scores file as needed
        String header = "Player Name      Money Earned\n-----------      ------------\n";
        //The actual scores to write out to the file
        String content = String.format("%-16s $%-5.2f\n",playerName,playerCash);

        try {

	        // if file doesn't exists, then create it
	        if (!file.exists()) {
	            file.createNewFile();
	            needsHeader=true;
	        }

	        FileWriter fw = new FileWriter(file.getAbsoluteFile(),true);
	        BufferedWriter bw = new BufferedWriter(fw);
	        if (needsHeader) { 
		        bw.write(header);
	        }
	        
	        bw.write(content);
	        bw.close();

	        System.out.println("High scores file updated.");

	    } catch (IOException e) {
	        e.printStackTrace();
	        System.out.format("Failed to update high scores file: %s",e.toString());
	    }
	}
}
