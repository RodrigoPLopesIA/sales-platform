import { User } from "../../src/entity/User";
import { IUserRepository } from "../../src/interface/IUserRepository";
import { User as UserDTO } from "../../src/model/User";

import { UserService } from "../../src/service/UserService";
jest.mock("../../src/data-source")

describe("User Service Test", () => {
  let userService: UserService;
  let userRepository: jest.Mocked<IUserRepository>;
  let mockUser: User;

  beforeAll(() => {
    mockUser = {
      id: "3c75a4f8-5e63-4f1a-bb7b-dc8b2bdf0e89",
      firstName: "Rodrigo",
      lastName: "Lopes",
      email: "test@email.com",
      password: "hashed_password_123",
      createdAt: new Date("2024-01-01T10:00:00.000Z"),
      updatedAt: new Date("2024-01-01T10:00:00.000Z"),
    };

    userRepository = {
      save: jest.fn().mockResolvedValue(mockUser),
    } as jest.Mocked<IUserRepository>;

    userService = new UserService(userRepository);
  });

  it("Should create a new user", async () => {
    const result: User = await userService.save({
      email: "test@email.com",
      password: "123456123456",
    });

    expect(result.email).toEqual(mockUser.email);
    expect(userRepository.save).toHaveBeenCalledWith({
      email: "test@email.com",
      password: "123456123456",
    })

    
  });
});
