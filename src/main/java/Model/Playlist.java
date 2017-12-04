package Model;

import java.util.ArrayList;
import java.util.List;

public class Playlist {
    private String playlistname;
    private List<Song> songs = new ArrayList<>();

    public Playlist(String playlistname, List<Song> songs) {
        this.playlistname = playlistname;
        this.songs = songs;
    }

    public Playlist(String playlistname) {
        this(playlistname, null);
    }

    public String getPlaylistname() {
        return playlistname;
    }

    public void setPlaylistname(String playlistname) {
        this.playlistname = playlistname;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    public void addSong(Song s) {
        songs.add(s);
    }

    public String toString() {
        return playlistname;
    }

    public String toStringList() {
        String songList = toString() + "\n";
        for (Song s: songs) {
            songList += "\n" + s.toString();
        }
        return songList;
    }
}
