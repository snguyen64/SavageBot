package POJO;

public class Song {
    private String song;
    private boolean youtube;

    public Song(String song) {
        this.song = song;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public String toString() {
        return (youtube) ? "ytsearch: " + song : song;
    }

    public boolean isYoutube() {
        return youtube;
    }

    public void setYoutube(boolean youtube) {
        this.youtube = youtube;
    }
}
