package Part1;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents individual race participants.
 * 
 * @author Xhoel Permeti 
 * @version 2.0
 */
public class Horse
{
    //Fields of class Horse
    private String name;
    private char symbol;
    private int distanceTravelled;
    private boolean hasFallen;
    private double confidence;
      
    private int wins = 0, totalRaces = 0;
    private List<Double> confidenceHistory = new ArrayList<>();
    private List<Integer> raceTimes = new ArrayList<>();
    
    private int totalDistance = 0;
    
    
    
    

    //Constructor of class Horse
    /**
     * Constructor for objects of class Horse
     */
    public Horse(char horseSymbol, String horseName, double horseConfidence)
    {
        symbol = horseSymbol;
        name = horseName;
        setConfidence(horseConfidence); 
        distanceTravelled = 0;
        hasFallen = false;
    }
    

    //Other methods of class Horse

    /**
     * Marks this horse as fallen.
     */
    public void fall()
    {
        hasFallen = true;

    }
    
    /**
     * Returns the confidence rating of the horse.
     */
    public double getConfidence()
    {
        return confidence;
    }
    
    /**
     * Returns the distance the horse has travelled so far.
     */
    public int getDistanceTravelled()
    {
        return distanceTravelled;
    }
    
    /**
     * Returns the name of the horse.
     */
    public String getName()
    {
        return name;   
    }
    
    /**
     * Returns the symbol used to represent the horse.
     */
    public char getSymbol()
    {
        return symbol;
    }
    
     /**
     * Resets the horse’s state to the beginning of the race.
     */
    public void goBackToStart()
    {
        distanceTravelled = 0;
        hasFallen = false;
    }
    
    /**
     * Checks if the horse has fallen.
     */
    public boolean hasFallen()
    {
        return hasFallen;
    }

    /**
     * Moves the horse forward by 1 unit.
     */
    public void moveForward()
    {
        distanceTravelled++;
    }

    /** accounts for errors
     * Sets a new confidence rating for the horse.
     */
    public void setConfidence(double newConfidence)
    {
        
        if (newConfidence < 0) {
            confidence = 0;
            System.out.println("Confidence rating cannot be lower than 0. Setting to 0.");
        } else if (newConfidence > 1) {
            confidence = 1;
            System.out.println("Confidence rating cannot exceed 1. Setting to 1.");
        } else {
            confidence = newConfidence;
        }
    }
    
    /**
     * Sets a new symbol for the horse.
     */
    public void setSymbol(char newSymbol)
    {
        symbol = newSymbol;
    }

    /**
     * creates a timer for the horse in ms
     */
    public void addRaceTime(int ms) { raceTimes.add(ms); }
    

    /**
     * Keeps a record for each horse
     */
    public void recordRaceEnd(boolean won, double confidence) {
        totalRaces++;
        if (won) wins++;
        confidenceHistory.add(confidence);
    }
    
    /**
     * Returns the number of wins for this horse.
     */

    public int getWins() { 
        return wins; 
    }
    
    /**
     * Returns the number of races it has participated
    */
    public int getTotalRaces() { 
        return totalRaces; 
    }
    
    /*  
     * Returns the win ratio of the horse.
     */
    public double getWinRatio() { 
        return totalRaces > 0 ? (double) wins / totalRaces : 0.0; 
    }
    
    /*
     * Returns the confidence history of the horse.
     */
    public List<Double> getConfidenceHistory() { 
        return confidenceHistory; 
    }
    
    /*
     * Returns a list of race times recorded
    */
    public List<Integer> getRaceTimes(){ 
        return raceTimes; 
    }
    

    
    private List<Integer> distanceHistory = new ArrayList<>();

    // adds the distance trveled by the horse to the totasl distance
    public void addDistance(int distance) {
        totalDistance += distance;
        distanceHistory.add(distance);
    }

    // returns the total distance traveled by the horse
    public int getTotalDistance() {
        return totalDistance;
    }

    // returns the average distance traveled by the horse between all the races
    public double getAverageDistance() {
        if (distanceHistory.isEmpty()) {
            return 0.0;
        } else {
            return (double) totalDistance / distanceHistory.size();
        }
        
    }

    //returns the time taken for the most recent race
    public int getLastRaceTime() {
        if (raceTimes.isEmpty()) {
            return 0;
        }
        return raceTimes.get(raceTimes.size() - 1); // last race's time
    }
    


}
