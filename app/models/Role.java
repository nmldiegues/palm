package models;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public abstract class Role extends Model {

	public static Role findOrCreateByName(String name) {
		// TODO complete
		return null;
	}

	@Override
	public abstract String toString();

	public abstract boolean isAdmin();
}
