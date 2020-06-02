package com.example.infotrans;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Register_Main extends AppCompatActivity {
    private FirebaseAuth mAuth;
    Spinner spinner;
    FirebaseDatabase firebase;
    DatabaseReference refDB;
    RadioButton r1, r2;
    EditText nombre, usuario, dni, contrasena, r_contrasena,email;

    private List<Registro_usuario> logins = new ArrayList<Registro_usuario>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register__main);


        Inic_Components();

        Llenado();
    }

    public void Inic_Components() {
        spinner = findViewById(R.id.spinner);

        mAuth = FirebaseAuth.getInstance();

        email=findViewById(R.id.R_Mail);

        String[] datos = {"A Coruña", "Alava", "Albacete", "Alicante", "Almeria", "Asturias", "Avila"
                , "Badajoz", "Baleares", "Barcelona", "Burgos", "Caceres", "Cadiz", "Cantabria", "Castellon"
                , "Ciudad Real", "Cordoba", "Cuenca", "Girona", "Granada", "Guadalajara", "Gipuzkoa", "Huelva", "Huesca"
                , "Jaen", "La Rioja", "Las Palmas", "Leon", "Lerida", "Lugo", "Madrid", "Malaga", "Murcia"
                , "Navarra", "Ourense", "Palencia", "Pontevedra", "Salamanca", "Segovia", "Sevilla", "Soria", "Tarragona"
                , "Santa Cruz de Tenerife", "Teruel", "Toledo", "Valencia", "Valladolid", "Vizcaya", "Zamora", "Zaragoza"};

        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(Register_Main.this, android.R.layout.simple_spinner_dropdown_item, datos);

        spinner.setAdapter(adaptador);

        spinner.setVisibility(View.INVISIBLE);

        nombre = findViewById(R.id.R_Nombre);

        usuario = findViewById(R.id.r_Login);

        dni = findViewById(R.id.R_dni);

        contrasena = findViewById(R.id.R_Contraseña);

        r_contrasena = findViewById(R.id.r_Contraseña_otra);

        r2 = findViewById(R.id.R_T_Empresa);

        r1 = findViewById(R.id.R_T_Cliente);

        mAuth = FirebaseAuth.getInstance();


        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.mystyle));
        }

        firebase = FirebaseDatabase.getInstance();

        refDB = firebase.getReference("Usuarios");


        r2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (r2.isChecked()) {
                    spinner.setVisibility(View.VISIBLE);
                    nombre.setHint("Nombre de la empresa");
                    dni.setHint("NIF de la Empresa");
                } else {
                    spinner.setVisibility(View.INVISIBLE);
                    nombre.setHint("Nombre");
                    dni.setHint("DNI");
                }
            }
        });
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


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Register_Main.this, databaseError.getCode() + "", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void crearUsuario(String email, String pass) {

        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(Register_Main.this,"Registro realizado correctamente",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(Register_Main.this,"Registro erroneo",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public void Register(View view) {
        String type = "";
        if (r1.isChecked()) {

            type = r1.getText().toString();
        }
        if (r2.isChecked()) {

            type = r2.getText().toString();

        }

        Comp_Reg(type);

    }

    public void Comp_Reg(String type) {

        if (validation()) {
            if (contrasena.getText().toString().equals(r_contrasena.getText().toString())) {
                if (type.equals("Empresas")) {
                    Registro_usuario u=new Registro_usuario(usuario.getText().toString(),
                            contrasena.getText().toString(), dni.getText().toString(), nombre.getText().toString(), type, spinner.getSelectedItem().toString(),email.getText().toString());
                    refDB.child(u.getLogin()).setValue(u);

                    crearUsuario(email.getText().toString(), contrasena.getText().toString());
                } else {
                    Registro_usuario d=new Registro_usuario(usuario.getText().toString(), contrasena.getText().toString(), dni.getText().toString().toUpperCase(),
                            nombre.getText().toString(), type , email.getText().toString());
                    refDB.child(d.getLogin()).setValue(d);

                    crearUsuario(email.getText().toString(), contrasena.getText().toString());
                }



                dni.setText(null);

                nombre.setText(null);

                usuario.setText(null);

                contrasena.setText(null);

                r_contrasena.setText(null);

                email.setText(null);

                Intent login_act = new Intent(this, MainActivity.class);

                startActivity(login_act);

            } else {

                Toast.makeText(this, "Ambas contraseñas tienen que ser iguales", Toast.LENGTH_SHORT).show();

            }
        }
    }

    public boolean validation() {
        if (Vacios().length() == 0) {

            if (login_exists(usuario.getText().toString())) {

                Toast.makeText(this, "Nombre de usuario no disponible", Toast.LENGTH_SHORT).show();
                return false;

            } else {

                if (dni_exists(dni.getText().toString().toUpperCase())) {

                    Toast.makeText(this, "El DNI/NIF: " + dni.getText().toString() + " ya existe en InfoTrans", Toast.LENGTH_LONG).show();
                    return false;
                } else {

                    if (dni.length() < 9 || dni.length() > 9) {

                        Toast.makeText(this, "El DNI/NIF debe contener 9 caracteres", Toast.LENGTH_LONG).show();

                        return false;
                    } else {

                        if (!dni_correct(dni.getText().toString())) {

                            Toast.makeText(this, "El DNI/NIF debe de contener 8 numeros y una letra", Toast.LENGTH_LONG).show();

                            return false;
                        } else {

                            if (contrasena.getText().toString().length() < 6) {

                                Toast.makeText(this, "La contrasena debe de tener al menos 8 caracteres", Toast.LENGTH_SHORT).show();

                                return false;
                            } else {

                                if (!Numeros(contrasena.getText().toString())) {

                                    Toast.makeText(this, "Debe de haber algun numero en la contrasena", Toast.LENGTH_LONG).show();

                                    return false;
                                } else {
                                    if (!Mayuscula(contrasena.getText().toString())) {
                                        Toast.makeText(this, "Debe de haber alguna mayuscula en la contrasena", Toast.LENGTH_LONG).show();
                                        return false;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    public boolean dni_exists(String DNI) {

        boolean exist = false;

        for (int i = 0; i < logins.size(); i++) {

            if (logins.get(i).getDni().equals(DNI)) {

                exist = true;

            }
        }

        return exist;
    }

    public boolean login_exists(String login) {

        boolean exist = false;

        for (int i = 0; i < logins.size(); i++) {

            if (logins.get(i).getLogin().equals(login)) {

                exist = true;

            }
        }

        return exist;

    }

    public String Vacios() {
        String msg = "";

        if (email.getText().toString().trim().isEmpty())

            msg += "Hay que insertar un email. \n";

        if (!r1.isChecked() && !r2.isChecked())

            msg += "Hay que seleccionar un tipo. \n";

        if (dni.getText().toString().trim().isEmpty())
            msg += "DNI/NIF es un campo obligatorio. \n";

        if (nombre.getText().toString().trim().isEmpty())

            msg += "Nombre es un campo obligatorio. \n";

        if (usuario.getText().toString().trim().isEmpty())

            msg += "Login es un campo obligatorio. \n";

        if (contrasena.getText().toString().trim().isEmpty())

            msg += "Contraseña es un campo obligatorio. \n";

        return msg;
    }

    public boolean Numeros(String string) {

        boolean num = false;

        char ca;

        for (int x = 0; x < string.length(); x++) {

            ca = string.charAt(x);

            try {

                int n = Integer.parseInt(String.valueOf(ca));

                switch (n) {

                    case 0:
                        num = true;
                        break;

                    case 1:
                        num = true;
                        break;

                    case 2:
                        num = true;
                        break;

                    case 3:
                        num = true;
                        break;

                    case 4:
                        num = true;
                        break;

                    case 5:
                        num = true;
                        break;

                    case 6:
                        num = true;
                        break;

                    case 7:
                        num = true;
                        break;

                    case 8:
                        num = true;
                        break;

                    case 9:
                        num = true;
                        break;

                    default:
                        break;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return num;
    }

    public boolean Mayuscula(String contra) {

        char caracter;

        boolean may = false;

        for (int i = 0; i < contra.length(); i++) {

            caracter = contra.charAt(i);

            if (Character.isUpperCase(caracter)) {

                may = true;

            }
        }
        return may;
    }

    public boolean dni_correct(String dni) {

        boolean cierto = false;

        String nums = "";

        char l = dni.charAt(8);

        for (int i = 0; i < dni.length() - 1; i++) {

            nums += String.valueOf(dni.charAt(i));

        }
        try {

            Integer.parseInt(nums);

            cierto = true;

            try {

                int n = Integer.parseInt(String.valueOf(l));

                cierto = false;

            } catch (Exception e) {

                cierto = true;

            }
        } catch (Exception ex) {

            cierto = false;

        }

        return cierto;
    }

}
