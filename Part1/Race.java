package Part1;

import java.util.List;
import java.util.ArrayList;
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
    private int laneCount;
    private List<Horse> horses;
    
    /**
     * Constructor for objects of class Race
     * Initially there are no horses in the lanes
     * 
     * @param distance the length of the racetrack (in metres/yards...)
     */
    public Race(int distance, int laneCount) {
        raceLength = distance;
        this.laneCount = laneCount;
        this.horses = new ArrayList<>(laneCount);
        for (int i = 0; i < laneCount; i++) {
            horses.add(null); // Initialize all lanes as empty
        }
    }

    /**
     * Adds a horse to the race in a given lane
     * 
     * @param theHorse the horse to be added to the race
     * @param laneNumber the lane that the horse will be added to
     */
    public void addHorse(Horse theHorse, int laneNumber) {
        if (laneNumber >= 0 && laneNumber < laneCount) {
            if (horses.get(laneNumber) != null) {
                System.out.println("Lane " + laneNumber + " is already occupied by " + horses.get(laneNumber).getName());
            } else {
                horses.set(laneNumber, theHorse);
            }
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

        for (Horse horse : horses) {
            if (horse != null) horse.goBackToStart();
        }

        while (!finished) {
            for (Horse horse : horses) {
                if (horse != null) moveHorse(horse);
            }

            printRace();

            //the race stops if all the horses have fallen or if one the horses has finished the race
            if (allHorsesHaveFallen() || anyHorseHasFinished()) {
                finished = true;
            }

            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Show winner name after race ends
        String winner = getWinner();

        if (winner != null) {
            System.out.println("\nThe winner is " + winner + "!");
        } else {
            System.out.println("\nNo horse finished the race!");
        }
    }

    public int getRaceLength() {
        return raceLength;
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

    private boolean anyHorseHasFinished() {
        for (Horse horse : horses) {
            if (horse != null && raceWonBy(horse)) {
                return true;
            }
        }
        return false;
    }

    private boolean allHorsesHaveFallen() {
        for (Horse horse : horses) {
            if (horse != null && !horse.hasFallen()) {
                return false;
            }
        }
        return true;
    }

    private String getWinner() {
        for (Horse horse : horses) {
            if (horse != null && raceWonBy(horse)) {
                return horse.getName();
            }
        }
        return null;
    }

    /**
     * Print the entire race display
     */
    private void printRace() {
        System.out.print('\u000C');
        multiplePrint('=', raceLength + 3);
        System.out.println();

        for (Horse horse : horses) {
            if (horse != null) {
                printLane(horse);
                System.out.print(" " + horse.getName() + " (Current confidence " + String.format("%.2f", horse.getConfidence()) + ")");
            } else {
                printEmptyLane();
            }
            System.out.println();
        }

        multiplePrint('=', raceLength + 3);
        System.out.println();
    }

    private void printEmptyLane() {
        System.out.print('|');
        multiplePrint(' ', raceLength + 1);
        System.out.print('|');
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
        int laneCount = 0;
        int nrHorses = 0;
        boolean validLengthInput = false;
        boolean validLaneInput = false;
        boolean validHorseInput = false;

        /*
        *  user input validation for int
        */
        while (!validLengthInput) {
            System.out.print("Enter the race length in meters (positive integer): ");
                String raceInput = scanner.nextLine();

                try {
                    raceLength = Integer.parseInt(raceInput);
                    if (raceLength <= 0) {
                        System.out.println("Please enter a number greater than 0.");
                    } else {
                        validLengthInput = true;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("That's not a valid number. Try again.");
                }
        }

        while (!validLaneInput) {
            System.out.print("Enter the lane count (positive integer): ");
            String laneInput = scanner.nextLine();

            try {
                laneCount = Integer.parseInt(laneInput);
                if (laneCount <= 0) {
                    System.out.println("Please enter a number greater than 0.");
                } else {
                    validLaneInput = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("That's not a valid number. Try again.");
            }
        }

        while (!validHorseInput) {
            System.out.print("Enter the number of horses (positive integer): ");
            String horsesInput = scanner.nextLine();

            try {
                nrHorses = Integer.parseInt(horsesInput);
                if (nrHorses <= 0) {
                    System.out.println("Please enter a number greater than 0.");
                } else if (nrHorses > laneCount) {
                    System.out.println("You can't have more horses than lanes. Please enter a number less than or equal to " + laneCount + ".");
                } else {
                    validHorseInput = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("That's not a valid number. Try again.");
            }
        }

        Race race = new Race(raceLength, laneCount);

        /*
        *  validates user input 
        */
        for (int i = 0; i < nrHorses; i++) {
            System.out.println("\nEnter details for Horse " + (i+1) + ":");

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
            

            int lane;

            do {
                lane = (int) (Math.random() * laneCount);
                } while (race.horses.get(lane) != null);

            race.addHorse(horse, lane);

            System.out.println("Horse " + name + " added to lane " + lane + ".");
        }

        race.startRace();
        scanner.close();
    }
}
