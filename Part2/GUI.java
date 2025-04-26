package Part2;

import Part1.Race;
import Part1.Horse;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/*
 * version 2.0
 * 
 */


public class GUI {
    private JFrame setupFrame;
    private JFrame raceFrame;
    private JTextField raceLengthField, laneCountField, horseCountField;
    private JComboBox<String> weatherBox;
    private JTextArea raceOutputArea;
    private JPanel raceButtonPanel;

    private List<Horse> horses = new ArrayList<>();
    private List<String> horseCoats = new ArrayList<>();
    private List<String> horseEmojis = new ArrayList<>();

    private int raceLength;
    private int laneCount;
    private double speedModifier = 1.0;
    private double fallModifier = 1.0;

    private Horse userBetHorse = null;
    private double userBetAmount = 0.0;
    private double userPotentialWinnings = 0.0;
    private List<String> bettingHistory = new ArrayList<>();


    public GUI() {
        showSetupWindow();
    }

    // Separate method to create setup window
    private void showSetupWindow() {
        setupFrame = new JFrame("Horse Race - Setup");
        setupFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setupFrame.setSize(500, 500);
        setupFrame.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(0, 2, 10, 5));

        raceLengthField = new JTextField();
        laneCountField = new JTextField();
        horseCountField = new JTextField();

        weatherBox = new JComboBox<>(new String[]{"Dry", "Wet", "Icy"});

        inputPanel.add(new JLabel("Race Length:"));
        inputPanel.add(raceLengthField);
        inputPanel.add(new JLabel("Lane Count:"));
        inputPanel.add(laneCountField);
        inputPanel.add(new JLabel("Horse Count:"));
        inputPanel.add(horseCountField);
        inputPanel.add(new JLabel("Weather:"));
        inputPanel.add(weatherBox);

        JButton setupButton = new JButton("Setup Horses");
        setupButton.addActionListener(this::setupHorses);

        JButton startButton = new JButton("Start Race");
        startButton.addActionListener(this::startRaceFromSetup);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(setupButton);
        buttonPanel.add(startButton);

        setupFrame.add(inputPanel, BorderLayout.NORTH);
        setupFrame.add(buttonPanel, BorderLayout.CENTER);
        setupFrame.setVisible(true);
    }

    private void setupHorses(ActionEvent e) {
        try {
            raceLength = Integer.parseInt(raceLengthField.getText().trim());
            laneCount = Integer.parseInt(laneCountField.getText().trim());
            int horseCount = Integer.parseInt(horseCountField.getText().trim());

            if (horseCount > laneCount) {
                showError("Horse count cannot exceed lane count!");
                return;
            }

            horses.clear();
            horseCoats.clear();
            horseEmojis.clear();
            speedModifier = 1.0;
            fallModifier = 1.0;

            for (int i = 0; i < horseCount; i++) {
                String name = JOptionPane.showInputDialog(setupFrame, "Enter name for Horse " + (i + 1) + ":");
                if (name == null || name.isEmpty()) {
                    name = "Horse" + (i + 1);
                }

                String symbolInput = JOptionPane.showInputDialog(setupFrame, "Enter symbol/emoji for Horse " + (i + 1) + ": (one character or emoji)");
                char symbol = (symbolInput.length() == 1) ? symbolInput.charAt(0) : (char) ('A' + i);
                horseEmojis.add(symbolInput);

                double horseConfidence = 0.5;

                // Horse Breed
                String[] breeds = {"Thoroughbred (normal)", "Arabian (fast)", "Quarter Horse (slow)"};
                String breed = (String) JOptionPane.showInputDialog(setupFrame, "Select breed for " + name + ":", "Breed", JOptionPane.QUESTION_MESSAGE, null, breeds, breeds[0]);

                if (breed != null) {
                    if (breed.startsWith("Arabian")) {
                        horseConfidence = 0.6;
                        speedModifier *= 1.1;
                        fallModifier *= 1.05;
                    } else if (breed.startsWith("Quarter")) {
                        horseConfidence = 0.4;
                        speedModifier *= 0.9;
                        fallModifier *= 0.9;
                    }
                }

                String[] coats = {"Brown", "White", "Black"};
                String coatChoice = (String) JOptionPane.showInputDialog(setupFrame, "Select coat color for " + name + ":", "Coat", JOptionPane.QUESTION_MESSAGE, null, coats, coats[0]);
                String coatEmoji = "🟫"; // default brown
                if ("White".equals(coatChoice)) coatEmoji = "⬜";
                if ("Black".equals(coatChoice)) coatEmoji = "⬛";
                horseCoats.add(coatEmoji);

                // Saddle
                String[] saddles = {"No Saddle", "Fast Saddle", "Sturdy Saddle"};
                String saddle = (String) JOptionPane.showInputDialog(setupFrame, "Select saddle for " + name + ":", "Saddle", JOptionPane.QUESTION_MESSAGE, null, saddles, saddles[0]);

                if (saddle != null) {
                    if (saddle.startsWith("Fast")) {
                        speedModifier *= 1.2;
                        fallModifier *= 1.2;
                    } else if (saddle.startsWith("Sturdy")) {
                        speedModifier *= 0.9;
                        fallModifier *= 0.8;
                    }
                }

                // Horseshoes
                String[] shoes = {"Regular Shoes", "Lightweight Shoes", "No Shoes"};
                String shoe = (String) JOptionPane.showInputDialog(setupFrame, "Select horseshoes for " + name + ":", "Horseshoes", JOptionPane.QUESTION_MESSAGE, null, shoes, shoes[0]);

                if (shoe != null) {
                    if (shoe.startsWith("Lightweight")) {
                        speedModifier *= 1.1;
                        fallModifier *= 1.1;
                    } else if (shoe.startsWith("No Shoes")) {
                        speedModifier *= 0.9;
                        fallModifier *= 0.9;
                    }
                }

                Horse horse = new Horse(symbol, name, horseConfidence);
                horses.add(horse);
            }

            // Weather
            String weather = (String) weatherBox.getSelectedItem();
            if ("Wet".equals(weather)) {
                speedModifier *= 0.95;
                fallModifier *= 1.05;
            } else if ("Icy".equals(weather)) {
                speedModifier *= 0.9;
                fallModifier *= 1.1;
            }

            JOptionPane.showMessageDialog(setupFrame, "Horses are ready! You can now start the race.");

        } catch (NumberFormatException ex) {
            showError("Please enter valid numbers!");
        }
    }

    private void openBettingWindow() {
        if (horses.isEmpty()) {
            showError("No horses available for betting.");
            return;
        }
    
        JPanel bettingPanel = new JPanel(new GridLayout(horses.size() + 2, 2, 10, 5));
        JLabel betLabel = new JLabel("Choose your horse to bet on:");
    
        bettingPanel.add(betLabel);
        bettingPanel.add(new JLabel(""));
    
        ButtonGroup group = new ButtonGroup();
        List<JRadioButton> radioButtons = new ArrayList<>();
        List<Double> oddsList = new ArrayList<>();
    
        for (Horse horse : horses) {
            double baseOdds = (1.0 / horse.getConfidence());
            double weatherModifier = ("Wet".equals(weatherBox.getSelectedItem())) ? 1.1 :
                                     ("Icy".equals(weatherBox.getSelectedItem())) ? 1.2 : 1.0;
            double finalOdds = baseOdds * weatherModifier;
            oddsList.add(finalOdds);
    
            JRadioButton button = new JRadioButton(horse.getName() + " (Odds: " + String.format("%.2f", finalOdds) + ")");
            radioButtons.add(button);
            group.add(button);
            bettingPanel.add(button);
        }
    
        bettingPanel.add(new JLabel("Enter bet amount:"));
        JTextField betAmountField = new JTextField();
        bettingPanel.add(betAmountField);
    
        int result = JOptionPane.showConfirmDialog(setupFrame, bettingPanel, "Place Your Bet", JOptionPane.OK_CANCEL_OPTION);
    
        if (result == JOptionPane.OK_OPTION) {
            int selectedIndex = -1;
            for (int i = 0; i < radioButtons.size(); i++) {
                if (radioButtons.get(i).isSelected()) {
                    selectedIndex = i;
                    break;
                }
            }
    
            if (selectedIndex == -1) {
                showError("No horse selected for betting!");
                return;
            }
    
            try {
                double amount = Double.parseDouble(betAmountField.getText().trim());
                if (amount <= 0) {
                    showError("Enter a positive bet amount.");
                    return;
                }
    
                userBetHorse = horses.get(selectedIndex);
                userBetAmount = amount;
                userPotentialWinnings = amount * oddsList.get(selectedIndex);
    
                JOptionPane.showMessageDialog(setupFrame, "✅ Bet placed on " + userBetHorse.getName() +
                        "\nAmount: $" + userBetAmount +
                        "\nPotential Winnings: $" + String.format("%.2f", userPotentialWinnings));
    
            } catch (NumberFormatException ex) {
                showError("Invalid amount entered.");
            }
        }
    }
    

    // Start race after setup
    private void startRaceFromSetup(ActionEvent e) {
        if (horses.isEmpty()) {
            showError("Setup horses first!");
            return;
        }
        openBettingWindow();
        setupFrame.dispose();
        startRace();
    }

    // Start race directly (used also for new race)
    private void startRace() {
        raceFrame = new JFrame("🏇 Horse Race Live View");
        raceFrame.setSize(800, 600);
        raceFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        raceFrame.setLayout(new BorderLayout());

        raceOutputArea = new JTextArea();
        raceOutputArea.setEditable(false);
        raceOutputArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(raceOutputArea);

        raceFrame.add(scrollPane, BorderLayout.CENTER);

        raceButtonPanel = new JPanel();
        raceFrame.add(raceButtonPanel, BorderLayout.SOUTH);

        raceFrame.setVisible(true);

        runRace();
    }

    private void runRace() {
        Race race = new Race(raceLength, laneCount);
    
        // Assign horses randomly to lanes
        // Random lane assignment, one horse per lane
        List<Integer> availableLanes = new ArrayList<>();
        for (int i = 0; i < laneCount; i++) {
            availableLanes.add(i);
        }
        java.util.Collections.shuffle(availableLanes);

        for (int i = 0; i < horses.size(); i++) {
            int lane = availableLanes.get(i);
            race.addHorse(horses.get(i), lane);
        }

    
        for (Horse horse : race.getHorses()) {
            if (horse != null) {
                horse.goBackToStart();
            }
        }
    
        race.startTimer();
    
        Timer raceTimer = new Timer(100, null);
        raceTimer.addActionListener(event -> {
            boolean finished = false;
    
            // Move horses
            for (Horse horse : race.getHorses()) {
                if (horse != null && !horse.hasFallen()) {
                    if (Math.random() < horse.getConfidence() * speedModifier) {
                        horse.moveForward();
                    }
                    if (Math.random() < (0.1 * horse.getConfidence() * horse.getConfidence() * fallModifier)) {
                        horse.fall();
                    }
                }
            }
    
            // Update view
            updateRaceOutput(race);
    
            // Check if race ended
            if (race.allHorsesHaveFallenPublic() || race.anyHorseHasFinishedPublic()) {
                finished = true;
            }
    
            if (finished) {
                raceTimer.stop();
                int totalTime = race.endTimer();
    
                String winner = race.getWinner();
                //
                if (userBetHorse != null) {
                    if (winner != null && userBetHorse.getName().equals(winner)) {
                        raceOutputArea.append("\n💰 Congratulations! You WON your bet!\n");
                        raceOutputArea.append("Winnings: $" + String.format("%.2f", userPotentialWinnings) + "\n");
                        bettingHistory.add("Won on " + userBetHorse.getName() + " and earned $" + String.format("%.2f", userPotentialWinnings));
                    } else {
                        raceOutputArea.append("\n💸 You LOST your bet.\n");
                        bettingHistory.add("Lost bet on " + (userBetHorse != null ? userBetHorse.getName() : "unknown horse"));
                    }
                    userBetHorse = null;
                    userBetAmount = 0.0;
                    userPotentialWinnings = 0.0;
                }

                if (winner != null) {
                    raceOutputArea.append("\n\n🏆 Winner: " + winner + "!\n");
                } else {
                    raceOutputArea.append("\n\n❌ No horse finished the race!\n");
                }
    
                // Update horses data
                for (Horse h : horses) {
                    boolean won = winner != null && h.getName().equals(winner);
    
                    // 👉 ADD: record the distance travelled, not race time
                    h.addDistance(h.getDistanceTravelled()); 
    
                    h.addRaceTime(totalTime); // still good to track time too
                    h.recordRaceEnd(won, h.getConfidence());
    
                    if (won) {
                        h.setConfidence(Math.min(1.0, h.getConfidence() + 0.1));
                    } else {
                        h.setConfidence(Math.max(0.0, h.getConfidence() - 0.1));
                    }
                }
    
                // Summary with correct steps
                raceOutputArea.append("\nSummary:\n");
                for (Horse h : horses) {
                    raceOutputArea.append(h.getName() +
                            " | Conf: " + String.format("%.2f", h.getConfidence()) +
                            " | Wins: " + h.getWins() + "/" + h.getTotalRaces() +
                            " (" + String.format("%.0f", h.getWinRatio() * 100) + "%)" +
                            " | Total Steps: " + h.getTotalDistance() +
                            " | Avg Steps: " + String.format("%.1f", h.getAverageDistance()) + "\n");
                }
    
                // Add New Race Button
                JButton newRaceButton = new JButton("New Race");
                newRaceButton.addActionListener(ev -> {
                    raceFrame.dispose();
                    openBettingWindow(); // <-- Open betting window FIRST
                    startRace();         // <-- Then start new race
                });
                raceButtonPanel.add(newRaceButton);

                // 🔵 Add View Betting History Button
                JButton historyButton = new JButton("View Betting History");
                historyButton.addActionListener(ev -> showBettingHistory());
                raceButtonPanel.add(historyButton);

                raceButtonPanel.revalidate();
            }
        });
    
        raceTimer.start();

        raceOutputArea.append("\n\n📜 Betting History:\n");
        for (String record : bettingHistory) {
            raceOutputArea.append(record + "\n");
        }

    }
    

    private void updateRaceOutput(Race race) {
        StringBuilder display = new StringBuilder();
        display.append("=".repeat(race.getRaceLength() + 3)).append("\n");
    
        for (int i = 0; i < horses.size(); i++) {
            Horse horse = horses.get(i);
            if (horse != null) {
                int before = horse.getDistanceTravelled();
                int after = race.getRaceLength() - horse.getDistanceTravelled();

                display.append("|")
                        .append(" ".repeat(before))
                        .append(horse.hasFallen() ? 'X' : horseEmojis.get(i))
                        .append(horseCoats.get(i))
                        .append(" ".repeat(after))
                        .append("| ")
                        .append(horse.getName())
                        .append(" (Conf ")
                        .append(String.format("%.2f", horse.getConfidence()))
                        .append(", Coat: ").append(horseCoats.get(i))
                        .append(")\n");
            } else {
                display.append("|").append(" ".repeat(race.getRaceLength() + 1)).append("|\n");
            }
        }
    
        display.append("=".repeat(race.getRaceLength() + 3)).append("\n");
    
        raceOutputArea.setText(display.toString());
    }
    
    // Show Betting History in a separate window
private void showBettingHistory() {
    JFrame historyFrame = new JFrame("📜 Betting History");
    historyFrame.setSize(400, 400);
    historyFrame.setLayout(new BorderLayout());

    JTextArea historyArea = new JTextArea();
    historyArea.setEditable(false);
    historyArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

    StringBuilder historyText = new StringBuilder();
    for (String record : bettingHistory) {
        historyText.append(record).append("\n");
    }

    historyArea.setText(historyText.toString());

    JScrollPane scrollPane = new JScrollPane(historyArea);

    JButton backButton = new JButton("⬅️ Back to Race");
    backButton.addActionListener(ev -> historyFrame.dispose());

    historyFrame.add(scrollPane, BorderLayout.CENTER);
    historyFrame.add(backButton, BorderLayout.SOUTH);

    historyFrame.setLocationRelativeTo(null); // Center on screen
    historyFrame.setVisible(true);
}


    private void showError(String message) {
        JOptionPane.showMessageDialog(setupFrame, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUI::new);
 
    }
}
