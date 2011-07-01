package models.bibliography;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;

@Entity
public class Document extends Model {

	public String identification;
	@Lob
	public String content;
	public Date creationDate;

	@ManyToOne
	public ArticleRecord record;

	public Document(String identification, String content, ArticleRecord record) {
		super();
		this.content = content;
		this.identification = identification;
		this.record = record;
		this.creationDate = new Date();
	}

}
