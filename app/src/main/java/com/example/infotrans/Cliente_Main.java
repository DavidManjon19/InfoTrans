package com.example.infotrans;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;
import java.util.ArrayList;

public class Cliente_Main extends AppCompatActivity {
    FirebaseDatabase firebase;

    DatabaseReference refDB;

    FirebaseUser currentUser;

    protected ArrayList<Registro_usuario> logins;

    TextView C_Nombre, C_NIF;

    Bundle bundle;

    ImageView perfil;

    int pick = 1;

    Uri uri;

    ProgressDialog pd;

    FirebaseStorage storage ;

    StorageReference storageref;

    Registro_usuario INC;

    InputStream imageIS;

    private FirebaseAuth mAuth;

    private static final int MenuOp1 = 1;

    private static final int MenuOp2 =2 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente__main);

        Inic_Components();

        Llenado();

    }
    public void Inic_Components(){
        firebase = FirebaseDatabase.getInstance();

        refDB = firebase.getReference("Usuarios");

        mAuth=FirebaseAuth.getInstance();

        currentUser=mAuth.getCurrentUser();

        storage= FirebaseStorage.getInstance();

        storageref = storage.getReference();

        C_Nombre = findViewById(R.id.C_Nombre);

        C_NIF = findViewById(R.id.C_Nif);

        perfil = findViewById(R.id.C_image);

        pd=new ProgressDialog(this);

        imageIS=null;
    }

    public void Llenado() {
        refDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                logins = new ArrayList<Registro_usuario>();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot obj : dataSnapshot.getChildren()) {
                        Registro_usuario log = obj.getValue(Registro_usuario.class);
                        logins.add(log);
                    }
                }

                Cambio_Datos();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(Cliente_Main.this, databaseError.getCode() + "", Toast.LENGTH_LONG).show();

            }

        });

    }
    public void Cambio_Datos() {

        bundle = getIntent().getExtras();

        for (int i = 0; i < logins.size(); i++) {


            if (bundle.getString("Usuario").equals(logins.get(i).getEmail())) {

                INC=logins.get(i);

                Img_Perfil();

                C_Nombre.setText(INC.getNombre());

                C_NIF.setText(INC.getDni());

            }


        }
        ActionBar actionBar= getSupportActionBar();

        if (actionBar!=null){
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.mystyle));
            actionBar.setTitle(INC.getLogin());
        }

    }

    public void Img_Perfil() {
        try{

            StorageReference st= storage.getReferenceFromUrl("gs://infotrans-ac955.appspot.com/").child("users/"+INC.getEmail()+"/profile.jpg");

            if (st != null) {
            st.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {

                    Glide.with(Cliente_Main.this).load(bytes).into(perfil);
                }
            });}
        }catch(Exception e){

            e.printStackTrace();
        }
    }

    public void Cargar_imagen(View view) {

        Intent pickear = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        pickear.setType("image/*");

        startActivityForResult(pickear,pick);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==pick && resultCode==RESULT_OK && data != null && data.getData() != null){

            pd.setTitle("Subiendo...");
            pd.setMessage("Subiendo foto");
            pd.setCancelable(false);
            pd.show();

            uri=data.getData();

            try {

                imageIS=getContentResolver().openInputStream(uri);

            }catch (Exception ex){

                ex.printStackTrace();

            }

            final StorageReference filepath= storageref.child("users/"+bundle.getString("Usuario")+"/profile.jpg");

            if( filepath != null){

                filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        pd.dismiss();

                        Bitmap photoBitmap= BitmapFactory.decodeStream(imageIS);
                        perfil.setImageBitmap(photoBitmap);

                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(Cliente_Main.this,e.getMessage()+"",Toast.LENGTH_SHORT).show();

                    }
                });
            }else{

                Toast.makeText(Cliente_Main.this,"Error",Toast.LENGTH_SHORT).show();

            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add(Menu.NONE, MenuOp1, Menu.NONE, "Dar de baja");

        menu.add(Menu.NONE, MenuOp2, Menu.NONE, "Listado de pedidos");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case MenuOp1:

                AlertDialog.Builder dialogo = new AlertDialog.Builder(this);

                dialogo.setTitle("Importante");

                dialogo.setMessage(" Si borras el usuario se perderá toda la información de la base de datos \n ¿Estás seguro de que quieres hacerlo?");
                dialogo.setCancelable(false);

                dialogo.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        borrar();
                    }
                });
                dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                dialogo.show();
                break;

            case MenuOp2:

                Intent pedidos = new Intent(this, Pedidos_main.class);

                Bundle bun = new Bundle();

                bun.putString("Nombre", INC.getLogin());

                bun.putString("Tipo",INC.getTipo());

                pedidos.putExtras(bun);

                startActivity(pedidos);

                break;
        }


        return super.onOptionsItemSelected(item);
    }

    public void borrar(){
        bundle=getIntent().getExtras();

        refDB.child(INC.getLogin()).removeValue();

        currentUser.delete();

        storage.getReferenceFromUrl("gs://infotrans-ac955.appspot.com/").child("users/"+INC.getEmail()+"/profile.jpg").delete();

        Toast.makeText(Cliente_Main.this,"Usuario borrado",Toast.LENGTH_SHORT).show();

        Intent intent= new Intent(this, MainActivity.class);

        startActivity(intent);
    }
}