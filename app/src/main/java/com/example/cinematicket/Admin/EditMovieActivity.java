package com.example.cinematicket.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cinematicket.Model.MovieModel;
import com.example.cinematicket.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EditMovieActivity extends AppCompatActivity {
    ImageView imageView;
    EditText title, releasedate, summary, language,genre;
    TextView update,firstshow,secondshow,thirdshow;
    String titleString, releaseString, summaryString,imgpaths,ids,titles,genreString,langString,first,second,third;
    ProgressDialog progressDialog;
    Uri filepath;
    Bitmap bitmap;
    FirebaseDatabase database;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_movie);

        imageView = findViewById(R.id.pflimage);
        title = findViewById(R.id.ettitle);
        releasedate = findViewById(R.id.etdate);
        summary = findViewById(R.id.etdesc);
        language = findViewById(R.id.etlang);
        genre = findViewById(R.id.etgenre);
        firstshow = findViewById(R.id.firstShow);
        secondshow = findViewById(R.id.secondshow);
        thirdshow = findViewById(R.id.thirdShow);
        update = findViewById(R.id.postMovie);
        database = FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(EditMovieActivity.this);
        progressDialog.setMessage("Saving...Please Wait.");

        Intent intent = getIntent();
        titles = intent.getStringExtra("title");
        String languages = intent.getStringExtra("language");
        String summarys = intent.getStringExtra("summary");
        String genres = intent.getStringExtra("genre");
        String images = intent.getStringExtra("image");
        String dates = intent.getStringExtra("date");
        String firsts = intent.getStringExtra("first");
        String seconds = intent.getStringExtra("second");
        String thirds = intent.getStringExtra("third");
        ids = intent.getStringExtra("id");
        imgpaths = intent.getStringExtra("imgpath");
        title.setText(titles);
        releasedate.setText(dates);
        summary.setText(summarys);
        language.setText(languages);
        genre.setText(genres);
        firstshow.setText(firsts);
        secondshow.setText(seconds);
        thirdshow.setText(thirds);
        title.setText(titles);
        Glide.with(getApplicationContext()).load(images).placeholder(R.drawable.camera).into(imageView);
        mDatabase = database.getReference("Users").child(titles);
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







                TimePickerDialog timePickerDialog = new TimePickerDialog(EditMovieActivity.this, new TimePickerDialog.OnTimeSetListener() {
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







                TimePickerDialog timePickerDialog = new TimePickerDialog(EditMovieActivity.this, new TimePickerDialog.OnTimeSetListener() {
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







                TimePickerDialog timePickerDialog = new TimePickerDialog(EditMovieActivity.this, new TimePickerDialog.OnTimeSetListener() {
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


        update.setOnClickListener(new View.OnClickListener() {
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
                        .child(imgpaths);
                if (filepath==null){

                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    DatabaseReference root = db.getReference("Movies");
//                                        MovieModel movieModel = new MovieModel(
//                                                titleString,summaryString,releaseString,uri.toString(),langString,genreString,
//                                                first,second,third,"","");

                    //creating directory name as Hall Name
                    //  root.child(title.getText().toString()).setValue(movieModel);

                    // root.push().setValue(registrationModel);
                    root.child(ids).child("date").setValue(releasedate.getText().toString());
                    root.child(ids).child("firstshow").setValue(firstshow.getText().toString());
                    root.child(ids).child("genre").setValue(genre.getText().toString());
                    root.child(ids).child("language").setValue(language.getText().toString());
                    root.child(ids).child("secondshow").setValue(secondshow.getText().toString());
                    root.child(ids).child("summary").setValue(summary.getText().toString());
                    root.child(ids).child("thirdshow").setValue(thirdshow.getText().toString());
                    root.child(ids).child("title").setValue(title.getText().toString());

                    progressDialog.dismiss();
                }else {

                    uploader.putFile(filepath)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {


                                            FirebaseDatabase db = FirebaseDatabase.getInstance();
                                            DatabaseReference root = db.getReference("Movies");

                                            root.child(ids).child("date").setValue(releasedate.getText().toString());
                                            root.child(ids).child("firstshow").setValue(firstshow.getText().toString());
                                            root.child(ids).child("genre").setValue(genre.getText().toString());
                                            root.child(ids).child("image").setValue(uri.toString());
                                            root.child(ids).child("imgpath").setValue(imgpaths);
                                            root.child(ids).child("language").setValue(language.getText().toString());
                                            root.child(ids).child("secondshow").setValue(secondshow.getText().toString());
                                            root.child(ids).child("summary").setValue(summary.getText().toString());
                                            root.child(ids).child("thirdshow").setValue(thirdshow.getText().toString());
                                            root.child(ids).child("title").setValue(title.getText().toString());

                                            progressDialog.dismiss();

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

            }
        });
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