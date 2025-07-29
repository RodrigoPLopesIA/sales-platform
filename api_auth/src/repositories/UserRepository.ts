import { getRepository, Repository } from "typeorm";
import { IUserRepository } from "../interface/IUserRepository";
import { User as UserDTO } from "../model/User";
import { AppDataSource } from "../data-source";
import { User } from "../entity/User";


export class UserRepository implements IUserRepository{

    private repository: Repository<User>

    constructor() {
       this.repository = AppDataSource.getRepository(User)
    }
    async findByEmail(email: string): Promise<User> {
        return this.repository.findOne({where: {email}})
    }
    async existsByEmail(email: string): Promise<Boolean> {
        return await this.repository.existsBy({email})
    }
    async save(user: UserDTO): Promise<User> {
        return await this.repository.save(user)
    }

}