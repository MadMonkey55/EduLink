package Model;

import java.sql.Timestamp;

public class DetalleTareas {
    private int idDetalle;
    private int idTarea;
    private int idAlumno;
    private Timestamp fDeEntrega;
    private byte[] archivoEntrega;
    private String comentarios;
    private Double calificacion;

    // Campos de conveniencia (JOIN)
    private String nombreAlumno;
    private String nombreTarea;

    public DetalleTareas() {}

    public DetalleTareas(int idDetalle, int idTarea, int idAlumno, Timestamp fDeEntrega,
                          byte[] archivoEntrega, String comentarios, Double calificacion) {
        this.idDetalle = idDetalle;
        this.idTarea = idTarea;
        this.idAlumno = idAlumno;
        this.fDeEntrega = fDeEntrega;
        this.archivoEntrega = archivoEntrega;
        this.comentarios = comentarios;
        this.calificacion = calificacion;
    }

    public int getIdDetalle() { return idDetalle; }
    public void setIdDetalle(int idDetalle) { this.idDetalle = idDetalle; }

    public int getIdTarea() { return idTarea; }
    public void setIdTarea(int idTarea) { this.idTarea = idTarea; }

    public int getIdAlumno() { return idAlumno; }
    public void setIdAlumno(int idAlumno) { this.idAlumno = idAlumno; }

    public Timestamp getfDeEntrega() { return fDeEntrega; }
    public void setfDeEntrega(Timestamp fDeEntrega) { this.fDeEntrega = fDeEntrega; }

    public byte[] getArchivoEntrega() { return archivoEntrega; }
    public void setArchivoEntrega(byte[] archivoEntrega) { this.archivoEntrega = archivoEntrega; }

    public String getComentarios() { return comentarios; }
    public void setComentarios(String comentarios) { this.comentarios = comentarios; }

    public Double getCalificacion() { return calificacion; }
    public void setCalificacion(Double calificacion) { this.calificacion = calificacion; }

    public String getNombreAlumno() { return nombreAlumno; }
    public void setNombreAlumno(String nombreAlumno) { this.nombreAlumno = nombreAlumno; }

    public String getNombreTarea() { return nombreTarea; }
    public void setNombreTarea(String nombreTarea) { this.nombreTarea = nombreTarea; }
}
