import bcrypt from "bcrypt"
import User from "../../modules/user/model/User.js"


let password = await bcrypt.hash('123456', 10)

export  async function createInitialdata(){
    try {
        await User.sync({force: true})
    
        await User.create({
            name: "User teste",
            email: "Teste",
            password: password
        })
        
    } catch (error) {
            console.log(error)
    }

}