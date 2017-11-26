var express = require("express");
var app = express();
var router = express.Router();

var path = require('path');
app.use(express.static(path.join(__dirname, "views")));

var CookieParser = require("cookie-parser");
app.use(CookieParser());

var bodyParser = require('body-parser');
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

var Search = require("./backend/search.js");
var search = new Search();

var Forum = require("./backend/forum.js");
var forum = new Forum();

var FavList = require("./backend/favlist.js");
var favlist = new FavList();

var User = require("./backend/user.js");
var user = new User();

var comparelist = ["", "", ""];

app.listen(3000,function()
{
    console.log("Live at Port 3000");
});

app.set('view engine', 'ejs');

app.get("/",function(req, res)
{
    search.GetAllSchools(function(schools)
    {
        res.render("index", {username: req.cookies.username, email: req.cookies.email,
                             schools: schools.name});
    });
});

app.get("/secondaryschool",function(req, res)
{
    var areaArr = [];
    var ccaArr = [];
    var subjectArr = [];
    var mrtArr = [];

    search.GetAreas(function(result)
    {
        areaArr = result.area;
    });

    search.GetCcas(function(result)
    {
        ccaArr = result.cca;
    });

    search.GetSubjects(function(result)
    {
        subjectArr = result.subject;
    });

    search.GetMrts(function(result)
    {
        mrtArr = result.mrt;
    });

    search.GetAllSchools(function(schools)
    {
        result = {area: areaArr, cca: ccaArr, subject: subjectArr, mrt: mrtArr};

        res.render("secondaryschool", {username: req.cookies.username, email: req.cookies.email,
                                           schools: schools.name, secondaryschools: result});
    });
});

app.get("/forum",function(req, res)
{
    search.GetAllSchools(function(schools)
    {
        forum.GetThread(function(threads)
        {
            forum.GetComment(function(comments)
            {
                res.render("forum", {username: req.cookies.username, email: req.cookies.email,
                                 schools: schools.name, thread: threads, comment: comments});
            });
        });
    });
});

app.get("/login",function(req, res)
{
    search.GetAllSchools(function(schools)
    {
        res.render("login", {schools: schools.name});
    });
});

app.get("/register", function(req, res)
{
    search.GetAllSchools(function(schools)
    {
        res.render("register", {schools: schools.name});
    });
});

app.get("/favourite",function(req, res)
{
    search.GetAllSchools(function(schools)
    {
        favlist.GetList(req.cookies.username, function(result)
        {
            res.render("favourite", {username: req.cookies.username, email: req.cookies.email, list: result.name, schools: schools.name});
        });
    });
});

app.get("/comparisonlist",function(req, res)
{
    search.GetAllSchools(function(schools)
    {
        favlist.GetList(req.cookies.username, function(favschool)
        {
            search.GetAllSecondarySchool(function(secschool)
            {
                var nameArr = [];
        		var areaArr = [];
        		var locationArr = [];
        		var telephoneArr = [];
        		var emailArr = [];
        		var websiteArr = [];
        		var mrtArr = [];
        		var busArr = [];
        		var ccaArr = [];
        		var subjectArr = [];

                for (i = 0; i < 3; i++)
                {
                    j = secschool.name.indexOf(comparelist[i]);
                    if (comparelist[i] !== "" && j > -1)
                    {
                        nameArr.push(secschool.name[j]);
                        areaArr.push(secschool.area[j]);
                        locationArr.push(secschool.location[j]);
                        telephoneArr.push(secschool.telephone[j]);
                        emailArr.push(secschool.email[j]);
                        websiteArr.push(secschool.website[j]);
                        mrtArr.push(secschool.mrt[j]);
                        busArr.push(secschool.bus[j]);
                        ccaArr.push(secschool.cca[j]);
                        subjectArr.push(secschool.subject[j]);
                    }
                }

                c = {name: nameArr,
                     area: areaArr,
                     location: locationArr,
                     telephone: telephoneArr,
                     email: emailArr,
                     website: websiteArr,
                     mrt: mrtArr,
                     bus: busArr,
                     cca: ccaArr,
                     subject: subjectArr};

                res.render("comparisonlist",
                           {username: req.cookies.username,
                            email: req.cookies.email,
                            schools: schools.name,
                            data: c,
                            clist: comparelist,
                            list: favschool.name});
            });
        });
    });
});

app.post("/searchindividualschool", function(req, res)
{
    search.GetAllSchools(function(schools)
    {
        search.SearchSchool(req.body.schoolname.replace(/\+/g, " "), function(result)
        {
            res.render("individualschool", {data: result, username: req.cookies.username,
                                            email: req.cookies.email, schools: schools.name});
        });
    });
});

app.post("/searchsecondaryschool", function(req, res)
{
    var areaArr = [];
    var ccaArr = [];
    var subjectArr = [];
    var mrtArr = [];

    search.GetAreas(function(result)
    {
        areaArr = result.area;
    });

    search.GetCcas(function(result)
    {
        ccaArr = result.cca;
    });

    search.GetSubjects(function(result)
    {
        subjectArr = result.subject;
    });

    search.GetMrts(function(result)
    {
        mrtArr = result.mrt;
    });

    search.GetAllSchools(function(schools)
    {
        search.SearchSecondarySchool(req.body, function(result)
        {
            favlist.GetList(req.cookies.username, function(favschool)
            {
                secschool = {area: areaArr, cca: ccaArr, subject: subjectArr, mrt: mrtArr};

                res.render("secondaryschool",
                           {data: result, username: req.cookies.username,
                                           email: req.cookies.email, schools: schools.name, secondaryschools: secschool, clist: comparelist,  list: favschool.name});
            });
        });
    });
});

app.get("/AddToCompare/:name", function(req, res)
{
    name = req.params.name.replace(/\+/g, " ");

    for (i = 0; i < 3; i++)
    {
        if (comparelist[i] === "")
        {
            comparelist[i] = name;
            break;
        }
    }
});

app.get("/RemoveFromCompare/:name", function(req, res)
{
    name = req.params.name.replace(/\+/g, " ");
    comparelist[comparelist.indexOf(name)] = "";
});

app.get("/RemoveFromCompareC/:name", function(req, res)
{
    name = req.params.name.replace(/\+/g, " ");
    comparelist[comparelist.indexOf(name)] = "";
    res.redirect(req.get('referer'));
});

app.get("/individualschoollink/:name", function(req, res)
{
    search.GetAllSchools(function(schools)
    {
        search.SearchSchool(req.params.name.replace(/\+/g, " "), function(result)
        {
            res.render("individualschool", {data: result, username: req.cookies.username,
                                            email: req.cookies.email, schools: schools.name});
        });
    });
});

app.get("/getroute/:name", function(req, res)
{
    search.GetAllSchools(function(schools)
    {
        name = req.params.name.replace(/\+/g, " ");
        res.render("route", {name: name, username: req.cookies.username, email: req.cookies.email,
                             schools: schools.name});
    });
});

app.post("/addthread/:id", function(req, res)
{
    params = {thread_id: req.params.id,
              text: req.body.thread,
              user: req.cookies.username};

    forum.AddThread(params, function(result)
    {
        res.redirect(req.get('referer'));
    });
});

app.post("/addcomment/:tid/:cid", function(req, res)
{
    params = {comment_id: req.params.cid, thread_id: req.params.tid, comment: req.body.comment, user: req.cookies.username};

    forum.AddComment(params, function(result)
    {
        res.redirect(req.get('referer'));
    });
});

app.get("/removecomment/:id", function(req, res)
{
    forum.DeleteComment(req.params.id, function(result)
    {
        res.redirect(req.get('referer'));
    });
});

app.post("/userlogin", function(req, res)
{
    search.GetAllSchools(function(schools)
    {
        user.Login(req.body, function(result)
        {
            if (result.username === "" ||
            req.body.username != result.username || req.body.password != result.password)
            {
                res.render("login", {error: "Username or password incorrect!",
                                     schools: schools.name});
            }
            else {
                res.cookie('username', result.username, {expires: new Date(Date.now() + 604800000)});
                res.cookie('email', result.email, {expires: new Date(Date.now() + 604800000)});
                res.render("index", {username: result.username, email: result.email,
                                     schools: schools.name});
            }
        });
    });
});

app.get("/userlogout", function(req, res)
{
    search.GetAllSchools(function(schools)
    {
        res.clearCookie('username');
        res.clearCookie('email');
        res.render("index", {username: "", email:"", schools: schools.name});
    });
});

app.post("/userregister", function(req, res)
{
    search.GetAllSchools(function(schools)
    {
        user.Register(req.body, function(result)
        {
            if (result == 1)
                res.render("index", {username: req.cookies.username, email: req.cookies.email,
                                     schools: schools.name});
            else
                res.render("register", {error: "Username exists!", schools: schools.name});
        });
    });
});

app.get("/AddToFav/:name", function(req, res)
{
    favlist.GetList(req.cookies.username, function(favschool)
    {
        if (favschool.name.indexOf(req.params.name) == -1)
        {
            var temp = {username: req.cookies.username, school: req.params.name.replace(/\+/g, " ")};

            favlist.AddSchool(temp, function(result) {});
        }
    });
});

app.get("/RemoveFromFav/:name", function(req, res)
{
    var temp = {username: req.cookies.username, school: req.params.name.replace(/\+/g, " ")};

    favlist.RemoveSchool(temp, function(result) {});
});

app.get("/RemoveFromFavR/:name", function(req, res)
{
    var temp = {username: req.cookies.username, school: req.params.name.replace(/\+/g, " ")};

    favlist.RemoveSchool(temp, function(result)
    {
        search.GetAllSchools(function(schools)
        {
            favlist.GetList(req.cookies.username, function(favschool)
            {
                res.render("favourite", {username: req.cookies.username, email: req.cookies.email, list: favschool.name, schools: schools.name});
            });
        });
    });
});
