import "reflect-metadata";
import express from "express";
import { AppDataSource } from "./data-source";
import userRouter from "./routes/UserRouter";
import { ErrorHandler } from "./exceptions/ErrorHandler";

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

AppDataSource.initialize()
  .then(() => {
    console.log("✅ Database is running!");
    app.listen(PORT, () =>
      console.log(`Server running at http://localhost:${PORT}/api/v1/health`)
    );
  })
  .catch((error) => {
    console.error("Error during database initialization:", error);
  });

export default app;
