package controllers.bibliography;

import models.bibliography.Document;
import play.mvc.With;
import controllers.CRUD;
import controllers.Check;
import controllers.Secure;

@Check("admin")
@With(Secure.class)
@CRUD.For(Document.class)
public class Documents extends CRUD {

}
