package POJO;

public enum SongType {

    YOUTUBE("youtube ", "ytsearch:"),
    SOUNDCLOUD("soundcloud ", "scsearch:"),
    LINK("link", "");

    private String representation;
    private String dbName;
    SongType(String representation, String dbName) {
        this.representation = representation;
        this.dbName = dbName;
    }
    @Override
    public String toString() {
        return representation;
    }

    public String getDbName() {
        return dbName;
    }
}