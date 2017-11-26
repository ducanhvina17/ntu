function InitializeDB()
{
    var Database = require("./database.js");
    return new Database();
}

var Forum = function ()
{
    this.GetThread = function(callback)
    {
        db = InitializeDB();

        db.ConnectDB(function(connection)
        {
            var sql = "select * from forum";

            connection.connect();

            connection.query(sql, function(err, result)
            {
                callback(result);
            });

            connection.end();
        });
    };

    this.GetComment = function(callback)
    {
        db = InitializeDB();

        db.ConnectDB(function(connection)
        {
            var sql = "select * from forum_comment";

            connection.connect();

            connection.query(sql, function(err, result)
            {
                callback(result);
            });

            connection.end();
        });
    };

    this.AddThread = function(params, callback)
    {
        db = InitializeDB();

        db.ConnectDB(function(connection)
        {
            var sql = "insert into forum values(" + params.thread_id + ", '" + params.text + "', '" +  params.user + "')";

            connection.connect();

            connection.query(sql, function(err, result)
            {

                callback(result);
            });

            connection.end();
        });
    };

    this.AddComment = function(params, callback)
    {
        db = InitializeDB();

        db.ConnectDB(function(connection)
        {
            var sql = "insert into forum_comment values(" + params.comment_id + ", " + params.thread_id + ", '" + params.comment + "', '" +  params.user + "')";

            connection.connect();

            connection.query(sql, function(err, result)
            {

                callback(result);
            });

            connection.end();
        });
    };

    this.DeleteComment = function(id, callback)
    {
        db = InitializeDB();

        db.ConnectDB(function(connection)
        {
            var sql = "delete from forum_comment where id=" + id;

            connection.connect();

            connection.query(sql, function(err, result)
            {

                callback(result);
            });

            connection.end();
        });
    };
};

module.exports = Forum;
