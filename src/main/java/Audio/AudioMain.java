package Audio;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.source.soundcloud.SoundCloudAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AudioMain {
    private final AudioPlayerManager playerManager;
    private final Map<Long, MusicManager> musicManagers;

    public AudioMain() {
        this.musicManagers = new HashMap<>();
        this.playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
        //when searching do ytsearch: [trackname]
        playerManager.registerSourceManager(new YoutubeAudioSourceManager(true));
        //when searching do scsearch: [trackname]
        playerManager.registerSourceManager(new SoundCloudAudioSourceManager());
//        playerManager.registerSourceManager(new BandcampAudioSourceManager());
//        playerManager.registerSourceManager(new VimeoAudioSourceManager());
//        playerManager.registerSourceManager(new TwitchStreamAudioSourceManager());
//        playerManager.registerSourceManager(new BeamAudioSourceManager());
//        playerManager.registerSourceManager(new HttpAudioSourceManager());
    }

    public synchronized MusicManager getGuildAudioPlayer(Guild guild) {
        long guildId = Long.parseLong(guild.getId());
        MusicManager musicManager = musicManagers.get(guildId);
        //so if the guild with that specific guild ID doesnt have any music managers,
        //then a new music manager will be assigned with the AudioPlayerManager
        if (musicManager == null) {
            musicManager = new MusicManager(playerManager);
            musicManagers.put(guildId, musicManager);
        }
        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());
        return musicManager;
    }

    public void disconnectAudio(Guild guild) {
        musicManagers.get(guild.getIdLong()).getScheduler().clear();
        musicManagers.get(guild.getIdLong()).getPlayer().destroy();
        guild.getAudioManager().closeAudioConnection();
    }

    public void connectAudio(Guild guild, VoiceChannel voiceChannel) {
        guild.getAudioManager().openAudioConnection(voiceChannel);
    }


    public void loadAndPlay(final TextChannel channel, final String trackUrl, boolean linkPlaylist) {
        MusicManager musicManager = getGuildAudioPlayer(channel.getGuild());

        playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                channel.sendMessage("Adding to queue " + track.getInfo().title).queue();
                play(musicManager, track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                if (linkPlaylist) {
                    //adds the full playlist
                    List<AudioTrack> queue = playlist.getTracks();
                    for (AudioTrack audioTrack : queue) {
                        channel.sendMessage("Adding to queue: " + audioTrack.getInfo().title).queue();
                    }
                    musicManager.getScheduler().queue(queue);
                    if (musicManager.getPlayer().getPlayingTrack() == null) {
                        musicManager.getScheduler().nextTrack();
                    }
                } else {
                    //adds just 1 track
                    AudioTrack audioTrack = playlist.getSelectedTrack();
                    if (audioTrack == null) {
                        audioTrack = playlist.getTracks().get(0);
                    }
                    play(musicManager, audioTrack);
                    channel.sendMessage(audioTrack.getInfo().title + " added to queue.").queue();
                }
            }

            @Override
            public void noMatches() {
                channel.sendMessage("Nothing found by " + trackUrl).queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                channel.sendMessage("Could not play: " + exception.getMessage()).queue();
            }
        });
    }

    public void play(MusicManager musicManager, AudioTrack track) {
        musicManager.getScheduler().queue(track);
    }

    public boolean skipTrack(MessageChannel channel, Guild guild) {
        MusicManager musicManager = getGuildAudioPlayer(guild);
        if (musicManager.getScheduler().isEmpty()) {
            channel.sendMessage("Nothing is is the queue.").queue();
            return false;
        } else {
            musicManager.getScheduler().nextTrack();
            channel.sendMessage("Skipped to next track.").queue();
            return true;
        }
    }

    public void pause(Guild guild) {
        getGuildAudioPlayer(guild).getPlayer().setPaused(true);
    }

    public void resume(Guild guild) {
        getGuildAudioPlayer(guild).getPlayer().setPaused(false);
    }

    public void stop(Guild guild) {
        getGuildAudioPlayer(guild).getPlayer().stopTrack();
    }
}
