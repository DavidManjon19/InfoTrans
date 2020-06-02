package com.example.infotrans;


public class Pedidos {
    String clave_ref, producto, nom_cliente, nom_empresa,fecha_realización,estado;

    int cantidad;



    public Pedidos() {
    }

    public Pedidos(String clave_ref, String producto, String nom_cliente, String nom_empresa, String fecha_realización, int cantidad,String estado) {

        this.clave_ref = clave_ref;

        this.producto = producto;

        this.nom_cliente = nom_cliente;

        this.nom_empresa = nom_empresa;

        this.fecha_realización = fecha_realización;

        this.cantidad = cantidad;
        this.estado=estado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getClave_ref() {
        return clave_ref;
    }

    public void setClave_ref(String clave_ref) {
        this.clave_ref = clave_ref;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public String getNom_cliente() {
        return nom_cliente;
    }

    public void setNom_cliente(String nom_cliente) {
        this.nom_cliente = nom_cliente;
    }

    public String getNom_empresa() {
        return nom_empresa;
    }

    public void setNom_empresa(String nom_empresa) {
        this.nom_empresa = nom_empresa;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getFecha_realización() {
        return fecha_realización;
    }

    public void setFecha_realización(String fecha_realización) {
        this.fecha_realización = fecha_realización;
    }
}
