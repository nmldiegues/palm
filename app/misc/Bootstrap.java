package misc;

import play.jobs.Job;
import play.jobs.OnApplicationStart;

@OnApplicationStart
public class Bootstrap extends Job {

	@Override
	public void doJob() {
		// Fixtures.delete();
		// // Check if the database is empty
		// if (User.count() == 0) {
		// Fixtures.load("data.yml");
		// }
		// List<ArticleRecord> articles = ArticleRecord.findAll();
		// for (ArticleRecord article : articles) {
		// article.findReferencesToMe();
		// article.save();
		// }
	}

}
