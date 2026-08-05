package Model;

import java.sql.Date;

public class Tarea {
    private int idTarea;
    private String nombre;
    private String descripcion;
    private Date fHabil;
    private Date fLimit;
    private int idCurso;
    private int idMaestro;
    private byte[] archivoEnunciado;

    public Tarea() {}

    public Tarea(int idTarea, String nombre, String descripcion, Date fHabil, Date fLimit,
                 int idCurso, int idMaestro, byte[] archivoEnunciado) {
        this.idTarea = idTarea;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fHabil = fHabil;
        this.fLimit = fLimit;
        this.idCurso = idCurso;
        this.idMaestro = idMaestro;
        this.archivoEnunciado = archivoEnunciado;
    }

    public int getIdTarea() { return idTarea; }
    public void setIdTarea(int idTarea) { this.idTarea = idTarea; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Date getfHabil() { return fHabil; }
    public void setfHabil(Date fHabil) { this.fHabil = fHabil; }

    public Date getfLimit() { return fLimit; }
    public void setfLimit(Date fLimit) { this.fLimit = fLimit; }

    public int getIdCurso() { return idCurso; }
    public void setIdCurso(int idCurso) { this.idCurso = idCurso; }

    public int getIdMaestro() { return idMaestro; }
    public void setIdMaestro(int idMaestro) { this.idMaestro = idMaestro; }

    public byte[] getArchivoEnunciado() { return archivoEnunciado; }
    public void setArchivoEnunciado(byte[] archivoEnunciado) { this.archivoEnunciado = archivoEnunciado; }

    @Override
    public String toString() { return nombre; }
}
