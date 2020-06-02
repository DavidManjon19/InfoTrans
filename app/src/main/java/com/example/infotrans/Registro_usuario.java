package com.example.infotrans;

import android.net.Uri;

import com.google.android.gms.tasks.Task;

public class Registro_usuario {
    String login, contraseña, tipo, nombre, dni,provincia,email,empresa_perteneciente;
    int num_iden;
    Task<Uri> imagen;
    public Registro_usuario() {
    }

    public Registro_usuario(String email, String contraseña) {
        this.email = email;
        this.contraseña = contraseña;
    }


    public Registro_usuario(String usuario, String contra, String Dni, String Nombre, String type,String provincia,String email) {
        this.login = usuario;
        this.contraseña = contra;
        this.dni = Dni;
        this.nombre = Nombre;
        this.tipo = type;
        this.provincia=provincia;
        this.email=email;
    }

    public Registro_usuario(String usuario, String contra, String Dni, String Nombre, String type,String email) {
        this.login = usuario;
        this.contraseña = contra;
        this.dni = Dni;
        this.nombre = Nombre;
        this.tipo = type;
        this.email= email;
    }

    public Registro_usuario(String usuario, String contra, int numero_iden, String Nombre,String type,String email,String empresa) {
        this.login = usuario;
        this.contraseña = contra;
        this.num_iden = numero_iden;
        this.nombre = Nombre;
        this.tipo = type;
        this.email= email;
        this.empresa_perteneciente=empresa;
    }


    public String getEmpresa_perteneciente() {
        return empresa_perteneciente;
    }

    public void setEmpresa_perteneciente(String empresa_perteneciente) {
        this.empresa_perteneciente = empresa_perteneciente;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public Task<Uri> getImagen() {
        return imagen;
    }

    public void setImagen(Task<Uri> imagen) {
        this.imagen = imagen;
    }
}
