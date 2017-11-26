-- Complaint status changes from 'BeingHandled' to 'Pending' when the Employee in
-- charged is removed from Employee table
CREATE TRIGGER [dbo].[employee_deletion]
ON [dbo].[Employees]
AFTER DELETE
AS
	UPDATE	complaints
	SET		Status='Pending'
	WHERE	Status='BeingHandled' AND Employee_ID IS NULL;
	 
-- When a user makes an order of a product, the stock of that product decreases by 1.
-- This is for stock count.
CREATE TRIGGER [dbo].[stock]
ON [dbo].[Products_in_Order]
FOR INSERT
AS
	DECLARE	@product_name varchar(100);
	DECLARE	@shop_name varchar(100);

	SELECT @product_name=product_name FROM inserted i;
	SELECT @shop_name=shop_name FROM inserted i;

	UPDATE	Products_in_Shop
	SET		Stock = Stock - 1
	WHERE	product_name = @product_name AND Shop_Name = @shop_name;

	
-- When user returns a product, the stock of the product increases by 1.
-- This is for stock count.
CREATE TRIGGER [dbo].[Refund_modify]
ON [dbo].[Products_in_Order]
FOR UPDATE
AS
	DECLARE @product_name varchar(100);
	DECLARE @shop_name varchar(100);
	DECLARE @status varchar(100);

	SELECT @product_name = product_name FROM inserted i;
	SELECT @shop_name = shop_name FROM inserted i;
	SELECT @status = status FROM inserted i;

	UPDATE Products_in_Shop
	SET Stock = Stock + 1
	WHERE product_name=@product_name AND Shop_Name=@shop_name AND @status='Returned';
