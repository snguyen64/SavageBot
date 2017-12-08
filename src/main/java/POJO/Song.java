package POJO;

public class Song {
    private String song;
    private SongType songType;

    public Song(String song, SongType songType) {
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
        return song + ", TYPE: " + songType;
    }

    public SongType getSongType() {
        return songType;
    }

    public void setSongType(SongType songType) {
        this.songType = songType;
    }
}
