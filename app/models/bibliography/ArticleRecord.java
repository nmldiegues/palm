package models.bibliography;

import java.io.File;
import java.io.FileNotFoundException;
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
	public int year;

	@Required
	@ManyToMany(cascade = CascadeType.PERSIST)
	public List<Author> authors;

	@Required
	public Date creationDate;

	@Required
	@ManyToOne
	public User submitter;

	@OneToMany(mappedBy = "record", cascade = CascadeType.ALL)
	public List<Document> documents;

	@ManyToMany(cascade = CascadeType.PERSIST)
	public Set<Tag> tags;

	public ArticleRecord(String name, Integer year, User submitter, List<Author> authors) {
		super();
		this.name = name;
		this.year = year;
		this.submitter = submitter;
		this.authors = authors;
		this.creationDate = new Date();
		this.documents = new ArrayList<Document>();
		this.tags = new TreeSet<Tag>();
	}

	public ArticleRecord(String name, Integer year, User submitter) {
		this(name, year, submitter, new ArrayList<Author>());
	}

	public ArticleRecord addDocument(String identification, DocumentType type, File content) {
		try {
			this.documents.add(Document.createDocument(identification, content, this, type));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			// FIXME failure message
		}
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

	public boolean hasArticle() {
		for (Document document : documents) {
			if (document.type.type().equals(DocumentType.NOTES_TYPE)) {
				return true;
			}
		}
		return false;
	}

	public ArticleRecord addAuthorship(String author) {
		authors.add(Author.findOrCreateByNames(author));
		return this;
	}

	@Override
	public String toString() {
		return name;
	}
}
