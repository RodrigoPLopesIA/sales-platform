import { UserService } from "../../src/service/UserService";
import request from "supertest";
import app from "../../src/server";
import TestAgent from "supertest/lib/agent";
describe("User Controller Test", () => {
  let userService: jest.Mocked<UserService>;
  let fetch: TestAgent;
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
    fetch = request(app);
  });

  it("should create a new user", async () => {
    const result = await fetch
      .post("/api/v1/users/register")
      .set("accept", "application/json")
      .send({ email: "test@email.com", password: "123456123456" });

    // expect(result.status).toEqual(201);
    expect(result.body).toEqual("asdas");
  });
});
