package Model;

import java.sql.Date;

public class Curso {
    private int idCurso;
    private String nombre;
    private String nivel;
    private String descripcion;
    private int idMaestro;
    private String nombreMaestro; // conveniencia para mostrar en tablas (join)
    private Date fDeInicio;
    private Date fDeFinalizacion;
    private byte[] imgCurso;

    public Curso() {
    }

    public Curso(int idCurso, String nombre, String nivel, String descripcion,
                 int idMaestro, Date fDeInicio, Date fDeFinalizacion, byte[] imgCurso) {
        this.idCurso = idCurso;
        this.nombre = nombre;
        this.nivel = nivel;
        this.descripcion = descripcion;
        this.idMaestro = idMaestro;
        this.fDeInicio = fDeInicio;
        this.fDeFinalizacion = fDeFinalizacion;
        this.imgCurso = imgCurso;
    }

    public int getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(int idCurso) {
        this.idCurso = idCurso;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getIdMaestro() {
        return idMaestro;
    }

    public void setIdMaestro(int idMaestro) {
        this.idMaestro = idMaestro;
    }

    public Date getfDeInicio() {
        return fDeInicio;
    }

    public void setfDeInicio(Date fDeInicio) {
        this.fDeInicio = fDeInicio;
    }

    public Date getfDeFinalizacion() {
        return fDeFinalizacion;
    }

    public void setfDeFinalizacion(Date fDeFinalizacion) {
        this.fDeFinalizacion = fDeFinalizacion;
    }

    public byte[] getImgCurso() {
        return imgCurso;
    }

    public void setImgCurso(byte[] imgCurso) {
        this.imgCurso = imgCurso;
    }

    public String getNombreMaestro() {
        return nombreMaestro;
    }

    public void setNombreMaestro(String nombreMaestro) {
        this.nombreMaestro = nombreMaestro;
    }

    @Override
    public String toString() {
        return nombre + " (" + nivel + ")";
    }
}
