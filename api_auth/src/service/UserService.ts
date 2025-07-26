import { IUserRepository } from "../interface/IUserRepository";
import { User as UserDTO } from "../model/User";
import { User } from "../entity/User";
import HTTPErrorMessage from "../exceptions/HTTPErrorMessage";
import bcrypt from 'bcrypt'
export class UserService {
    
  private userRepository: IUserRepository;
  constructor(userRepository: IUserRepository) {
    this.userRepository = userRepository;
  }
  public async save(data: UserDTO): Promise<User> {
      
    data.password = bcrypt.hashSync(data.password, 10)
      
      if(await this.userRepository.existsByEmail(data.email))
        throw new HTTPErrorMessage(400, `User with ${data.email} already exists`)
      return await this.userRepository.save(data);
      

  }
}
