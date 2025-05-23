package ru.mirea.chirkans.mireaproject.ui;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.google.gson.Gson;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.HashSet;
import java.util.Set;

import ru.mirea.chirkans.mireaproject.R;

public class PlacesFragment extends Fragment {
    private MapView mapView;
    private Context context;
    private static final String PREFS_NAME = "SavedPlaces";
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_places, container, false);
        mapView = view.findViewById(R.id.mapView);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);

        IMapController mapController = mapView.getController();
        mapController.setZoom(15.0);
        GeoPoint startPoint = new GeoPoint(55.751574, 37.573856); // Центр Москвы
        mapController.setCenter(startPoint);

        checkPermissions();
        addMarkers();

        mapView.getOverlays().add(new MapEventsOverlay(new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                return false;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                showAddPlaceDialog(p);
                return true;
            }
        }));

        loadSavedPlaces();
        return view;

    }

    private void showAddPlaceDialog(GeoPoint point) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_add_place, null);

        EditText etTitle = dialogView.findViewById(R.id.et_title);
        EditText etDescription = dialogView.findViewById(R.id.et_description);

        builder.setView(dialogView)
                .setTitle("Добавить место")
                .setPositiveButton("Сохранить", (dialog, which) -> {
                    String title = etTitle.getText().toString();
                    String description = etDescription.getText().toString();
                    if (!title.isEmpty()) {
                        savePlace(point, title, description);
                        addMarker(point, title, description);
                    }
                })
                .setNegativeButton("Отмена", null)
                .show();
    }

    private void savePlace(GeoPoint point, String title, String description) {
        Gson gson = new Gson();
        Set<String> savedPlaces = sharedPreferences.getStringSet("places", new HashSet<>());

        Place newPlace = new Place(
                point.getLatitude(),
                point.getLongitude(),
                title,
                description
        );

        savedPlaces.add(gson.toJson(newPlace));
        sharedPreferences.edit().putStringSet("places", savedPlaces).apply();
    }

    private void loadSavedPlaces() {
        Set<String> savedPlaces = sharedPreferences.getStringSet("places", new HashSet<>());
        Gson gson = new Gson();

        for (String json : savedPlaces) {
            Place place = gson.fromJson(json, Place.class);
            addMarker(new GeoPoint(place.lat, place.lon), place.title, place.description);
        }
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            enableMyLocation();
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
    }

    private static class Place {
        double lat;
        double lon;
        String title;
        String description;

        Place(double lat, double lon, String title, String description) {
            this.lat = lat;
            this.lon = lon;
            this.title = title;
            this.description = description;
        }
    }

    private void enableMyLocation() {
        MyLocationNewOverlay locationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(context), mapView);
        locationOverlay.enableMyLocation();
        mapView.getOverlays().add(locationOverlay);
    }

    private void addMarkers() {
        addMarker(new GeoPoint(55.760459, 37.618716), "Большой театр", "Исторический театр");
        addMarker(new GeoPoint(55.752023, 37.617499), "Красная площадь", "Главная площадь Москвы");
    }

    private void addMarker(GeoPoint point, String title, String description) {
        Marker marker = new Marker(mapView);
        marker.setPosition(point);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker.setTitle(title);
        marker.setSnippet(description);
        marker.setOnMarkerClickListener((m, mv) -> {
            Toast.makeText(context, title + ": " + description, Toast.LENGTH_SHORT).show();
            return true;
        });
        mapView.getOverlays().add(marker);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            enableMyLocation();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }
}