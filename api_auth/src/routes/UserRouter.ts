import { Router } from "express";
import { UserController } from "../controllers/UserController";
import { UserService } from "../service/UserService";
import { UserRepository } from "../repositories/UserRepository";
import { celebrate, Joi, Segments } from "celebrate";

const userService = new UserService(new UserRepository());
const userController = new UserController(userService);

const userRouter = Router();

userRouter.post(
  "/register",
  celebrate({
    [Segments.BODY]: Joi.object().keys({
      email: Joi.string().required(),
      password: Joi.string().required(),
      firstName: Joi.string(),
      lastName: Joi.string(),
      
    }),
  }),
  userController.create.bind(userController)
);

export default userRouter;
