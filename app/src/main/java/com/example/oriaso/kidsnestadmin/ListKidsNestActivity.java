package com.example.oriaso.kidsnestadmin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ListKidsNestActivity extends AppCompatActivity {
//    private static final String TAG = ListNewsActivity.class.getSimpleName();
    RecyclerView rv;
    LinearLayoutManager linearLayoutManager;
    DatabaseReference db;
    FirebaseRecyclerAdapter<KidsNestModel, KidsNestViewHolder> firebasenewsRecycleAdapter;
    ProgressBar progressBarAgencyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_kids_nest);

        //Initialize Firebase DB
        db = FirebaseDatabase.getInstance().getReference();


        //SETUP RECYCLER
        rv = (RecyclerView) findViewById(R.id.recyclerViewKidsNestList);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(false);


        progressBarAgencyList = (ProgressBar) findViewById(R.id.progressBarAgencyList);
        progressBarAgencyList.setVisibility(View.VISIBLE);

        firebasenewsRecycleAdapter = new FirebaseRecyclerAdapter<KidsNestModel, KidsNestViewHolder>(KidsNestModel.class, R.layout.kids_nest_list_item, KidsNestViewHolder.class, db.child("KidsNest")) {
            @Override
            protected void populateViewHolder(KidsNestViewHolder viewHolder, final KidsNestModel model, final int position) {
                viewHolder.textViewKidsNestListTitle.setText(model.getName());
                progressBarAgencyList.setVisibility(View.GONE);
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openKidsNestDetailActivity(model.getName(), model.getId(), model.getEmail(), model.getPhone(), model.getContactPerson(), model.getContactEmail(), model.getContactPhone(), model.imageUrl);
                    }
                });
            }

            private void openKidsNestDetailActivity(String name, String id, String email, String phone, String contact_person, String contact_email, String contact_phone, String imageUrl) {

                Intent kidsNestIntent = new Intent(ListKidsNestActivity.this, ViewKidsNestActivity.class);
                kidsNestIntent.putExtra("nameKey", name);
                kidsNestIntent.putExtra("idKey", id);
                kidsNestIntent.putExtra("emailKey", email);
                kidsNestIntent.putExtra("phoneKey", phone);
                kidsNestIntent.putExtra("contactPersonKey", contact_person);
                kidsNestIntent.putExtra("contactEmailKey", contact_email);
                kidsNestIntent.putExtra("contactPhoneKey", contact_phone);
                kidsNestIntent.putExtra("imageKey", imageUrl);
                startActivity(kidsNestIntent);
            }
        };
        rv.setLayoutManager(linearLayoutManager);
        rv.setAdapter(firebasenewsRecycleAdapter);
    }

    public void openCreateKidsNestActivity(View v){
        startActivity( new Intent(ListKidsNestActivity.this, CreateKidsNestActivity.class));
    }
}
