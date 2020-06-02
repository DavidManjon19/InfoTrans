package com.example.infotrans;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Cliente_Main extends AppCompatActivity {
    FirebaseDatabase firebase;
    DatabaseReference refDB;
    private List<Registro_usuario> logins=new ArrayList<Registro_usuario>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente__main);
        firebase = FirebaseDatabase.getInstance();
        refDB = firebase.getReference("Tipos");
        refDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                logins.clear();
                if (dataSnapshot.child("Empresas").exists()) {
                    for (DataSnapshot obj : dataSnapshot.child("Empresas").getChildren()) {
                        Registro_usuario log = obj.getValue(Registro_usuario.class);
                        logins.add(log);
                    }
                }
                if (dataSnapshot.child("Transportistas").exists()) {
                    for (DataSnapshot obj : dataSnapshot.child("Transportistas").getChildren()) {
                        Registro_usuario log = obj.getValue(Registro_usuario.class);
                        logins.add(log);
                    }
                }
                if (dataSnapshot.child("Clientes").exists()) {
                    for (DataSnapshot obj : dataSnapshot.child("Clientes").getChildren()) {
                        Registro_usuario log = obj.getValue(Registro_usuario.class);
                        logins.add(log);
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        Bundle  bundle = getIntent().getExtras();
        getSupportActionBar().setTitle(bundle.getString("Usuario"));
    }
}
