package com.example.oriaso.kidsnestadmin;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class KidsNestModel {
    String id, name, email, phone, imageUrl, contact_person, contact_email, contact_phone;
    
    public KidsNestModel(){}

    public KidsNestModel(String id, String name, String email, String phone, String imageUrl, String contact_person, String contact_email, String contact_phone){
        this.id =id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.imageUrl = imageUrl;
        this.contact_person = contact_person;
        this.contact_email = contact_email;
        this.contact_phone = contact_phone;

    }
    public String getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public String getEmail(){
        return this.email;
    }

    public String getPhone(){
        return this.phone;
    }

    public String getImageUrl(){
        return this.imageUrl;
    }

    public String getContactPerson(){
        return this.contact_person;
    }

    public String getContactEmail(){
        return this.contact_email;
    }

    public String getContactPhone(){
        return this.contact_phone;
    }


}
