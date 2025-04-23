
/**
 * Write a description of class Horse here.
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
        } else if (newConfidence > 1) {
            confidence = 1;
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
}
