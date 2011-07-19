package models.bibliography;

import java.util.List;
import java.util.Map;

import javax.persistence.Entity;

import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class Tag extends Model implements Comparable<Tag> {

	@Required
	public String name;

	private Tag(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public int compareTo(Tag otherTag) {
		return name.compareTo(otherTag.name);
	}

	public static Tag findOrCreateByName(String name) {
		Tag tag = Tag.find("byName", name).first();
		if (tag == null) {
			tag = new Tag(name);
		}
		return tag;
	}

	public static List<Map> getCloud() {
		List<Map> result = Tag
				.find("select new map(t.name as tag, count(p.id) as pound) from ArticleRecord p join p.tags as t group by t.name order by count(p.id) desc, t.name")
				.fetch();
		return result;
	}

}