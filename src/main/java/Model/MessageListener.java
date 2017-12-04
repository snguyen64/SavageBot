package Model;

import Controller.Main;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.List;

public class MessageListener extends ListenerAdapter {

    private User user;
    private Message message;
    private MessageChannel channel;

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        user = event.getAuthor();
        message = event.getMessage();
        channel = event.getChannel();
        //this is the full readable message
        String msg = message.getContent();

        if (msg.startsWith("$")) {
            //this is the message after the '$'
            String mentionMessage = msg.substring(1);

            //regular user commands
            //leaders can also use these
            if (mentionMessage.startsWith("hi!")) {
                greetings();
            }

            //command to show commands
            if (mentionMessage.startsWith("commands")) {
                commandCall();
            }

            //playlist related commands
            if (mentionMessage.contains("playlist")) {
                playlistCommands(mentionMessage);
            }

            //song in playlist related commands
            if (mentionMessage.contains("song")) {
                songCommands(mentionMessage);
            }

            //insult related commands
            if (mentionMessage.contains("insult")) {
                insultCommands(mentionMessage);
            }

            //compliment related commands
            if (mentionMessage.contains("compliment")) {
                complimentCommands(mentionMessage);
            }

            //LEADER SPECIFIC COMMANDS
            if (Main.getHandler().getUserIsLeader(user.getName())) {

                //Leadership commands
                if (mentionMessage.contains("leadership")) {
                    leadershipCommands(mentionMessage);
                }

                //killing the bot
                if (mentionMessage.startsWith("Kill Bot")) {
                    channel.sendMessage("Good Bye").queue();
                    event.getJDA().shutdown();
                }
            }
        }
    }

    /**
     * sends the channel a speciic greeting when
     * $hi! is sent
     */
    public void greetings() {
        if (Main.getHandler().getUserIsLeader(user.getName())) {
            channel.sendMessage("Heil, mein Führer!  ヾ(´ε｀*)").queue();
        } else {
            channel.sendMessage("Hola " + user.getAsMention() + "!").queue();
        }
    }

    /**
     * These are commands involved when "leadership"
     * is called in the message which deals with
     * granting leadership and revoking leadership
     * @param mentionMessage is the message after the $
     */
    public void leadershipCommands(String mentionMessage) {
        //Granting leadership
        if (mentionMessage.startsWith("grant leadership to ")) {
            List<User> mentionedLeaders = message.getMentionedUsers();
            if (mentionedLeaders.size() > 0) {
                for (User u: mentionedLeaders) {
                    if (!Main.getHandler().getUserIsLeader(u.toString())) {
                        Main.getHandler().insertLeader(u.getName());
                        channel.sendMessage("Heil, mein Führer!  ヾ(´ε｀*)"
                                + u.getAsMention()).queue();
                    } else {
                        channel.sendMessage(u.getAsMention() + " is already " +
                                "a leader").queue();
                    }
                }
            }
        }
        //removing leadership
        if (mentionMessage.startsWith("remove leadership of ")) {
            List<User> toBeRemoved = message.getMentionedUsers();
            if (toBeRemoved.size() > 0) {
                for (User u : toBeRemoved) {
                    if (Main.getHandler().getUserIsLeader(u.getName())
                            //adding an exception for me
                            //you can't dethrone the king
                            && !u.getName().equals("Steven")) {
                        Main.getHandler().removeLeader(u.getName());
                        channel.sendMessage("Pfft.... you were " +
                                "a shitty leader anyways, " + u.getAsMention()).queue();
                    } else {
                        channel.sendMessage("Hey, your weren't a leader..." +
                                u.getAsMention()).queue();
                    }
                }
            }
        }
    }

    /**
     * The command call to print the commands list for the bot
     */
    public void commandCall() {
        channel.sendMessage("```" +
                "Commands List\n" +
                ".-*`*-.-*`*-.-*`*-.-*`*-.-*`*-.-*`*-.\n" +
                "1. $show playlists\n" +
                "2. $add playlist [playlistname]\n" +
                "3. $remove playlist [playlistname]\n" +
                "4. $show songs for [playlist name]\n" +
                "5. $add song [songname] to [playlistname]\n" +
                "6. $remove song [songname] from [playlistname]\n" +
                "7. $add insult [insult phrase]\n" +
                "8. $add compliment [compliment phrase]\n" +
                "9. $insult @[personname]\n" +
                "10. $compliment @[personname]\n" +
                "```").queue();
        if (Main.getHandler().getUserIsLeader(user.getName())) {
            channel.sendMessage("\n```" +
                    "Command List FOR LEADERS!\n" +
                    ".-*`*-.-*`*-.-*`*-.-*`*-.-*`*-.-*`*-.\n" +
                    "1. $grant leadership to @[user] (can be multiple users)\n" +
                    "2. $remove leadership of @[user] (can be multiple users)\n" +
                    "```").queue();
        }
    }

    /**
     * Commands dealing with playlists
     * -show list of playlists
     * -add a playlist
     * -remove a playlist
     * @param mentionMessage the message to specify which command
     */
    public void playlistCommands(String mentionMessage) {
        if (mentionMessage.startsWith("show playlists")) {
            List<Playlist> playlists = Main.getHandler().getAllPlaylists();
            if (playlists.size() == 0) {
                channel.sendMessage("There aren't any playlists...").queue();
            } else {
                channel.sendMessage("~Playlist List~").queue();
                for (Playlist playlist : playlists) {
                    channel.sendMessage(playlist.toString() + "\n").queue();
                }
            }
        }

        if (mentionMessage.startsWith("add playlist ")) {
            String playlistname = mentionMessage.replaceFirst("add playlist ", "");
            boolean added = Main.getHandler().createPlaylist(playlistname);
            if (added) {
                channel.sendMessage("The playlist " + playlistname + " was added, boss!").queue();
            } else {
                channel.sendMessage("It didnt work. Check the name or something." +
                        " Might have been a duplicate.").queue();
            }
        }


        if (mentionMessage.startsWith("remove playlist ")) {
            String playlistname = mentionMessage.replaceFirst("remove playlist ", "");
            boolean removed = Main.getHandler().deletePlaylist(playlistname);
            if (removed) {
                channel.sendMessage(playlistname + " was removed!").queue();
            } else {
                channel.sendMessage("The playlist didnt exist...").queue();
            }
        }

    }

    /**
     * This is for song commands
     * --showing all songs in a playlist
     * --adding a song to a playlist
     * --removing a song from a playlist
     * @param mentionMessage the message after the $
     */
    public void songCommands(String mentionMessage) {
        //showing all songs for playlist
        if (mentionMessage.startsWith("show songs for ")) {
            String playlistname = mentionMessage.replaceFirst("show songs for ", "");
            if (Main.getHandler().playlistExists(playlistname)) {
                Playlist playlist = Main.getHandler().getPlaylist(playlistname);
                if (playlist.getSongs() == null || playlist.getSongs().size() == 0) {
                    channel.sendMessage("That playlist doesn't have any songs....").queue();
                } else {
                    channel.sendMessage("~" + playlistname + "~").queue();
                    for (Song s : playlist.getSongs()) {
                        channel.sendMessage(s.toString() + "\n").queue();
                    }
                }
            } else {
                channel.sendMessage("That playlist doesn't exist...").queue();
            }
        }

        //adding a song TO a playlist
        if (mentionMessage.startsWith("add song ")) {
            String songAndPlaylist = mentionMessage.replaceFirst("add song ", "");
            String[] songPlaylist = songAndPlaylist.split(" to ");
            if (songPlaylist.length == 2) {
                if (Main.getHandler().playlistExists(songPlaylist[1])) {
                    if (Main.getHandler().insertSong(songPlaylist[0], songPlaylist[1])) {
                        channel.sendMessage(songPlaylist[0] + " was added to " + songPlaylist[1]).queue();
                    } else {
                        channel.sendMessage(songPlaylist[0] + " could not be added " +
                                "to " + songPlaylist[1] +
                                ". check if the song is already in the playlist " +
                                " or if the playlist doesn't exist").queue();
                    }
                } else {
                    channel.sendMessage("That playlist doesn't exist...").queue();
                }
            } else {
                channel.sendMessage("Check your format").queue();
            }
        }

        //removing a song from a playlist
        if (mentionMessage.startsWith("remove song ")) {
            String songAndPlaylist = mentionMessage.replaceFirst("remove song ", "");
            String[] songPlaylist = songAndPlaylist.split(" from ");
            if (songPlaylist.length == 2) {
                if (Main.getHandler().playlistExists(songPlaylist[1])) {
                    if (Main.getHandler().songInPlaylist(songPlaylist[0], songPlaylist[1])) {
                        Main.getHandler().deleteSong(songPlaylist[0], songPlaylist[1]);
                        channel.sendMessage(songPlaylist[0] + " was removed from " +
                                songPlaylist[1] + "!").queue();
                    } else {
                        channel.sendMessage("Song is not in the playlist...").queue();
                    }
                } else {
                    channel.sendMessage("Playlist doesnt exist...").queue();
                }
            } else {
                channel.sendMessage("Check your format...").queue();
            }
        }
    }

    /**
     * This is the insults command to add/insult a user
     * ~might implement removing an insult later
     * @param mentionMessage the message after $
     */
    public void insultCommands(String mentionMessage) {
        //adding an insult into db
        if (mentionMessage.startsWith("add insult")) {
            String insult = mentionMessage.replaceFirst("add insult ", "");
            if (Main.getHandler().insertInsult(new Insult(insult))) {
                channel.sendMessage("The insult was added.").queue();
            } else {
                channel.sendMessage("The insult already existed.").queue();
            }
        }

        //insulting a user
        if (mentionMessage.startsWith("insult")) {
            List<User> mentionedUsers = message.getMentionedUsers();
            if (mentionedUsers.size() > 0) {
                for (User u: mentionedUsers) {
                    channel.sendMessage(Main.getHandler()
                            .getInsult().getInsult()
                            + ", " + u.getAsMention()).queue();
                }
            }
        }
    }

    /**
     * Add/compliment a user
     * ~might implement remove compliment later
     * @param mentionMessage the message after $
     */
    public void complimentCommands(String mentionMessage) {
        //adding a compliment
        if (mentionMessage.startsWith("add compliment")) {
            String compliment = mentionMessage.replaceFirst("add compliment ", "");
            if (Main.getHandler().insertCompliment(compliment)) {
                channel.sendMessage("The compliment was added!").queue();
            } else {
                channel.sendMessage("The compliment already existed...").queue();
            }
        }

        //complimenting a user
        if (mentionMessage.startsWith("compliment")) {
            List<User> mentionedUsers = message.getMentionedUsers();
            if (mentionedUsers.size() > 0) {
                for (User u: mentionedUsers) {
                    channel.sendMessage(Main.getHandler()
                            .getCompliment().getCompliment()
                            + ", " + u.getAsMention()).queue();
                }
            }
        }
    }
}
