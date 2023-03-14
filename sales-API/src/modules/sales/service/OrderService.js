import { BAD_REQUEST, INTERNAL_SERVER_ERROR, SUCCESS } from "../../../config/constants/httpStatus.js";
import ProductClient from "../../product/client/ProductClient.js";
import { sendMessageToProductStockUpdateQueue } from "../../product/rabbitmq/productStockUpdateSender.js";
import OrderException from "../exception/OrderException.js";


import OrderRepository from "../repository/OrderRepository.js";
import { PENDING, REJECTED, ACCEPTED } from "../status/OrderStatus.js";


class OrderService{
    async createOrder(req){
        try {
            let orderData = req.body

            const {transactionid, serviceid} = req.headers
            console.info(`Request to POST new order data with data ${JSON.stringify(orderData)} | [TrasactionID: ${transactionid} | serviceID: ${serviceid}]\n\n\n`)

            
            this.validateOrderData(orderData)        
            const {authUser} = req
            const {authorization} = req.headers
            let order = this.createInitailData(orderData, authUser)
            await this.validateProductStock(order, authorization,transactionid)
            let createOrder = await OrderRepository.save(order)
           
            this.sendMessage(createOrder)
           
            let response = {
                status:SUCCESS,
                createOrder
            }
            console.info(`Request to POST LOGIN with data ${JSON.stringify(response)} | [TrasactionID: ${transactionid} | serviceID: ${serviceid}]`)
            return response
        } catch (error) {
            console.log(error)
            return {
                status: error.status ? error.status : INTERNAL_SERVER_ERROR,
                message: error.message,
              }
        }
    }
    async updateOrder(orderMessage){
        try {
            const order =JSON.parse(orderMessage)
            if(!order.salesId && !order.status){
                let existingOrder = await OrderRepository.findById(order.salesId)
                if(existingOrder && order.status !== existingOrder.status){
                    existingOrder.status = order.status
                    existingOrder.updatedAt = new  Date()
                    await OrderRepository.save(existingOrder)
                }
    
            }else {
                console.warn("The order message was not complete")
            }

        } catch (error) {
            console.error("Could not parse order message from queue")
            console.error(error.message)
        }
    }
    validateOrderData(data){
        if(!data || !data.products){
            throw new OrderException (BAD_REQUEST, 'The products must be informed')
        }
    }

   async validateProductStock(order, token, transactionid){

        let stockIsOut = await ProductClient.checkProductStock(order.products,token, transactionid)
        if(stockIsOut){
            throw new OrderException(BAD_REQUEST,
                'The stock is out for the products')
        }
    }


    createInitailData(orderData, authUser){
        return {
            status: PENDING,
            user: authUser,
            createdAt: new Date(),
            updatedAt: new Date(),
            products: orderData.products
        }
    }

    sendMessage(createOrder){
        const message = {
            salesId: createOrder.id,
            products: createOrder.products
        }
        sendMessageToProductStockUpdateQueue(message)

    }

    async findById(req){
        try {
            
            const {id} = req.params
            const {transactionid, serviceid} = req.headers
            console.info(`Request to GET ORDER by id ${id} | [TrasactionID: ${transactionid} | serviceID: ${serviceid}]`)
            this.validateInformedId(id)
            const existingOrder = await OrderRepository.findById(id)

            if(!existingOrder){
                throw new OrderException(BAD_REQUEST, "The order was not found")
            }
            let response =  {
                status: SUCCESS,
                existingOrder
            }
            console.info(`Response to GET ORDER by id ${id} : ${JSON.stringify(response)}
            | [TrasactionID: ${transactionid} | serviceID: ${serviceid}]`)

            return response
        } catch (error) {
            return {
                status: error.status ? error.status : INTERNAL_SERVER_ERROR,
                message: error.message
            }
        }

    }
    async findAll(){
        try {
            const {transactionid, serviceid} = req.headers
            console.info(`Request to GET ALL ORDERS | [TrasactionID: ${transactionid} | serviceID: ${serviceid}]`)
            const orders = await OrderRepository.findAll()
            if(!orders){
                throw new OrderException(BAD_REQUEST, "No orders were found")
            }
            let response = {
                status: SUCCESS,
                orders,
            }
            console.info(`Response to GET ALL ORDERS | [TrasactionID: ${transactionid} | serviceID: ${serviceid}]`)
            return response
        } catch (error) {
            return {
                status: error.status ? error.status : INTERNAL_SERVER_ERROR,
                message: error.message
            }
        }

    }

    async findbyProductId(req){
        try {
            
            const productId = req.params
            const {transactionid, serviceid} = req.headers

            console.info(`Request to GET ORDER by productID: ${productId}| [TrasactionID: ${transactionid} | serviceID: ${serviceid}]`)
            
            this.validateInformedProductId(productId)
            const orders = await OrderRepository.findByProductId(productId.id)
            console.log("ORDERS", orders)
            if(!orders){
                throw new OrderException(BAD_REQUEST, "No orders were found")
            }
            let response = {
                status: SUCCESS,
                salesId:orders.map(order => {
                    return order.id
                }),
            }
            console.info(`
                Response to GET order by productId ${productId} :${JSON.stringify(response)} | transaction ID ${transactionid}
                
                | [transaction id: ${transactionid} | serviceID: ${serviceid} ]
            `)

            return response
        } catch (error) {
            return {
                status: error.status ? error.status : INTERNAL_SERVER_ERROR,
                message: error.message
            }
        }

    }

    validateInformedId(id){
  
        if(!id){
            throw new OrderException(BAD_REQUEST, "THE ORDER ID MUST BE INFORMED!")
        }
    }
    validateInformedProductId(id){

        if(!id){
            throw new OrderException(BAD_REQUEST, "The order's productId must be informed")
        }
    }
}



export default new OrderService
