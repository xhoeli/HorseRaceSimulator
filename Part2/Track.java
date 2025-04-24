package Part2;

public class Track {
    private int laneCount;
    // Length of the track in meters
    private int length;
    private String shape;
    private String weather;

    public Track(int laneCount, int length, String shape, String weather) {
        this.laneCount = laneCount;
        this.length = length;
        this.shape = shape;
        this.weather = weather;
    }

    public int getLaneCount() {
        return laneCount;
    }

    public void setLaneCount(int laneCount) {
        this.laneCount = laneCount;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }
}
