import  expres  from "express";
import {connect} from './src/config/db/mongodbConfig.js'
import { createInitialData } from "./src/config/db/initialData.js"; 

const app = expres()
const env = process.env;

const PORT = env.PORT || 8083

connect()
createInitialData()
app.get('/api/status', async (req, res) => {

    console.log("DBB: ", teste)
    return res.status(200).json({
        service: "Sales - API",
        status: "UP",
        httpStatus: 200
    })
})

app.listen(PORT, () => {
    console.info(`Server Started in PORT: ${PORT}`)
})