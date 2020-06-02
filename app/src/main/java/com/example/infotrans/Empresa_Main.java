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
import android.widget.EditText;
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
import java.util.Collections;
import java.util.Comparator;

public class Empresa_Main extends AppCompatActivity {

    private static final int MnuOpc1 = 1;

    private static final int MnuOpc2 = 2;

    String usuario;

    FirebaseDatabase firebase;

    DatabaseReference refDB,refDBp;

    FirebaseUser currentUser;

    protected ArrayList<Registro_usuario> logins;

    TextView E_Nombre, E_NIF;

    EditText E_ENombre;

    Bundle bundle;

    ImageView perfil;

    int pick = 1;

    Uri uri;

    ProgressDialog pd;

    FirebaseStorage storage;

    StorageReference storageref;

    Registro_usuario INC;

    InputStream imageIS;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa__main);

        //Declaracion de las variables

        Inic_Components();

        Llenado();

    }


    //Inicializacion de variables.

    public void Inic_Components() {
        firebase = FirebaseDatabase.getInstance();

        refDBp=firebase.getReference("Pedidos");

        refDB = firebase.getReference("Usuarios");

        mAuth = FirebaseAuth.getInstance();

        currentUser = mAuth.getCurrentUser();

        storage = FirebaseStorage.getInstance();

        storageref = storage.getReference();

        E_Nombre = findViewById(R.id.E_Nombre);

        E_NIF = findViewById(R.id.E_NIF);

        E_ENombre = findViewById(R.id.E_NEmpresas);

        perfil = findViewById(R.id.E_Perfil);

        pd = new ProgressDialog(this);

        imageIS = null;
    }


//Llenar arraylist.

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

                Toast.makeText(Empresa_Main.this, databaseError.getCode() + "", Toast.LENGTH_LONG).show();

            }

        });

    }

    //Carga de la imagen

    public void Carga_imagen(View view) {

        Intent pickear = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        pickear.setType("image/*");

        startActivityForResult(pickear, pick);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == pick && resultCode == RESULT_OK && data != null && data.getData() != null) {

            pd.setTitle("Subiendo...");
            pd.setMessage("Subiendo foto");
            pd.setCancelable(false);
            pd.show();

            uri = data.getData();

            try {

                imageIS = getContentResolver().openInputStream(uri);

            } catch (Exception ex) {

                ex.printStackTrace();

            }

            final StorageReference filepath = storageref.child("users/" + bundle.getString("Usuario") + "/profile.jpg");

            if (filepath != null) {

                filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        pd.dismiss();

                        Bitmap photoBitmap = BitmapFactory.decodeStream(imageIS);
                        perfil.setImageBitmap(photoBitmap);

                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(Empresa_Main.this, e.getMessage() + "", Toast.LENGTH_SHORT).show();

                    }
                });
            } else {

                Toast.makeText(Empresa_Main.this, "Error", Toast.LENGTH_SHORT).show();

            }
        }
    }


    //Poner valor a los text.
    public void Cambio_Datos() {

        bundle = getIntent().getExtras();

        Ordenacion();
        for (int i = 0; i < logins.size(); i++) {

            if (logins.get(i).getTipo().equals("Empresas")) {

                E_ENombre.append("\n" + logins.get(i).getNombre() + " -- " + logins.get(i).getProvincia());
            }

            if (bundle.getString("Usuario").equals(logins.get(i).getEmail())) {

                INC = logins.get(i);

                Img_Perfil();

                E_Nombre.setText(logins.get(i).getNombre());

                usuario = logins.get(i).getLogin();

                E_NIF.setText(logins.get(i).getDni());

            }


        }
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.mystyle));
            actionBar.setTitle(INC.getLogin());
        }

    }

    public void Img_Perfil() {
        try {

            StorageReference st = storage.getReferenceFromUrl("gs://infotrans-ac955.appspot.com/").child("users/" + INC.getEmail() + "/profile.jpg");
            if (st != null) {
                st.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {

                        Glide.with(Empresa_Main.this).load(bytes).into(perfil);
                    }
                });
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void Ordenacion() {
        Collections.sort(logins, new Comparator<Registro_usuario>() {
            public int compare(Registro_usuario p1, Registro_usuario p2) {
                if (p1.getTipo().equals("Empresas") && p2.getTipo().equals("Empresas"))
                    return new String(p1.getProvincia()).compareTo(new String(p2.getProvincia()));
                else {
                    return 0;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add(Menu.NONE, MnuOpc1, Menu.NONE, "Dar de baja la empresa");

        menu.add(Menu.NONE, MnuOpc2, Menu.NONE, "Listado de pedidos");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case MnuOpc1:

                AlertDialog.Builder dialogo = new AlertDialog.Builder(this);

                dialogo.setTitle("Importante");

                dialogo.setMessage(" Si borras el usuario se perderá toda la información de la base de datos \n ¿Estás seguro de que quieres hacerlo?");
                dialogo.setCancelable(false);

                dialogo.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        borrar_empresa();
                    }
                });
                dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                dialogo.show();
                break;
            case MnuOpc2:
                Intent pedidos = new Intent(this, Pedidos_main.class);

                Bundle bun = new Bundle();

                bun.putString("Nombre", usuario);

                bun.putString("Tipo","Empresas");

                pedidos.putExtras(bun);

                startActivity(pedidos);

                break;
        }


        return super.onOptionsItemSelected(item);
    }

    public void borrar_empresa() {

        bundle = getIntent().getExtras();

        refDB.child(INC.getLogin()).removeValue();

        refDBp.child(INC.getLogin()).removeValue();

        currentUser.delete();

        storage.getReferenceFromUrl("gs://infotrans-ac955.appspot.com/").child("users/" + INC.getEmail() + "/profile.jpg").delete();

        Toast.makeText(Empresa_Main.this, "Usuario borrado", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);
    }
}