package models.bibliography;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class Citation extends Model implements Comparable<Citation> {

	// 1
	@Required
	public int bibNum;

	// "E. Author, B. Author and C. Author. In Proceedings"
	@Lob
	@Required
	public String reference;

	// "In [1] the authors claim that this is good."
	@Lob
	@Required
	public String citation;

	// "Paper that performs the citation"
	@Required
	@ManyToOne
	public ArticleRecord recordThatReferences;

	// "Paper that is referenced in the citation
	@ManyToOne
	public ArticleRecord recordReferencedBy;

	public Citation(int bibNum, String reference, String citation, ArticleRecord recordThatReferences) {
		this.bibNum = bibNum;
		this.reference = reference;
		this.citation = citation;
		this.recordThatReferences = recordThatReferences;
	}

	public int compareTo(Citation o) {
		if (this.bibNum < o.bibNum) {
			return -1;
		} else if (this.bibNum == o.bibNum) {
			return 0;
		} else {
			return 1;
		}
	}

}
