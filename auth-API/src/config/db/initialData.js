import bcrypt from "bcrypt"
import User from "../../modules/user/model/User.js"


let password = await bcrypt.hash('123456', 10)

export  async function createInitialdata(){
    console.log("Criando Dados\n")
    try {
        await User.sync({force: true})
    
        await User.create({
            name: "User teste",
            email: "user@gmail.com",
            password: password
        })

        await User.create({
            name: "Aldo Pereira",
            email: "aldo@gmail.com",
            password: password
        })
        await User.create({
            name: "Aldo Pereira",
            email: "aldo@gmail.com",
            password: password
        })
        await User.create({
            name: "Aldo Pereira",
            email: "aldo@gmail.com",
            password: password
        })
        
    } catch (error) {
            console.log(error)
    }
    console.log('\n Dados criados com sucesso!')
}