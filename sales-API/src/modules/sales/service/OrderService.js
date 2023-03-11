import { sendMessageToProductStockUpdateQueue } from "../../product/rabbitmq/productStockUpdateSender.js";
import OrderRepository from "../repository/OrderRepository.js";
import { PENDING, REJECTED, ACCEPTED } from "../status/OrderStatus.js";


class OrderService{
    async createOrder(req){
        try {
            let orderData = req.body
            const {authUser} = req
            this.validateOrderData(orderData)
            let order = {
                status: PENDING,
                user: authUser,
                createdAt: new Date(),
                updatedAt: new Date(),
                products: orderData

            }
            let createOrder = await OrderRepository.save(order)
            return {
                status: httpStatus.SUCCESS,
                createOrder
            }
        } catch (error) {
            return {
                status: error.status
                    ? error.status
                    : httpStatus.INTERNAL_SERVER_ERROR,
                message: error.message
            }
        }
    }
    
    validateOrderData(data){
        if(!data || data.products){
            throw new OrderException(BAD_REQUEST, 'The products must be informed')
        }
    }
}

export default new OrderService
