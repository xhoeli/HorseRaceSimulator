package Part1;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents individual race participants.
 * 
 * @author Xhoel Permeti 
 * @version 1.0
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
        // slightly decrease confidence once the horse has fallen
        confidence = confidence - 0.1; 
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


    public void addRaceTime(int ms) { raceTimes.add(ms); }
    
    public void recordRaceEnd(boolean won, double confidence) {
        totalRaces++;
        if (won) wins++;
        confidenceHistory.add(confidence);
    }
    
    public int getWins() { return wins; }
    
    public int getTotalRaces() { return totalRaces; }
    
    public double getWinRatio() { return totalRaces > 0 ? (double) wins / totalRaces : 0.0; }
    
    public List<Double> getConfidenceHistory() { return confidenceHistory; }
    
    public List<Integer> getRaceTimes() { return raceTimes; }
    

    
    private List<Integer> distanceHistory = new ArrayList<>();

    // Track how much distance a horse traveled in a race
    public void addDistance(int distance) {
        totalDistance += distance;
        distanceHistory.add(distance);
    }

    public int getTotalDistance() {
        return totalDistance;
    }

    public double getAverageDistance() {
        return distanceHistory.isEmpty() ? 0.0 : (double) totalDistance / distanceHistory.size();
    }


}
