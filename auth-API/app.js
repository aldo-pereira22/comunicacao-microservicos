import express from "express";
import * as db from "./src/config/db/initialData.js"

const app = express();
const env = process.env;
const PORT = env.port || 8085;


db.createInitialdata();

app.get("/api/status",(req, res) => {
    return res.status(200).json({
        service: "Auth-API",
        status: "UP",
        httpStatus: 200
    })
})
app.listen(PORT, ()=> {
    console.info("Server started sucessfull at port: ", PORT);
})