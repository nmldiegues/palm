package models;

import javax.persistence.Entity;

@Entity
public class Submitter extends Role {

	@Override
	public String toString() {
		return "Submitter";
	}

	@Override
	public boolean isAdmin() {
		return false;
	}

}
