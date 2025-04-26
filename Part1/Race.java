package Part1;

import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.lang.Math;


/**
 * A three-horse race, each horse running in its own lane
 * for a given distance.
 * 
 * @author Xhoel Permeti
 * @version 4.0
 */
public class Race {
    private int raceLength;
    private int laneCount;
    List<Horse> horses;

    public Race(int distance, int laneCount) {
        raceLength = distance;
        this.laneCount = laneCount;
        horses = new ArrayList<>(laneCount);
        for (int i = 0; i < laneCount; i++) {
            horses.add(null);
        }
    }

    // Accessor method to get horses
public List<Horse> getHorses() {
    return horses;
}

// Public method to check if a lane is occupied
public boolean isLaneOccupied(int lane) {
    return horses.get(lane) != null;
}

// Public method to check if all horses have fallen
public boolean allHorsesHaveFallenPublic() {
    return allHorsesHaveFallen();
}

// Public method to check if any horse has finished
public boolean anyHorseHasFinishedPublic() {
    return anyHorseHasFinished();
}


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

    public void startRace(double speedModifier, double fallModifier) {
        boolean finished = false;

        for (Horse horse : horses) {
            if (horse != null) horse.goBackToStart();
        }

        while (!finished) {
            for (Horse horse : horses) {
                if (horse != null) moveHorse(horse, speedModifier, fallModifier);
            }

            printRace();

            if (allHorsesHaveFallen() || anyHorseHasFinished()) {
                finished = true;
            }

            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public int getRaceLength() {
        return raceLength;
    }

    private void moveHorse(Horse theHorse, double speedModifier, double fallModifier) {
        if (!theHorse.hasFallen()) {
            if (Math.random() < theHorse.getConfidence() * speedModifier) {
                theHorse.moveForward();
            }

            if (Math.random() < (0.1 * theHorse.getConfidence() * theHorse.getConfidence() * fallModifier)) {
                theHorse.fall();
            }
        }
    }

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

    public String getWinner() {
        for (Horse horse : horses) {
            if (horse != null && raceWonBy(horse)) {
                return horse.getName();
            }
        }
        return null;
    }

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

    private void multiplePrint(char aChar, int times) {
        for (int i = 0; i < times; i++) {
            System.out.print(aChar);
        }
    }


    private int startTime;

    public void startTimer() { startTime = (int) System.currentTimeMillis(); }

    public int endTimer() { return (int) System.currentTimeMillis() - startTime; }


    // Main method
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        List<Horse> horses = new ArrayList<>();
        double speedModifier = 1.0;
        double fallModifier = 1.0;
        int raceLength = 0;
        int laneCount = 0;

        while (true) {
            if (horses.isEmpty()) {
                // First time setup
                boolean validLengthInput = false;
                boolean validLaneInput = false;
                boolean validHorseInput = false;
                List<String> horseNames = new ArrayList<>();

                while (!validLengthInput) {
                    System.out.print("Enter the race length in meters (positive integer): ");
                    String raceInput = scanner.nextLine();
                    try {
                        raceLength = Integer.parseInt(raceInput);
                        if (raceLength > 0) {
                            validLengthInput = true;
                        } else {
                            System.out.println("Please enter a number greater than 0.");
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
                        if (laneCount > 0) {
                            validLaneInput = true;
                        } else {
                            System.out.println("Please enter a number greater than 0.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("That's not a valid number. Try again.");
                    }
                }

                int nrHorses = 0;
                while (!validHorseInput) {
                    System.out.print("Enter the number of horses (positive integer): ");
                    String horsesInput = scanner.nextLine();
                    try {
                        nrHorses = Integer.parseInt(horsesInput);
                        if (nrHorses > 0 && nrHorses <= laneCount) {
                            validHorseInput = true;
                        } else {
                            System.out.println("Invalid number of horses.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("That's not a valid number. Try again.");
                    }
                }

                Race race = new Race(raceLength, laneCount);

                for (int i = 0; i < nrHorses; i++) {
                    String name = null;
                    while (true) {
                        System.out.print("Enter name for Horse " + (i+1) + ": ");
                        name = scanner.nextLine();
                        if (name.length() > 0 && !horseNames.contains(name)) {
                            horseNames.add(name);
                            break;
                        } else {
                            System.out.println("Please enter a unique name for the horse.");
                        }
                    }

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

                    Horse horse = new Horse(symbol, name, 0.5); // default confidence
                    horses.add(horse);

                    int lane;
                    do {
                        lane = (int) (Math.random() * laneCount);
                    } while (race.horses.get(lane) != null);

                    race.addHorse(horse, lane);
                }
            }

            Race race = new Race(raceLength, laneCount);
            for (Horse horse : horses) {
                int lane;
                do {
                    lane = (int) (Math.random() * laneCount);
                } while (race.horses.get(lane) != null);
                race.addHorse(horse, lane);
            }

            race.startRace(speedModifier, fallModifier);

            String winner = race.getWinner();
            if (winner != null) {
                System.out.println("\nWinner: " + winner);
            } else {
                System.out.println("\nNo winner.");
            }

            for (Horse horse : horses) {
                if (winner != null && horse.getName().equals(winner)) {
                    horse.setConfidence(Math.min(1.0, horse.getConfidence() + 0.1));
                } else {
                    horse.setConfidence(Math.max(0.0, horse.getConfidence() - 0.1));
                }
            }

            System.out.print("\nDo you want to race again? (yes/no): ");
            String again = scanner.nextLine().trim().toLowerCase();
            if (!again.equals("yes")) {
                System.out.println("Goodbye!");
                break;
            }
        }

        scanner.close();
    }
}
