package com.argo.whatsapp;

import android.provider.ContactsContract;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ConfiguracaoFirebase {

    private static DatabaseReference database;
    private static FirebaseAuth auth;
    private static StorageReference storege ;

     public static DatabaseReference getFirebaseDatabase(){
         if (database == null){
             database = FirebaseDatabase.getInstance ().getReference ();
         }
         return database;
     }
     public static FirebaseAuth getFirebaseAutenticacao(){
         if (auth == null){
             auth = FirebaseAuth.getInstance ();

         }
         return auth;
     }
    public static StorageReference getFirebaseStorege(){
        if (storege == null){
            storege = FirebaseStorage.getInstance ().getReference ();

        }
        return storege;

    }

}

