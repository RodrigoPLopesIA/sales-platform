import { IUserRepository } from "../interface/IUserRepository";
import { User as UserDTO } from "../model/User";
import { User } from "../entity/User";
import HTTPException from "../exceptions/HTTPException";
import bcrypt from 'bcrypt'
import { Credentials } from "../model/Credentials";
export class UserService {


  private userRepository: IUserRepository;
  constructor(userRepository: IUserRepository) {
    this.userRepository = userRepository;
  }
  authenticate(auth: Credentials) {
    throw new Error("Method not implemented.");
  }
    
  public async save(data: UserDTO): Promise<User> {
      
    data.password = bcrypt.hashSync(data.password, 10)
      
      if(await this.userRepository.existsByEmail(data.email))
        throw new HTTPException(400, `User with ${data.email} already exists`)
      return await this.userRepository.save(data);
      

  }
}
