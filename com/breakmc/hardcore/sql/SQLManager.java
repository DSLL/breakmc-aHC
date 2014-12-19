package com.breakmc.hardcore.sql;

import org.bukkit.plugin.java.*;
import com.avaje.ebean.validation.*;
import java.sql.*;

public class SQLManager
{
    @NotNull
    private static JavaPlugin plugin;
    @NotNull
    private static String url;
    @NotNull
    private static String db_name;
    @NotNull
    private static String user;
    @NotNull
    private static String pass;
    private static Connection con;
    
    public SQLManager(final JavaPlugin plugin) {
        super();
        SQLManager.plugin = plugin;
        enable();
    }
    
    public static Connection getConnection() {
        return SQLManager.con;
    }
    
    public static boolean isConnected() {
        try {
            if (getConnection() == null || getConnection().isClosed()) {
                return false;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    public static void enable() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            SQLManager.url = "192.99.46.113";
            SQLManager.db_name = "stats";
            SQLManager.user = "root";
            SQLManager.pass = "Oxmpm8Nd15U7";
            SQLManager.con = DriverManager.getConnection("jdbc:mysql://" + SQLManager.url + "/" + SQLManager.db_name, SQLManager.user, SQLManager.pass);
        }
        catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }
    
    public static void disable() {
        try {
            SQLManager.con.close();
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public static synchronized boolean doesDatabaseContainUuid(final String table, final String uuid) {
        try {
            final PreparedStatement sql = SQLManager.con.prepareStatement("SELECT * FROM `" + table + "` WHERE uuid=?;");
            sql.setString(1, uuid);
            final ResultSet result = sql.executeQuery();
            final boolean contains = result.next();
            return contains;
        }
        catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    public static synchronized boolean doesDatabaseContainName(final String table, final String name) {
        try {
            final PreparedStatement sql = SQLManager.con.prepareStatement("SELECT * FROM `" + table + "` WHERE name=?;");
            sql.setString(1, name);
            final ResultSet result = sql.executeQuery();
            final boolean contains = result.next();
            return contains;
        }
        catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    public static synchronized Object doQuery(final String table, final String column, final String uuid) {
        try {
            final PreparedStatement sql = SQLManager.con.prepareStatement("SELECT " + column + " FROM `" + table + "` WHERE uuid=?;");
            sql.setString(1, uuid);
            final ResultSet result = sql.executeQuery();
            result.next();
            final Object obj = result.getObject(column);
            sql.close();
            result.close();
            return obj;
        }
        catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    public static synchronized Object doQuery(final String table, final String column, final String where, final String where2) {
        try {
            final PreparedStatement sql = SQLManager.con.prepareStatement("SELECT " + column + " FROM `" + table + "` WHERE " + where + "=?;");
            sql.setString(1, where2);
            final ResultSet result = sql.executeQuery();
            result.next();
            final Object obj = result.getObject(column);
            sql.close();
            result.close();
            return obj;
        }
        catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    public static synchronized void doSet(final String table, final String column, final Object set, final String uuid) {
        try {
            final PreparedStatement sql = SQLManager.con.prepareStatement("UPDATE `" + table + "` SET " + column + "=? WHERE uuid=?;");
            sql.setObject(1, set);
            sql.setString(2, uuid);
            sql.executeUpdate();
            sql.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static synchronized void doSet(final String table, final String column, final Object set, final String where, final String where2) {
        try {
            final PreparedStatement sql = SQLManager.con.prepareStatement("UPDATE `" + table + "` SET " + column + "=? WHERE " + where + "=?;");
            sql.setObject(1, set);
            sql.setString(2, where2);
            sql.executeUpdate();
            sql.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
