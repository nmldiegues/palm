package controllers;

import models.User;
import play.mvc.With;

@Check("admin")
@With(Secure.class)
@CRUD.For(User.class)
public class Users extends CRUD {

}
