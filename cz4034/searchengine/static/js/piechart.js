function DisplayPieChart(category) {
    category = category.replace(/&#39;/g, '"');
    json = JSON.parse(category);

    google.charts.load('current', {'packages':['corechart']});
    google.charts.setOnLoadCallback(drawChart);

    function drawChart() {
        var data = google.visualization.arrayToDataTable([
          ['Category', 'Number of Tweets'],
          ['Economy', json.Economy],
          ['Entertainment', json.Entertainment],
          ['Health', json.Health],
          ['Politics', json.Politics],
          ['Sports', json.Sports],
          ['Technology', json.Technology],
        ]);

        var options = {
          title: "Tweet Categories"
        };

        var chart = new google.visualization.PieChart(document.getElementById('piechart'));

        chart.draw(data, options);
    }
}
