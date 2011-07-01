import java.util.List;

import models.User;
import models.bibliography.ArticleRecord;
import models.bibliography.Document;

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
		List<ArticleRecord> bobRecords = ArticleRecord.find("byAuthor", bob)
				.fetch();

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
		ArticleRecord bobRecord = new ArticleRecord("My Article Record", bob)
				.save();

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
		assertEquals("Some Paper Conference",
				bobRecord.documents.get(0).identification);

		// Delete the article record
		bobRecord.delete();

		// Check that all comments have been deleted
		assertEquals(1, User.count());
		assertEquals(0, ArticleRecord.count());
		assertEquals(0, Document.count());
	}

	@Before
	public void setup() {
		Fixtures.deleteDatabase();
	}
}
