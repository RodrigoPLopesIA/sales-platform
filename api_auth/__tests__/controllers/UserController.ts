import express from "express"
import { UserService } from "../../src/service/UserService";
import { UserController } from "../../src/controllers/UserController";
import request from "supertest"
jest.mock("../../src/data-source")

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
      "/api/v1/users/register",
      userController.create.bind(userController)
    );
  });

  it("should create a new user", async () => {
    const result = await request(app)
      .post("/api/v1/users/register")
      .send({ email: "test@email.com", password: "123456123456" });

    expect(result.status).toEqual(201);
    expect(result.body.email).toEqual("test@email.com");
  });
});
