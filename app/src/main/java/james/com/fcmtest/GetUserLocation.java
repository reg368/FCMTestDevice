package james.com.fcmtest;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by R30 on 2015/11/30.
 */
public class GetUserLocation  implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private GoogleApiClient googleApiClient;
    private Location myLoc;
    private Context context;
    private LatLng myLatLng;

    private static GetUserLocation mylocation;
    public static GetUserLocation getMylocation(Context context){
        if(mylocation == null){
            mylocation = new GetUserLocation(context);
        }
        return mylocation;
    };

    public GetUserLocation(Context context){
        this.context = context;
        initGoogleApiClient();
    }

    public GetUserLocation(){}

    public void initGoogleApiClient(){
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(context)
                    .addApi(LocationServices.API).addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();
        }
        googleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        try{
            myLoc = LocationServices.FusedLocationApi
                    .getLastLocation(googleApiClient);
            LocationRequest locationRequest = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(10000).setSmallestDisplacement(1000);
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    googleApiClient, locationRequest, this);
            if(myLoc != null)
                myLatLng  = new LatLng(myLoc.getLatitude(),myLoc.getLongitude());
        }catch (SecurityException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    public LatLng getMyLatLng() {
        return myLatLng;
    }

    public void setMyLatLng(LatLng myLatLng) {
        this.myLatLng = myLatLng;
    }

}
