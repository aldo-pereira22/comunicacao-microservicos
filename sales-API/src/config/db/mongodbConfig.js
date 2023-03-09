import mongoose from "mongoose";
import {MONGO_DB_URL} from '../secrets/secrets.js'


export function connect(){
    mongoose.connect(MONGO_DB_URL, {
        useNewUrlParser: true

    })
    mongoose.connection.on('connected', function(){
        console.info('The application connected to MONGO DB successfully')
    })
    mongoose.connection.on('error', function(){
        console.error('The application failed when connecting to the server! ')

    })
}