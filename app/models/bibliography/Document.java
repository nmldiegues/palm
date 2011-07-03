package models.bibliography;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.data.validation.Required;
import play.db.jpa.Blob;
import play.db.jpa.Model;
import play.libs.MimeTypes;

@Entity
public class Document extends Model {

	public enum DocumentType {
		ARTICLE("Article"), NOTES("Notes"), OTHER("Other");

		private final String type;

		DocumentType(String type) {
			this.type = type;
		}

		public String type() {
			return this.type;
		}
	}

	@Required
	public String identification;

	@Required
	public Blob content;

	@Required
	public Date creationDate;

	@Required
	@ManyToOne
	public ArticleRecord record;

	@Required
	public DocumentType type;

	public Document() {
		super();
	}

	public static Document createDocument(String identification, File content, ArticleRecord record, DocumentType type)
			throws FileNotFoundException {
		Document document = new Document();
		document.content = new Blob();
		document.content.set(new FileInputStream(content), MimeTypes.getContentType(content.getName()));
		document.identification = identification;
		document.record = record;
		document.creationDate = new Date();
		document.type = type;
		return document.save();
	}

	@Override
	public String toString() {
		return identification;
	}

}
