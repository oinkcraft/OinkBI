package com.github.oinkcraft.oinkbi.managers;

import com.github.oinkcraft.oinkbi.Main;
import com.github.oinkcraft.oinkbi.util.Constants;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.*;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;

public class SQLManager {

    private static SQLManager instance;
    FileConfiguration config = Main.getInstance().getConfig();

    public static String ALL_COLUMNS = "*";

    public final String prefix = config.getString("sql.prefix");

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
            return DriverManager.getConnection(url + "?useSSL=false", USERNAME, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            System.err.print("An error occurred while establishing connection to the SQL server. See stacktrace below for more information.");
            e.printStackTrace();
        }
        // Should never happen
        return null;
    }

    private boolean tableExist(String tableName) {
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
        return tExists;
    }

    public boolean hasSeenTelemetryMessage(UUID uuid) {
        Future<Boolean> future = CompletableFuture.supplyAsync(() -> {
            Connection connection = getConnection();
            try (ResultSet rs = connection.prepareStatement("SELECT telemetry_shown FROM oinkbi_telemetry WHERE uuid = '"+uuid+"'").executeQuery()) {
                Thread.sleep(1000);
                while (rs.next()) {
                    return rs.getBoolean("telemetry_shown");
                }
            } catch (SQLException | InterruptedException ignored) {
                /*BLANK*/
            }
            return false;
        });

        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        Main.log.severe("COULD NOT GET TELEMETRY VALUE FOR " + uuid);
        return false;
    }

    public void createMobSlaysTable() {
        Future<Void> future = CompletableFuture.supplyAsync(() -> {
            if (!tableExist("oinkbi_mob_slays")) {
                try {
                    Connection connection = getConnection();
                    connection.createStatement().execute(
                            "CREATE TABLE IF NOT EXISTS oinkbi_mob_slays " +
                                    "(uuid VARCHAR(255) NOT NULL, mobtype VARCHAR(255) NOT NULL, playerSlayedMob INT NOT NULL, mobSlayedPlayer INT NOT NULL);"
                    );
                    connection.close();
                    Main.log.info("Created table oinkbi_mob_slays");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            return null;
        });

        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void createOnlineWorldTimeTable() {
        Future<Void> future = CompletableFuture.supplyAsync(() -> {
            if (!tableExist("oinkbi_time_in_world")) {
                try {
                    Connection connection = getConnection();
                    connection.createStatement().execute(
                            "CREATE TABLE IF NOT EXISTS oinkbi_time_in_world " +
                                    "(uuid VARCHAR(255) NOT NULL, world VARCHAR(255) NOT NULL, time_online BIGINT NOT NULL);"
                    );
                    connection.close();
                    Main.log.info("Created table oinkbi_time_in_world");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            return null;
        });

        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void createOnlineTimeTable() {
        Future<Void> future = CompletableFuture.supplyAsync(() -> {
            if (!tableExist("oinkbi_time_in_world")) {
                try {
                    Connection connection = getConnection();
                    connection.createStatement().execute(
                            "CREATE TABLE IF NOT EXISTS " + Constants.TABLE_ONLINE_TIME + " " +
                                    "(uuid VARCHAR(255) NOT NULL, time_online BIGINT NOT NULL);"
                    );
                    connection.close();
                    Main.log.info("Created table oinkbi_time_in_world");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            return null;
        });

        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void createTelemetryTable() {
        Future<Void> future = CompletableFuture.supplyAsync(() -> {
            if (!tableExist("oinkbi_telemetry")) {
                try {
                    Connection connection = getConnection();
                    connection.createStatement().execute(
                            "CREATE TABLE IF NOT EXISTS oinkbi_telemetry " +
                                    "(uuid VARCHAR(255) NOT NULL, telemetry_shown BOOLEAN NOT NULL, is_in BOOLEAN NOT NULL, PRIMARY KEY (uuid));"
                    );
                    connection.close();
                    Main.log.info("Created table oinkbi_telemetry");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            return null;
        });

        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void initialisePlayer(UUID uuid) {
        executeRaw("INSERT INTO oinkbi_telemetry (uuid,telemetry_shown,is_in) VALUES ('"+uuid+"','Yes','Yes');");
        Main.getInstance().getServer().getWorlds().forEach(world -> executeRaw("INSERT INTO oinkbi_time_in_world (uuid,world,time_online) VALUES ('"+uuid.toString()+"','"+world.getName()+"',0);"));
    }

    public void setUpTables() {
        createMobSlaysTable();
    }

    public void executeRaw(String statement) {
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
                @SuppressWarnings("SqlResolve")
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
