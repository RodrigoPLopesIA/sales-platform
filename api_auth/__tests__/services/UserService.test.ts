import { UserService } from "../../src/service/UserService"


describe("User Service Test", () => {
    let userService: UserService

    beforeAll(() => {
        userService = new UserService()
    })

    it("Should create a new user", () => {


        const result = userService.save({email: "test@email.com", password: "123456123456"})
    
        expect(result).toBeDefined()
    })
})