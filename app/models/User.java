package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import models.bibliography.ArticleRecord;
import play.db.jpa.Model;

@Entity
public class User extends Model {

	public String email;
	public String password;
	public String firstName;
	public String lastName;

	@OneToMany(mappedBy = "author", cascade = CascadeType.DETACH)
	public List<ArticleRecord> records;

	public User(String email, String password, String firstName, String lastName) {
		super();
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.records = new ArrayList<ArticleRecord>();
	}

	public static User connect(String email, String password) {
		return find("byEmailAndPassword", email, password).first();
	}

	public User addRecord(String articleName) {
		ArticleRecord newRecord = new ArticleRecord(articleName, this).save();
		this.records.add(newRecord);
		return this;
	}

}
