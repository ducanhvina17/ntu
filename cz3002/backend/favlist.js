function InitializeDB()
{
    var Database = require("./database.js");
    return new Database();
}

var FavList = function ()
{
    this.GetList = function(username, callback)
    {
        db = InitializeDB();

        db.ConnectDB(function(connection)
        {
            var sql = "select * from fav_list where username='" + username + "'";

            connection.connect();

            connection.query(sql, function(err, result)
            {
                if (err)
                    callback(err);

                var nameArr = [];

                for (i = 0; i < result.length; i++)
                    nameArr.push(result[i].schoolname);

                result = {name: nameArr};
                callback(result);
            });

            connection.end();
        });
    };

    this.AddSchool = function(params, callback)
    {
        db = InitializeDB();

        db.ConnectDB(function(connection)
        {
            var sql = "insert into fav_list values('"+ params.username + "', '" + params.school + "')";

            connection.connect();

            connection.query(sql, function(err, result)
            {
                if (err)
                    callback(err);

                callback(result);
            });

            connection.end();
        });
    };

    this.RemoveSchool = function(params, callback)
    {
        db = InitializeDB();

        db.ConnectDB(function(connection)
        {
            var sql = "delete from fav_list where username='"+ params.username + "' and schoolname='" + params.school + "'";

            connection.connect();

            connection.query(sql, function(err, result)
            {
                if (err)
                    callback(err);
                    
                callback(result);
            });

            connection.end();
        });
    };
};

module.exports = FavList;
