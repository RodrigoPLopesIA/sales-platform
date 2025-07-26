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
  UserValidation.validate(),
  userController.create.bind(userController)
);

export default userRouter;
