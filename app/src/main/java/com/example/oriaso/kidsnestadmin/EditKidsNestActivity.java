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
import com.squareup.picasso.Picasso;

public class EditKidsNestActivity extends AppCompatActivity {

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

    String id, name, email, phone, contact_person, contact_phone, contact_email, imageUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_kids_nest);

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

        Intent i = getIntent();
        name = i.getStringExtra("nameKey");
        id = i.getStringExtra("idKey");
        email = i.getStringExtra("emailKey");
        phone = i.getStringExtra("phoneKey");
        contact_person = i.getStringExtra("contactPersonKey");
        contact_email = i.getStringExtra("contactEmailKey");
        contact_phone = i.getStringExtra("contactPhoneKey");
        imageUrl = i.getStringExtra("imageKey");

        getSupportActionBar().setTitle(name);
        editTextKidsNestName.setText(name);
        editTextKidsNestEmail.setText(email);
        editTextKidsNestPhone.setText(phone);
        editTextKidsNestContactPerson.setText(contact_person);
        editTextKidsNestContactEmail.setText(contact_email);
        editTextKidsNestContactPhone.setText(contact_phone);
        Picasso.with(getApplicationContext()).load(imageUrl).into(imageButtonKidsNestLogo);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            imageUri = data.getData();
            imageButtonKidsNestLogo.setImageURI(imageUri);

        }
    }

    public void editKidsNest(View v){
        attemptKidsNestUpload();
    }

    private void attemptKidsNestUpload(){
        name = editTextKidsNestName.getText().toString().trim();
        email = editTextKidsNestEmail.getText().toString().trim();
        phone = editTextKidsNestPhone.getText().toString().trim();
        contact_person = editTextKidsNestContactPerson.getText().toString().trim();
        contact_email = editTextKidsNestContactEmail.getText().toString().trim();
        contact_phone = editTextKidsNestContactPhone.getText().toString().trim();
        if(name.equals("") || email.equals("") || phone.equals("")){
            Toast.makeText(EditKidsNestActivity.this, "Fill in all the fields", Toast.LENGTH_SHORT).show();
            return;
        }
        progress.show();

        if (imageUri != null)
        {
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
                    Toast.makeText(EditKidsNestActivity.this, "Kids Nest edited", Toast.LENGTH_SHORT).show();
                }
                catch (DatabaseException e){
                    Toast.makeText(EditKidsNestActivity.this, e+"", Toast.LENGTH_SHORT).show();
                    return;
                }
    //            finish();
    //            startActivity(new Intent(EditKidsNestActivity.this, ListKidsNestActivity.class));
                Intent kidsNestIntent = new Intent(EditKidsNestActivity.this, ViewKidsNestActivity.class);
                kidsNestIntent.putExtra("nameKey", name);
                kidsNestIntent.putExtra("idKey", id);
                kidsNestIntent.putExtra("emailKey", email);
                kidsNestIntent.putExtra("phoneKey", phone);
                kidsNestIntent.putExtra("contactPersonKey", contact_person);
                kidsNestIntent.putExtra("contactEmailKey", contact_email);
                kidsNestIntent.putExtra("contactPhoneKey", contact_phone);
                kidsNestIntent.putExtra("imageKey", downloadUrl.toString());
                startActivity(kidsNestIntent);
                finish();
                startActivity(kidsNestIntent);
                }
            });
        }
        else
        {
            KidsNestModel kidsNest = new KidsNestModel(id, name, email, phone, imageUrl, contact_person, contact_email, contact_phone);


            try{
                dbKidsNest.child(id).setValue(kidsNest);
                progress.dismiss();
                Toast.makeText(EditKidsNestActivity.this, "Agency edited", Toast.LENGTH_SHORT).show();
            }
            catch (DatabaseException e){
                Toast.makeText(EditKidsNestActivity.this, e+"", Toast.LENGTH_SHORT).show();
                return;
            }
//                    startActivity(new Intent(EditAgencyActivity.this, ViewAgencyActivity.class));
            Intent kidsNestIntent = new Intent(EditKidsNestActivity.this, ViewKidsNestActivity.class);
            kidsNestIntent.putExtra("nameKey", name);
            kidsNestIntent.putExtra("idKey", id);
            kidsNestIntent.putExtra("emailKey", email);
            kidsNestIntent.putExtra("phoneKey", phone);
            kidsNestIntent.putExtra("contactPersonKey", contact_person);
            kidsNestIntent.putExtra("contactEmailKey", contact_email);
            kidsNestIntent.putExtra("contactPhoneKey", contact_phone);
            kidsNestIntent.putExtra("imageKey", imageUrl);
            finish();
            startActivity(kidsNestIntent);
        }

    }

}
