import { IUserRepository } from "../../src/interface/IUserRepository";
import { User } from "../../src/model/User";
import { UserService } from "../../src/service/UserService";

describe("User Service Test", () => {
  let userService: UserService;
  let userRepository: jest.Mocked<IUserRepository>;
  beforeAll(() => {
    userRepository = {
      save: jest.fn(),
    } as jest.Mocked<IUserRepository>;

    userService = new UserService(userRepository);
  });

  it("Should create a new user", () => {
    const result = userService.save({
      email: "test@email.com",
      password: "123456123456",
    });

    expect(result).toBeInstanceOf(User)
  });
});
