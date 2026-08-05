package Model;

public class Alumno {
    private int idAlumno;
    private String nombre;
    private String apellido;
    private String correo;
    private int matricula;
    private String password;
    private String domicilio;
    private String localidad;
    private String telefono;
    private int edad;

    public Alumno() {}

    public Alumno(int idAlumno, String nombre, String apellido, String correo, int matricula,
                  String password, String domicilio, String localidad, String telefono, int edad) {
        this.idAlumno = idAlumno;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.matricula = matricula;
        this.password = password;
        this.domicilio = domicilio;
        this.localidad = localidad;
        this.telefono = telefono;
        this.edad = edad;
    }

    public int getIdAlumno() { return idAlumno; }
    public void setIdAlumno(int idAlumno) { this.idAlumno = idAlumno; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public int getMatricula() { return matricula; }
    public void setMatricula(int matricula) { this.matricula = matricula; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getDomicilio() { return domicilio; }
    public void setDomicilio(String domicilio) { this.domicilio = domicilio; }

    public String getLocalidad() { return localidad; }
    public void setLocalidad(String localidad) { this.localidad = localidad; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }

    public String getNombreCompleto() { return nombre + " " + apellido; }
}
