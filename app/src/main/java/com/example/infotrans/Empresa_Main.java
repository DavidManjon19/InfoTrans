package com.example.infotrans;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class Empresa_Main extends AppCompatActivity {

    FirebaseDatabase firebase;

    DatabaseReference refDB;

    List<Registro_usuario> logins;

    TextView E_Nombre, E_NIF, img_E;

    Bundle bundle;

    ImageView perfil;

    int pick = 111;

    Uri filePath;

    ProgressDialog pd;

    FirebaseStorage storage = FirebaseStorage.getInstance();

    StorageReference storageref = storage.getReferenceFromUrl("gs://infotrans-ac955.appspot.com");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa__main);

        //Declaracion de las variables
        firebase = FirebaseDatabase.getInstance();
        refDB = firebase.getReference("infotrans-ac955/Tipos");

        refDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                logins= new ArrayList<Registro_usuario>();
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
                Toast.makeText(Empresa_Main.this,databaseError.getCode()+"",Toast.LENGTH_LONG).show();
            }
        });

        E_Nombre = findViewById(R.id.E_Nombre);
        E_NIF = findViewById(R.id.E_NIF);
        perfil = findViewById(R.id.E_Perfil);
        img_E = findViewById(R.id.img_E);


        //asignacion del login al actionbar

        bundle = getIntent().getExtras();
        getSupportActionBar().setTitle(bundle.getString("Usuario"));
        E_Nombre.setText("");
        E_NIF.setText("");
        /*
        for (int i=0; i<logins.size();i++){
            if (bundle.getString("Usuario").equals(logins.get(i).getLogin())){
            E_Nombre.setText(logins.get(i).getNombre());
            E_NIF.setText(logins.get(i).getDni());
            }
            else{
                Toast.makeText(this,"Pene largo",Toast.LENGTH_LONG).show();
            }
        }
*/
        //Cambio de la imagen



        // carga y subida de imagen en la bbdd

        pd = new ProgressDialog(this);
        pd.setMessage("Subiendo...");
        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Seleccionar Imagen"), pick);

            }
        });

        img_E.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filePath != null) {
                    pd.show();

                    StorageReference childRef = storageref.child(bundle.getString("Usuario")+".jpg");

                    UploadTask uploadTask = childRef.putFile(filePath);

                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            pd.dismiss();
                            Toast.makeText(Empresa_Main.this, "Subida exitosamente", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(Empresa_Main.this, "Problema al subir -> " + e, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(Empresa_Main.this, "Seleccionar una imagen", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == pick && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                perfil.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



}
