package controllers.bibliography;

import models.bibliography.Tag;
import play.mvc.With;
import controllers.CRUD;
import controllers.Check;
import controllers.Secure;

@Check("admin")
@With(Secure.class)
@CRUD.For(Tag.class)
public class Tags extends CRUD {

}
