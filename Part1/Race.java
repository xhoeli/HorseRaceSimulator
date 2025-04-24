import java.util.concurrent.TimeUnit;
import java.util.Scanner;
import java.lang.Math;

/**
 * A three-horse race, each horse running in its own lane
 * for a given distance.
 * 
 * @author Xhoel
 * @version 3.0
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

            //made so when all horses fall the race stops
            if (raceWonBy(lane1Horse) || raceWonBy(lane2Horse) || raceWonBy(lane3Horse) ||(lane1Horse.hasFallen() && lane2Horse.hasFallen() && lane3Horse.hasFallen())) {
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
     * makes it more efficient
     */
    private boolean raceWonBy(Horse theHorse) {
        return theHorse.getDistanceTravelled() == raceLength;
    }

    /**
     * Print the entire race display
     */
    private void printRace() {
        System.out.print('\u000C');
        multiplePrint('=', raceLength + 3);
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

        multiplePrint('=', raceLength + 3);
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

    //
    //Main Method
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int raceLength = 0;

        /*
        *  user input validation for int
        */
        while (true) {
            System.out.print("Enter the race length (positive integer): ");
            String input = scanner.nextLine();

            try {
                raceLength = Integer.parseInt(input);
                if (raceLength > 0) {
                    break;
                } else {
                    System.out.println("Please enter a number greater than 0.");
                }
            } catch (NumberFormatException e) {
                System.out.println("That's not a valid number. Try again.");
            }
        }

        Race race = new Race(raceLength);

        /*
        *  validates user input 
        */
        for (int i = 1; i <= 3; i++) {
            System.out.println("\nEnter details for Horse " + i + ":");

            String name;
            while (true) {
                System.out.print("Name: ");
                name = scanner.nextLine();
                if (!name.trim().isEmpty()) {
                    break;
                } else {
                    System.out.println("Name can't be empty.");
                }
            }

            /*
            *  validates the Input of the user so it accepts a char or a unicode symbol
            */
            char symbol;
            while (true) {
                System.out.print("Symbol (1 character or \\uXXXX): ");
                String userInput = scanner.nextLine().trim();

                if (userInput.length() == 1) {
                    symbol = userInput.charAt(0);
                    break;
                } else if (userInput.matches("\\\\u[0-9a-fA-F]{4}")) {
                    try {
                        int codePoint = Integer.parseInt(userInput.substring(2), 16);
                        if (Character.isValidCodePoint(codePoint)) {
                            symbol = (char) codePoint;
                            break;
                        } else {
                            System.out.println("Invalid Unicode code point.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid Unicode escape sequence.");
                    }
                } else {
                    System.out.println("Enter a single character or valid Unicode like \\u265E.");
                }
            }


            Horse horse = new Horse(symbol, name, 0.5);
            race.addHorse(horse, i);
        }

        race.startRace();
        scanner.close();
    }
}
