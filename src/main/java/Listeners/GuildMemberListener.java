package Listeners;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class GuildMemberListener extends ListenerAdapter {
    private Guild guild;
    private MessageChannel channel;
    private User user;

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        guild = event.getGuild();
        user = event.getUser();
        channel = event.getGuild().getDefaultChannel();
        channel.sendMessage("Welcome " + user.getName() + " to "
                + guild.getName() + "!").queue();
    }
    @Override
    public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
        guild = event.getGuild();
        user = event.getUser();
        channel = guild.getDefaultChannel();
        channel.sendMessage("We didn't want you here anyways :(").queue();
    }

}
