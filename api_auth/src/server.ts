import express from "express";
import "reflect-metadata";
import { AppDataSource } from "./data-source";

const app = express();
const env = process.env;

const PORT = process.env.PORT || 8081;

app.get("/api/v1/health", (req, res) => {
  return res.status(200).json({
    status: 200,
    message: "API auth health",
  });
});
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
