import { User } from "../model/User";
import { UserService } from "../service/UserService";
import { Request, Response } from "express";

export class UserController {
  private userService: UserService;

  constructor(userService: UserService) {
    this.userService = userService;
  }

  public async create(request: Request, response: Response): Promise<any> {
    const { email, password, firstName, lastName } = request.body as User;
    const user = await this.userService.save({ email, password, firstName, lastName })
    return response.status(201).json(user);
  }

  public async login(request: Request, response: Response): Promise<any> {
    const {email, password} = request.body as User
    const token = await this.userService.authenticate({email, password});
    return response.status(200).json(token)
  }
}
