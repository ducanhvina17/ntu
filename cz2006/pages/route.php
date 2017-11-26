<?php include_once('header.php') ?>
<?php echo '<script src="../js/GoogleDirectionAPI.js"></script>'; ?>
<?php echo '<script src="../js/tab.js"></script>';?>
<script>
SetAdd("<?php echo $_SESSION['add'] ?>");
</script>
<!-- Begin page content -->
<link href="../css/btn.css" rel="stylesheet">
<div class="container">
    <div class="row">
        <div class="col-xs-12 text-center">
            <input id="pac-input" class="controls" type="text" placeholder="Enter a location">

            <div id="type-selector" class="controls">
                <input type="radio" name="type" id="changetype-all" checked="checked">
                <label for="changetype-all">All</label>

                <input type="radio" name="type" id="changetype-establishment">
                <label for="changetype-establishment">Establishments</label>

                <input type="radio" name="type" id="changetype-address">
                <label for="changetype-address">Addresses</label>

                <input type="radio" name="type" id="changetype-geocode">
                <label for="changetype-geocode">Geocodes</label>
            </div>

            <div id="map"></div>
            <br>
            <ul class="nav nav-tabs" id="product-table" style="background-color: white">
                <li class="active"><a href="#1" data-toggle="tab" aria-expanded="true" onclick="transitMode('DRIVING')" id="DRIVING_TAB">Driving</a>
                </li>
                <li><a href="#2" data-toggle="tab" onclick="transitMode('TRANSIT')" id="TRANSIT_TAB">Transit</a>
                </li>
                <li><a href="#3" data-toggle="tab" onclick="transitMode('WALKING')" id="WALKING_TAB">Walking</a>
                </li>
            </ul>
            <div style="background-color: white">
            <div align="left">
                <div id="DRIVING">
                    <p id="DRIVING_INSTR"></p>
                </div>

                <div id="TRANSIT" class="w3-container mode">
                    <p id="TRANSIT_INSTR"></p>
                </div>

                <div id="WALKING" class="w3-container mode">
                    <p id="WALKING_INSTR"></p>
                </div>
            </div>
            </div>
            <div class="btn-group pull-right">
                <div class="col-xs-6">
                    <button style="width: 220px" class="btn btn-primary" type="submit">Add to favourite list</button>
                </div>
                <div class="col-xs-6">
                    <button style="width: 220px" class="btn btn-primary" type="submit">Add to comparison list</button>
                </div>
            </div>
			<input type="hidden" id="name" value="<?php echo $_POST["name"]; ?>" />
            <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAX0TqO4Me09jXw9XGltmQzntSXZKVJ3UE&libraries=places&callback=initMap"
            async defer></script>
        </div>
    </div>
</div>
<?php include_once('../footer.php') ?>