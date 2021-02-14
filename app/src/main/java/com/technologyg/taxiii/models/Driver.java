package com.technologyg.taxiii.models;

public class Driver {
    // Elementos del primer registro
    String id;
    String email;
    String name;
    String apePat;
    String apeMat;
    String tel;
    String pass;

    public Driver(String id, String email, String name, String apePat, String apeMat, String tel, String pass) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.apePat = apePat;
        this.apeMat = apeMat;
        this.tel = tel;
        this.pass = pass;
    }

    public Driver() {
    }

    // Elementos del segundo registro
    String IFE;
    String Licencia;
    String Tarjeta_Circulacion;

    public Driver(String IFE, String lic, String target, String seg, String RFC) {
        this.IFE = IFE;
        this.Licencia = lic;
        this.Tarjeta_Circulacion = target;
        this.Seguro = seg;
        this.RFC = RFC;
    }

    //Elementos del tercer registro.

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApePat() {
        return apePat;
    }

    public void setApePat(String apePat) {
        this.apePat = apePat;
    }

    public String getApeMat() {
        return apeMat;
    }

    public void setApeMat(String apeMat) {
        this.apeMat = apeMat;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getIFE() {
        return IFE;
    }

    public void setIFE(String IFE) {
        this.IFE = IFE;
    }

    public String getLicencia() {
        return Licencia;
    }

    public void setLicencia(String licencia) {
        Licencia = licencia;
    }

    public String getTarjeta_Circulacion() {
        return Tarjeta_Circulacion;
    }

    public void setTarjeta_Circulacion(String tarjeta_Circulacion) {
        Tarjeta_Circulacion = tarjeta_Circulacion;
    }

    public String getSeguro() {
        return Seguro;
    }

    public void setSeguro(String seguro) {
        Seguro = seguro;
    }

    public String getRFC() {
        return RFC;
    }

    public void setRFC(String RFC) {
        this.RFC = RFC;
    }

    String Seguro;
    String RFC;


}
