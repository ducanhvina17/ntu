function InitializeDB()
{
    var Database = require("./database.js");
    return new Database();
}

var Search = function ()
{
    this.GetAllSchools = function(callback)
    {
        db = InitializeDB();

        db.ConnectDB(function(connection)
        {
            var sql = "select * from school";

            connection.connect();

            connection.query(sql, function(err, result)
            {
                if (err)
                    callback(err);

                var nameArr = [];

                for (i = 0; i < result.length; i++)
                    nameArr.push(result[i].school_name);

                result = {name: nameArr};
                callback(result);
            });

            connection.end();
        });
    };

    this.SearchSchool = function(name, callback)
    {
        db = InitializeDB();

        db.ConnectDB(function(connection)
        {
            var sql = "select * from school where school_name like '%" + name + "'";

            connection.connect();

            connection.query(sql, function(err, result)
            {
                if (err)
                    callback(err);

                var nameArr = [];
        		var codeArr = [];
        		var typeArr = [];
        		var areaArr = [];
        		var locationArr = [];
        		var telephoneArr = [];
        		var emailArr = [];
        		var websiteArr = [];
        		var mrtArr = [];
        		var busArr = [];
        		var ccaArr = [];
        		var subjectArr = [];

                for (i = 0; i < result.length; i++)
                {
                    nameArr.push(result[i].school_name);
                    codeArr.push(result[i].school_code);
                    typeArr.push(result[i].type);
                    areaArr.push(result[i].area);
                    locationArr.push(result[i].location);
                    telephoneArr.push(result[i].Telephone);
                    emailArr.push(result[i].Email);
                    websiteArr.push(result[i].website);
                    mrtArr.push(result[i].Nearest_MRT);
                    busArr.push(result[i].Bus_number);
                    ccaArr.push(result[i].CCA);
                    subjectArr.push(result[i].subjects);
                }

                result = {name: nameArr,
                          code: codeArr,
                          type: typeArr,
                          area: areaArr,
                          location: locationArr,
                          telephone: telephoneArr,
                          email: emailArr,
                          website: websiteArr,
                          mrt: mrtArr,
                          bus: busArr,
                          cca: ccaArr,
                          subject: subjectArr};

                callback(result);
            });

            connection.end();
        });
    };

    this.GetAreas = function(callback)
    {
        db = InitializeDB();

        db.ConnectDB(function(connection)
        {
            var sql = "select * from area_name";

            connection.connect();

            connection.query(sql, function(err, result)
            {
                if (err)
                    callback(err);

        		var areaArr = [];

                for (i = 0; i < result.length; i++)
                    areaArr.push(result[i].area_name);

                result = {area: areaArr};
                callback(result);
            });

            connection.end();
        });
    };

    this.GetCcas = function(callback)
    {
        db = InitializeDB();

        db.ConnectDB(function(connection)
        {
            var sql = "select * from cca_list";

            connection.connect();

            connection.query(sql, function(err, result)
            {
                if (err)
                    callback(err);

                var ccaArr = [];

                for (i = 0; i < result.length; i++)
                    ccaArr.push(result[i].CCA);

                result = {cca: ccaArr};
                callback(result);
            });

            connection.end();
        });
    };

    this.GetMrts = function(callback)
    {
        db = InitializeDB();

        db.ConnectDB(function(connection)
        {
            var sql = "select * from nearest_mrt";

            connection.connect();

            connection.query(sql, function(err, result)
            {
                if (err)
                    callback(err);

                var mrtArr = [];

                for (i = 0; i < result.length; i++)
                    mrtArr.push(result[i].nearest_mrt);

                result = {mrt: mrtArr};
                callback(result);
            });

            connection.end();
        });
    };

    this.GetSubjects = function(callback)
    {
        db = InitializeDB();

        db.ConnectDB(function(connection)
        {
            var sql = "select * from subjects";

            connection.connect();

            connection.query(sql, function(err, result)
            {
                if (err)
                    callback(err);

                var subjectArr = [];

                for (i = 0; i < result.length; i++)
                    subjectArr.push(result[i].subjects);

                result = {subject: subjectArr};
                callback(result);
            });

            connection.end();
        });


    };

    this.SearchSecondarySchool = function(params, callback)
    {
        db = InitializeDB();

        db.ConnectDB(function(connection)
        {
            var where = "where";

            if (params.area !== "")
                where += " area like '%" + params.area + "%' and";

            if (params.cca !== "")
                where += " like '%" + params.cca + "%' and";

            if (params.subject !== "")
                where += " subjects like '%" + params.subject + "%' and";

            if (params.mrt !== "")
                where += " Nearest_MRT like '%" + params.mrt + "%' and";

            if (params.bus !== "")
                where += " Bus_number like '%" + params.bus + "%' and";

            where += " type like 'Secondary'";

            if (params.type == "range")
            {
                switch(params.category)
                {
                    case "e":
    					sql = "select school.*, psle_score.psle_E_min as lowest, psle_score.psle_E_max as highest from school left join psle_score on psle_score.school_id = school.school_id " + where + " and " + params.score + " BETWEEN psle_score.psle_E_min and psle_score.psle_E_max";
    					break;
    				case "n":
    					sql = "select school.*, psle_score.psle_N_min as lowest, psle_score.psle_N_max as highest from school left join psle_score on psle_score.school_id = school.school_id " + where + " and " + params.score + " BETWEEN psle_score.psle_N_min and psle_score.psle_N_max";
    					break;
    				case "t":
    					sql = "select school.*, psle_score.psle_NT_min as lowest, psle_score.psle_NT_max as highest from school left join psle_score on psle_score.school_id = school.school_id " + where + " and " + params.score + " BETWEEN psle_score.psle_NT_min and psle_score.psle_NT_max";
    					break;
                }
            }
            else
            {
                switch(params.category)
        		{
        			case "e":
        				sql = "select school.*, psle_score.psle_E_min as lowest from school left join psle_score on psle_score.school_id = school.school_id " + where + "and psle_score.psle_E_min>=" + params.score;
        				break;
        			case "n":
        				sql = "select school.*, psle_score.psle_N_min as lowest from school left join psle_score on psle_score.school_id = school.school_id " + where + "and psle_score.psle_N_min>=" + params.score ;
        				break;
        			case "t":
        				sql = "select school.*, psle_score.psle_NT_min as lowest from school left join psle_score on psle_score.school_id = school.school_id " + where + "and psle_score.psle_NT_min>=" + params.score;
        				break;
        		}
            }

            connection.connect();

            connection.query(sql, function(err, result)
            {
                if (err)
                    callback(err);

                var nameArr = [];
        		var codeArr = [];
        		var typeArr = [];
        		var areaArr = [];
        		var locationArr = [];
        		var telephoneArr = [];
        		var emailArr = [];
        		var websiteArr = [];
        		var mrtArr = [];
        		var busArr = [];
        		var ccaArr = [];
        		var subjectArr = [];
                var scoreArr = [];

                for (i = 0; i < result.length; i++)
                {
                    nameArr.push(result[i].school_name);
                    codeArr.push(result[i].school_code);
                    typeArr.push(result[i].type);
                    areaArr.push(result[i].area);
                    locationArr.push(result[i].location);
                    telephoneArr.push(result[i].Telephone);
                    emailArr.push(result[i].Email);
                    websiteArr.push(result[i].website);
                    mrtArr.push(result[i].Nearest_MRT);
                    busArr.push(result[i].Bus_number);
                    ccaArr.push(result[i].CCA);
                    subjectArr.push(result[i].subjects);

                    scoreArr.push(typeof(result[i].highest) != "undefined" ?
                        result[i].lowest : result[i].lowest + "-" + result[i].highest);
                }
                result = {name: nameArr,
                          code: codeArr,
                          type: typeArr,
                          area: areaArr,
                          location: locationArr,
                          telephone: telephoneArr,
                          email: emailArr,
                          website: websiteArr,
                          mrt: mrtArr,
                          bus: busArr,
                          cca: ccaArr,
                          subjet: subjectArr,
                          psle: scoreArr};

                callback(result);
            });

            connection.end();
        });
    };

    this.GetAllSecondarySchool = function(callback)
    {
        db = InitializeDB();

        db.ConnectDB(function(connection)
        {
            var sql = "select * from school where type='Secondary'";

            connection.connect();

            connection.query(sql, function(err, result)
            {
                if (err)
                    callback(err);

                var nameArr = [];
        		var codeArr = [];
        		var typeArr = [];
        		var areaArr = [];
        		var locationArr = [];
        		var telephoneArr = [];
        		var emailArr = [];
        		var websiteArr = [];
        		var mrtArr = [];
        		var busArr = [];
        		var ccaArr = [];
        		var subjectArr = [];

                for (i = 0; i < result.length; i++)
                {
                    nameArr.push(result[i].school_name);
                    codeArr.push(result[i].school_code);
                    typeArr.push(result[i].type);
                    areaArr.push(result[i].area);
                    locationArr.push(result[i].location);
                    telephoneArr.push(result[i].Telephone);
                    emailArr.push(result[i].Email);
                    websiteArr.push(result[i].website);
                    mrtArr.push(result[i].Nearest_MRT);
                    busArr.push(result[i].Bus_number);
                    ccaArr.push(result[i].CCA);
                    subjectArr.push(result[i].subjects);
                }

                result = {name: nameArr,
                          code: codeArr,
                          type: typeArr,
                          area: areaArr,
                          location: locationArr,
                          telephone: telephoneArr,
                          email: emailArr,
                          website: websiteArr,
                          mrt: mrtArr,
                          bus: busArr,
                          cca: ccaArr,
                          subject: subjectArr};

                callback(result);
            });

            connection.end();
        });
    };
};

module.exports = Search;
