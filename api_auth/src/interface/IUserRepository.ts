import { User } from "../entity/User";
import {User as UserDTO} from "../model/User"


export interface IUserRepository {
    save(user: UserDTO) : Promise<User>
}