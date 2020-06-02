package com.example.infotrans;

public class Comp_Empresas {
    String NIF;
    String Nombre;
    String Fecha;
    public Comp_Empresas(){
    }
    public Comp_Empresas(String NIF, String nombre, String fecha) {
        this.NIF = NIF;
        Nombre = nombre;
        Fecha = fecha;
    }

    public String getNIF() {
        return NIF;
    }

    public void setNIF(String NIF) {
        this.NIF = NIF;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }
}
