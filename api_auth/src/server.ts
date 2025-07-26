import "reflect-metadata";
import express from "express";
import { AppDataSource } from "./data-source";
import userRouter from "./routes/UserRouter";
import { ErrorHandler } from "./exceptions/ErrorHandler";
import logger from "./logs/Logger";

const app = express();
const PORT = process.env.PORT || 8081;

app.use(express.json());
app.use(express.urlencoded({ extended: true }));

app.use("/api/v1/users", userRouter);

app.get("/api/v1/health", (req, res) => {
  return res.status(200).json({
    status: 200,
    message: "API auth health",
  });
});

app.use(ErrorHandler.execute);

app.listen(PORT, () =>
  logger.info(`Server running at http://localhost:${PORT}/api/v1/health`)
);
export default app;
