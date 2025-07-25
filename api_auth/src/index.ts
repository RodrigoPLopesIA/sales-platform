import express from "express"


const app = express();
const env = process.env

const PORT = process.env.PORT || 8080

app.get("/api/v1/health", (req, res) => {
    return res.status(200).json({
        "status": 200,
        "message": "API auth health"
    })
})

app.listen(PORT, () => console.log("http://localhost:8080/api/v1/health"))

