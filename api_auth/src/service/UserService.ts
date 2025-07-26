import { IUserRepository } from "../interface/IUserRepository";
import { User as UserDTO } from "../model/User";
import { User } from "../entity/User";

export class UserService {
    
  private userRepository: IUserRepository;
  constructor(userRepository: IUserRepository) {
    this.userRepository = userRepository;
  }
  public async save(data: UserDTO): Promise<User> {
    return await this.userRepository.save(data);
  }
}
