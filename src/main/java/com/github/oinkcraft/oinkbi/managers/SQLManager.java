package com.github.oinkcraft.oinkbi.managers;

import com.github.oinkcraft.oinkbi.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.intellij.lang.annotations.Language;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.*;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;

import static com.github.oinkcraft.oinkbi.util.Constants.*;

@SuppressWarnings({"SqlResolve", "ConstantConditions", "LoopStatementThatDoesntLoop"})
public class SQLManager {

    private static SQLManager instance;
    FileConfiguration config = Main.getInstance().getConfig();

    public static String ALL_COLUMNS = "*";

    private final int PORT = config.getInt("sql.port");
    private final String USERNAME = config.getString("sql.username");
    private final String PASSWORD = config.getString("sql.password");
    private final String HOST = config.getString("sql.host");
    private final String DATABASE = config.getString("sql.database");

    private SQLManager() {
    }

    public static SQLManager getInstance() {
        return instance == null ? instance = new SQLManager() : instance;
    }

    private Connection getConnection() {
        String driver = "com.mysql.jdbc.Driver";
        String url = String.format("jdbc:mysql://%s:%d/%s", HOST, PORT, DATABASE);

        try {
            // Check if driver exists
            Class.forName(driver);
            Connection connection = DriverManager.getConnection(url + "?useSSL=false", USERNAME, PASSWORD);
            assert connection != null;
            return connection;
        } catch (ClassNotFoundException | SQLException e) {
            System.err.print("An error occurred while establishing connection to the SQL server. See stacktrace below for more information.");
            e.printStackTrace();
        }
        // Should never happen
        return null;
    }

    private boolean tableNotExist(String tableName) {
        Connection connection = getConnection();
        boolean tExists = false;
        try (ResultSet rs = connection.getMetaData().getTables(null, null, tableName, null)) {
            while (rs.next()) {
                String tName = rs.getString("TABLE_NAME");
                if (tName != null && tName.equals(tableName)) {
                    tExists = true;
                    break;
                }
            }
            connection.close();
        } catch (SQLException e) {
            System.err.print("An error occurred while checking if a table exists in your database. See stacktrace below for more information.");
            e.printStackTrace();
        }
        // Close connection to prevent too many open connections
        if (!tExists) {
            Main.log.log(Level.WARNING, "Table " + tableName + " did not exist!");
        }
        return !tExists;
    }

    private boolean getBoolean(String sql, String col) {
        Future<Boolean> future = CompletableFuture.supplyAsync(() -> {
            Connection connection = getConnection();
            try (ResultSet rs = connection.prepareStatement(sql).executeQuery()) {
                while (rs.next()) {
                    return rs.getBoolean(col);
                }
            } catch (SQLException e) {
                Main.log.severe("Could not execute: " + sql);
            }
            return false;
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        Main.log.severe("Could not get value in " + col);
        return false;
    }

    private String getString(String sql, String col) {
        Future<String> future = CompletableFuture.supplyAsync(() -> {
            Connection connection = getConnection();
            try (ResultSet rs = connection.prepareStatement(sql).executeQuery()) {
                while (rs.next()) {
                    return rs.getString(col);
                }
            } catch (SQLException e) {
                Main.log.severe("Could not execute: " + sql);
            }
            return null;
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        Main.log.severe("Could not get value in " + col);
        return null;
    }

    private int getInt(String sql, String col) {
        Future<Integer> future = CompletableFuture.supplyAsync(() -> {
            Connection connection = getConnection();
            try (ResultSet rs = connection.prepareStatement(sql).executeQuery()) {
                while (rs.next()) {
                    return rs.getInt(col);
                }
            } catch (SQLException e) {
                Main.log.severe("Could not execute: " + sql);
            }
            return 0;
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        Main.log.severe("Could not get value in " + col);
        return 0;
    }

    public boolean getTelemetryOption(UUID uuid, String option) {
        String sql = "SELECT " + option + " FROM " + TABLE_TELEMETRY + " WHERE uuid = '" + uuid + "'";
        return getBoolean(sql, option);
    }

    public int getTimeSinceLastLogin(UUID uuid) {
        String sql = "SELECT times_since_last_login FROM " + TABLE_TELEMETRY + " WHERE uuid = '" + uuid + "'";
        return getInt(sql, "times_since_last_login");
    }

    public void createMobSlaysTable() {
        if (tableNotExist("oinkbi_mob_slays")) {
            executeRaw("CREATE TABLE IF NOT EXISTS oinkbi_mob_slays (uuid VARCHAR(255) NOT NULL, mobtype VARCHAR(255) NOT NULL, playerSlayedMob INT NOT NULL, mobSlayedPlayer INT NOT NULL);");
            Main.log.info("Created table oinkbi_mob_slays");
        }
    }

    public void createCommandTable() {
        if (tableNotExist(TABLE_COMMAND_USAGE)) {
            executeRaw("CREATE TABLE IF NOT EXISTS " + TABLE_COMMAND_USAGE + "(command VARCHAR(255) NOT NULL PRIMARY KEY, times BIGINT DEFAULT 0)");
            Main.log.info("Create table " + TABLE_COMMAND_USAGE);
        }
    }

    public void createOnlineWorldTimeTable() {
        if (tableNotExist(TABLE_TIME_IN_WORLD)) {
            executeRaw("CREATE TABLE IF NOT EXISTS " + TABLE_TIME_IN_WORLD + " (uuid VARCHAR(255) NOT NULL, world VARCHAR(255) NOT NULL, time_online BIGINT DEFAULT 0);");
            Main.log.info("Created table " + TABLE_TIME_IN_WORLD);
        }
    }

    public void createOnlineTimeTable() {
        if (tableNotExist(TABLE_ONLINE_TIME)) {
            executeRaw("CREATE TABLE IF NOT EXISTS " + TABLE_ONLINE_TIME + " (uuid VARCHAR(255) NOT NULL, time_online BIGINT DEFAULT 0, PRIMARY KEY (uuid));");
            Main.log.info("Created table " + TABLE_ONLINE_TIME);
        }
    }

    public void createTelemetryTable() {
        if (tableNotExist(TABLE_TELEMETRY)) {
            executeRaw("CREATE TABLE IF NOT EXISTS " + TABLE_TELEMETRY + " (uuid VARCHAR(255) NOT NULL, telemetry_shown BOOLEAN DEFAULT 1, is_in BOOLEAN DEFAULT 1, logins_total INTEGER DEFAULT 0, PRIMARY KEY (uuid));");
            Main.log.info("Created table " + TABLE_TELEMETRY);
        }
    }

    public void initialisePlayer(UUID uuid) {
        executeRaw("INSERT INTO " + TABLE_TELEMETRY + " (uuid) VALUES ('" + uuid + "');");
        executeRaw("INSERT INTO " + TABLE_ONLINE_TIME + "(uuid) VALUES ('" + uuid + "');");
        Main.getInstance().getServer().getWorlds().forEach(world -> executeRaw("INSERT INTO oinkbi_time_in_world (uuid,world) VALUES ('" + uuid.toString() + "','" + world.getName() + "');"));
        Main.log.info("Initialised " + uuid + " in the database!");
    }

    public void notifyAboutOinkBI(Player player) {
        String sql = "SELECT logins_total FROM " + TABLE_TELEMETRY + " WHERE uuid = '" + player.getUniqueId() + "';";
        int timeSinceLastNotify = getInt(sql, "logins_total");
        if (getTelemetryOption(player.getUniqueId(), "is_in")) {
            if (timeSinceLastNotify % 20 == 0 && timeSinceLastNotify > 0) {
                player.sendMessage(TELEMETRY_NOTIFY_IS_IN);
            }
        } else {
            if (timeSinceLastNotify % 10 == 0 && timeSinceLastNotify > 0) {
                player.sendMessage(TELEMETRY_NOTIFY_IS_OUT);
            }
        }
        executeRaw("UPDATE " + TABLE_TELEMETRY + " SET logins_total = logins_total + 1 WHERE uuid = '" + player.getUniqueId() + "';");
    }

    public void setUpTables() {
        createTelemetryTable();
        createOnlineTimeTable();
        createOnlineWorldTimeTable();
    }

    public void executeRaw(@Language("sql") String statement) {
        Future<Void> future = CompletableFuture.supplyAsync(() -> {
            try {
                Connection connection = getConnection();
                connection.createStatement().execute(statement);
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        });

        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public JSONArray getStat(final String table, final String columnName, final UUID uuid) {
        Future<JSONArray> future = CompletableFuture.supplyAsync(() -> {
            try {
                Connection connection = getConnection();

                PreparedStatement ps = connection.prepareStatement("SELECT ? FROM ? WHERE uuid = ?");

                ps.setString(1, columnName);
                ps.setString(2, table);
                ps.setString(3, uuid.toString());
                return convertToJSON(ps.executeQuery());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        });

        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private JSONArray convertToJSON(ResultSet resultSet) throws Exception {
        JSONArray jsonArray = new JSONArray();
        while (resultSet.next()) {
            int total_rows = resultSet.getMetaData().getColumnCount();
            for (int i = 0; i < total_rows; i++) {
                JSONObject obj = new JSONObject();
                obj.put(resultSet.getMetaData().getColumnLabel(i + 1)
                        .toLowerCase(), resultSet.getObject(i + 1));
                jsonArray.put(obj);
            }
        }
        return jsonArray;
    }

    public boolean testConnection() {
        Future<Boolean> future = CompletableFuture.supplyAsync(() -> {
            try {
                Connection connection = getConnection();
                connection.close();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            Main.log.log(Level.SEVERE, "Could not connect to the database. Disabling.");
            return false;
        });

        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        Main.log.log(Level.SEVERE, "Could not connect to the database. Disabling.");
        return false;
    }
}
