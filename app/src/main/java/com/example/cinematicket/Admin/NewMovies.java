package com.example.cinematicket.Admin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.example.cinematicket.FcmRetrofitDAO;
import com.example.cinematicket.Model.FCMModel;
import com.example.cinematicket.Model.MovieModel;
import com.example.cinematicket.Model.NotificationModel;
import com.example.cinematicket.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewMovies extends AppCompatActivity {
ImageView imageView;
EditText title, releasedate, summary, language,genre;
TextView post,firstshow,secondshow,thirdshow;
String titleString, releaseString, summaryString ,titles,subject,genreString,langString,first,second,third;
ProgressDialog progressDialog;
Uri filepath;
Bitmap bitmap;
List<String> registration_ids;
private FcmRetrofitDAO retrofitDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_movies);

        imageView = findViewById(R.id.pflimage);
        title = findViewById(R.id.ettitle);
        releasedate = findViewById(R.id.etdate);
        summary = findViewById(R.id.etdesc);
        language = findViewById(R.id.etlang);
        genre = findViewById(R.id.etgenre);
        firstshow = findViewById(R.id.firstShow);
        secondshow = findViewById(R.id.secondshow);
        thirdshow = findViewById(R.id.thirdShow);
        post = findViewById(R.id.postMovie);
        progressDialog = new ProgressDialog(NewMovies.this);
        progressDialog.setMessage("Saving...Please Wait.");
        retrofitDAO = FcmRetrofitDAO.RETROFIT.create(FcmRetrofitDAO.class);

        //getting image from gallery
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                startActivityForResult(Intent.createChooser(intent, "Select Image File"), 1);

            }
        });


        firstshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar calendar = Calendar.getInstance();
                int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                int currentMinute = calendar.get(Calendar.MINUTE);







                TimePickerDialog timePickerDialog = new TimePickerDialog(NewMovies.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if(hourOfDay>=0 && hourOfDay<12){
                            first = hourOfDay + " : " + minutes+ " AM";
                        } else {
                            if(hourOfDay == 12){
                                first = hourOfDay + " : " + minutes + " PM";
                            } else{
                                hourOfDay = hourOfDay -12;
                                first = hourOfDay + " : " + minutes + " PM";
                            }
                        }

                        firstshow.setText(first);
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();
            }
        });
        secondshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar calendar = Calendar.getInstance();
                int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                int currentMinute = calendar.get(Calendar.MINUTE);







                TimePickerDialog timePickerDialog = new TimePickerDialog(NewMovies.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if(hourOfDay>=0 && hourOfDay<12){
                            second = hourOfDay + " : " + minutes+ " AM";
                        } else {
                            if(hourOfDay == 12){
                                second = hourOfDay + " : " + minutes + " PM";
                            } else{
                                hourOfDay = hourOfDay -12;
                                second = hourOfDay + " : " + minutes + " PM";
                            }
                        }

                        secondshow.setText(second);
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();
            }
        });
        thirdshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar calendar = Calendar.getInstance();
                int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                int currentMinute = calendar.get(Calendar.MINUTE);







                TimePickerDialog timePickerDialog = new TimePickerDialog(NewMovies.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if(hourOfDay>=0 && hourOfDay<12){
                            third = hourOfDay + " : " + minutes+ " AM";
                        } else {
                            if(hourOfDay == 12){
                                third = hourOfDay + " : " + minutes + " PM";
                            } else{
                                hourOfDay = hourOfDay -12;
                                third = hourOfDay + " : " + minutes + " PM";
                            }
                        }

                        thirdshow.setText(third);
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();
            }
        });




        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                titleString = title.getText().toString();
                releaseString = releasedate.getText().toString();
                summaryString = summary.getText().toString();
                langString = language.getText().toString();
                genreString = genre.getText().toString();


                if (TextUtils.isEmpty(titleString)){
                    title.setError("Name is Required");
                    return;
                }

                else if (TextUtils.isEmpty(releaseString)){
                    releasedate.setError("Date is Required");
                    return;
                }


                else if (TextUtils.isEmpty(summaryString)) {
                    summary.setError("Summary is Required");
                    return;

                }else if (TextUtils.isEmpty(genreString)) {
                    genre.setError("Genre is Required");
                    return;

                }else if (TextUtils.isEmpty(langString)) {
                    language.setError("Language is Required");
                    return;
                }


                    //gettting firebase storage instance to store images
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    final StorageReference uploader = storage.getReference("Movies")
                            .child(filepath.getLastPathSegment());

                    uploader.putFile(filepath)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {


                                            FirebaseDatabase db = FirebaseDatabase.getInstance();
                                            DatabaseReference root = db.getReference("Movies");
                                            String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                                            MovieModel movieModel = new MovieModel(
                                                    titleString,summaryString,releaseString,uri.toString(),langString,genreString,first,second,third,currentTime.replace(":",""),filepath.getLastPathSegment());

                                            //creating directory name as Hall Name
                                            root.child(currentTime.replace(":","")).setValue(movieModel);

                                            // root.push().setValue(registrationModel);


                                            title.setText("");
                                            releasedate.setText("");
                                            summary.setText("");
                                            genre.setText("");
                                            language.setText("");
                                            firstshow.setText("");
                                            secondshow.setText("");
                                            thirdshow.setText("");
                                            imageView.setImageResource(R.drawable.camera);
                                            sendNotification();
                                            progressDialog.dismiss();
                                            Toast.makeText(getApplicationContext(), "Movie Added Successfully", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            })
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    float percent = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                    progressDialog.setMessage("Uploaded :" + (int) percent + " %");
                                }
                            });

            }
        });


    }

    private void sendNotification() {
        titles = this.title.getText().toString().trim();

        titles = titleString+" Alert";
        subject = this.title.getText().toString().trim();
        subject = titleString+ " is out now Hurry up And Book Your Tickets Now!";
        FirebaseFirestore.getInstance().collection("tokens")
                .limit(1)
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots!=null && queryDocumentSnapshots.getDocuments().size()>0) {
                        registration_ids = new ArrayList<>();

                        for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                           // String ids = snapshot.getString("fcm_token");
                            registration_ids.add(snapshot.getString("fcm_token"));
                            Log.d("TAGids", "sendNotification: " + snapshot.getString("fcm_token"));

                            FCMModel fcmModel = new FCMModel(registration_ids, new NotificationModel(titles, subject));
                            Call<Void> voidCall = retrofitDAO.sendNotification(getHeaders(), fcmModel);
                            voidCall.enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {

                                    if (response.isSuccessful())
//                                    Toast.makeText(getApplicationContext(),
//                                                "Notification Sent!",Toast.LENGTH_SHORT)
//                                        .show();
                                        Log.d("TAGfcmtok", "onResponse: " + "Sent");
                                }

                                @Override
                                public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
//                                Toast.makeText(getApplicationContext()
//                                        ,"Failed to send notification!",
//                                        Toast.LENGTH_SHORT).show();
                                    Log.d("TAGfcmtok", "onResponse: " + "Failed");
                                }
                            });
                        }
                    }
                })
                .addOnFailureListener(e -> Log.d("errorss", "sendNotification: "+e.getMessage()));
    }

    private HashMap<String,String> getHeaders(){
        HashMap<String,String> headers=new HashMap<>();
        headers.put("Content-Type","application/json");
        headers.put("Authorization","key=AAAA8oblU0w:APA91bEz7TpVw5M0iFAwynjcPfsi2B1Eq-7LzvdJnginffgXz9Gv3RMLZ_-PQrBuB_HkWXv9L1NWCe5yg-kmH5x4n6155EJ3La2gYaBI5ZrP13YJYNuOoZ0if4wLgd5zQ_sNcRQtX3Sg");
        return headers;

    }

    //getting image from gallery and setting it into imageView as Bitmap
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            filepath = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(filepath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                imageView.setImageBitmap(bitmap);
            } catch (Exception ex) {

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}