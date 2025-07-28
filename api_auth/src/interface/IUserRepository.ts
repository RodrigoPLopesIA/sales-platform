import { User } from "../entity/User";
import {User as UserDTO} from "../model/User"


export interface IUserRepository {
    findByEmail(email: string): Promise<User>;
    save(user: UserDTO) : Promise<User>
    existsByEmail(email: string) : Promise<Boolean>
}