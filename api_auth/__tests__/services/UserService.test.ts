import { UserService } from "../../src/service/UserService"


describe("User Service Test", () => {
    let userService: UserService
    let userRepository: jest.Mock<IUserRepository>
    beforeAll(() => {
        userService = new UserService()

        userRepository = jest.mock({
            save: jest.fn()
        })
    })

    it("Should create a new user", () => {


        const result = userService.save({email: "test@email.com", password: "123456123456"})
    
        expect(result).toBeDefined()
    })
})