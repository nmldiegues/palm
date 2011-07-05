package models.bibliography;

import java.util.Arrays;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Transient;

import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class DocumentType extends Model {

	@Transient
	public static transient final String ARTICLE_TYPE = "Article";

	@Transient
	public static transient final String NOTES_TYPE = "Notes";

	@Transient
	public static transient final String OTHER_TYPE = "Other";

	@Transient
	public static transient final List<String> TYPES = Arrays.asList(ARTICLE_TYPE, NOTES_TYPE, OTHER_TYPE);

	@Required
	public String type;

	private DocumentType(String type) {
		this.type = type;
	}

	public String type() {
		return this.type;
	}

	public static DocumentType getOrCreate(String type) {
		DocumentType documentType = DocumentType.find("byType", type).first();
		if (documentType == null) {
			documentType = new DocumentType(type);
		}
		return documentType;
	}

	public boolean isArticleType() {
		return this.type == ARTICLE_TYPE;
	}

	public boolean isNotesType() {
		return this.type == NOTES_TYPE;
	}

	public boolean isOtherType() {
		return this.type == OTHER_TYPE;
	}

	@Override
	public String toString() {
		return type;
	}
}