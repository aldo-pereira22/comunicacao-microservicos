import User from "../model/User";

class UserRepository{

    async findById(id){
        try {
            return await User.findOne({ where: id})
        } catch (error) {
            console.error("Erro ao pesquisar email: ", error.message);
            return null
        }

    }


    async findByEmail(email) {
        try {
            return await User.findOne({where: email})
        } catch (error) {
            console.error("Erro ao pesquisar email: ", error.message);
            return null
        }
    }
}

export default new UserRepository()