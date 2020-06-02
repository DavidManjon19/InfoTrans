package com.example.infotrans;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //base de datos.
    FirebaseDatabase firebase;
    //Referencia a la base de datos.
    DatabaseReference refDB;
    //Usuario con correo para la base de datos.
    FirebaseAuth mauth;
    //EditTexts en los que se añaden email y contraseña.
    EditText log, cont;
    //Lista que se actualizara con los cambios en la base de datos.
    private List<Registro_usuario> logins = new ArrayList<Registro_usuario>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Barra superior en la app.
        ActionBar actionBar = getSupportActionBar();
        //Si el actionbar no es nulo se cabia el color por uno creado por mi.
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.mystyle));
        }
        //Inicializacion de las variables.
        firebase = FirebaseDatabase.getInstance();

        log = findViewById(R.id.a1_usu);

        cont = findViewById(R.id.a1_pass);

        refDB = firebase.getReference("Usuarios");

        mauth = FirebaseAuth.getInstance();

        Llenado();

    }
//Metodo que se encarga de llenar la lista con los datos exijidos de la base de datos en tiempo real.
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


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, databaseError.getMessage() + "", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void inicio_Sesion(final String mail, String pass) {

        if (mail.trim().length()!=0 && pass.trim().length()!=0){

        mauth.signInWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    for (int i = 0; i < logins.size(); i++) {
                        if (logins.get(i).getEmail().equals(mail)) {
                            if (logins.get(i).getTipo().equals("Empresas")) {
                                Intent intent = new Intent(MainActivity.this, Empresa_Main.class);

                                Bundle bundle = new Bundle();

                                bundle.putString("Usuario", log.getText().toString());

                                intent.putExtras(bundle);

                                startActivity(intent);
                            }
                            if (logins.get(i).getTipo().equals("Clientes")) {
                                Intent intent = new Intent(MainActivity.this, Cliente_Main.class);

                                Bundle bundle = new Bundle();

                                bundle.putString("Usuario", log.getText().toString());

                                intent.putExtras(bundle);

                                startActivity(intent);
                            }
                        }
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Error al iniciar sesion", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }else{
            Toast.makeText(MainActivity.this,"Email y contraseña no pueden estar vacios",Toast.LENGTH_SHORT).show();
        }
    }

    public void Logeo(View view) {
        try{

            inicio_Sesion(log.getText().toString(), cont.getText().toString());

        }catch(Exception e){

            e.printStackTrace();

        }
    }

    public void Click(View view) {
        Intent intent = new Intent(this, Register_Main.class);
        startActivity(intent);
    }
}
