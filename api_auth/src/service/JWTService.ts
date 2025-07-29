import jwt from 'jsonwebtoken';

export class JWTService {
  private static readonly SECRET_KEY = process.env.JWT_SECRET || 'default_secret';
  private static readonly EXPIRES_IN = '1h'; 

  public static sign(payload: object): string {
    return jwt.sign(payload, JWTService.SECRET_KEY, {
      expiresIn: JWTService.EXPIRES_IN,
    });
  }

  public static verify<T = any>(token: string): T | null {
    try {
      return jwt.verify(token, JWTService.SECRET_KEY) as T;
    } catch (err) {
      return null;
    }
  }
}
