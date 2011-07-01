package models.bibliography;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import models.User;
import play.db.jpa.Model;

@Entity
public class ArticleRecord extends Model {

	public String name;
	public Date creationDate;

	@ManyToOne
	public User author;

	@OneToMany(mappedBy = "record", cascade = CascadeType.ALL)
	public List<Document> documents;

	public ArticleRecord(String name, User author) {
		super();
		this.name = name;
		this.author = author;
		this.creationDate = new Date();
		this.documents = new ArrayList<Document>();
	}

	public ArticleRecord addDocument(String identification, String content) {
		Document newDocument = new Document(identification, content, this)
				.save();
		this.documents.add(newDocument);
		return this;
	}
}
