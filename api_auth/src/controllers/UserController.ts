import { UserService } from "../service/UserService";
import { Request, Response } from "express";

export class UserController {
  private userService: UserService;

  constructor(userService: UserService) {
    this.userService = userService;
  }

  public async create(request: Request, response: Response): Promise<any> {
    const { email, password } = request.body;
    const user = await this.userService.save({ email, password })
    return response.status(201).json(user);
  }
}
