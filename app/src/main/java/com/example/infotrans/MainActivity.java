package com.example.infotrans;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    FirebaseDatabase firebase;
    DatabaseReference  refDB;
    EditText log,cont;
    private List<Registro_usuario> logins=new ArrayList<Registro_usuario>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebase = FirebaseDatabase.getInstance();
        log = findViewById(R.id.a1_usu);
        cont = findViewById(R.id.a1_pass);
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
    }
    public void Login(View view){
        String tipo;
        if (log.getText().toString().isEmpty()){
            Toast.makeText(this,"Login es un campo obligatorio",Toast.LENGTH_SHORT).show();
        }
        else {
            if (cont.getText().toString().isEmpty()){
                Toast.makeText(this,"Contraseña es un campo obligatorio",Toast.LENGTH_SHORT).show();
            }else {
                Registro_usuario login = new Registro_usuario(log.getText().toString(), cont.getText().toString());
                if (Exists(login)) {

                    for (int i=0;i<logins.size();i++) {
                        if (login.getLogin().equals(logins.get(i).getLogin()) &&
                                login.getContraseña().equals(logins.get(i).getContraseña())) {
                            tipo = logins.get(i).getTipo();
                            if (tipo.equals("Clientes")){
                                Intent intent=new Intent(this,Cliente_Main.class);
                                Bundle bundle=new Bundle();
                                bundle.putString("Usuario",logins.get(i).getLogin());
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }else{
                                if(tipo.equals("Empresas")){
                                    Intent intent=new Intent(this,Empresa_Main.class);
                                    Bundle bundle=new Bundle();
                                    bundle.putString("Usuario",logins.get(i).getLogin());
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }else{
                                    Intent intent=new Intent(this,Transporter_Main.class);
                                    Bundle bundle=new Bundle();
                                    bundle.putString("Usuario",logins.get(i).getLogin());
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            }
                        }
                    }
                } else {
                        Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    public void Click(View view){
    Intent intent=new Intent(this,Register_Main.class);
    startActivity(intent);
    }
    public boolean Exists(Registro_usuario logi){
        boolean exist=false;
        for (int i=0;i<logins.size();i++){
            if (logins.get(i).getContraseña().equals(logi.getContraseña()) && logins.get(i).getLogin().equals(logi.getLogin())){
                exist=true;
            }
        }
        return exist;
    }

}
