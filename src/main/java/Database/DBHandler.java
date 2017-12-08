package Database;

import Controller.Main;
import POJO.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class DBHandler {
    private Database handler;

    //the sql calls

    //"adding leaders and stuff"
    private String insertLeader = "INSERT INTO Leaders(name) VALUES (?)";
    private String removeLeader = "DELETE FROM Leaders WHERE name = ?";
    private String getAllLeaders = "SELECT name FROM Leaders";
    private String getLeader = "SELECT name FROM Leaders WHERE name = ?";

    //playlist calls
    private String insertPlaylist = "INSERT INTO Playlists(playlist_name) VALUES (?)";
    private String getAllPlaylists = "SELECT * FROM Playlists";
    private String getPlaylistSongsShuffle = "SELECT song FROM Songs WHERE playlist = ? " +
            "ORDER BY RAND() ";
    private String deletePlaylist = "DELETE FROM Playlists WHERE playlist_name = ?";
    private String playlistExists = "SELECT * FROM Playlists WHERE playlist_name = ?";

    //song calls
    private String insertSong = "INSERT INTO Songs(song, playlist, linkType) VALUES (?, " +
            "(SELECT playlist_name FROM Playlists WHERE playlist_name = ?), ?)";
    private String deleteSong = "DELETE FROM Songs WHERE song = ? AND playlist = ?";
    private String getAllSongsFromPlaylist = "SELECT * FROM Songs WHERE playlist = ?";
    private String songIsInPlaylist = "SELECT song FROM Songs WHERE song = ? AND playlist = ?";

    //insults
    private String insertInsult = "INSERT INTO Insults(insult) VALUES (?)";
    private String getInsult = "SELECT insult FROM INSULTS " +
            "ORDER BY RAND() " +
            "LIMIT 1";
    private String removeInsult = "REMOVE FROM Insult WHERE insult = ?";

    //complments
    private String insertCompliment = "INSERT INTO Compliments(compliment) VALUES (?)";
    private String getCompliment = "SELECT compliment FROM Compliments " +
            "ORDER BY RAND() " +
            "LIMIT 1";
    private String removeCompliment = "DELETE FROM Compliment WHERE compliment = ?";

    //these will be implemented later at my leisure
    //emoticons
    private String insertEmote = "INSERT INTO Emoticons(emote, type) VALUES (?, ?)";
    private String getEmote = "SELECT emote FROM Emoticons WHERE type = ? " +
            "ORDER BY RAND() " +
            "LIMIT 1";

    /**
     * This creates the DB connection using the configuration
     * @param config the config file with the information to the db
     */
    public DBHandler(Configuration config) {
        handler = new Database(config.getHost(), config.getPort(), config.getDBName(),
                config.getUser(), config.getPassword(), Main.getLogger());
    }

    /**
     * Tries to connect to the db
     * @return if the db is connected or not
     */
    public boolean initialize() {
        return handler.connect();
    }

    /**
     *
     * @return returns if the database connection was initialized
     */
    public boolean isInitialized() {
        return handler.isConnected();
    }

    /**
     * inserts a leader into the db
     * leaders are able to make specific calls that regular users can't
     * @param s the leader's name
     * @return returns if it was successful
     */
    public boolean insertLeader(String s) {
        PreparedStatement insertLeader = handler.prepareStatement(this.insertLeader);
        try {
            insertLeader.setString(1, s);
            insertLeader.execute();
            return true;
        } catch (SQLException e) {
            Main.getLogger().log(Level.SEVERE, e.getMessage());
            return false;
        }
    }

    /**
     * This returns if the user was in the leader schema
     * if the user was not then it will return false
     * @param s the name of the user
     * @return returns if the user is a leader or not
     */
    public boolean getUserIsLeader(String s) {
        PreparedStatement getUserIsLeader = handler.prepareStatement(this.getLeader);
        try {
            getUserIsLeader.setString(1, s);
            ResultSet rs = getUserIsLeader.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            Main.getLogger().log(Level.SEVERE, e.getMessage());
            return false;
        }
    }

    /**
     * This removes the leader of their rights
     * @param s the name of the leader that's going to be removed
     * @return returns if it's successful
     */
    public boolean removeLeader(String s) {
        PreparedStatement removeLeader = handler.prepareStatement(this.removeLeader);
        try {
            removeLeader.setString(1, s);
            removeLeader.execute();
            return true;
        } catch (SQLException e) {
            Main.getLogger().log(Level.SEVERE, e.getMessage());
            return false;
        }
    }

    /**
     * this returns a list of all the leaders
     * @return ^
     */
    public List<String> getAllLeaders() {
        List<String> leaders = new ArrayList<>();
        PreparedStatement getAllLeaders = handler.prepareStatement(this.getAllLeaders);
        try {
            ResultSet rs = getAllLeaders.executeQuery();
            while (rs.next()) {
                leaders.add(rs.getString(1));
            }
        } catch (SQLException e) {
            Main.getLogger().log(Level.SEVERE, e.getMessage());
        }
        return leaders;
    }


    /**
     * This creates a new Playlist
     * @param s the name of the new playlist
     * @return returns whether it was successfully created or not
     */
    public boolean createPlaylist(String s) {
        PreparedStatement insertPlaylist = handler.prepareStatement(this.insertPlaylist);
        try {
            insertPlaylist.setString(1, s);
            insertPlaylist.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * This will get all the playlists that are stored
     * @return returns the list of playlists
     */
    public List<Playlist> getAllPlaylists() {
        List<Playlist> playlists = new ArrayList<>();
        PreparedStatement getAllPlaylists = handler.prepareStatement(this.getAllPlaylists);
        try {
            ResultSet rs = getAllPlaylists.executeQuery();
            while (rs.next()) {
                playlists.add(new Playlist(rs.getString(1)));
            }
        } catch (SQLException e) {
            Main.getLogger().log(Level.SEVERE, e.getMessage());
        }
        return playlists;
    }

    /**
     * This will return a given playlist
     * The playlist will have a list of songs in it
     * @param s this is the playlist name
     * @return
     */
    public Playlist getPlaylist(String s) {
        Playlist playlist = new Playlist(s, new ArrayList<>());
        PreparedStatement getPlaylist = handler.prepareStatement(this.getAllSongsFromPlaylist);
        try {
            getPlaylist.setString(1, s);
            ResultSet rs = getPlaylist.executeQuery();
            while (rs.next()) {
                String songtype = rs.getString(3);

                playlist.addSong(new Song(rs.getString(1),
                        songtype.equals("youtube ") ? SongType.YOUTUBE :
                        songtype.equals("soundcloud ") ? SongType.SOUNDCLOUD :
                        SongType.LINK));
            }
        } catch (SQLException e) {
            Main.getLogger().log(Level.SEVERE, e.getMessage());
        }
        return playlist;
    }

    public boolean playlistExists(String s) {
        PreparedStatement playlistExists = handler.prepareStatement(this.playlistExists);
        try {
            playlistExists.setString(1, s);
            ResultSet rs = playlistExists.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            Main.getLogger().log(Level.SEVERE, e.getMessage());
            return false;
        }
    }

    /**
     * This gets the playlist except shuffled
     * @param s is the playlist name
     * @return returns the playlist
     */
    public Playlist getPlaylistShuffled(String s) {
        Playlist playlist = new Playlist(s);
        PreparedStatement getPlaylistShuffled = handler.prepareStatement(this.getPlaylistSongsShuffle);
        try {
            getPlaylistShuffled.setString(1, s);
            ResultSet rs = getPlaylistShuffled.executeQuery();
            while (rs.next()) {
                playlist.addSong(new Song(rs.getString(1), SongType.valueOf(rs.getString(3))));
            }
        } catch (SQLException e) {
            Main.getLogger().log(Level.SEVERE, e.getMessage());
        }
        return playlist;
    }

    /**
     * This method deletes playlist
     * @param s the playlist name
     * @return returns if deletion was successful or not
     */
    public boolean deletePlaylist(String s) {
        PreparedStatement deleteStation = handler.prepareStatement(this.deletePlaylist);
        try {
            deleteStation.setString(1, s);
            deleteStation.execute();
            return true;
        } catch (SQLException e) {
            Main.getLogger().log(Level.SEVERE, e.getMessage());
            return false;
        }
    }

    /**
     * inserts specific song into the playlist
     * @param song the song name
     * @param playlist the playlist name
     * @param songtype the type of song: yt/sc/link
     * @return returns whether or not it was successful
     */
    public boolean insertSong(String song, String playlist, String songtype) {
        PreparedStatement insertSong = handler.prepareStatement(this.insertSong);
        try {
            insertSong.setString(1, song);
            insertSong.setString(2, playlist);
            insertSong.setString(3, songtype);
            insertSong.execute();
            return true;
        } catch (SQLException e) {
            Main.getLogger().log(Level.SEVERE, e.getMessage());
            return false;
        }
    }

    public boolean songInPlaylist(String song, String playlist) {
        PreparedStatement songIsInPlaylist = handler.prepareStatement(this.songIsInPlaylist);
        try {
            songIsInPlaylist.setString(1, song);
            songIsInPlaylist.setString(2, playlist);
            ResultSet rs = songIsInPlaylist.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            Main.getLogger().log(Level.SEVERE, e.getMessage());
            return false;
        }
    }

    /**
     * This deletes a song from a specific playlist
     * @param song the song name
     * @param playlist the playlist name
     * @return returns if it was a successful deletion
     */
    public boolean deleteSong(String song, String playlist) {
        PreparedStatement deleteSong = handler.prepareStatement(this.deleteSong);
        try {
            deleteSong.setNString(1, song);
            deleteSong.setNString(2, playlist);
            deleteSong.execute();
            return true;
        } catch (SQLException e) {
            Main.getLogger().log(Level.SEVERE, e.getMessage());
            return false;
        }
    }

    /**
     * Inserts an insult into the database
     * @param insult the insult
     * @return returns if it was successful
     */
    public boolean insertInsult(Insult insult) {
        PreparedStatement insertInsult = handler.prepareStatement(this.insertInsult);
        try {
            insertInsult.setString(1, insult.getInsult());
            insertInsult.execute();
            return true;
        } catch (SQLException e) {
            Main.getLogger().log(Level.SEVERE, e.getMessage());
            return false;
        }
    }

    /**
     * Gets a random insult from the database
     * @return returns the insult
     */
    public Insult getInsult() {
        Insult insult = null;
        PreparedStatement getInsult = handler.prepareStatement(this.getInsult);
        try {
            ResultSet rs = getInsult.executeQuery();
            rs.next();
            insult = new Insult(rs.getString(1));
        } catch (SQLException e) {
            Main.getLogger().log(Level.SEVERE, e.getMessage());
        }
        return insult;
    }

    /**
     * Method to remove an insult from the db
     * @param s the whole insult
     * @return returns if it was successful or not
     */
    public boolean removeInsult(String s) {
        PreparedStatement removeInsult = handler.prepareStatement(this.removeInsult);
        try {
            removeInsult.setString(1, s);
            removeInsult.execute();
            return true;
        } catch (SQLException e) {
            Main.getLogger().log(Level.SEVERE, e.getMessage());
            return false;
        }
    }

    /**
     * inserts a new compliment to the database
     * @param s the compliment string
     * @return returns if the compliment was inserted into the db
     */
    public boolean insertCompliment(String s) {
        PreparedStatement insertCompliment = handler.prepareStatement(this.insertCompliment);
        try {
            insertCompliment.setString(1, s);
            insertCompliment.execute();
            return true;
        } catch (SQLException e) {
            Main.getLogger().log(Level.SEVERE, e.getMessage());
            return false;
        }
    }

    /**
     * gets a random compliment from the db
     * @return the compliment from the db
     */
    public Compliment getCompliment() {
        Compliment compliment = null;
        PreparedStatement getCompliment = handler.prepareStatement(this.getCompliment);
        try {
            ResultSet rs = getCompliment.executeQuery();
            rs.next();
            compliment = new Compliment(rs.getString(1));
        } catch (SQLException e) {
            Main.getLogger().log(Level.SEVERE, e.getMessage());
        }
        return compliment;
    }

    /**
     * Method to remove a compliment from the db
     * @param s the whole compliment
     * @return returns if it was successful or not
     */
    public boolean removeCompliment(String s) {
        PreparedStatement removeCompliment = handler.prepareStatement(this.removeCompliment);
        try {
            removeCompliment.setString(1, s);
            removeCompliment.execute();
            return true;
        } catch (SQLException e) {
            Main.getLogger().log(Level.SEVERE, e.getMessage());
            return false;
        }
    }
}