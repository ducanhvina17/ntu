Set DateFormat MDY

INSERT INTO dbo.Users
(User_ID, Name)
VALUES
(1,		'Mary'),
(2,		'John'),
(3,		'Lucy'),
(4,		'Alice'),
(5,		'Jasmine'),
(6,		'Nathalie'),
(7,		'Jamie'),
(8,		'Alvin'),
(9,		'Cheryll'),
(10,	'Calvin'),
(11,	'Peter'),
(12,	'Shawn'),
(13,	'Sean'),
(14,	'Zander'),
(15,	'Christine'),
(16,	'Christabel'),
(17,	'Chris'),
(18,	'Jeremy'),
(19,	'Jonathon'),
(20,	'Jessica'),
(21,	'Iris');

INSERT INTO dbo.Employees
(Employee_ID, Name, Salary)
VALUES
(1,		'Jamie',		2000),
(2,		'Shermaine',	NULL),
(3,		'Mandy',		1800),
(4,		'Bryan',		1900),
(5,		'Lucas',		2200),
(6,		'Lucy',			2000),
(7,		'Brenda',		2300),
(8,		'Jennifer',		2400),
(9,		'Akon',			NULL),
(10,	'Jay',			2100);

INSERT INTO dbo.Shops
(Shop_Name)
VALUES
('Shop A'),
('Shop B'),
('Shop C'),
('Shop D'),
('Shop E'),
('Shop F'),
('Shop G'),
('Shop H'),
('Shop I'),
('Shop J');

INSERT INTO dbo.Products
(Product_Name, Category, Maker)
VALUES
('Galaxy Note 7',	'Phone',	'Samsung'),
('Galaxy S6',		'Phone',	'Samsung'),
('Galaxy S7',		'Phone',	'Samsung'),
('Galaxy Tab A',	'Tablet',	'Samsung'),
('Galaxy Tab S2',	'Tablet',	'Samsung'),
('iMac',			'Monitor',	'Apple'),
('iPad',			'Tablet',	'Apple'),
('iPhone 6s',		'Phone',	'Apple'),
('iPhone 7',		'Phone',	'Apple'),
('T-Shirt',			'Cloth',	'Zara');

INSERT INTO dbo.Products_in_Shop
(Product_Name, Shop_Name, Stock, Price)
VALUES
('Galaxy Note 7','Shop A',5,576),
('Galaxy Note 7','Shop B',89,950),
('Galaxy Note 7','Shop D',5,576),
('Galaxy Note 7','Shop E',5,576),
('Galaxy Note 7','Shop F',5,576),
('Galaxy Note 7','Shop G',5,576),
('Galaxy Note 7','Shop J',5,576),
('Galaxy S6','Shop A',5,789.99),
('Galaxy S6','Shop D',6,789.99),
('Galaxy S6','Shop E',6,789.99),
('Galaxy S6','Shop F',6,789.99),
('Galaxy S6','Shop G',6,789.99),
('Galaxy S6','Shop J',6,789.99),
('Galaxy S7','Shop A',7,888.88),
('Galaxy S7','Shop B',7,888.88),
('Galaxy S7','Shop D',7,888.88),
('Galaxy S7','Shop E',7,888.88),
('Galaxy S7','Shop F',7,888.88),
('Galaxy S7','Shop G',7,888.88),
('Galaxy S7','Shop H',7,888.88),
('Galaxy S7','Shop I',7,888.88),
('Galaxy S7','Shop J',7,888.88),
('Galaxy Tab A','Shop A',0,123.12),
('Galaxy Tab A','Shop B',1,123.12),
('Galaxy Tab A','Shop C',1,123.12),
('Galaxy Tab A','Shop F',1,123.12),
('Galaxy Tab A','Shop G',1,123.12),
('Galaxy Tab A','Shop H',1,123.12),
('Galaxy Tab A','Shop I',1,123.12),
('Galaxy Tab A','Shop J',1,123.12),
('Galaxy Tab S2','Shop A',2,500),
('Galaxy Tab S2','Shop B',2,500),
('Galaxy Tab S2','Shop C',2,500),
('Galaxy Tab S2','Shop E',2,500),
('Galaxy Tab S2','Shop F',2,500),
('Galaxy Tab S2','Shop G',2,500),
('Galaxy Tab S2','Shop H',2,500),
('Galaxy Tab S2','Shop I',2,500),
('iMac','Shop A',5,999.99),
('iMac','Shop B',888,2100),
('iMac','Shop D',5,999.99),
('iMac','Shop E',5,2000),
('iMac','Shop H',5,999.99),
('iPad','Shop A',99,144.56),
('iPad','Shop C',100,144.56),
('iPad','Shop D',99,144.56),
('iPad','Shop E',99,600),
('iPad','Shop H',99,144.56),
('iPhone 6s','Shop A',889,1200),
('iPhone 6s','Shop B',888,1100),
('iPhone 6s','Shop C',888,1100),
('iPhone 6s','Shop D',888,1000),
('iPhone 6s','Shop E',890,1200),
('iPhone 6s','Shop F',888,1200),
('iPhone 6s','Shop G',888,1200),
('iPhone 6s','Shop H',888,1200),
('iPhone 6s','Shop J',888,1200),
('iPhone 7','Shop A',123,1300),
('iPhone 7','Shop B',123,1300),
('iPhone 7','Shop C',123,1300),
('iPhone 7','Shop D',123,1300),
('iPhone 7','Shop E',123,1300),
('iPhone 7','Shop F',123,1300),
('iPhone 7','Shop G',123,1300),
('iPhone 7','Shop H',123,1300),
('iPhone 7','Shop I',123,1300),
('iPhone 7','Shop J',123,1300);

INSERT INTO dbo.Price_History
(Product_Name, Shop_Name, Start_Date, End_Date, Price)
VALUES
('Galaxy Note 7','Shop A','2016-01-20 00:00:00.000',NULL,576.00),
('Galaxy S6','Shop A','2016-01-20 00:00:00.000',NULL,789.99),
('Galaxy S7','Shop A','2016-01-20 00:00:00.000',NULL,888.88),
('Galaxy Tab A','Shop A','2016-01-20 00:00:00.000',NULL,123.12),
('Galaxy Tab S2','Shop A','2016-01-20 00:00:00.000','2016-06-30 00:00:00.000',510),
('iMac','Shop A','2016-01-20 00:00:00.000',NULL,999.99),
('iPad','Shop A','2016-01-20 00:00:00.000',NULL,144.56),
('iPhone 6s','Shop A','2016-01-20 00:00:00.000','2016-06-30 00:00:00.000',1100),
('iPhone 7','Shop A','2016-01-20 00:00:00.000',NULL,1300),
('Galaxy S7',       'Shop B','2016-01-21 00:00:00.000',NULL,      888.88),
('Galaxy Tab A',    'Shop B','2016-01-20 00:00:00.000',NULL,      123.12),
('Galaxy Tab S2',   'Shop B','2016-01-20 00:00:00.000',NULL,      500),
('iPhone 6s',       'Shop B','2016-01-20 00:00:00.000','2016-06-30 00:00:00.000',    1150),
('iPhone 7',        'Shop B','2016-01-20 00:00:00.000',NULL,1300),
('iMac','Shop B','2016-01-20 00:00:00.000',NULL,2100),
('Galaxy Tab A','Shop C','2016-01-20 00:00:00.000',NULL,123.12),
('Galaxy Tab S2','Shop C','2016-01-20 00:00:00.000',NULL,500),
('iPad','Shop C','2016-01-20 00:00:00.000',NULL,144.56),
('iPhone 6s','Shop C','2016-01-20 00:00:00.000','2016-07-05 00:00:00.000',1050),
('iPhone 7','Shop C','2016-01-20 00:00:00.000',NULL,1300),
('Galaxy Note 7','Shop D','2016-01-20 00:00:00.000',NULL,576),
('Galaxy S6','Shop D','2016-01-20 00:00:00.000',NULL,789.99),
('Galaxy S7','Shop D','2016-01-20 00:00:00.000',NULL,888.88),
('iMac','Shop D','2016-01-20 00:00:00.000',NULL,999.99),
('iPad','Shop D','2016-01-20 00:00:00.000',NULL,144.56),
('iPhone 6s','Shop D','2016-01-20 00:00:00.000','2016-07-10 00:00:00.000',950),
('iPhone 7','Shop D','2016-01-20 00:00:00.000',NULL,1300),
('Galaxy Note 7','Shop E','2016-01-20 00:00:00.000',NULL,576),
('Galaxy S6','Shop E','2016-01-20 00:00:00.000',NULL,789.99),
('Galaxy S7','Shop E','2016-01-20 00:00:00.000',NULL,888.88),
('Galaxy Tab S2',   'Shop E','2016-01-20 00:00:00.000',NULL,500),
('iMac','Shop E','2016-01-20 00:00:00.000','2016-07-07 00:00:00.000',2050),
('iPad','Shop E','2016-01-20 00:00:00.000','2016-06-10 00:00:00.000',610),
('iPhone 6s','Shop E','2016-01-20 00:00:00.000',NULL,1200),
('iPhone 7','Shop E','2016-01-20 00:00:00.000',NULL,1300),
('Galaxy Note 7','Shop F','2016-01-20 00:00:00.000',NULL,576),
('Galaxy S6','Shop F','2016-01-20 00:00:00.000',NULL,789.99),
('Galaxy S7','Shop F','2016-01-20 00:00:00.000',NULL,888.88),
('Galaxy Tab A','Shop F','2016-01-20 00:00:00.000',NULL,123.12),
('Galaxy Tab S2','Shop F','2016-01-20 00:00:00.000',NULL,500),
('iPhone 6s','Shop F','2016-01-20 00:00:00.000',NULL,1200),
('iPhone 7','Shop F','2016-01-20 00:00:00.000',NULL,1300),
('Galaxy Note 7','Shop G','2016-01-20 00:00:00.000',NULL,576),
('Galaxy S6','Shop G','2016-01-20 00:00:00.000',NULL,789.99),
('Galaxy S7','Shop G','2016-01-20 00:00:00.000',NULL,888.88),
('Galaxy Tab A','Shop G','2016-01-20 00:00:00.000',NULL,123.12),
('Galaxy Tab S2','Shop G','2016-01-20 00:00:00.000',NULL,500),
('iPhone 6s','Shop G','2016-01-20 00:00:00.000',NULL,1200),
('iPhone 7','Shop G','2016-01-20 00:00:00.000',NULL,1300),
('Galaxy S7','Shop H','2016-01-20 00:00:00.000',NULL,888.88),
('Galaxy Tab A','Shop H','2016-01-20 00:00:00.000',NULL,123.12),
('Galaxy Tab S2','Shop H','2016-01-20 00:00:00.000',NULL,500),
('iMac','Shop H','2016-01-20 00:00:00.000',NULL,999.99),
('iPad','Shop H','2016-01-20 00:00:00.000',NULL,144.56),
('iPhone 6s','Shop H','2016-01-20 00:00:00.000',NULL,1200),
('iPhone 7','Shop H','2016-01-20 00:00:00.000',NULL,1300),
('Galaxy S7','Shop I','2016-01-20 00:00:00.000',NULL,888.88),
('Galaxy Tab A','Shop I','2016-01-20 00:00:00.000',NULL,123.12),
('Galaxy Tab S2','Shop I','2016-01-20 00:00:00.000',NULL,500),
('iPhone 7','Shop I','2016-01-20 00:00:00.000',NULL,1300),
('Galaxy Note 7','Shop J','2016-01-20 00:00:00.000','2016-06-15 00:00:00.000',576),
('Galaxy S6','Shop J','2016-01-20 00:00:00.000',NULL,789.99),
('Galaxy S7','Shop J','2016-01-20 00:00:00.000',NULL,888.88),
('Galaxy Tab A','Shop J','2016-01-20 00:00:00.000',NULL,123.12),
('iPhone 6s','Shop J','2016-01-20 00:00:00.000',NULL,1200),
('iPhone 7','Shop J','2016-01-20 00:00:00.000',NULL,1300),
('Galaxy Note 7',	'Shop B','2016-01-20 00:00:00.000','2016-05-20 00:00:00.000',960),
('Galaxy Note 7',	'Shop B',	'2016-05-20 00:00:00.000',	'2016-06-15 00:00:00.000',	980),
('Galaxy Note 7',	'Shop B',	'2016-06-15 00:00:00.000',	NULL,						950),
('Galaxy Tab S2',	'Shop A',	'2016-06-30 00:00:00.000',	NULL,						500),
('iMac',			'Shop E',	'2016-07-07 00:00:00.000',	NULL,						2000),
('iPad',			'Shop E',	'2016-06-10 00:00:00.000',	NULL,						600),
('iPhone 6s',		'Shop A',	'2016-06-30 00:00:00.000',	'2016-07-06 00:00:00.000',	1000),
('iPhone 6s',		'Shop A',	'2016-07-06 00:00:00.000',	NULL,						1200),
('iPhone 6s',		'Shop B',	'2016-06-30 00:00:00.000',	NULL,						1100),
('iPhone 6s',		'Shop C',	'2016-07-05 00:00:00.000',	NULL,						1100),
('iPhone 6s',		'Shop D',	'2016-07-10 00:00:00.000',	NULL,						1000);

INSERT INTO dbo.Orders
(Order_ID, User_ID, Date_Time, Shipping_Address, Total_Shipping_Cost)
VALUES
(1,		1,	'2016-05-02 00:00:00.000', '11 Jurong West, S654321',	 20),
(2,		2,	'2016-05-05 00:00:00.000', '12 Jurong East, S647321',	15),
(3,		3,	'2016-05-07 00:00:00.000', '28 Boonlay, S874321',		20.20),
(4,		4,	'2016-05-28 00:00:00.000', '34 Jokoon, S675321',		10),
(5,		5,	'2016-06-01 08:00:00.000', '86 Jookee, S995321',		9.90),
(6,		6,	'2016-06-02 00:00:00.000', '11 Bedok, S461321', 		20),
(7,		7,	'2016-06-05 00:00:00.000', '12 Jurong East, S647321',	15),
(8,		8,	'2016-06-07 00:00:00.000', '28 Tampines, S876321',		20.20),
(9,		9,	'2016-06-12 00:00:00.000', '34 Bishan, S205321',		10),
(10,	10,	'2016-06-29 00:00:00.000', '86 Ang Mo Kio, S694321',	9.90),
(11,	11,	'2016-05-02 03:00:00.000', '11 Jurong West, S654321', 	20),
(12,	12,	'2016-05-05 21:00:00.000', '12 Jurong East, S647321',	15),
(13,	13,	'2016-05-07 18:00:00.000', '28 Boonlay, S874321',		20.20),
(14,	14,	'2016-06-12 13:00:00.000', '34 Jokoon, S675321',		10),
(15,	15,	'2016-05-29 19:00:00.000', '86 Jookee, S995321',		9.90),
(16,	16,	'2016-05-30 10:20:30.000', '11 Bedok, S461321', 		20),
(17,	17,	'2016-06-05 16:00:00.000', '12 Jurong East, S647321',	15),
(18,	18,	'2016-05-30 10:20:30.000', '28 Tampines, S876321',		20.20),
(19,	19,	'2016-06-12 09:00:00.000', '34 Bishan, S205321',		10),
(20,	20,	'2016-05-29 07:00:00.000', '86 Ang Mo Kio, S694321',	9.90),
(21,	21,	'2016-05-29 07:00:00.000', '86 Ang Mo Kio, S694321',	9.90),
(22,	1,	'2016-05-29 10:20:30.000', '11 Jurong West, S654321', 	20),
(23,	2,	'2016-05-20 10:20:30.000', '12 Jurong East, S647321',	15),
(24,	3,	'2016-06-05 11:00:00.000', '28 Boonlay, S874321',		20.20),
(25,	1,	'2016-06-10 11:00:00.000', '11 Jurong West, S654321',	20),
(26,	1,	'2016-05-29 10:20:30.000', '11 Jurong West, S654321',	20);

INSERT INTO dbo.Rate
(Product_Name, Shop_Name, Order_ID, Date_Time, Rating)
VALUES
('iMac',		'Shop A',	1,	'2016-06-16 11:00:00.000',	3),
('iMac',		'Shop B',	2,	'2016-06-18 11:30:00.000',	1),
('Galaxy Note 7',	'Shop E',	3,	'2016-10-14 09:10:43.000',	2),
('iPad',			'Shop A',	2,	'2016-10-14 08:30:40.000',	4),
('Galaxy Tab A',	'Shop F',	1,	'2016-10-13 11:12:47.000',	3),
('iPhone 6s',		'Shop A',	1,	'2016-06-16 11:00:00.000',	5),
('iPhone 6s',		'Shop D',	2,	'2016-06-18 11:00:00.000',	5),
('iPhone 6s',		'Shop E',	3,	'2016-06-19 11:00:00.000',	5),
('iPhone 6s',		'Shop E',	4,	'2016-05-31 10:20:30.000',	5),
('iPhone 6s',		'Shop D',	5,	'2016-06-13 11:30:00.000',	5),
('iPhone 6s',		'Shop D',	6,	'2016-05-31 10:20:30.000',	5),
('iPhone 6s',		'Shop A',	7,	'2016-06-14 11:00:00.000',	5),
('iPhone 6s',		'Shop D',	8,	'2016-06-13 11:00:00.000',	5),
('iPhone 6s',		'Shop A',	9,	'2016-06-15 11:00:00.000',	5),
('iPhone 6s',		'Shop E',	10,	'2016-05-31 10:20:30.000',	5),
('iPhone 6s',		'Shop E',	11,	'2016-06-14 11:00:00.000',	5),
('Galaxy S6',		'Shop D',	12,	'2016-05-31 10:20:30.000',	5),
('Galaxy S6',		'Shop A',	13,	'2016-06-13 11:00:00.000',	5),
('Galaxy S6',		'Shop E',	14,	'2016-06-13 11:00:00.000',	5),
('Galaxy S6',		'Shop A',	15,	'2016-06-14 11:00:00.000',	5),
('Galaxy S6',		'Shop F',	16,	'2016-05-31 10:20:30.000',	5),
('Galaxy S6',		'Shop J',	17,	'2016-06-13 11:00:00.000',	5),
('Galaxy S6',		'Shop D',	18,	'2016-05-31 10:20:30.000',	5),
('Galaxy S6',		'Shop A',	19,	'2016-06-14 11:00:00.000',	5),
('Galaxy S6',		'Shop J',	20,	'2016-06-13 11:00:00.000',	5),
('Galaxy S6',		'Shop F',	21,	'2016-06-15 11:00:00.000',	5),
('Galaxy S6',		'Shop F',	22, '2016-06-01 10:20:30.000',	4),
('Galaxy S6',		'Shop F',	23,	'2016-06-15 11:00:00.000',	2),
('Galaxy Tab S2',	'Shop C',	24,	'2016-06-19 11:00:00.000',	5),
('iPhone 6s',		'Shop A',	25, '2016-06-13 11:00:00.000',	1),
('iPad',			'Shop C',	26,	'2016-05-30 10:20:30.000',	1);

INSERT INTO dbo.Products_in_Order
(Product_Name, Shop_Name, Order_ID, Status, Delivery_Date, Price, Quantity)
VALUES
('Galaxy Note7',	'Shop D',	1,'Delivered','2016-06-13 12:00:00.000',	576	,	1),
('Galaxy Note7',	'Shop E',	3,'Delivered','2016-06-15 12:00:00.000',	576	,	1),
('Galaxy S6',	'Shop A',	13,	'Delivered','2016-06-12 11:00:00.000',	789.99,	1),
('Galaxy S6',	'Shop A',	15,	'Delivered','2016-06-12 11:00:00.000',	789.99,	1),
('Galaxy S6',	'Shop A',	19,	'Delivered','2016-06-14 11:00:00.000',	789.99,	1),
('Galaxy S6',	'Shop A',	28,	'BeingProcessed',NULL,	789.99,	1),
('Galaxy S6',	'Shop D',	12,	'Delivered','2016-05-30 10:20:30.000',	789.99,	1),
('Galaxy S6',	'Shop D',	18,	'Delivered','2016-06-07 11:00:00.000',	789.99,	1),
('Galaxy S6',	'Shop E',	14,	'Delivered','2016-06-14 11:00:00.000',	789.99,	1),
('Galaxy S6',	'Shop F',	16,	'Delivered','2016-06-02 21:00:00.000',	789.99,	1),
('Galaxy S6',	'Shop F',	21,	'Delivered','2016-06-12 11:00:00.000',	789.99,	1),
('Galaxy S6',	'Shop F',	22,	'Delivered','2016-05-30 10:20:30.000',	789.99,	1),
('Galaxy S6',	'Shop F',	23,	'Delivered','2016-05-30 10:20:30.000',	789.99,	1),
('Galaxy S6',	'Shop J',	17,	'Delivered','2016-06-12 11:00:00.000',	789.99,	1),
('Galaxy S6',	'Shop J',	20,	'Delivered','2016-06-12 11:00:00.000',	789.99,	1),
('Galaxy Tab A',	'Shop F',	1,	'Delivered','2016-06-13 12:00:00.000',	123.12,	1),
('Galaxy Tab A',	'Shop F',	4,	'Delivered','2016-05-30 12:00:00.000',	123.12,	1),
('Galaxy Tab S2',	'Shop  C',	3,	'Delivered','2016-06-15 12:00:00.000',	500	,	1),
('iMac',	'Shop  A',	1,	'Delivered','2016-06-13 12:00:00.000',	999.99,	1),
('iMac',	'Shop  B',	2,	'Delivered','2016-06-12 11:00:00.000',	2100,	1),
('iMac',	'Shop  B',	24,	'Delivered','2016-06-12 11:00:00.000',	2100,	1),
('iMac',	'Shop  E',	5,	'Delivered','2016-06-12 11:30:00.000',	2050,	1),
('iMac',	'Shop  H',	2,	'Delivered','2016-06-12 11:00:00.000',	999.99,	1),
('iPad',	'Shop A',	2,	'Delivered','2016-06-12 11:00:00.000',	144.56,	1),
('iPad',	'Shop A',	3,	'Delivered','2016-06-15 12:00:00.000',	144.56,	1),
('iPad',	'Shop C',	26,	'Returned','2016-05-30 10:20:30.000',	144.56,	1),
('ipad',	'Shop C',	27,	'BeingProcessed',NULL,	144.56,	1),
('iPhone 6s','Shop  A',	1,	'Delivered','2016-06-13 12:00:00.000',	1100,	1),
('iPhone 6s','Shop  A',	7,	'Delivered','2016-06-12 11:00:00.000',	1100,	1),
('iPhone 6s','Shop  A',	9,	'Delivered','2016-06-12 15:00:00.000',	1100,	1),
('iPhone 6s','Shop  A',	25,	'Returned','2016-06-12 11:00:00.000',	1100,	1),
('iPhone 6s','Shop  D',	2,	'Delivered','2016-06-12 11:00:00.000',	950,	1),
('iPhone 6s','Shop  D',	5,	'Delivered','2016-06-12 11:30:00.000',	950,	1),
('iPhone 6s','Shop  D',	6,	'Delivered','2016-06-10 10:20:30.000',	950,	1),
('iPhone 6s','Shop  D',	8,	'Delivered','2016-06-12 11:00:00.000',	950,	1),
('iPhone 6s','Shop  E',	3,	'Delivered','2016-06-15 12:00:00.000',	1200,	1),
('iPhone 6s','Shop  E',	4,	'Delivered','2016-05-30 10:20:30.000',	1200,	1),
('iPhone 6s','Shop  E',	10,	'Delivered','2016-06-18 10:20:30.000',	1200,	1),
('iPhone 6s','Shop  E',	11,	'Returned','2016-06-12 11:00:00.000',	1200,	1);

INSERT INTO dbo.Comments
(Comment_ID, Text, User_ID)
VALUES
(1, 'This thing is good!',	1),
(2, 'How good is it?',		3),
(3, 'I wanna know too!',	2);

INSERT INTO dbo.Comments_on_Comment
(Comments_on_Comment_ID, Reference_Comment_ID)
VALUES
(1,	2),
(3,	2);

INSERT INTO dbo.Comments_on_Product
(Comments_on_Product_ID, Product_Name, Shop_Name, Order_ID)
VALUES
(1, 'iPhone 6s',	'Shop A',	1);

INSERT INTO dbo.Complaints
(Complaint_ID, User_ID, Filling_Date_Time, Text, Status, Employee_ID, Addressed_Date_Time)
VALUES
(1,		1,	'2016-06-15 16:12:30.000',	'Slow replies.',											'Pending',	1,		NULL),
(2,		2,	'2016-06-14 13:12:30.000',	'The product is undesirable.',								'Pending',	NULL,	NULL),
(3,		3,	'2016-06-13 12:00:00.000',	'Item has not arrived.',									'Solved',	2,		'2016-06-15 15:19:00.000'), --early than delivered date since the complaint is about item undelivered.
(4,		4,	'2016-06-15 10:20:30.000',	'Comment is not reliable.',									'Solved',	2,		'2016-06-19 18:19:00.000'),
(5,		5,	'2016-06-13 14:12:47.000',	'The products in this shop are relatively more expensive.',	'Pending',	NULL,	NULL),
(6,		6,	'2016-06-15 11:20:30.000',	'This comment is misleading.',								'Pending',	NULL,	NULL),
(7,		7,	'2016-06-15 10:03:00.000',	'Refund.',													'Solved',	3,		'2016-06-15 15:42:13.000'),
(8,		8,	'2016-06-14 11:00:00.000',	'Product doesnt worth the price.',							'Solved',	4,		'2016-06-15 13:39:30.000'),
(9,		9,	'2016-06-13 17:00:00.000',	'Wrong product.',											'Solved',	5,		'2016-06-15 09:17:10.000'),
(10,	10,	'2016-06-01 11:20:31.000',	'Poor customer service',									'Solved',	5,		'2016-06-08 09:17:10.000');

INSERT INTO dbo.Complaints_on_Comment
(Complaint_ID, Comment_ID)
VALUES
(4,	1),
(2,	1),
(6,	1);

INSERT INTO dbo.Complaints_on_Product
(Complaint_ID, Product_Name)
VALUES
(3, 'Galaxy Note 7'),
(7, 'iPhone 6s'),
(8, 'iPhone 6s'),
(2, 'iPad');

INSERT INTO dbo.Complaints_on_Shop
(Complaint_ID, Shop_Name)
VALUES
(1,		'Shop A'),
(5,		'Shop E'),
(10,	'Shop E');
