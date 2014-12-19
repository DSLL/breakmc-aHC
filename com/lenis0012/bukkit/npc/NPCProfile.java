package com.lenis0012.bukkit.npc;

import net.minecraft.util.com.mojang.authlib.*;
import com.google.common.base.*;
import net.minecraft.util.org.apache.commons.lang3.*;
import java.util.*;
import org.json.simple.parser.*;
import org.json.simple.*;
import org.bukkit.*;
import java.util.logging.*;
import java.net.*;
import java.io.*;

public class NPCProfile extends GameProfile
{
    private UUID uuid;
    private String name;
    
    public NPCProfile() {
        super(UUID.randomUUID(), "internal");
    }
    
    public NPCProfile(final GameProfile profile) {
        this();
        this.uuid = UUID.fromString(profile.getId().toString());
        this.name = profile.getName();
    }
    
    public NPCProfile(final String name) {
        this();
        this.uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes(Charsets.UTF_8));
        this.name = name;
    }
    
    public NPCProfile(final String name, final String skinOwner) {
        this(name, parseUUID(getUUID(name)), skinOwner);
    }
    
    public NPCProfile(final String name, final UUID uuid) {
        this(name, uuid, uuid);
    }
    
    public NPCProfile(final String name, final UUID uuid, final String skinOwner) {
        this(name, uuid, parseUUID(getUUID(skinOwner)));
    }
    
    public NPCProfile(final String name, final UUID uuid, final UUID skinUUID) {
        this();
        this.uuid = uuid;
        this.name = name;
        addProperties(this, skinUUID);
    }
    
    public UUID getId() {
        return this.uuid;
    }
    
    public String getName() {
        return this.name;
    }
    
    public boolean isComplete() {
        return this.uuid != null && StringUtils.isNotBlank((CharSequence)this.getName());
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GameProfile)) {
            return false;
        }
        final GameProfile that = (GameProfile)o;
        Label_0054: {
            if (this.uuid != null) {
                if (this.uuid.equals(that.getId())) {
                    break Label_0054;
                }
            }
            else if (that.getId() == null) {
                break Label_0054;
            }
            return false;
        }
        if (this.name != null) {
            if (this.name.equals(that.getName())) {
                return true;
            }
        }
        else if (that.getName() == null) {
            return true;
        }
        return false;
    }
    
    public int hashCode() {
        int result = (this.uuid != null) ? this.uuid.hashCode() : 0;
        result = 31 * result + ((this.name != null) ? this.name.hashCode() : 0);
        return result;
    }
    
    private static void addProperties(final GameProfile profile, final UUID id) {
        final String uuid = id.toString().replaceAll("-", "");
        try {
            final URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
            final URLConnection uc = url.openConnection();
            uc.setUseCaches(false);
            uc.setDefaultUseCaches(false);
            uc.addRequestProperty("User-Agent", "Mozilla/5.0");
            uc.addRequestProperty("Cache-Control", "no-cache, no-store, must-revalidate");
            uc.addRequestProperty("Pragma", "no-cache");
            final Scanner scanner = new Scanner(uc.getInputStream(), "UTF-8");
            final String json = scanner.useDelimiter("\\A").next();
            scanner.close();
            final JSONParser parser = new JSONParser();
            final Object obj = parser.parse(json);
            final JSONArray properties = (JSONArray)((JSONObject)obj).get((Object)"properties");
            for (int i = 0; i < properties.size(); ++i) {
                try {
                    final JSONObject property = (JSONObject)properties.get(i);
                    final String name = (String)property.get((Object)"name");
                    final String value = (String)property.get((Object)"value");
                    final String s = property.containsKey((Object)"signature") ? ((String)property.get((Object)"signature")) : null;
                }
                catch (Exception e) {
                    Bukkit.getLogger().log(Level.WARNING, "Failed to apply auth property", e);
                }
            }
        }
        catch (Exception e2) {
            e2.printStackTrace();
        }
    }
    
    private static String getUUID(final String name) {
        return Bukkit.getOfflinePlayer(name).getUniqueId().toString().replaceAll("-", "");
    }
    
    private static UUID parseUUID(final String uuidStr) {
        final String[] uuidComponents = { uuidStr.substring(0, 8), uuidStr.substring(8, 12), uuidStr.substring(12, 16), uuidStr.substring(16, 20), uuidStr.substring(20, uuidStr.length()) };
        final StringBuilder builder = new StringBuilder();
        String[] array;
        for (int length = (array = uuidComponents).length, i = 0; i < length; ++i) {
            final String component = array[i];
            builder.append(component).append('-');
        }
        builder.setLength(builder.length() - 1);
        return UUID.fromString(builder.toString());
    }
}
