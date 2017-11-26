<?php
$name = $_POST['name'];

if($_POST['payment'] == "visa")
    $payment = "Visa";
else if($_POST['payment'] == "mastercard")
    $payment = "Mastercard";
else if($_POST['payment'] == "discover")
    $payment = "Discover";

if( (isset($_POST['apples'])) && (isset($_POST['oranges'])) && (isset($_POST['bananas'])))
{
    $apples = $_POST['apples'];
    $oranges = $_POST['oranges'];
    $bananas = $_POST['bananas'];

    $total = number_format((($apples * 0.69) + ($oranges * 0.59) + ($bananas * 0.39)), 2 );

    addQuantities($apples, $oranges, $bananas);

    header("Location: receipt.php?" .
            "name=$name&" .
            "apples=$apples&" .
            "oranges=$oranges&" .
            "bananas=$bananas&" .
            "payment=$payment&" .
            "total=$total");
}
else
    exit;

// Add the new quantity to the existing total quantity in the file
function addQuantities($apples, $oranges, $bananas)
{
    $temp = ReadFromFile();
    $temp["apples"] += $apples;
    $temp["oranges"] += $oranges;
    $temp["bananas"] += $bananas;

    WriteToFile($temp);
}

function ReadFromFile()
{
    $file = fopen("order.txt", "r+") or exit("Unable to open order.txt file!");
    $total_records = array("apples" => 0, "oranges" => 0, "bananas" => 0);

    foreach ($total_records as $fruit => $quantity)
    {
        $line = fgets($file);
        $quantity = explode(":", $line);

        // Assign corresponding value of quantity to the fruit
        $total_records[$fruit] = (int)$quantity[1];
    }

    fclose($file);
    return $total_records;
}

function WriteToFile($total_records)
{
    $file = fopen("order.txt", "w+");

    // Write total of each fruit into file
    foreach ($total_records as $fruit => $quantity)
        fwrite($file, "Total number of " .$fruit. " : " .$quantity. "\r\n");

    fclose($file);
}
?>