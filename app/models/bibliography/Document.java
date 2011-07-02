package models.bibliography;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class Document extends Model {

	@Required
	public String identification;

	@Lob
	@Required
	public String content;

	@Required
	public Date creationDate;

	@Required
	@ManyToOne
	public ArticleRecord record;

	public Document(String identification, String content, ArticleRecord record) {
		super();
		this.content = content;
		this.identification = identification;
		this.record = record;
		this.creationDate = new Date();
	}

	@Override
	public String toString() {
		return identification;
	}

}
