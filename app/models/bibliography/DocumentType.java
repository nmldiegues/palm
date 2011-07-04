package models.bibliography;

import javax.persistence.Entity;

import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class DocumentType extends Model {

	public static final int ARTICLE_TYPE = 0;

	public static final int NOTES_TYPE = 1;

	public static final int OTHER_TYPE = 2;

	@Required
	public int type;

	private DocumentType(int type) {
		this.type = type;
	}

	public int type() {
		return this.type;
	}

	public static DocumentType getOrCreate(int type) {
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
}