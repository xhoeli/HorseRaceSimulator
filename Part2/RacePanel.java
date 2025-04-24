package Part2;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import Part1.Horse;

class RacePanel extends JPanel {
    private final List<Horse> horses;
    private final int raceLength;

    public RacePanel(List<Horse> horses, int raceLength) {
        this.horses = horses;
        this.raceLength = raceLength;
        setPreferredSize(new Dimension(800, horses.size() * 60));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int laneHeight = 60;

        for (int i = 0; i < horses.size(); i++) {
            Horse horse = horses.get(i);
            int x = (int) ((double) horse.getDistanceTravelled() / raceLength * (getWidth() - 100));
            int y = i * laneHeight + 20;

            g.setColor(Color.GRAY);
            g.drawLine(0, y + 15, getWidth(), y + 15);

            g.setColor(horse.hasFallen() ? Color.RED : Color.GREEN);
            g.fillOval(x, y, 30, 30);

            g.setColor(Color.BLACK);
            g.drawString(horse.getName(), x + 35, y + 20);
        }
    }
}