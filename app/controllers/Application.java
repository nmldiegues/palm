package controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import models.bibliography.ArticleRecord;
import models.bibliography.Author;
import models.bibliography.Document;
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
			@Required(message = "Content is required") File content,
			@Required(message = "Please type the code") String code, String randomID) {

		ArticleRecord articleRecord = ArticleRecord.findById(recordId);
		if (!Play.id.equals("test")) {
			validation.equals(code, Cache.get(randomID)).message("Invalid code. Please type it again");
		}
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

	public static void downloadDocument(Long id) {
		final Document document = Document.findById(id);
		notFoundIfNull(document);
		response.setContentTypeIfNotSet(document.content.type());
		renderBinary(document.content.get(), document.identification);
	}

	public static void newArticleRecord() {
		render("Application/submit.html");
	}

	public static void addArticleRecord(@Required(message = "Name is required") String name,
			@Required(message = "Authors are required") String authors) {

		if (validation.hasErrors()) {
			render("Application/show.html");
		}

		List<Author> articleAuthors = new ArrayList<Author>();
		for (String author : authors.split("\\s+")) {
			if (author.trim().length() > 0) {
				articleAuthors.add(Author.findOrCreateByNames(author));
			}
		}

		ArticleRecord articleRecord = new ArticleRecord(name, Security.fetchConnected(), articleAuthors);
		articleRecord.save();

		flash.success("Your Article Record '%s' has been added.", name);
		// FIXME jump to the article record
		index();
	}
}