package models;

import javax.persistence.Entity;

@Entity
public class Manager extends Role {

	@Override
	public String toString() {
		return "Manager";
	}

	@Override
	public boolean isAdmin() {
		return true;
	}

}
