package Controller;

import Database.Configuration;
import Database.DBHandler;
import Listeners.GuildMemberListener;
import Listeners.MessageListener;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Main {
    private static File configFile;
    private static Logger logger;
    private static DBHandler handler;
    private static Configuration config;
    private static JDA discordBot;

    public static void main(String[] args) {
        startConnection();
        startBot();
    }

    public static Logger getLogger() {
        return logger;
    }

    private static void startConnection() {
        logger = Logger.getLogger(Main.class.getName());
        FileHandler fh;
        try {
            fh = new FileHandler("log.log");
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        loadConfig();
        // Handle Db
        handler = new DBHandler(config);
        if (!handler.initialize()) {
            logger.log(Level.SEVERE, "Database failed to initialize.");
        }
    }

    private static void loadConfig() {
        configFile = new File("config.json");
        if (!configFile.isFile()) {
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            config = new Configuration();
            config.saveConfiguration(configFile);
        } else {
            config = Configuration.loadConfiguration(configFile);
            if (config == null) {
                config = new Configuration();
            }
            config.saveConfiguration(configFile);
        }
    }

    private static void startBot() {
        try
        {
            discordBot = new JDABuilder(AccountType.BOT)
                    .setToken("MzQyNzMyMjE2OTA1NzYwNzY5.DGUBNQ.StB-YnlMAJ6FodJh233Wq1YbHcs")
                    .addEventListener(new MessageListener())
                    .addEventListener(new GuildMemberListener())
                    .buildBlocking();
        }
        catch (LoginException e)
        {
            e.printStackTrace();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        catch (RateLimitedException e)
        {
            e.printStackTrace();
        }
    }

    public static DBHandler getHandler() {
        return handler;
    }
}
