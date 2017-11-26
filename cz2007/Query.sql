-- Query 1
SELECT	Product_Name AS Product, CAST(AVG(Price)as decimal(10,2)) AS AveragePrice
FROM	Price_History
WHERE	((Start_Date between '2016-07-01' and '2016-07-31') OR (End_Date between '2016-07-01' and '2016-07-31'))
		and Product_Name = 'iPhone 6s'
GROUP BY Product_Name;

-- Query 2
WITH GoodProducts (GoodProduct,NumberOf5StarsRating) AS (
	SELECT 	Product_Name, COUNT(*) AS NumberOf5StarsRating
	FROM 		Rate
	WHERE 		Rating='5'
	GROUP BY	Product_Name
	HAVING		COUNT(*) >= 10)
SELECT		Product_Name,
			AVG(CAST (Rating AS decimal)) AS AverageRating
FROM		Rate,GoodProducts
WHERE		Product_Name = GoodProduct
GROUP BY	Product_Name
ORDER BY	AverageRating ASC;

-- Query 3
SELECT	CAST(AVG(DATEDIFF(DAY, Date_Time, Delivery_Date))AS decimal(10, 2)) AS AverageDeliveryTimeForJune
FROM	Products_in_Order P, Orders O
WHERE	P.Order_ID = O.Order_ID AND
		Status = 'Delivered' AND
		Month(Date_Time) = '6';

		
-- Query 4
SELECT		Employee_ID,
			CAST(AVG(DATEDIFF(DAY, Filling_Date_Time, Addressed_Date_Time)) AS INT) AS Latency
FROM		Complaints
WHERE		Status='Solved'
GROUP BY	Employee_ID
ORDER BY	Latency DESC;


-- Query 5
WITH ProductsAvailableInNumShops (Product_Name) AS (
SELECT Product_Name
FROM Products
WHERE Maker = 'Samsung')
SELECT		PinShop.Product_Name,
			COUNT(Shop_Name) AS NumOfShops
FROM		ProductsAvailableInNumShops P, Products_in_Shop PinShop
WHERE		P.Product_Name=PinShop.Product_Name
GROUP BY	PinShop.Product_Name;
