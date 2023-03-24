import UserRepository from "../repository/UserRepository.js"
import  * as httpStatus from '../../../config/constants/httpStatus.js'
import UserException from "../exception/UserException.js";
import bcrypt from 'bcrypt'
import  jwt  from "jsonwebtoken";
import * as secrets from '../../../config/constants/secrets.js'

class UserService {
    async findByEmail(req) {
        try {
            const {email} = req.params;
            const {authUser} = req
          
            this.validateRequestData(email)
            let user = await UserRepository.findByEmail(email)

            this.validateUserNotFound(user)
            this.validateAuthenticatedUser(user,authUser)
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
        if(!email){

            throw new UserException(httpStatus.BAD_REQUEST, "User email was not informed!")
        }

    }

    validateUserNotFound(user){
        if(!user){
            throw new Error(httpStatus.BAD_REQUEST, "User was not foud.")
        }

    }

    validateAuthenticatedUser(user, authUser){
        if(!authUser || user.id !== authUser.id){
            throw new UserException(httpStatus.FORBIDDEN, "You cannot see  this user data")
        }
    }
    async getAccessToken(req) {
        try {
            const {transactionid, serviceid} = req.headers
            console.info(`Request to POST LOGIN order with data ${JSON.stringify(req.body)} | [TrasactionID: ${transactionid} | serviceID: ${serviceid}]\n\n\n`)
          const { email, password } = req.body;
          this.validateAccessTokenData(email, password);
          let user = await UserRepository.findByEmail(email);
          this.validateUserNotFound(user);
          await this.validatePassword(password, user.password);
          const authUser = { id: user.id, name: user.name, email: user.email };
          const accessToken = jwt.sign({ authUser }, secrets.API_SECRET, {
            expiresIn: "365d",
          });
          let response = {
            status: httpStatus.SUCCESS,
            accessToken,
          };
          console.info(`Request to POST LOGIN with data ${JSON.stringify(response)} | [TrasactionID: ${transactionid} | serviceID: ${serviceid}]`)

          return response
        } catch (err) {
          return {
            status: err.status ? err.status : httpStatus.INTERNAL_SERVER_ERROR,
            message: err.message,
          };
        }
      }


    validateAccessTokenData(email, password){
        if(!email || !password){
            throw new UserException(httpStatus.UNAUTHORIZED, "Email and password  must be informed");
        }
    }

    async validatePassword(password, hashPassword){
        if(!( await bcrypt.compare(password, hashPassword))){
            throw new UserException(
                httpStatus.UNAUTHORIZED,
                "Passowrd doesn't match."
            )
        }
    }
}

export default new UserService()