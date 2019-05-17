package com.example.aizat.travelook_v1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aizat.travelook_v1.FavouriteModel.FavouriteAttr;
import com.example.aizat.travelook_v1.PlacesModel.Photos;
import com.example.aizat.travelook_v1.PlacesModel.PlaceDetail;
import com.example.aizat.travelook_v1.Remote.IGoogleAPIService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ViewPlaces extends AppCompatActivity {

    ImageView photo;
    RatingBar ratingBar;
    TextView place_open_hours,place_address,place_name,no_phone,website;
    Button show_map,show_direction,call,openWebsite,favPlace;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    String Uid;

    IGoogleAPIService mService;

    PlaceDetail mPlace;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_places);

        mService = Common.getGoogleAPIService();


        photo = (ImageView) findViewById(R.id.photo);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        place_open_hours = (TextView) findViewById(R.id.place_open_hours);
        place_address = (TextView) findViewById(R.id.place_address);
        place_name = (TextView) findViewById(R.id.place_name);
        no_phone = (TextView) findViewById(R.id.no_phone);
        website = (TextView) findViewById(R.id.website);
        show_map = (Button)findViewById(R.id.show_map);
        call = (Button) findViewById(R.id.call);
        openWebsite = (Button) findViewById(R.id.openWebsite);
        favPlace = (Button) findViewById(R.id.favPlace);
        //show_direction =(Button) findViewById(R.id.show_direction);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        Uid = firebaseUser.getUid().toString();

        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Favorite");

        //empty all view
        no_phone.setText("");
        place_name.setText("");
        place_address.setText("");
        place_open_hours.setText("");

        show_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mPlace.getResult().getUrl()));
                startActivity(mapIntent);

            }
        });


        favPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String placeid = mPlace.getResult().getId();
                final String url = mPlace.getResult().getUrl();
                final String name = mPlace.getResult().getName();
                final String addr = mPlace.getResult().getFormatted_address();
                String phoneNum = mPlace.getResult().getFormatted_phone_number();
                final String phone;
                if (phoneNum != null){
                    phone = phoneNum;
                }else {
                    phone = "Phone number not available";
                }
                String website = mPlace.getResult().getWebsite();

                final String fWebsite;
                if (website != null){
                    fWebsite = website;
                }else {
                    fWebsite = "Website not available";
                }

                final String id = UUID.randomUUID().toString();
                final String cuid = Uid;
                final String favid = id;

                FavouriteAttr favouriteAttr = new FavouriteAttr(placeid,url,name,addr,phone,fWebsite,favid,cuid);
                databaseReference.child(id).setValue(favouriteAttr);
                Toast.makeText(ViewPlaces.this, "Places added to favorite places", Toast.LENGTH_SHORT).show();

            }
        });



        /*show_direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mapIntent = new Intent(ViewPlaces.this, ViewDirections.class);
                startActivity(mapIntent);

            }
        });*/
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                if (mPlace.getResult().getFormatted_phone_number() == null){
                    Toast.makeText(ViewPlaces.this,"Phone number not available",Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    callIntent.setData(Uri.parse("tel:"+mPlace.getResult().getFormatted_phone_number()));
                }

                if (ActivityCompat.checkSelfPermission(ViewPlaces.this, Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED){
                    return;
                }
                startActivity(callIntent);
            }
        });

        openWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mPlace.getResult().getWebsite() == null){
                    Toast.makeText(ViewPlaces.this,"Website not available",Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    Intent websiteIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mPlace.getResult().getWebsite()));
                    startActivity(websiteIntent);
                }
            }
        });
        //Photo
        if (Common.currentResult.getPhotos() != null && Common.currentResult.getPhotos().length > 0)
        {
            Picasso.get()
                    .load(getPhotoOfPlace(Common.currentResult.getPhotos()[0].getPhoto_reference(),1000))
                    .placeholder(R.drawable.noimagea)
                    .error(R.drawable.error)
                    .into(photo);
        }
        //Rating
        if (Common.currentResult.getRating() != null && !TextUtils.isEmpty(Common.currentResult.getRating()))
        {
            ratingBar.setRating(Float.parseFloat(Common.currentResult.getRating()));
        }
        else
        {
            ratingBar.setVisibility(View.GONE);
        }

        //opening hours
        if (Common.currentResult.getOpening_hours() != null)
        {
            place_open_hours.setText("Open Now: Yes");
        }
        else
        {
            place_open_hours.setText("Open Now: No");
        }


        //user service to fetch name and address
        mService.getDetailPlace(getPlaceDetailUrl(Common.currentResult.getPlace_id()))
                .enqueue(new Callback<PlaceDetail>() {
                    @Override
                    public void onResponse(Call<PlaceDetail> call, Response<PlaceDetail> response) {
                        mPlace = response.body();

                        place_address.setText(mPlace.getResult().getFormatted_address());
                        place_name.setText(mPlace.getResult().getName());
                        if(mPlace.getResult().getFormatted_phone_number() == null){
                            no_phone.setText("Phone Number: Not Available");

                        }
                        else {
                            no_phone.setText("Phone Number: "+ mPlace.getResult().getFormatted_phone_number());
                        }
                        if(mPlace.getResult().getWebsite() == null){
                            website.setText("Website: Not Available");
                        }
                        else{
                            website.setText("Website: " + mPlace.getResult().getWebsite());
                        }

                    }

                    @Override
                    public void onFailure(Call<PlaceDetail> call, Throwable t) {

                    }



                });

    }

    private String getPlaceDetailUrl(String place_id) {
        StringBuilder url = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json");
        url.append("?placeid="+ place_id);
        url.append("&key="+getResources().getString(R.string.browser_key));
        return url.toString();
    }

    private String getPhotoOfPlace(String photos_reference, int maxWidth) {
        StringBuilder url = new StringBuilder("https://maps.googleapis.com/maps/api/place/photo");
        url.append("?maxwidth="+maxWidth);
        url.append("&photoreference="+photos_reference);
        url.append("&key="+getResources().getString(R.string.browser_key));
        return url.toString();
    }
}
