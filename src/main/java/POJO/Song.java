package POJO;

public class Song {
    private String song;
    private String songType;

    public Song(String song, String songType) {
        this.song = song;
        this.songType = songType;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public String toString() {
        return song + " " + songType;
    }

    public String getSongType() {
        return songType;
    }

    public void setSongType(String songType) {
        this.songType = songType;
    }
}
