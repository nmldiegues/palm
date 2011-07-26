package controllers;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import models.bibliography.ArticleRecord;
import models.bibliography.Author;
import models.bibliography.Citation;
import models.bibliography.Document;
import models.bibliography.DocumentType;
import models.bibliography.Tag;
import play.Play;
import play.cache.Cache;
import play.data.validation.Required;
import play.libs.Codec;
import play.libs.Images;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;

/*
 * TODO space between article records
 * TODO move the "PALM" big header into the login screen.
 * TODO if new features come up, create small menu "taggish" buttons at the top instead of the big header
 * TODO add new models to CRUD
 * TODO update itext when a new version is out. some papers aren't decodable due to internal NPE in itext
 */

@With(Secure.class)
public class Application extends Controller {

	@Before
	static void addDefaults() {
		renderArgs.put("siteTitle", Play.configuration.getProperty("site.title"));
		renderArgs.put("siteBaseline", Play.configuration.getProperty("site.baseline"));
	}

	public static void index() {
		List<ArticleRecord> records = ArticleRecord.find("order by year desc").fetch();
		render(records);
	}

	public static void show(Long id) {
		ArticleRecord articleRecord = ArticleRecord.findById(id);
		render(articleRecord);
	}

	public static void newDocument(Long id) {
		ArticleRecord articleRecord = ArticleRecord.findById(id);
		String randomID = Codec.UUID();
		List<String> possibleTypes = DocumentType.TYPES;
		render("Application/upload.html", articleRecord, randomID, possibleTypes);
	}

	public static void submitDocument(Long recordId,
			@Required(message = "Identification is required") String identification,
			@Required(message = "Content is required") File content,
			@Required(message = "Type is required") String type, String[] parse,
			@Required(message = "Please type the code") String code, String randomID) {

		ArticleRecord articleRecord = ArticleRecord.findById(recordId);
		if (!Play.id.equals("test")) {
			validation.equals(code, Cache.get(randomID)).message("Invalid code. Please type it again");
		}
		if (validation.hasErrors()) {
			render("Application/show.html", articleRecord, randomID);
		}

		Boolean parseCitations = false;
		if (parse != null) {
			parseCitations = true;
		}

		articleRecord.addDocument(identification, DocumentType.getOrCreate(type), content, parseCitations);

		List<ArticleRecord> articles = ArticleRecord.findAll();
		for (ArticleRecord article : articles) {
			article.findReferencesToMe();
			article.save();
		}

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

	public static void listAuthoredBy(String author) {
		List<ArticleRecord> articleRecords = ArticleRecord.findAuthoredBy(author);
		render(author, articleRecords);
	}

	public static void downloadDocument(Long id) {
		final Document document = Document.findById(id);
		notFoundIfNull(document);
		response.setContentTypeIfNotSet(document.mimeType);
		renderBinary(new ByteArrayInputStream(document.content), document.fileName);
	}

	public static void newArticleRecord() {
		render("Application/submit.html");
	}

	// FIXME add tags
	public static void addArticleRecord(@Required(message = "Name is required") String name,
			@Required(message = "Publication year is required") Integer year,
			@Required(message = "Authors are required") String authors) {

		if (validation.hasErrors()) {
			render("Application/show.html");
		}

		List<Author> articleAuthors = new ArrayList<Author>();
		for (String author : authors.split(";")) {
			if (author.trim().length() > 0) {
				articleAuthors.add(Author.findOrCreateByNames(author));
			}
		}

		ArticleRecord articleRecord = new ArticleRecord(name, year, Security.fetchConnected(), articleAuthors);
		articleRecord.save();
		articleRecord.submitter.records.add(articleRecord);
		articleRecord.submitter.save();

		flash.success("Your Article Record '%s' has been added.", name);
		// FIXME jump to the article record
		index();
	}

	public static void prepareToTag(Long id) {
		ArticleRecord articleRecord = ArticleRecord.findById(id);
		List<Tag> possibleTags = Tag.findAll();
		render("Application/addTags.html", articleRecord, possibleTags);
	}

	public static void addTags(Long id, String[] existentTags, String tags) {
		ArticleRecord articleRecord = ArticleRecord.findById(id);
		articleRecord.tags.clear();
		if (existentTags != null) {
			for (String tag : existentTags) {
				articleRecord.tagItWith(tag);
			}
		}
		for (String tag : tags.split(";")) {
			if (tag.trim().length() > 0) {
				articleRecord.tagItWith(tag);
			}
		}

		articleRecord.save();

		show(id);
	}

	public static void prepareCloudTag() {
		List<Map> tags = Tag.getCloud();
		Long tmp = 0L;
		for (Map map : tags) {
			tmp += (Long) map.get("pound");
		}
		Double totalCount = 0.0 + tmp;
		List<Tag> tmp2 = Tag.findAll();
		Tag tagExample = null;
		if (tmp2.size() > 0) {
			tagExample = tmp2.get(0);
		}
		render("Application/cloudTag.html", tags, totalCount, tagExample);
	}

	public static void removeCitations(Long articleId) {
		ArticleRecord articleRecord = ArticleRecord.findById(articleId);
		for (Citation cit : articleRecord.references) {
			ArticleRecord referencedBy = cit.recordReferencedBy;
			if (referencedBy != null) {
				referencedBy.referencedBy.remove(cit);
			}
			cit.delete();
		}
		articleRecord.references.clear();
		articleRecord.save();
		show(articleId);
	}

	public static void removeDocument(Long articleId, Long documentId) {
		ArticleRecord articleRecord = ArticleRecord.findById(articleId);
		Document document = null;
		for (Document doc : articleRecord.documents) {
			if (doc.id.equals(documentId)) {
				document = doc;
				break;
			}
		}

		validation.required(document).message("Invalid document id: " + documentId);
		validation.equals(articleRecord.references.size(), 0).message("Remove all existing citations first.");
		if (validation.hasErrors()) {
			render("Application/show.html", articleRecord);
		}

		articleRecord.removeDocument(document);
		articleRecord.save();

		flash.success("The article has been successfully removed.");
		show(articleId);
	}
}