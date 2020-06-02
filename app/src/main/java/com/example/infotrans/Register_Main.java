package com.example.infotrans;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Register_Main extends AppCompatActivity {
    FirebaseDatabase firebase;
    DatabaseReference refDB;
    RadioButton r1, r2, r3;
    EditText nombre, usuario, dni, contraseña, r_contraseña;
    private List<Registro_usuario> logins = new ArrayList<Registro_usuario>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register__main);
        nombre = findViewById(R.id.R_Nombre);
        usuario = findViewById(R.id.R_Login);
        dni = findViewById(R.id.R_dni);
        contraseña = findViewById(R.id.R_Contraseña);
        r_contraseña = findViewById(R.id.r_Contraseña_otra);
        r1 = findViewById(R.id.R_T_Empresa);
        r2 = findViewById(R.id.R_T_Transporte);
        r3 = findViewById(R.id.R_T_Cliente);
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

    }

    public void Register(View view) {
        String type = "";
        if (r1.isChecked()) {
            type = r1.getText().toString();
        }
        if (r2.isChecked()) {
            type = r2.getText().toString();
        }
        if (r3.isChecked()) {
            type = r3.getText().toString();
        }
        if (Vacios().length() == 0) {
            if (login_exists(usuario.getText().toString())) {
                Toast.makeText(this, "Nombre de usuario no disponible", Toast.LENGTH_SHORT).show();
            } else {
                if (dni_exists(dni.getText().toString())) {
                    Toast.makeText(this, "El DNI/NIF: " + dni.getText().toString() + " ya existe en InfoTrans", Toast.LENGTH_LONG).show();
                } else {
                    if (dni.length() < 9 || dni.length() > 9) {
                        Toast.makeText(this, "El DNI/NIF debe contener 9 caracteres", Toast.LENGTH_LONG).show();
                    } else {
                        if (!dni_correct(dni.getText().toString())) {
                            Toast.makeText(this, "El DNI/NIF debe de contener 8 numeros y una letra", Toast.LENGTH_LONG).show();
                        } else {
                            if (contraseña.getText().toString().length() < 6) {
                                Toast.makeText(this, "La contraseña debe de tener al menos 8 caracteres", Toast.LENGTH_SHORT).show();
                            } else {
                                if (!Numeros(contraseña.getText().toString())) {
                                    Toast.makeText(this, "Debe de haber algun numero en la contraseña", Toast.LENGTH_LONG).show();
                                } else {
                                    if (!Mayuscula(contraseña.getText().toString())) {
                                        Toast.makeText(this, "Debe de haber alguna mayuscula en la contraseña", Toast.LENGTH_LONG).show();
                                    } else {
                                        if (contraseña.getText().toString().equals(r_contraseña.getText().toString())) {
                                            refDB.child(type).push().setValue(new Registro_usuario(usuario.getText().toString(),
                                                    contraseña.getText().toString(), dni.getText().toString(), nombre.getText().toString(), type));
                                            dni.setText(null);
                                            nombre.setText(null);
                                            usuario.setText(null);
                                            contraseña.setText(null);
                                            r_contraseña.setText(null);
                                            Toast.makeText(this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(this, "Ambas contraseñas tienen que ser iguales", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            Toast.makeText(this, Vacios(), Toast.LENGTH_LONG).show();
        }

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

        if (!r1.isChecked() && !r2.isChecked() && !r3.isChecked())
            msg += "Hay que seleccionar un tipo \n";
        if (dni.getText().toString().trim().isEmpty()) msg += "DNI/NIF es un campo obligatorio \n";
        if (nombre.getText().toString().trim().isEmpty())
            msg += "Nombre es un campo obligatorio \n";
        if (usuario.getText().toString().trim().isEmpty())
            msg += "Login es un campo obligatorio \n";
        if (contraseña.getText().toString().trim().isEmpty())
            msg += "Contraseña es un campo obligatorio \n";
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
            nums += String.valueOf(i);
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
