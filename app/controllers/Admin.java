package controllers;

import java.util.List;

import models.User;
import models.bibliography.ArticleRecord;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;

@With(Secure.class)
public class Admin extends Controller {

	@Before
	static void setConnectedUser() {
		if (Security.isConnected()) {
			User user = User.find("byEmail", Security.connected()).first();
			renderArgs.put("user", user.toString());
		}
	}

	public static void index() {
		List<ArticleRecord> articleRecords = ArticleRecord.find("author.email", Security.connected()).fetch();
		render(articleRecords);
	}

	public static void form(Long id) {
		if (id != null) {
			ArticleRecord articleRecord = ArticleRecord.findById(id);
			render(articleRecord);
		}
		render();
	}

	public static void form() {
		render();
	}

	public static void save(Long id, String name, String tags) {
		ArticleRecord articleRecord;
		if (id == null) {
			// Create ArticleRecord
			User author = User.find("byEmail", Security.connected()).first();
			articleRecord = new ArticleRecord(name, author);
		} else {
			// Retrieve the ArticleRecord
			articleRecord = ArticleRecord.findById(id);
			// Edit
			articleRecord.name = name;
			articleRecord.tags.clear();
		}
		// Set tags list
		for (String tag : tags.split("\\s+")) {
			if (tag.trim().length() > 0) {
				articleRecord.tagItWith(tag);
			}
		}
		// Validate
		validation.valid(articleRecord);
		if (validation.hasErrors()) {
			render("@form", articleRecord);
		}
		// Save
		articleRecord.save();
		index();
	}

}