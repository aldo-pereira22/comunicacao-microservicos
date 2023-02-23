import Sequelize from "sequelize"

const sequelize = new Sequelize("auth-db","admin","123456",{
    host: "localhost",
    dialect: "postgres",
    port: "5436",
    quoteIdentifiers: false,
    define: {
        syncOnAssociation: true,
        timestamps: false,
        underscored: true,
        underscoredAll: true,
        freezeTableName: true
    }
} )

sequelize
    .authenticate()
    .then(()=> {
        console.log("Conection has been stablished!")
    })
    .catch( (err) => {
        console.error("Unable to conect  to the database.")
        console.error("ERRO :: ----->",err.message)
    })



export default sequelize;