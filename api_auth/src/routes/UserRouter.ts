import { Router } from "express";
import { UserController } from "../controllers/UserController";
import { UserService } from "../service/UserService";
import { UserRepository } from "../repositories/UserRepository";
import UserValidation from "../validations/UserValidations";

const userService = new UserService(new UserRepository());
const userController = new UserController(userService);

const userRouter = Router();
/**
 * @openapi
 * components:
 *   schemas:
 *     RegisterUserInput:
 *       type: object
 *       required:
 *         - name
 *         - email
 *         - password
 *         - confirmPassword
 *       properties:
 *         name:
 *           type: string
 *           example: Rodrigo Lopes
 *         email:
 *           type: string
 *           format: email
 *           example: rodrigo@example.com
 *         password:
 *           type: string
 *           format: password
 *           example: 123456
 *         confirmPassword:
 *           type: string
 *           format: password
 *           example: 123456
 *
 *     LoginUserInput:
 *       type: object
 *       required:
 *         - email
 *         - password
 *       properties:
 *         email:
 *           type: string
 *           format: email
 *           example: rodrigo@example.com
 *         password:
 *           type: string
 *           format: password
 *           example: 123456
 *
 *     UserResponse:
 *       type: object
 *       properties:
 *         id:
 *           type: string
 *           example: "user-123"
 *         name:
 *           type: string
 *           example: Rodrigo Lopes
 *         email:
 *           type: string
 *           example: rodrigo@example.com
 *         createdAt:
 *           type: string
 *           format: date-time
 *         updatedAt:
 *           type: string
 *           format: date-time
 */

/**
 * @openapi
 * /api/v1/register:
 *   post:
 *     summary: Create a new user
 *     tags:
 *       - Users
 *     requestBody:
 *       required: true
 *       content:
 *         application/json:
 *           schema:
 *             $ref: '#/components/schemas/RegisterUserInput'
 *     responses:
 *       201:
 *         description: User created successfully
 *         content:
 *           application/json:
 *             schema:
 *               $ref: '#/components/schemas/UserResponse'
 *       400:
 *         description: Validation error
 */
userRouter.post(
  "/register",
  UserValidation.register(),
  userController.create.bind(userController)
);

/**
 * @openapi
 * /api/v1/login:
 *   post:
 *     summary: Authenticate user
 *     tags:
 *       - Users
 *     requestBody:
 *       required: true
 *       content:
 *         application/json:
 *           schema:
 *             $ref: '#/components/schemas/LoginUserInput'
 *     responses:
 *       200:
 *         description: User authenticated
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 token:
 *                   type: string
 *                   example: "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
 *       401:
 *         description: Invalid credentials
 */
userRouter.post(
  "/login",
  UserValidation.register(),
  userController.login.bind(userController)
);


export default userRouter;
