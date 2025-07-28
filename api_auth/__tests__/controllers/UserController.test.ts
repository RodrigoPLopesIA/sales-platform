import express from "express";
import { UserService } from "../../src/service/UserService";
import { UserController } from "../../src/controllers/UserController";
import request from "supertest";
import UserValidation from "../../src/validations/UserValidations";
import HTTPErrorMiddleware from "../../src/middlewares/HTTPErrorMiddleware";
import HTTPException from "../../src/exceptions/HTTPException";


describe("User Controller Test", () => {
  let userService: jest.Mocked<UserService>;
  let app: express.Express;

  beforeAll(() => {
    userService = {
      save: jest.fn().mockResolvedValue({
        email: "test@email.com",
        password: "123456123456",
        firstName: null,
        lastName: null,
        id: "98b7938f-3c10-4ffa-9fbf-93a17ee23312",
        createdAt: "2025-07-26T20:09:51.201Z",
        updatedAt: "2025-07-26T20:09:51.201Z",
      }),
    } as unknown as jest.Mocked<UserService>;

    const userController = new UserController(userService);

    app = express();
    app.use(express.json());

    app.post(
      "/api/v1/register",
      UserValidation.validate(),
      userController.create.bind(userController)
    );

    app.use(HTTPErrorMiddleware.execute);
  });

  it("should create a new user", async () => {
    const result = await request(app)
      .post("/api/v1/register")
      .send({ email: "test@email.com", password: "123456123456" });

    expect(result.status).toEqual(201);
    expect(result.body.email).toEqual("test@email.com");
  });

  it("should return error 400 when try to create a user with null values", async () => {
    const result = await request(app)
      .post("/api/v1/register")
      .send({ email: "", password: "" });

    expect(result.status).toEqual(400);
    expect(JSON.parse(result.text).validation.message).toEqual(
      '"email" is not allowed to be empty'
    );
  });
  it("should return error 400 when try to create a user with null values", async () => {
    const result = await request(app)
      .post("/api/v1/register")
      .send({ email: "test@email.com", password: "" });

    expect(result.status).toEqual(400);
    expect(JSON.parse(result.text).validation.message).toEqual(
      '"password" is not allowed to be empty'
    );
  });
  it("should return a error 400 when try to create a user exists ", async () => {
    userService.save.mockRejectedValue(
      new HTTPException(400, `User with test@email.com already exists`)
    );
    const result = await request(app)
      .post("/api/v1/register")
      .send({ email: "test@email.com", password: "123456123456" });

    expect(result.status).toEqual(400);
    expect(JSON.parse(result.text).status).toEqual(400);
    expect(JSON.parse(result.text).message).toEqual(
      "User with test@email.com already exists"
    );
  });
});
