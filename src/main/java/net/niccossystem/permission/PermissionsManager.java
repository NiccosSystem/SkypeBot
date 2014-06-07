package net.niccossystem.permission;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import net.niccossystem.skypebot.SkypeBot;

public class PermissionsManager {
	
	private Map<String, List<String>> userPermissions;
	
	
	public void givePermission(String user, String permission) {
		String mapUser = null;
		for (String mU : userPermissions.keySet()) {
			if (mU.equalsIgnoreCase(user)) { mapUser = mU; break; }
		}
		
		if (mapUser == null) {
			List<String> permissions = new ArrayList<String>();
			permissions.add(permission);
			userPermissions.put(user, permissions);
			savePermissionsToFile();
			return;
		} else {
			userPermissions.get(mapUser).add(permission);
			savePermissionsToFile();
			return;
		}
	}
	
	public void revokePermission(String user, String permission) {
		String mapUser = null;
		for (String mU : userPermissions.keySet()) {
			if (mU.equalsIgnoreCase(user)) { mapUser = mU; break; }
		}
		
		if (mapUser == null) {
			return;
		} else {
			for (String perm : userPermissions.get(mapUser)) {
				if (perm.equals(permission)) {
					userPermissions.get(mapUser).remove(perm);
					return;
				}
			}
		}
	}
	
	public boolean hasPermission(String user, String[] permissions) {
		if (permissions == null || permissions.length == 0) return true;
		String mapUser = null;
		for (String u : userPermissions.keySet()) {
			if (u.equalsIgnoreCase(user)) { mapUser = u; break; }
		}
		
		if (mapUser == null) { return false; }
		
		for (String permission : permissions) {
			for (String mapPermission : userPermissions.get(mapUser)) {
				if (mapPermission.equals(permission) || mapPermission.equals("*")) return true;
			}
		}
		
		return false;
	}
	
	public void loadPermissionsFromFile() {
		SkypeBot.checkPermissionsFile();
		JsonObject jsonPermissions = SkypeBot.getPermissions();
		Gson gson = new Gson();
		Type permissionsMap = new TypeToken<Map<String, List<String>>>(){}.getType();
		Map<String, List<String>> deSerPermissions = gson.fromJson(jsonPermissions, permissionsMap);
		userPermissions = deSerPermissions;
	}
	
	public void savePermissionsToFile() {
		Gson gson = new Gson();
		JsonObject jsonPermissions = new JsonParser().parse(gson.toJson(userPermissions)).getAsJsonObject();
		SkypeBot.setPermissions(jsonPermissions);
		try {
			SkypeBot.savePermissionsFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public List<String> getPermissions(String user) {
		String mapUser = null;
		for (String u : userPermissions.keySet()) {
			if (u.equalsIgnoreCase(user)) { mapUser = u; break; }
		}
		if (mapUser == null) return null;
		
		return userPermissions.get(mapUser);
	}
	
	
	
}
