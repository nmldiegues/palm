import java.util.List;
import java.util.Map;

import models.User;
import models.bibliography.ArticleRecord;
import models.bibliography.Document;
import models.bibliography.Tag;

import org.junit.Before;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;

public class BasicTest extends UnitTest {

	@Test
	public void createAndRetrieveUser() {
		// Create a new user and save it
		new User("bob@gmail.com", "secret", "Bob", "Spark").save();

		// Retrieve the user with email address bob@gmail.com
		User bob = User.find("byEmail", "bob@gmail.com").first();

		// Test
		assertNotNull(bob);
		assertEquals("Bob", bob.firstName);
	}

	@Test
	public void tryConnectAsUser() {
		// Create a new user and save it
		new User("bob@gmail.com", "secret", "Bob", "Spark").save();

		// Test
		assertNotNull(User.connect("bob@gmail.com", "secret"));
		assertNull(User.connect("bob@gmail.com", "badpassword"));
		assertNull(User.connect("tom@gmail.com", "secret"));
	}

	@Test
	public void createRecord() {
		// Create a new user and save it
		User bob = new User("bob@gmail.com", "secret", "Bob", "Spark").save();

		// Create a new article record
		new ArticleRecord("First Article Record", bob).save();

		// Test that the article record has been created
		assertEquals(1, ArticleRecord.count());

		// Retrieve all article records created by bob
		List<ArticleRecord> bobRecords = ArticleRecord.find("byAuthor", bob).fetch();

		// Tests
		assertEquals(1, bobRecords.size());
		ArticleRecord firstRecord = bobRecords.get(0);
		assertNotNull(firstRecord);
		assertEquals(bob, firstRecord.author);
		assertEquals("First Article Record", firstRecord.name);
		assertNotNull(firstRecord.creationDate);
	}

	@Test
	public void useTheDocumentsRelation() {
		// Create a new user and save it
		User bob = new User("bob@gmail.com", "secret", "Bob", "Spark").save();

		// Create a new article record
		ArticleRecord bobRecord = new ArticleRecord("My Article Record", bob).save();

		// Add two documents
		bobRecord.addDocument("Some Paper Conference", "Huge contents");
		bobRecord.addDocument("My Notes Title", "Notes taken");

		// Count things
		assertEquals(1, User.count());
		assertEquals(1, ArticleRecord.count());
		assertEquals(2, Document.count());

		// Retrieve Bob's article record
		bobRecord = ArticleRecord.find("byAuthor", bob).first();
		assertNotNull(bobRecord);

		// Navigate to documents
		assertEquals(2, bobRecord.documents.size());
		assertEquals("Some Paper Conference", bobRecord.documents.get(0).identification);

		// Delete the article record
		bobRecord.delete();

		// Check that all comments have been deleted
		assertEquals(1, User.count());
		assertEquals(0, ArticleRecord.count());
		assertEquals(0, Document.count());
	}

	@Test
	public void fullTest() {
		Fixtures.load("data.yml");

		// Count things
		assertEquals(2, User.count());
		assertEquals(3, ArticleRecord.count());
		assertEquals(3, Document.count());

		// Try to connect as users
		assertNotNull(User.connect("bob@gmail.com", "secret"));
		assertNotNull(User.connect("jeff@gmail.com", "secret"));
		assertNull(User.connect("jeff@gmail.com", "badpassword"));
		assertNull(User.connect("tom@gmail.com", "secret"));

		// Find all bob's article records
		List<ArticleRecord> bobRecords = ArticleRecord.find("author.email", "bob@gmail.com").fetch();
		assertEquals(2, bobRecords.size());

		// Find all documents related to bob's article records
		List<Document> bobDocuments = Document.find("record.author.email", "bob@gmail.com").fetch();
		assertEquals(3, bobDocuments.size());

		// Find the most recent article record
		ArticleRecord latestRecord = ArticleRecord.find("order by creationDate desc").first();
		assertNotNull(latestRecord);
		assertEquals("About the model layer", latestRecord.name);

		// Check that this post has two comments
		assertEquals(2, latestRecord.documents.size());

		// Post a new comment
		latestRecord.addDocument("Other doc", "Huge unwordly contents");
		assertEquals(3, latestRecord.documents.size());
		assertEquals(4, Document.count());
	}

	@Test
	public void testTags() {
		// Create a new user and save it
		User bob = new User("bob@gmail.com", "secret", "Bob", "Spark").save();

		// Create a new post
		ArticleRecord bobRecord = new ArticleRecord("My first post", bob).save();
		ArticleRecord anotherBobRecord = new ArticleRecord("Hop", bob).save();

		// Well
		assertEquals(0, ArticleRecord.findTaggedWith("Red").size());

		// Tag it now
		bobRecord.tagItWith("Red").tagItWith("Blue").save();
		anotherBobRecord.tagItWith("Red").tagItWith("Green").save();

		// Check
		assertEquals(2, ArticleRecord.findTaggedWith("Red").size());
		assertEquals(1, ArticleRecord.findTaggedWith("Blue").size());
		assertEquals(1, ArticleRecord.findTaggedWith("Green").size());

		// Multiple tags
		assertEquals(1, ArticleRecord.findTaggedWith("Red", "Blue").size());
		assertEquals(1, ArticleRecord.findTaggedWith("Red", "Green").size());
		assertEquals(0, ArticleRecord.findTaggedWith("Red", "Green", "Blue").size());
		assertEquals(0, ArticleRecord.findTaggedWith("Green", "Blue").size());

		// Test tag cloud
		List<Map> cloud = Tag.getCloud();
		assertEquals("[{tag=Red, pound=2}, {tag=Blue, pound=1}, {tag=Green, pound=1}]", cloud.toString());
	}

	@Before
	public void setup() {
		Fixtures.deleteDatabase();
	}
}
