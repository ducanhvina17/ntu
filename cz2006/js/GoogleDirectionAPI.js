var ad;
var destlatlng;

function initMap(add) {
    var directionsService = new google.maps.DirectionsService;
    var directionsDisplay = new google.maps.DirectionsRenderer;

    var map = new google.maps.Map(document.getElementById('map'), {
        center: {lat: -33.8688, lng: 151.2195},
        zoom: 13
    });
    var input = document.getElementById('pac-input');

    var types = document.getElementById('type-selector');
    map.controls[google.maps.ControlPosition.TOP_LEFT].push(input);
    map.controls[google.maps.ControlPosition.TOP_LEFT].push(types);

    var autocomplete = new google.maps.places.Autocomplete(input);
    autocomplete.bindTo('bounds', map);

    var infowindow = new google.maps.InfoWindow({map : map});

    var marker = new google.maps.Marker({
        map: map,
        anchorPoint: new google.maps.Point(0, -29)
    });

    var service = new google.maps.places.PlacesService(map);
	var currlatlng; //= new google.maps.LatLng(1.348235, 103.682963); //ntu

     if (navigator.geolocation) {
          navigator.geolocation.getCurrentPosition(function(position) {
            currlatlng = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);

            infowindow.setPosition(currlatlng);
            infowindow.setContent('Location found.');

        var displaySuggestions = function(predictions, status) {
            if (status != google.maps.places.PlacesServiceStatus.OK) {
                return;
        }

          destlatlng = predictions[0].description;
          calculateAndDisplayRoute(directionsService, directionsDisplay, currlatlng, destlatlng, 'DRIVING');
        };


        var service = new google.maps.places.AutocompleteService();
        service.getQueryPredictions({ input: ad }, displaySuggestions);

    directionsDisplay.setMap(map);
          }, function() {
            infowindow.setPosition(currlatlng);
            infowindow.setContent(browserHasGeolocation ?
                              'Error: The Geolocation service failed.' :
                              'Error: Your browser doesn\'t support geolocation.');

          });
        } else {
            // Browser doesn't support Geolocation
            // currlatlng = new google.maps.LatLng(1.348235, 103.682963);
            infowindow.setPosition(currlatlng);
            infowindow.setContent(browserHasGeolocation ?
                              'Error: The Geolocation service failed.' :
                              'Error: Your browser doesn\'t support geolocation.');

        }



    //create the eventlistener
    document.getElementById("DRIVING_TAB").addEventListener("click", function () {
        calculateAndDisplayRoute(directionsService, directionsDisplay, currlatlng, destlatlng, 'DRIVING');
        directionsDisplay.setMap(map);

    });

    document.getElementById("TRANSIT_TAB").addEventListener("click", function () {
        calculateAndDisplayRoute(directionsService, directionsDisplay, currlatlng, destlatlng, 'TRANSIT');
        directionsDisplay.setMap(map);

    });

    document.getElementById("WALKING_TAB").addEventListener("click", function () {
        calculateAndDisplayRoute(directionsService, directionsDisplay, currlatlng, destlatlng, 'WALKING');
        directionsDisplay.setMap(map);

    });

    autocomplete.addListener('place_changed', function () {
        infowindow.close();
        marker.setVisible(false);
        var place = autocomplete.getPlace();
        if (!place.geometry) {
            window.alert("Invalid address format! Please use the autocomplete when specificing the address. ");
            return;
        }

        // If the place has a geometry, then present it on a map.
        if (place.geometry.viewport) {
            map.fitBounds(place.geometry.viewport);
        } else {
            map.setCenter(place.geometry.location);
            map.setZoom(17);
        }
        marker.setIcon(/** @type {google.maps.Icon} */({
            url: place.icon,
            size: new google.maps.Size(71, 71),
            origin: new google.maps.Point(0, 0),
            anchor: new google.maps.Point(17, 34),
            scaledSize: new google.maps.Size(35, 35)
        }));
        marker.setPosition(destlatlng);
        marker.setVisible(true);

        var address = '';
        if (place.address_components) {
            address = [
                (place.address_components[0] && place.address_components[0].short_name || ''),
                (place.address_components[1] && place.address_components[1].short_name || ''),
                (place.address_components[2] && place.address_components[2].short_name || '')
            ].join(' ');
        }

        infowindow.setContent('<div><strong>' + place.name + '</strong><br>' + address);
        infowindow.open(map, marker);

        destlatlng = place.geometry.location;

        //display the default route
        calculateAndDisplayRoute(directionsService, directionsDisplay, currlatlng, destlatlng, 'DRIVING');
        directionsDisplay.setMap(map);
    });


    // Sets a listener on a radio button to change the filter type on Places
    // Autocomplete.
    function setupClickListener(id, types) {
        var radioButton = document.getElementById(id);
        radioButton.addEventListener('click', function () {
            autocomplete.setTypes(types);
        });
    }

    setupClickListener('changetype-all', []);
    setupClickListener('changetype-address', ['address']);
    setupClickListener('changetype-establishment', ['establishment']);
    setupClickListener('changetype-geocode', ['geocode']);

}

//calculate and display the route
function calculateAndDisplayRoute(directionsService, directionsDisplay, start, end, type) {

    directionsService.route({
        origin: start,
        destination: end,
        travelMode: type
    }, function (response, status) {
        if (status === 'OK') {
            directionsDisplay.setDirections(response);
            //alert("testing");
        } else {
            window.alert('Directions request failed due to ' + status);
        }
    });
    var e = type + '_INSTR';
    directionsDisplay.setPanel(document.getElementById(e));

}

function SetAdd(add)
{
	ad = add;
}