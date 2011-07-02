package controllers;

import java.util.List;

import models.bibliography.ArticleRecord;
import play.Play;
import play.cache.Cache;
import play.data.validation.Required;
import play.libs.Codec;
import play.libs.Images;
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
		String randomID = Codec.UUID();
		render(articleRecord, randomID);
	}

	public static void submitDocument(Long recordId,
			@Required(message = "Identification is required") String identification,
			@Required(message = "Content is required") String content,
			@Required(message = "Please type the code") String code, String randomID) {

		ArticleRecord articleRecord = ArticleRecord.findById(recordId);
		validation.equals(code, Cache.get(randomID)).message("Invalid code. Please type it again");
		if (validation.hasErrors()) {
			render("Application/show.html", articleRecord, randomID);
		}
		articleRecord.addDocument(identification, content);
		flash.success("Your document '%s' has been uploaded.", identification);
		show(recordId);
	}

	public static void captcha(String id) {
		Images.Captcha captcha = Images.captcha();
		String code = captcha.getText("#E4EAFD");
		Cache.set(id, code, "10mn");
		renderBinary(captcha);
	}

	public static void listTagged(String tag) {
		List<ArticleRecord> articleRecords = ArticleRecord.findTaggedWith(tag);
		render(tag, articleRecords);
	}
}