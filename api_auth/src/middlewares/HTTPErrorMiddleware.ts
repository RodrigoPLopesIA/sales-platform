import { isCelebrateError } from "celebrate";
import HTTPException from "../exceptions/HTTPException";

class HTTPErrorMiddleware {
  public execute(err, req, res, next) {
    if (isCelebrateError(err)) {
      const body = err.details.get("body");
      if (body) {
        const validation = {
          source: "body",
          keys: body.details.map((detail) => detail.context?.key || ""),
          message: body.details.map((detail) => detail.message).join(", "),
        };

        return res.status(400).json({
          statusCode: 400,
          error: "Bad Request",
          validation,
        });
      }
    }
    if (err instanceof HTTPException) {
      return res.status(err.status).json({
        status: err.status,
        message: err.message,
      });
    }
    return res.status(500).json({
      status: 500,
      message: err.message || "Internal Server Error",
    });
  }
}
export default new HTTPErrorMiddleware();
