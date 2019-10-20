package com.myappcompany.rajan.ucheck;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class UserData extends AppCompatActivity {

    private TextView name;
    private TextView age;
    private Button history;
    private Button upload;
    private String downloadUri;
    private String data2;
    private Uri fileUri;
    private File file;

    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        final String data = getIntent().getExtras().getString("NAME","default id");
        data2 = getIntent().getExtras().getString("AADHAAR","000000000000");
        name = (TextView)findViewById(R.id.name);
        name.setText(data);
        history = (Button)findViewById(R.id.history);
        upload = (Button)findViewById(R.id.upload);
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserData.this, Record.class);
                intent.putExtra("AADHAAR", data2);
                startActivity(intent);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                
                
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }


                    String[] projection = new String[]{
                            MediaStore.Images.ImageColumns._ID,
                            MediaStore.Images.ImageColumns.DATA,
                            MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                            MediaStore.Images.ImageColumns.DATE_TAKEN,
                            MediaStore.Images.ImageColumns.MIME_TYPE
                    };
                    final Cursor cursor = UserData.this.getContentResolver()
                            .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null,
                                    null, MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");
                    File imageFile = null;
                    while (cursor.moveToNext()) {
                        String imagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
                        imageFile = new File(imagePath);
                        if (imageFile.canRead() && imageFile.exists()) {
                            // we have found the latest picture in the public folder, do whatever you want
                            break;
                        }
                    }
                    fileUri = Uri.fromFile(imageFile);
                    Intent galleryIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    galleryIntent.setData(fileUri);
                    UserData.this.sendBroadcast(galleryIntent);
                    StorageReference filepath = mStorageRef.child("Photos").child(fileUri.getLastPathSegment());
                    filepath.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            downloadUri = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();

                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            //db.collection("user").document(data2).update("record", downloadUri, downloadUri);


                        }
                    });



            }
        });



    }





    }

