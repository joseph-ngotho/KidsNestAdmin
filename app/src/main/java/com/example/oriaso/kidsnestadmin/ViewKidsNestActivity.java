package com.example.oriaso.kidsnestadmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ViewKidsNestActivity extends AppCompatActivity {
    TextView textViewShowKidsNestName, textViewShowKidsNestEmail, textViewShowKidsNestPhone, textViewShowKidsNestContactPerson, textViewShowKidsNestContactEmail, textViewShowKidsNestContactPhone;
    String name, email, imageUrl, id, phone, contact_person, contact_email, contact_phone;
    ImageView imageViewShowKidsNestLogo;
    ProgressDialog progress;


    FirebaseDatabase db;
    DatabaseReference dbKidsNest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_kids_nest);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        textViewShowKidsNestName = (TextView) findViewById(R.id.textViewShowKidsNestName);
        textViewShowKidsNestEmail = (TextView) findViewById(R.id.textViewShowKidsNestEmail);
        textViewShowKidsNestPhone = (TextView) findViewById(R.id.textViewShowKidsNestPhone);
        textViewShowKidsNestContactPerson = (TextView) findViewById(R.id.textViewShowKidsNestContactPerson);
        textViewShowKidsNestContactEmail = (TextView) findViewById(R.id.textViewShowKidsNestContactEmail);
        textViewShowKidsNestContactPhone = (TextView) findViewById(R.id.textViewShowKidsNestContactPhone);
        imageViewShowKidsNestLogo = (ImageView) findViewById(R.id.imageViewShowKidsNestLogo);

        db = FirebaseDatabase.getInstance();
        dbKidsNest = db.getReference("KidsNest");

        progress = new ProgressDialog(this);
        progress.setMessage("Posting article.. ");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);

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
        textViewShowKidsNestName.setText(name);
        textViewShowKidsNestEmail.setText(email);
        textViewShowKidsNestPhone.setText(phone);
        textViewShowKidsNestContactPerson.setText(contact_person);
        textViewShowKidsNestContactEmail.setText(contact_email);
        textViewShowKidsNestContactPhone.setText(contact_phone);
        Picasso.with(getApplicationContext()).load(imageUrl).into(imageViewShowKidsNestLogo);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.crud_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                Intent kidsNestIntent = new Intent(ViewKidsNestActivity.this, EditKidsNestActivity.class);

                kidsNestIntent.putExtra("nameKey", name);
                kidsNestIntent.putExtra("idKey", id);
                kidsNestIntent.putExtra("emailKey", email);
                kidsNestIntent.putExtra("phoneKey", phone);
                kidsNestIntent.putExtra("contactPersonKey", contact_person);
                kidsNestIntent.putExtra("contactEmailKey", contact_email);
                kidsNestIntent.putExtra("contactPhoneKey", contact_phone);
                kidsNestIntent.putExtra("imageKey", imageUrl);

                startActivity(kidsNestIntent);
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_delete:

                try{
                    dbKidsNest.child(id).removeValue();
                    progress.dismiss();
                    Toast.makeText(ViewKidsNestActivity.this, "Nest deleted", Toast.LENGTH_SHORT).show();
                }
                catch (DatabaseException e){
                    Toast.makeText(ViewKidsNestActivity.this, e+"", Toast.LENGTH_SHORT).show();
                }
                finish();
                startActivity(new Intent(ViewKidsNestActivity.this, ListKidsNestActivity.class));
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
