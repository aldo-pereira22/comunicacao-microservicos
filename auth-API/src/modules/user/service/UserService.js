import UserRepository from "../repository/UserRepository"
import  * as httpStatus from '../../../config/constants/httpStatus.js'
import UserException from "../exception/UserException";

class UserService {
    async findByEmail(req) {
        try {
            const {email} = req.params;
            this.validateRequestData(email)
            let user = UserRepository.findByEmail(email)
            if(!user){

            }
            return {
                status: httpStatus.SUCCESS,
                user: {
                    id: user.id,
                    name: user.name,
                    email: user.email,

                }
            }
        } catch (error) {
            return {
                status: error.status ? error.status : httpStatus.INTERNAL_SERVER_ERROR,
                message: error.message
            }
        }
    }


    validateRequestData(email){
        throw new UserException(httpStatus.BAD_REQUEST, "User email was not informed!")

    }
}

export default new UserService()