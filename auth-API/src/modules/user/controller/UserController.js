import UserService from "../service/UserService.js"

class UserController {
    async getAccessToken(req, res){
        let accessToken = await UserService.getAccessToken(req)
        console.log(accessToken)
        return res.status(accessToken.stauts).json(accessToken.accessToken)

    }
    
    async findByEmail(req, res){
        let user = await UserService.findByEmail(req)
        return res.status(user.status).json(user)
    }
}

export default new UserController()