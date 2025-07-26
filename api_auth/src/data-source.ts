import { DataSource } from "typeorm";
import { User } from "./entity/User";
import { CreateUserTable1753544195560 } from "./database/migrations/1753544195560-CreateUserTable";

export const AppDataSource = new DataSource({
  type: "postgres",
  host: process.env.DB_HOST || "localhost",
  port: Number(process.env.DB_PORT || 5432),
  username: process.env.DB_USERNAME || "admin",
  password: process.env.DB_PASSWORD || "123456",
  database: process.env.DB_DATABASE || "auth_db",
  synchronize: false,
  logging: false,
  entities: [User],
  migrations: [CreateUserTable1753544195560],
  subscribers: [],
});
