package models.bibliography;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import models.User;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class ArticleRecord extends Model {

	@Required
	public String name;

	@Required
	public Date creationDate;

	@Required
	@ManyToOne
	public User author;

	@OneToMany(mappedBy = "record", cascade = CascadeType.ALL)
	public List<Document> documents;

	@ManyToMany(cascade = CascadeType.PERSIST)
	public Set<Tag> tags;

	public ArticleRecord(String name, User author) {
		super();
		this.name = name;
		this.author = author;
		this.creationDate = new Date();
		this.documents = new ArrayList<Document>();
		this.tags = new TreeSet<Tag>();
	}

	public ArticleRecord addDocument(String identification, String content) {
		Document newDocument = new Document(identification, content, this).save();
		this.documents.add(newDocument);
		return this;
	}

	public ArticleRecord previous() {
		return ArticleRecord.find("creationDate < ? order by creationDate desc", creationDate).first();
	}

	public ArticleRecord next() {
		return ArticleRecord.find("creationDate > ? order by creationDate asc", creationDate).first();
	}

	public ArticleRecord tagItWith(String name) {
		tags.add(Tag.findOrCreateByName(name));
		return this;
	}

	public static List<ArticleRecord> findTaggedWith(String tag) {
		return ArticleRecord.find("select distinct p from ArticleRecord p join p.tags as t where t.name = ?", tag)
				.fetch();
	}

	public static List<ArticleRecord> findTaggedWith(String... tags) {
		return ArticleRecord
				.find("select distinct p.id from ArticleRecord p join p.tags as t where t.name in (:tags) group by p.id having count(t.id) = :size")
				.bind("tags", tags).bind("size", tags.length).fetch();
	}

	@Override
	public String toString() {
		return name;
	}
}
