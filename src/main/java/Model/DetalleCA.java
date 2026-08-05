package Model;

public class DetalleCA {
    private int idCurso;
    private int idAlumno;
    private Double calificacion;

    // Campos de conveniencia (para mostrar en tablas, provienen de JOIN)
    private String nombreAlumno;
    private String nombreCurso;

    public DetalleCA() {}

    public DetalleCA(int idCurso, int idAlumno, Double calificacion) {
        this.idCurso = idCurso;
        this.idAlumno = idAlumno;
        this.calificacion = calificacion;
    }

    public int getIdCurso() { return idCurso; }
    public void setIdCurso(int idCurso) { this.idCurso = idCurso; }

    public int getIdAlumno() { return idAlumno; }
    public void setIdAlumno(int idAlumno) { this.idAlumno = idAlumno; }

    public Double getCalificacion() { return calificacion; }
    public void setCalificacion(Double calificacion) { this.calificacion = calificacion; }

    public String getNombreAlumno() { return nombreAlumno; }
    public void setNombreAlumno(String nombreAlumno) { this.nombreAlumno = nombreAlumno; }

    public String getNombreCurso() { return nombreCurso; }
    public void setNombreCurso(String nombreCurso) { this.nombreCurso = nombreCurso; }
}
