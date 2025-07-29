import { Router } from "express";
import { UserController } from "../controllers/UserController";
import { UserService } from "../service/UserService";
import { UserRepository } from "../repositories/UserRepository";
import UserValidation from "../validations/UserValidations";

const userService = new UserService(new UserRepository());
const userController = new UserController(userService);

const userRouter = Router();

userRouter.post(
  "/register",
  UserValidation.register(),
  userController.create.bind(userController)
);
userRouter.post(
  "/login",
  UserValidation.register(),
  userController.login.bind(userController)
);

export default userRouter;
