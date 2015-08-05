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

	public int calculateWeight(Long usageCount, Double totalCount) {
		Double category = usageCount / totalCount;
		if (category > 0.2) {
			return 1;
		} else if (category > 0.1) {
			return 2;
		} else if (category > 0.05) {
			return 3;
		} else if (category > 0.025) {
			return 4;
		} else {
			return 5;
		}
	}

	public static List<Map> getCloud() {
		List<Map> result = Tag
				.find("select new map(t.name as tag, count(p.id) as pound) from ArticleRecord p join p.tags as t group by t.name order by count(p.id) desc, t.name")
				.fetch();
		return result;
	}

}
