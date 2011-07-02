package controllers;

import java.util.List;

import models.bibliography.ArticleRecord;
import play.Play;
import play.data.validation.Required;
import play.mvc.Before;
import play.mvc.Controller;

public class Application extends Controller {

	@Before
	static void addDefaults() {
		renderArgs.put("siteTitle", Play.configuration.getProperty("site.title"));
		renderArgs.put("siteBaseline", Play.configuration.getProperty("site.baseline"));
	}

	public static void index() {
		ArticleRecord frontRecord = ArticleRecord.find("order by creationDate desc").first();
		List<ArticleRecord> olderRecords = ArticleRecord.find("order by creationDate desc").from(1).fetch(10);
		render(frontRecord, olderRecords);
	}

	public static void show(Long id) {
		ArticleRecord articleRecord = ArticleRecord.findById(id);
		render(articleRecord);
	}

	public static void submitDocument(Long recordId, @Required String identification, @Required String content) {
		ArticleRecord articleRecord = ArticleRecord.findById(recordId);
		if (validation.hasErrors()) {
			render("Application/show.html", articleRecord);
		}
		articleRecord.addDocument(identification, content);
		flash.success("Your document '%s' has been uploaded.", identification);
		show(recordId);
	}

}