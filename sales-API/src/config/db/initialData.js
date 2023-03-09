import Order from "../../modules/sales/model/Order.js";


export async function createInitialData(){
    await Order.collection.drop()
  await Order.create({
        products: [
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
        ],
        user: {
            id:"12312312",
            name:"Aldo test",
            email: "aldo@gmail"
        },
        status:"APROVED",
        createdAt: new Date(),
        updatedAt: new Date()

    })
    await Order.create({
        products: [
            {
                productId: 1001,
                quantity: 4

            },
            {
                productId: 1002,
                quantity: 2
            },
            {
                productId: 1003,
                quantity: 1
            }
        ],
        user: {
            id:"asdasds",
            name:"Teste test",
            email: "teste@gmail"
        },
        status:"REJECTED",
        createdAt: new Date(),
        updatedAt: new Date()

    })
    let initialData = await Order.find()
    console.info(`Initial data was created: ${JSON.stringify(initialData, undefined, 3)}`)
    // console.info(`Initial data was created: ${initialData}`)

}

