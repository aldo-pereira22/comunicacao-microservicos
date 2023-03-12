import  expres  from "express";
import {connectMonogDb} from './src/config/db/mongodbConfig.js'
import { createInitialData } from "./src/config/db/initialData.js"; 
import checkToken from "./src/config/auth/checkToken.js";
import { connectRabbitMq } from "./src/config/rabbitmq/rabbitConfig.js";
import { sendMessageToProductStockUpdateQueue } from './src/modules/product/rabbitmq/productStockUpdateSender.js'
import orderRouter from "./src/modules/sales/routes/OrderRoutes.js";



const app = expres()
const env = process.env;

const PORT = env.PORT || 8083


connectMonogDb()
createInitialData()
connectRabbitMq()
app.use(checkToken)
app.use(orderRouter)
app.get('/teste', (req, res) => {
    try {
        sendMessageToProductStockUpdateQueue([
            {
                productId: 1001,
                quantity: 3
    
            },
            {
                productId: 1002,
                quantity: 2
    
            },
            {
                productId: 1003,
                quantity: 1
    
            }
        ])
        return res.status(200).json({status: 200})
        
    } catch (error) {
        console.log(error)
        return res.status(500).json({error: true})
    }
})
app.get('/api/status', async (req, res) => {

    return res.status(200).json({
        service: "Sales - API",
        status: "UP",
        httpStatus: 200
    })
})

app.listen(PORT, () => {
    console.info(`Server Started in PORT: ${PORT}`)
})