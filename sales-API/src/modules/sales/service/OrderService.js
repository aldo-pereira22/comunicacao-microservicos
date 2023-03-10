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
            this.validateOrderData(orderData)        
            const {authUser} = req
            const {authorization} = req.headers
            let order = this.createInitailData(orderData, authUser)
           
            await this.validateProductStock(order, authorization)
            let createOrder = await OrderRepository.save(order)
           
            this.sendMessage(createOrder)
            return {
                status:SUCCESS,
                createOrder
            }
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

   async validateProductStock(order, token){
        let stockIsOut = await ProductClient.checkProductStock(order.products,token)
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
            this.validateInformedId(id)
            const existingOrder = await OrderRepository.findById(id)
            console.log("OBJETO", existingOrder)
            if(!existingOrder){
                throw new OrderException(BAD_REQUEST, "The order was not found")
            }
            return {
                status: SUCCESS,
                existingOrder
            }
        } catch (error) {
            return {
                status: error.status ? error.status : INTERNAL_SERVER_ERROR,
                message: error.message
            }
        }

    }

    validateInformedId(id){
        console.log("IDDD", id)
        if(!id){
            throw new OrderException(BAD_REQUEST, "THE ORDER ID MUST BE INFORMED!")
        }
    }
}



export default new OrderService
