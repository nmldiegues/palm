package controllers;

import models.Role;
import models.User;

public class Security extends Secure.Security {

	static boolean authentify(String username, String password) {
		return User.connect(username, password) != null;
	}

	static void onDisconnected() {
		Application.index();
	}

	static void onAuthenticated() {
		Admin.index();
	}

	static boolean check(String profile) {
		if ("admin".equals(profile)) {
			for (Role role : User.find("byEmail", connected()).<User> first().roles) {
				if (role.isAdmin()) {
					return true;
				}
			}
		}
		return false;
	}

}
