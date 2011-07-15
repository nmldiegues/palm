package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import models.bibliography.ArticleRecord;
import play.data.validation.Email;
import play.data.validation.Password;
import play.data.validation.Required;
import play.db.jpa.Model;
import play.libs.Crypto;

@Entity
public class User extends Model {

	@Email
	@Required
	public String email;

	@Password
	@Required
	public String password; // it is hashed

	@Required
	public String firstName;

	public String lastName;

	@OneToMany(mappedBy = "submitter", cascade = CascadeType.DETACH)
	public List<ArticleRecord> records;

	@ManyToMany(cascade = CascadeType.PERSIST)
	public Set<Role> roles;

	public User(String email, String password, String firstName, String lastName) {
		super();
		this.email = email;
		this.password = Crypto.passwordHash(password);
		this.firstName = firstName;
		this.lastName = lastName;
		this.records = new ArrayList<ArticleRecord>();
	}

	public static User connect(String email, String password) {
		return find("byEmailAndPassword", email, Crypto.passwordHash(password)).first();
	}

	@Override
	public String toString() {
		return firstName + " " + lastName;
	}
}
