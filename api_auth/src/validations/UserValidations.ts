import { celebrate, Joi, Segments } from "celebrate";

class UserValidation {
  public static register() {
    return celebrate({
      [Segments.BODY]: Joi.object().keys({
        email: Joi.string().required(),
        password: Joi.string().required(),
        firstName: Joi.string(),
        lastName: Joi.string(),
      }),
    });
  }
  public static login() {
    return celebrate({
      [Segments.BODY]: Joi.object().keys({
        email: Joi.string().required(),
        password: Joi.string().required(),
      }),
    });
  }
}

export default UserValidation
