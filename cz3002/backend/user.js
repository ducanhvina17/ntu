function InitializeDB()
{
    var Database = require("./database.js");
    return new Database();
}

var User = function ()
{
    this.Login = function(params, callback)
    {
        db = InitializeDB();

        db.ConnectDB(function(connection)
        {
            var sql = "select * from user where Username='" + params.username + "' and Password='" + params.password + "'";

            connection.connect();

            connection.query(sql, function(err, result)
            {
                if (err)
                    callback(err);

                if (result.length !== 0)
                {
                    result = {username: result[0].Username,
                              password: result[0].Password,
                              email: result[0].Email};
                }

                callback(result);
            });

            connection.end();
        });
    };

    this.Register = function(params, callback)
    {
        db = InitializeDB();

        db.ConnectDB(function(connection)
        {
            var sql = "select Username from user where Username='" + params.username + "'";

            connection.connect();

            connection.query(sql, function(err, result)
            {
                if (err)
                    callback(err);

                if (typeof(result) != 'undefined' && result.length === 0)
                {
                    sql = "insert into user values ('" + params.username + "', '" + params.password + "', '" +
                          params.email + "')";

                    connection.query(sql, function(err, result)
                    {
                        callback(1);
                    });
                }

                callback(0);
            });

            connection.end();
        });
    };
};

module.exports = User;
