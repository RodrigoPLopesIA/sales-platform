import { User } from "../../src/entity/User";
import { IUserRepository } from "../../src/interface/IUserRepository";
import { JWTService } from "../../src/service/JWTService";
import bcrypt from 'bcrypt'
import { UserService } from "../../src/service/UserService";
jest.mock("../../src/data-source");


describe("User Service Test", () => {
  let userService: UserService;
  let userRepository: jest.Mocked<IUserRepository>;
  let mockUser: User;
  let token: string

  beforeAll(() => {
    mockUser = {
      id: "3c75a4f8-5e63-4f1a-bb7b-dc8b2bdf0e89",
      firstName: "Rodrigo",
      lastName: "Lopes",
      email: "test@email.com",
      password: "123456123456",
      createdAt: new Date("2024-01-01T10:00:00.000Z"),
      updatedAt: new Date("2024-01-01T10:00:00.000Z"),
    };
    token =  "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjNjNzVhNGY4LTVlNjMtNGYxYS1iYjdiLWRjOGIyYmRmMGU4OSIsImVtYWlsIjoidGVzdEBlbWFpbC5jb20iLCJmaXJzdE5hbWUiOiJSb2RyaWdvIiwibGFzdE5hbWUiOiJMb3BlcyIsImlhdCI6MTc1Mzc0NzQ3NywiZXhwIjoxNzUzNzUxMDc3fQ.SDwp6BVmxQ_3KncIWSizWKGofb4RD4ZsK8Pjff6syPE"
    userRepository = {
      save: jest.fn().mockResolvedValue(mockUser),
      existsByEmail: jest.fn().mockResolvedValue(false),
      findByEmail: jest.fn().mockResolvedValue(mockUser),
    } as jest.Mocked<IUserRepository>;

    userService = new UserService(userRepository);
  });

  it("Should create a new user", async () => {
    bcrypt.hashSync = jest.fn().mockReturnValue("123456123456")

    const result: User = await userService.save({
      email: "test@email.com",
      password: "123456123456",
    });

    expect(result.email).toEqual(mockUser.email);
    expect(userRepository.save).toHaveBeenCalledWith({
      email: "test@email.com",
      password: "123456123456",
    });
  });

  it("Should return error when try to create a new user with email exists", async () => {
    userRepository.existsByEmail.mockResolvedValueOnce(true);
    await expect(
      userService.save({
        email: "test@email.com",
        password: "123456123456",
      })
    ).rejects.toThrow("User with test@email.com already exists");
  });

  it("Should authenticate user", async () => {

    JWTService.sign = jest.fn().mockReturnValue(token)
    bcrypt.compareSync = jest.fn().mockReturnValue(true)

    const result = await userService.authenticate({
      email: "test@email.com",
      password: "123456123456",
    });

    expect(result.token).toEqual(token);
    
  });

  it("Should throw credentials invalid user email", async () => {

    userRepository.findByEmail.mockResolvedValue(null)
    await expect(userService.authenticate({
      email: "test@email.com",
      password: "123456123456",
    })).rejects.toThrow("Credentials invalid!");

  });

  it("Should throw credentials invalid user password invalid", async () => {

    userRepository.findByEmail.mockResolvedValue(mockUser)
    bcrypt.compareSync = jest.fn().mockReturnValue(false)

    await expect(userService.authenticate({
      email: "test@email.com",
      password: "123456123456",
    })).rejects.toThrow("Credentials invalid!");

  });
});
