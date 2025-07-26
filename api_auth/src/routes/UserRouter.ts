import { Router } from "express";
import { UserController } from "../controllers/UserController";
import { UserService } from "../service/UserService";
import { UserRepository } from "../repositories/UserRepository";


const userService = new UserService(new UserRepository())

const userController = new UserController(userService)

const userRouter = Router() 

userRouter.post("/register", userController.create.bind(userController))

export default userRouter