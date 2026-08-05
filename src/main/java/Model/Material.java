package Model;

public class Material {
    private int idMaterial;
    private int idCurso;
    private int idMaestro;
    private String nombre;
    private byte[] archivo;
    private String curso;       // nombre del curso (columna redundante de la BD)
    private String descripcion;
    private int leccion;

    public Material() {}

    public Material(int idMaterial, int idCurso, int idMaestro, String nombre, byte[] archivo,
                     String curso, String descripcion, int leccion) {
        this.idMaterial = idMaterial;
        this.idCurso = idCurso;
        this.idMaestro = idMaestro;
        this.nombre = nombre;
        this.archivo = archivo;
        this.curso = curso;
        this.descripcion = descripcion;
        this.leccion = leccion;
    }

    public int getIdMaterial() { return idMaterial; }
    public void setIdMaterial(int idMaterial) { this.idMaterial = idMaterial; }

    public int getIdCurso() { return idCurso; }
    public void setIdCurso(int idCurso) { this.idCurso = idCurso; }

    public int getIdMaestro() { return idMaestro; }
    public void setIdMaestro(int idMaestro) { this.idMaestro = idMaestro; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public byte[] getArchivo() { return archivo; }
    public void setArchivo(byte[] archivo) { this.archivo = archivo; }

    public String getCurso() { return curso; }
    public void setCurso(String curso) { this.curso = curso; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public int getLeccion() { return leccion; }
    public void setLeccion(int leccion) { this.leccion = leccion; }

    @Override
    public String toString() { return "Leccion " + leccion + " - " + nombre; }
}
