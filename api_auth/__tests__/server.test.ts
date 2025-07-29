import request from "supertest"
import app from '../src/server'

describe("Service tests", () => {


    it("should sum", async () => {
        // act & arrange
        const response = await request(app).get("/api/v1/health").send();

        // assert
        expect(response.status).toEqual(200)
        expect(response.body.message).toEqual("API auth health")
    })
})