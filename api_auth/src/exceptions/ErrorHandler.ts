import { NextFunction, Request, Response } from "express";

export class ErrorHandler {

    public static execute(err: Error, req: Request, res: Response, next: NextFunction) {

        if (err) {
            return res.status(400).json({
                name: err.name,
                message: err.message
            })
        }

        next();
    }
}

