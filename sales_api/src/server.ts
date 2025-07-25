import express from "express"
import * as dotenv from "dotenv"

dotenv.config()

const app = express();

const PORT = process.env.PORT || 8082

app.get("/api/v1/health", (req, res) => {
    return res.status(200).json({
        "status": 200,
        "message": "API auth health"
    })
})

app.listen(PORT, () => console.log("http://localhost:8082/api/v1/health"))

