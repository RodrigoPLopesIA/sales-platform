import { IUserRepository } from "../interface/IUserRepository";
import { User } from "../model/User";



export class UserService{
    private userRepository: IUserRepository
    constructor(userRepository: IUserRepository) {
        this.userRepository = userRepository;
    }
    public save(data: User) : User {
        return this.userRepository.save(data)
    }
}