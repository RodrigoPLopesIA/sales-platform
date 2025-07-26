import { celebrate, Joi, Segments } from "celebrate";

class UserValidation {
  public static validate() {
    return celebrate({
      [Segments.BODY]: Joi.object().keys({
        email: Joi.string().required(),
        password: Joi.string().required(),
        firstName: Joi.string(),
        lastName: Joi.string(),
      }),
    });
  }
}

export default UserValidation
