package Model;

public class Maestro {
    private int idMaestro;
    private String nombre;
    private String apellido;
    private String correo;
    private String password;
    private String domicilio;
    private String localidad;
    private String telefono;
    private int edad;

    public Maestro() {}

    public Maestro(int idMaestro, String nombre, String apellido, String correo, String password,
                   String domicilio, String localidad, String telefono, int edad) {
        this.idMaestro = idMaestro;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.password = password;
        this.domicilio = domicilio;
        this.localidad = localidad;
        this.telefono = telefono;
        this.edad = edad;
    }

    public int getIdMaestro() { return idMaestro; }
    public void setIdMaestro(int idMaestro) { this.idMaestro = idMaestro; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

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

    @Override
    public String toString() { return getNombreCompleto(); }
}
