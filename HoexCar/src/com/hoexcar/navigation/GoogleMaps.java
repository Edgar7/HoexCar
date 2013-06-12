package com.hoexcar.navigation;

import android.app.Activity;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hoexcar.R;


public class GoogleMaps extends Activity implements LocationListener {
	
	private String latituteField = "51.766681";
	private String longitudeField = "9.369975";
	private LocationManager locationManager;
	private String provider;
	
	private LatLng AktuellerStandort;
	static final LatLng Hoexter = new LatLng(51.766681,9.369975);
    private GoogleMap map;
    private Marker aktuellePosition;
    MarkerOptions aktuellePositionMarker;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.google_maps);

		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
		        .getMap();
		
		// GMap Icon für die Zentrale
		    Marker hoexter = map.addMarker(new MarkerOptions()
		        .position(Hoexter)
		        .title("Höxter")
		        .snippet("CarSharing Zentrale")
		        .icon(BitmapDescriptorFactory
		            .fromResource(R.drawable.ic_house)));
		    
		    // GMap Icon für die aktuelle Position. 
		    aktuellePositionMarker = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_zentrale));

		    // Move the camera instantly to hamburg with a zoom of 15.
		    map.moveCamera(CameraUpdateFactory.newLatLngZoom(Hoexter, 15));
		    // Zoom in, animating the camera.
		    map.animateCamera(CameraUpdateFactory.zoomTo(16), 300, null);
			
		    pruefeGPS();  
		    
		    // locationManager initialisieren
		    locationManager = (LocationManager) getSystemService(this.LOCATION_SERVICE);
		    // Default Kriterium wie der Location Manager empfangen soll.(GPS, Netzwerk..)
		    Criteria criteria = new Criteria();
		    provider = locationManager.getBestProvider(criteria, false);
		    Location location = locationManager.getLastKnownLocation(provider);
		    // Die Textfelder für die Standort-Werte initialisieren.
		    if (location != null) {
		      System.out.println("Provider " + provider + " wurde ausgewählt.");
		      onLocationChanged(location);
		    }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/*
	 * Hier werden die Buttons ausgewertet.
	 */
	public void onClick(View view) {
		switch (view.getId()) {
			// Standort auf der Map anzeigen
			case R.id.bStandortAktuell:
				// Falls Icon schon gesetzt, entfernen da sonst doppelte Icons auf der Map.
				if (aktuellePositionMarker.getPosition() != null) {
					aktuellePosition.remove();
				}
				double doubleLatitute = Double.valueOf(latituteField);
				double doubleLongitude = Double.valueOf(longitudeField);
				AktuellerStandort = new LatLng(doubleLatitute, doubleLongitude);
				aktuellePositionMarker.position(AktuellerStandort);
				aktuellePosition = map.addMarker(aktuellePositionMarker);
				map.moveCamera(CameraUpdateFactory.newLatLngZoom(AktuellerStandort, 15));
				map.animateCamera(CameraUpdateFactory.zoomTo(12), 2000, null);
				break;
			// Standort der "Zentrale" anzeigen
			case R.id.bStandortZentrale:
				aktuellePosition.remove();
				 // Move the camera instantly to hamburg with a zoom of 15.
			    map.moveCamera(CameraUpdateFactory.newLatLngZoom(Hoexter, 15));
			    // Zoom in, animating the camera.
			    map.animateCamera(CameraUpdateFactory.zoomTo(16), 2000, null);
				break;
		}
	}
	
	/*
	 * Überprüfen ob GPS aktiv ist. Gegebenenfalls Zentrale benachrichtigen.
	 */
	public void pruefeGPS() {
		LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
		boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
	
		if (!enabled) {
			Toast toast = Toast.makeText(this, "GPS-Modul ist deaktiviert. Zentrale wird benachrichtigt.", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
			toast.show();
		} 
	}

	/* Request updates at startup */
	@Override
	protected void onResume() {
	    super.onResume();
	    locationManager.requestLocationUpdates(provider, 400, 1, this);
	}
	
	/* Remove the locationlistener updates when Activity is paused */
	@Override
	protected void onPause() {
	    super.onPause();
	    locationManager.removeUpdates(this);
	}
	
	@Override
	public void onLocationChanged(Location location) {
		 int lat = (int) (location.getLatitude());
		 int lng = (int) (location.getLongitude());
		 latituteField = String.valueOf(lat);
		 longitudeField = String.valueOf(lng);
		 Toast.makeText(this, "Location ist latitude: " + latituteField + " und longitude: " + longitudeField + ".", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onProviderDisabled(String provider) {
		Toast.makeText(this, "Provider " + provider + " deaktiviert.", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onProviderEnabled(String provider) {
		Toast.makeText(this, "Neuer Provider " + provider + ".", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
	}	
}
