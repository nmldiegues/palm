package models.bibliography;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class Author extends Model {

	@Required
	public String name;

	@ManyToMany
	public List<ArticleRecord> articles;

	public Author(String name) {
		this.name = name;
		this.articles = new ArrayList<ArticleRecord>();
	}

	public static Author findOrCreateByNames(String name) {
		Author author = Author.find("byName", name).first();
		if (author == null) {
			author = new Author(name);
		}
		return author;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
