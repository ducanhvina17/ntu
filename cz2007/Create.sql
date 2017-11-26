CREATE TABLE Users(
	User_ID	int			PRIMARY KEY	CHECK (User_ID > 0),
	Name	varchar(50)	NOT NULL	CHECK (Name NOT LIKE '%[^a-Z]%')
);


CREATE TABLE Employees(
	Employee_ID	int			PRIMARY KEY		CHECK (Employee_ID > 0),
	Name		varchar(50)	NOT NULL		CHECK (Name NOT LIKE '%[^a-Z]%'),
	Salary		float		NULL			CHECK (Salary > 0)
);


CREATE TABLE Shops(
	Shop_Name	varchar(50)	PRIMARY KEY,
);


CREATE TABLE Products(
	Product_Name	varchar(50)	PRIMARY KEY,
	Category		varchar(50)	NULL,
	Maker			varchar(50)	NULL
);


CREATE TABLE Products_in_Shop(
	Product_Name	varchar(50),
	Shop_Name		varchar(50),
	PRIMARY KEY (Product_Name, Shop_Name),
	Stock			int				NOT NULL	CHECK (Stock >= 0),
	Price			float			NULL		CHECK (Price > 0 OR Price = NULL),
	FOREIGN KEY	(Product_Name)	REFERENCES Products(Product_Name)
	ON DELETE CASCADE
	ON UPDATE CASCADE,
	FOREIGN KEY	(Shop_Name)		REFERENCES Shops(Shop_Name)
	ON DELETE CASCADE
	ON UPDATE CASCADE
);


CREATE TABLE Price_History(
	Product_Name	varchar(50),
	Shop_Name		varchar(50),
	Start_Date		datetime		NOT NULL,
	PRIMARY KEY (Product_Name, Shop_Name, Start_Date),
	End_Date		datetime		NULL,
	CONSTRAINT	CK_END_START	CHECK (End_Date > Start_Date),
	Price			float			NOT NULL	CHECK (Price >= 0),
	FOREIGN KEY	(Product_Name)	REFERENCES Products(Product_Name)
	ON UPDATE CASCADE,
	FOREIGN KEY	(Shop_Name)		REFERENCES Shops(Shop_Name)
	ON UPDATE CASCADE
);


CREATE TABLE Orders(
	Order_ID			int				PRIMARY KEY		CHECK (Order_ID > 0),
	User_ID				int				NOT NULL		CHECK (User_ID > 0),
	Date_Time			datetime		NOT NULL,	
	Shipping_Address	varchar(200)	NOT NULL,
	Total_Shipping_Cost	float			NOT NULL		CHECK (Total_Shipping_Cost > 0),
	FOREIGN KEY	(User_ID)	REFERENCES Users(User_ID)
	ON UPDATE CASCADE
);


CREATE TABLE Products_in_Order(
	Product_Name	varchar(50),
	Shop_Name		varchar(50),
	Order_ID		int,
	PRIMARY KEY (Product_Name, Shop_Name, Order_ID),
	Status			varchar(50)		NOT NULL	CHECK (Status NOT LIKE '%[^a-Z]%'),
	Quantity		int				NOT NULL	CHECK (Quantity > 0),
	Price			float			NOT NULL	CHECK (Price > 0),
	Delivery_Date	datetime		NULL,
	FOREIGN KEY (Product_Name)	REFERENCES Products(Product_Name)
	ON UPDATE CASCADE,
	FOREIGN KEY (Shop_Name)		REFERENCES Shops(Shop_Name)
	ON UPDATE CASCADE,
	FOREIGN KEY (Order_ID)		REFERENCES Orders(Order_ID)
	ON DELETE CASCADE
	ON UPDATE CASCADE
);

-- Function
CREATE FUNCTION [dbo].[orderFunction] (
    @orderid int
)
RETURNS VARCHAR(5)
AS
BEGIN
    IF EXISTS (SELECT	O.Order_ID
				FROM	Products_in_Shop p, Products_in_Order O, Orders orders
				WHERE	p.Product_Name = o.Product_Name AND
						p.Shop_Name = o.Shop_Name AND
						orders.Order_ID = o.Order_ID AND
						o.Order_ID = @orderid AND
						Stock = 0)
        return 'True'
    return 'False'
END;

-- User cannot make order of products with 0 quantity
ALTER TABLE Products_in_Order
ADD CONSTRAINT Quantity_Con
CHECK ([dbo].[OrderFunction]([order_id])='False')


CREATE TABLE Rate(
	Product_Name	varchar(50),
	Shop_Name		varchar(50),
	Order_ID		int						CHECK (Order_ID > 0),
	PRIMARY KEY (Product_Name, Shop_Name, Order_ID),
	Date_Time		datetime	NOT NULL,
	Rating			int			NULL		CHECK (Rating > 0 AND Rating <= 5),
	FOREIGN KEY (Product_Name)	REFERENCES Products(Product_Name)
	ON DELETE CASCADE
	ON UPDATE CASCADE,
	FOREIGN KEY (Shop_Name)		REFERENCES Shops(Shop_Name)
	ON UPDATE CASCADE,
	FOREIGN KEY (Order_ID)		REFERENCES Orders(Order_ID)
	ON UPDATE CASCADE
);


CREATE TABLE Comments(
	Comment_ID	int				PRIMARY KEY		CHECK (Comment_ID > 0),
	User_ID		int				NOT NULL,
	Text		VARCHAR(1000)	NOT NULL,
	FOREIGN KEY (User_ID)		REFERENCES Users(User_ID)
	ON DELETE CASCADE
	ON UPDATE CASCADE
);

-- Function
CREATE FUNCTION [dbo].[myFunction] (
    @orderid int
)
RETURNS VARCHAR(5)
AS
BEGIN
    IF EXISTS (SELECT	Products_in_Order.order_id
				FROM	Products_in_Order
				WHERE	Products_in_Order.order_id = @orderid AND
						Products_in_Order.status NOT IN('Delivered','Returned'))
        return 'True'
    return 'False'
END;

-- Orders that have been rated must be either delivered or returned
ALTER TABLE Rate
ADD CONSTRAINT Rate_chk
CHECK ([dbo].[myFunction]([order_id])='False')



CREATE TABLE Comments_on_Comment(
	Comments_on_Comment_ID	int	PRIMARY KEY,
	Reference_Comment_ID	int NOT NULL,
	FOREIGN KEY (Reference_Comment_ID)	REFERENCES Comments(Comment_ID)
	ON DELETE CASCADE
	ON UPDATE CASCADE
);


CREATE TABLE Comments_on_Product(
	Comments_on_Product_ID	int			PRIMARY KEY	CHECK (Comments_on_Product_ID > 0),
	Product_Name			varchar(50)	NOT NULL,
	Shop_Name				varchar(50)	NOT NULL,
	Order_ID				int			NOT NULL	CHECK (Order_ID > 0),
	FOREIGN KEY (Product_Name)	REFERENCES Products(Product_Name)
	ON DELETE CASCADE
	ON UPDATE CASCADE,
	FOREIGN KEY (Shop_Name)		REFERENCES Shops(Shop_Name)
	ON DELETE CASCADE
	ON UPDATE CASCADE,
	FOREIGN KEY (Order_ID)		REFERENCES Orders(Order_ID)
	ON DELETE CASCADE
	ON UPDATE CASCADE
);


CREATE TABLE Complaints(
	Complaint_ID		int				PRIMARY KEY	CHECK (Complaint_ID > 0),
	User_ID				int				NOT NULL	CHECK (User_ID > 0),
	Filling_Date_Time	datetime		NOT NULL,
	Text				varchar(1000)	NOT NULL,
	Status				varchar(50)		NOT NULL	CHECK (Status NOT LIKE '%[^a-Z]%'),
	Employee_ID			int				NULL		CHECK (Employee_ID > 0),
	Addressed_Date_Time	datetime		NULL,
	CONSTRAINT	CK_ADDRESSED_FILLING	CHECK (Addressed_Date_Time > Filling_Date_Time),
	FOREIGN KEY (User_ID)		REFERENCES Users(User_ID)
	ON DELETE CASCADE
	ON UPDATE CASCADE,
	FOREIGN KEY (Employee_ID)	REFERENCES Employees(Employee_ID)
	ON DELETE CASCADE
	ON UPDATE CASCADE
);

-- Complaint filling in date and time must be later than addressed date and time
CHECK([Addressed_Date_Time]>[Filling_Date_Time])

-- When user keys in "Addressed" for Status of one particular record in Complaint table,
-- the Addressed_Date_Time 
ALTER Trigger [dbo].[dateset]
on [dbo].[Complaints]
after Update
As
UPDATE [dbo].[Complaints] 
SET Addressed_Date_Time = GETDATE()
where Status='Addressed';


CREATE TABLE Complaints_on_Comment(
	Complaint_ID	int	PRIMARY KEY	CHECK (Complaint_ID > 0),
	Comment_ID		int	NOT NULL	CHECK (Comment_ID > 0),
	FOREIGN KEY (Comment_ID) REFERENCES Comments(Comment_ID)
	ON DELETE CASCADE
	ON UPDATE CASCADE
);


CREATE TABLE Complaints_on_Product(
	Complaint_ID	int			PRIMARY KEY	CHECK (Complaint_ID > 0),
	Product_Name	varchar(50)	NOT NULL,
	FOREIGN KEY (Product_Name) REFERENCES Products(Product_Name)
	ON DELETE CASCADE
	ON UPDATE CASCADE
);


CREATE TABLE Complaints_on_Shop(
	Complaint_ID	int			PRIMARY KEY	CHECK (Complaint_ID > 0),
	Shop_Name		varchar(50)	NOT NULL,
	FOREIGN KEY (Shop_Name) REFERENCES Shops(Shop_Name)
	ON DELETE CASCADE
	ON UPDATE CASCADE
);

--Constraint-- User who made the complaint must have shoped in the complainted shop
CHECK([dbo].[ComplaintFunction]([Complaint_ID],[Shop_Name])=(1))

--Function--  ComplaintFunction
CREATE FUNCTION [dbo].[ComplaintFunction] (
    @id int,
	@shop varchar(50)
)
RETURNS int
AS
BEGIN
    IF EXISTS (SELECT DISTINCT	Shop_Name
				FROM			Products_in_Order p, Orders o, Complaints c
				WHERE			o.Order_ID = p.Order_ID AND
								o.User_ID = c.User_ID AND
								c.Complaint_ID = @id AND
								Shop_Name = @shop)
		return 1
	return 0
END;