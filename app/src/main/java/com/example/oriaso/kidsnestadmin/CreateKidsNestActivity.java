package com.example.oriaso.kidsnestadmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class CreateKidsNestActivity extends AppCompatActivity {

    EditText editTextKidsNestName;
    EditText editTextKidsNestEmail;
    EditText editTextKidsNestPhone;
    EditText editTextKidsNestContactPerson;
    EditText editTextKidsNestContactEmail;
    EditText editTextKidsNestContactPhone;
    ImageButton imageButtonKidsNestLogo;
    ProgressDialog progress;


    FirebaseDatabase db;
    DatabaseReference dbKidsNest;
    private StorageReference mStorage;

    private static final int GALLERY_REQUEST = 1;
    private Uri imageUri = null;
    private Uri downloadUrl;

    String id, name, email, phone, contact_person, contact_phone, contact_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_kids_nest);

        editTextKidsNestName = (EditText) findViewById(R.id.editTextKidsNestName);
        editTextKidsNestEmail = (EditText) findViewById(R.id.editTextKidsNestEmail);
        editTextKidsNestPhone = (EditText) findViewById(R.id.editTextKidsNestPhone);
        editTextKidsNestContactPerson = (EditText) findViewById(R.id.editTextKidsNestContactPerson);
        editTextKidsNestContactEmail = (EditText) findViewById(R.id.editTextKidsNestContactEmail);
        editTextKidsNestContactPhone = (EditText) findViewById(R.id.editTextKidsNestContactPhone);

        imageButtonKidsNestLogo = (ImageButton) findViewById(R.id.imageButtonKidsNestLogo);

        progress = new ProgressDialog(this);
        progress.setMessage("Saving Kids Nest.. ");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);

        db = FirebaseDatabase.getInstance();
        dbKidsNest = db.getReference("KidsNest");
        mStorage = FirebaseStorage.getInstance().getReference();

        imageButtonKidsNestLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
        });
    }

    public void createKidsNest(View v){
        attemptKidsNestUpload();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            imageUri = data.getData();
            imageButtonKidsNestLogo.setImageURI(imageUri);

        }
    }

    private void attemptKidsNestUpload(){
        name = editTextKidsNestName.getText().toString().trim();
        email = editTextKidsNestEmail.getText().toString().trim();
        phone = editTextKidsNestPhone.getText().toString().trim();
        contact_person = editTextKidsNestContactPerson.getText().toString().trim();
        contact_email = editTextKidsNestContactEmail.getText().toString().trim();
        contact_phone = editTextKidsNestContactPhone.getText().toString().trim();
        if(name.equals("") || email.equals("") || phone.equals("") || imageUri == null){
            Toast.makeText(CreateKidsNestActivity.this, "Fill in all the fields", Toast.LENGTH_SHORT).show();
            return;
        }
        progress.show();
        StorageReference filePath = mStorage.child("KidsNest_Logos").child(imageUri.getLastPathSegment());
        filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Get a URL to the uploaded content
                //noinspection VisibleForTests
                downloadUrl = taskSnapshot.getDownloadUrl();
                id = dbKidsNest.push().getKey();
                KidsNestModel kidsNest = new KidsNestModel(id, name, email, phone, downloadUrl.toString(), contact_person, contact_email, contact_phone);

                try{
                    dbKidsNest.child(id).setValue(kidsNest);
                    progress.dismiss();
                    Toast.makeText(CreateKidsNestActivity.this, "Kids Nest saved", Toast.LENGTH_SHORT).show();
                }
                catch (DatabaseException e){
                    Toast.makeText(CreateKidsNestActivity.this, e+"", Toast.LENGTH_SHORT).show();
                    return;
                }
                finish();
//                startActivity(new Intent(CreateKidsNestActivity.this, ListKidsNestActivity.class));
            }
        });

    }
}
