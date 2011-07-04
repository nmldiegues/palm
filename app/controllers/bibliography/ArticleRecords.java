package controllers.bibliography;

import models.bibliography.ArticleRecord;
import play.mvc.With;
import controllers.CRUD;
import controllers.Check;
import controllers.Secure;

@Check("admin")
@With(Secure.class)
@CRUD.For(ArticleRecord.class)
public class ArticleRecords extends CRUD {

}
