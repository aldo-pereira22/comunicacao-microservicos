import  jwt  from "jsonwebtoken";
import pomisify from "util" 
import AuthException from "./AuthException.js";
import * as secrets from '../constants/secrets.js'
import * as httpStatus from '../constants/httpStatus.js'


const bearer = 'bearer'
export default async  (req, res, next) => {
    try {
        const {authorization} = req.headers

        if(!authorization){
            throw new AuthException (
                httpStatus.UNAUTHORIZED,
                "Access token was no informed!"
            )
        }
        let accessToken = authorization
        if(accessToken.toLowrCase().includes(bearer)){
            accessToken = accessToken.replace(bearer,"")
        }
        const decoded = await pomisify(Jwt.verify)(accessToken, secrets.API_SECRET)

        req.authUser =decoded.authUser
        return next()
    } catch (error) {
        return  res.status(error.status).json({
            status: httpStatus.INTERNAL_SERVER_ERROR,
            message: error.message
        })
    }
}