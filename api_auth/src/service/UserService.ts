import { IUserRepository } from "../interface/IUserRepository";
import { User as UserDTO } from "../model/User";
import { User } from "../entity/User";
import HTTPErrorMessage from "../exceptions/HTTPErrorMessage";

export class UserService {
    
  private userRepository: IUserRepository;
  constructor(userRepository: IUserRepository) {
    this.userRepository = userRepository;
  }
  public async save(data: UserDTO): Promise<User> {
      if(await this.userRepository.existsByEmail(data.email))
        throw new HTTPErrorMessage(400, `User with ${data.email} already exists`)
      return await this.userRepository.save(data);
      

  }
}
