package Part2;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.ArrayList;

import Part1.*;

public class GUI extends JFrame {
    private JTextField trackLengthField;
    private JTextField laneCountField;
    private JButton startButton;
    private RacePanel racePanel;
    private Race race;
    private List<Horse> horses;

    public GUI() {
        setTitle("Horse Race GUI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 400);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputPanel.add(new JLabel("Track Length (meters):"));
        trackLengthField = new JTextField("50");
        inputPanel.add(trackLengthField);

        inputPanel.add(new JLabel("Lane Count:"));
        laneCountField = new JTextField("3");
        inputPanel.add(laneCountField);

        startButton = new JButton("Start Race");
        startButton.addActionListener(this::startRace);
        inputPanel.add(startButton);

        add(inputPanel, BorderLayout.NORTH);
    }

    private void startRace(ActionEvent e) {
        try {
            int raceLength = Integer.parseInt(trackLengthField.getText());
            int laneCount = Integer.parseInt(laneCountField.getText());

            if (raceLength <= 0 || laneCount <= 0) {
                throw new IllegalArgumentException("Values must be positive integers.");
            }

            race = new Race(raceLength, laneCount);
            horses = new ArrayList<>();

            Horse h1 = new Horse('B', "Black Beauty", 0.6);
            Horse h2 = new Horse('R', "Red Thunder", 0.5);
            Horse h3 = new Horse('S', "Storm Rider", 0.4);

            race.addHorse(h1, 0);
            race.addHorse(h2, 1);
            race.addHorse(h3, 2);

            horses.add(h1);
            horses.add(h2);
            horses.add(h3);

            if (racePanel != null) {
                remove(racePanel);
            }

            racePanel = new RacePanel(horses, raceLength);
            add(racePanel, BorderLayout.CENTER);
            revalidate();
            repaint();

            startRaceAnimation();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter valid numbers.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void startRaceAnimation() {
        // Reset horses
        for (Horse horse : horses) {
            horse.goBackToStart();
        }

        new Thread(() -> {
            boolean finished = false;

            while (!finished) {
                for (Horse horse : horses) {
                    if (!horse.hasFallen()) {
                        if (Math.random() < horse.getConfidence()) {
                            horse.moveForward();
                        }

                        if (Math.random() < (0.1 * horse.getConfidence() * horse.getConfidence())) {
                            horse.fall();
                        }
                    }
                }

                racePanel.repaint();

                if (horses.stream().anyMatch(h -> h.getDistanceTravelled() >= race.getRaceLength())
                        || horses.stream().allMatch(Horse::hasFallen)) {
                    finished = true;
                }

                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {}
            }

            Horse winner = horses.stream()
                    .filter(h -> h.getDistanceTravelled() >= race.getRaceLength())
                    .findFirst()
                    .orElse(null);

            if (winner != null) {
                JOptionPane.showMessageDialog(this, "🏁 Winner: " + winner.getName(), "Race Finished", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "All horses fell! No winner.", "Race Finished", JOptionPane.WARNING_MESSAGE);
            }
        }).start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RaceGUI().setVisible(true));
    }
}