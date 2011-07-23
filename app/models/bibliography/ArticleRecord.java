package models.bibliography;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
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

import com.citext.pdf.BibUtils;
import com.citext.pdf.CitationMetadata;
import com.citext.pdf.CitationParser;

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

	@OneToMany(mappedBy = "recordThatReferences", cascade = CascadeType.ALL)
	public List<Citation> references;

	@OneToMany(mappedBy = "recordReferencedBy", cascade = CascadeType.ALL)
	public List<Citation> referencedBy;

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
		this.references = new ArrayList<Citation>();
		this.referencedBy = new ArrayList<Citation>();
	}

	public ArticleRecord(String name, Integer year, User submitter) {
		this(name, year, submitter, new ArrayList<Author>());
	}

	public String shortName() {
		if (this.name.length() > 60) {
			return this.name.substring(0, 60) + "(...)";
		}
		return this.name;
	}

	public ArticleRecord addDocument(String identification, DocumentType type, File content, Boolean parseCitations) {

		try {
			Document newDoc = Document.createDocument(identification, content, this, type);
			this.documents.add(newDoc);
		} catch (IOException e) {
			e.printStackTrace();
			return this;
		}

		try {
			parseCitations(type, BibUtils.readPdf(content), parseCitations);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return this;
	}

	public ArticleRecord parseCitations(DocumentType type, String content, Boolean parseCitations) {
		if (type.isArticleType() && parseCitations) {
			List<CitationMetadata> metadata = null;
			CitationParser parser = new CitationParser(content);
			metadata = parser.fetchAllCitations();
			for (CitationMetadata citMeta : metadata) {
				for (Map.Entry<Integer, String> entry : citMeta.getReferences().entrySet()) {
					Citation newCit = new Citation(entry.getKey(), entry.getValue(), citMeta.getCitation(), this);
					newCit.save();
					this.references.add(newCit);
				}
			}
		}
		return this;
	}

	public ArticleRecord removeDocument(Document document) {
		this.documents.remove(document);
		document.delete();
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

	public boolean hasNotes() {
		for (Document document : documents) {
			if (document.type.isNotesType()) {
				return true;
			}
		}
		return false;
	}

	public boolean hasArticle() {
		for (Document document : documents) {
			if (document.type.isArticleType()) {
				return true;
			}
		}
		return false;
	}

	public ArticleRecord addAuthorship(String author) {
		authors.add(Author.findOrCreateByNames(author));
		return this;
	}

	public List<Citation> getOrderedCitations() {
		List<Citation> result = new ArrayList<Citation>(this.references);
		Collections.sort(result);
		return result;
	}

	public void findReferencesToMe() {
		List<Citation> citations = Citation.findAll();
		for (Citation cit : citations) {
			if (cit.reference.toLowerCase().contains(this.name.toLowerCase())) {
				boolean alreadyExists = false;
				for (Citation alreadyHave : this.referencedBy) {
					if (alreadyHave.id.equals(cit.id) && cit.recordReferencedBy == this) {
						alreadyExists = true;
						break;
					}
				}
				if (!alreadyExists) {
					this.referencedBy.add(cit);
					cit.recordReferencedBy = this;
					cit.save();
				}
			}
		}
	}

	@Override
	public String toString() {
		return name;
	}
}
