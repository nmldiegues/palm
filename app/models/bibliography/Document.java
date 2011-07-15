package models.bibliography;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import org.apache.commons.io.IOUtils;

import play.data.validation.Required;
import play.db.jpa.Model;
import play.libs.MimeTypes;

@Entity
public class Document extends Model {

	@Required
	public String identification;

	@Lob
	@Required
	public byte[] content;

	@Required
	public String fileName;

	@Required
	public String mimeType;

	@Required
	public Date creationDate;

	@Required
	@ManyToOne
	public ArticleRecord record;

	@Required
	@ManyToOne(cascade = CascadeType.ALL)
	public DocumentType type;

	public Document() {
		super();
	}

	public static Document createDocument(String identification, File content, ArticleRecord record, DocumentType type)
			throws IOException {
		Document document = new Document();
		document.content = IOUtils.toByteArray(new FileInputStream(content));
		document.fileName = content.getName();
		document.mimeType = MimeTypes.getContentType(content.getName());
		document.identification = identification;
		document.record = record;
		document.creationDate = new Date();
		document.type = type;
		type.save();
		return document.save();
	}

	@Override
	public String toString() {
		return identification;
	}

}
