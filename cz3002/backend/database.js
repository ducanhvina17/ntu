var Database = function ()
{
    this.ConnectDB = function(callback)
    {
        var mysql = require('mysql');

        var connection = mysql.createConnection({
            host: 'localhost',
            user: 'admin',
            password: 'mysql',
            database: 'cz3002'
        });

        callback(connection);
    };
};

module.exports = Database;
