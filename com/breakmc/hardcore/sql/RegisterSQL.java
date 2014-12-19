package com.breakmc.hardcore.sql;

import java.sql.*;
import java.util.*;
import org.bukkit.*;
import java.text.*;
import java.security.*;

public class RegisterSQL
{
    private static Connection con;
    
    public RegisterSQL(final String url, final String database, final String user, final String pass) {
        super();
        try {
            RegisterSQL.con = DriverManager.getConnection("jdbc:mysql://" + url + "/" + database, user, pass);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void close() {
        try {
            if (RegisterSQL.con != null && !RegisterSQL.con.isClosed()) {
                RegisterSQL.con.close();
            }
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public static boolean doesDatabaseContainUuid(final UUID uuid, final String table) {
        try {
            final PreparedStatement sql = RegisterSQL.con.prepareStatement("SELECT * FROM `" + table + "` WHERE uuid=?;");
            sql.setString(1, uuid.toString());
            return sql.executeQuery().next();
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean doInsert(final UUID uuid, final String table, final String password, final String email) {
        try {
            final OfflinePlayer ot = Bukkit.getOfflinePlayer(uuid);
            if (ot != null && ot.getName() != null && !doesDatabaseContainUuid(uuid, table)) {
                final DateFormat date = new SimpleDateFormat("dd/MM/yyyy");
                final Calendar cal = Calendar.getInstance();
                final PreparedStatement insert = RegisterSQL.con.prepareStatement("INSERT INTO `users` values(?, ?, ?, ?, ?, ?);");
                insert.setString(1, uuid.toString());
                insert.setString(2, ot.getName());
                insert.setString(3, hash(password));
                insert.setString(4, email);
                insert.setString(5, "");
                insert.setString(6, date.format(cal.getTime()));
                insert.executeUpdate();
                insert.close();
                return true;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
    
    public static boolean doSet(final UUID uuid, final String table, final String column, final Object set, final String where_column, final Object where) {
        try {
            final PreparedStatement sql = RegisterSQL.con.prepareStatement("UPDATE `" + table + "` SET " + column + "=? WHERE " + where_column + "=?;");
            sql.setObject(1, set);
            sql.setObject(2, where);
            sql.executeUpdate();
            sql.close();
            return true;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static String hash(final String password) {
        String generatedPass = null;
        try {
            final MessageDigest md = MessageDigest.getInstance("SHA-1");
            final byte[] bytes = md.digest(password.getBytes());
            final StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; ++i) {
                sb.append(Integer.toString((bytes[i] & 0xFF) + 256, 16).substring(1));
            }
            generatedPass = sb.toString();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPass;
    }
}
