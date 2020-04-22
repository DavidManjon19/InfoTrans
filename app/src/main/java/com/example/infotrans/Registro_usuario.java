package com.example.infotrans;

public class Registro_usuario {
    String login, contraseña, tipo, nombre, dni;

    public Registro_usuario() {
    }

    public Registro_usuario(String login, String contraseña) {
        this.login = login;
        this.contraseña = contraseña;
    }

    public Registro_usuario(String usuario, String contra, String Dni, String Nombre, String type) {
        this.login = usuario;
        this.contraseña = contra;
        this.dni = Dni;
        this.nombre = Nombre;
        this.tipo = type;
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

}
