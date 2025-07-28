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

  public async authenticate({email, password}: Credentials) : Promise<User>{
    const user = await this.userRepository.findByEmail(email);

    if(!user) throw new HTTPException(401, "Credentials invalid!")

    if(!bcrypt.compareSync(password, user.password)) throw new HTTPException(401, "Credentials invalid!")
      
    return user;
  }
    
  public async save(data: UserDTO): Promise<User> {
      
    data.password = bcrypt.hashSync(data.password, 10)
      
      if(await this.userRepository.existsByEmail(data.email))
        throw new HTTPException(400, `User with ${data.email} already exists`)
      return await this.userRepository.save(data);
      

  }
}
