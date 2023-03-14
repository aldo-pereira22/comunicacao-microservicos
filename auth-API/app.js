import express from "express";
import * as db from "./src/config/db/initialData.js"
import checkToken from "./src/config/auth/checkToken.js";
import userRoutes from "./src/modules/user/routes/UserRoutes.js";
import tracing from "./src/config/tracing.js";



const app = express();
const env = process.env;
const PORT = env.port || 8085;


db.createInitialdata();
app.use(tracing)
app.get("/api/status",(req, res) => {
    return res.status(200).json({
        service: "Auth-API",
        status: "UP",
        httpStatus: 200
    })
})
app.use(express.json())
app.use(userRoutes)

app.listen(PORT, ()=> {
    console.info("Server started sucessfull at port: ", PORT);
})