package Database;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class Configuration {

    private String host = "localhost";
    private int port = 3306;
    private String db = "savagebot";
    private String user = "savage";
    private String password = "savagepassword";

    public static Configuration loadConfiguration(File f) {
        Gson g = new Gson();
        try {
            return (Configuration) g.fromJson(new FileReader(f), Configuration.class);
        } catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new Configuration();
    }

    public void saveConfiguration(File f) {
        Gson g = new GsonBuilder().setPrettyPrinting().create();
        try {
            String json = g.toJson(this);

            FileWriter fw = new FileWriter(f);
            fw.write(json);
            fw.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getDBName() {
        return db;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}
