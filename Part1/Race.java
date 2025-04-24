import java.util.concurrent.TimeUnit;
import java.util.Scanner;
import java.lang.Math;

/**
 * A three-horse race, each horse running in its own lane
 * for a given distance.
 * 
 * @author Xhoel
 * @version 2.0
 */
public class Race {
    private int raceLength;
    private Horse lane1Horse;
    private Horse lane2Horse;
    private Horse lane3Horse;

    /**
     * Constructor for objects of class Race
     * Initially there are no horses in the lanes
     * 
     * @param distance the length of the racetrack (in metres/yards...)
     */
    public Race(int distance) {
        raceLength = distance;
        lane1Horse = null;
        lane2Horse = null;
        lane3Horse = null;
    }

    /**
     * Adds a horse to the race in a given lane
     * 
     * @param theHorse the horse to be added to the race
     * @param laneNumber the lane that the horse will be added to
     */
    public void addHorse(Horse theHorse, int laneNumber) {
        if (laneNumber == 1) {
            lane1Horse = theHorse;
        } else if (laneNumber == 2) {
            lane2Horse = theHorse;
        } else if (laneNumber == 3) {
            lane3Horse = theHorse;
        } else {
            System.out.println("Cannot add horse to lane " + laneNumber + " because there is no such lane");
        }
    }

    /**
     * Start the race
     * The horses are brought to the start and
     * then repeatedly moved forward until the 
     * race is finished
     */
    public void startRace() {
        boolean finished = false;

        lane1Horse.goBackToStart();
        lane2Horse.goBackToStart();
        lane3Horse.goBackToStart();

        
        while (!finished) {
            moveHorse(lane1Horse);
            moveHorse(lane2Horse);
            moveHorse(lane3Horse);

            printRace();

            if (raceWonBy(lane1Horse) || raceWonBy(lane2Horse) || raceWonBy(lane3Horse)) {
                finished = true;
            }

            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Show winner name after race ends
        String winner = null;
        if (lane1Horse.getDistanceTravelled() == raceLength) {
            winner = lane1Horse.getName();
        } else if (lane2Horse.getDistanceTravelled() == raceLength) {
            winner = lane2Horse.getName();
        } else if (lane3Horse.getDistanceTravelled() == raceLength) {
            winner = lane3Horse.getName();
        }

        if (winner != null) {
            System.out.println("\nThe winner is " + winner + "!");
        } else {
            System.out.println("\nNo horse finished the race!");
        }
    }

    /**
     * Moves the horse forward or makes it fall based on confidence
     */
    private void moveHorse(Horse theHorse) {
        if (!theHorse.hasFallen()) {
            if (Math.random() < theHorse.getConfidence()) {
                theHorse.moveForward();
            }

            if (Math.random() < (0.1 * theHorse.getConfidence() * theHorse.getConfidence())) {
                theHorse.fall();
            }
        }
    }

    /**
     * Determines if the horse has won
     */
    private boolean raceWonBy(Horse theHorse) {
        if (theHorse.getDistanceTravelled() == raceLength) {
            return true;
        }
        return false;
    }

    /**
     * Print the entire race display
     */
    private void printRace() {
        System.out.print('\u000C');  // Clears the terminal window (on BlueJ or similar)
        multiplePrint('=', raceLength + 3); // top edge
        System.out.println();

        printLane(lane1Horse);
        System.out.print(" " + lane1Horse.getName() + " (Current confidence " + String.format("%.2f", lane1Horse.getConfidence()) + ")");
        System.out.println();

        printLane(lane2Horse);
        System.out.print(" " + lane2Horse.getName() + " (Current confidence " + String.format("%.2f", lane2Horse.getConfidence()) + ")");
        System.out.println();

        printLane(lane3Horse);
        System.out.print(" " + lane3Horse.getName() + " (Current confidence " + String.format("%.2f", lane3Horse.getConfidence()) + ")");
        System.out.println();

        multiplePrint('=', raceLength + 3); // bottom edge
        System.out.println();
    }

    /**
     * Print one lane with the horse's position
     */
    private void printLane(Horse theHorse) {
        int spacesBefore = theHorse.getDistanceTravelled();
        int spacesAfter = raceLength - theHorse.getDistanceTravelled();

        System.out.print('|');
        multiplePrint(' ', spacesBefore);

        if (theHorse.hasFallen()) {
            System.out.print('\u2322'); 
        } else {
            System.out.print(theHorse.getSymbol());
        }

        multiplePrint(' ', spacesAfter);
        System.out.print('|');
    }

    /**
     * Helper to print a character multiple times
     */
    private void multiplePrint(char aChar, int times) {
        for (int i = 0; i < times; i++) {
            System.out.print(aChar);
        }
    }

//allowes the user to enter the length of the race, the horses name and symbol
   public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Ask user for race length using parse
        System.out.print("Enter the race length: ");
        int raceLength = Integer.parseInt(scanner.nextLine());

        Race race = new Race(raceLength);

        // Loop to create 3 horses with user input
        for (int i = 1; i <= 3; i++) {
            System.out.println("\nEnter details for Horse " + i + ":");

            System.out.print("Name: ");
            String name = scanner.nextLine();

            System.out.print("Symbol (single character): ");
            char symbol = scanner.nextLine().charAt(0);

            Horse horse = new Horse(symbol, name, 0.5);
            race.addHorse(horse, i);
        }

        // Start the race
        race.startRace();

        scanner.close();
    }

}
