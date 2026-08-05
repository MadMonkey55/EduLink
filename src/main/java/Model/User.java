package Model;

public class User {
    private int id;
    private String nombre;
    private String apellido;
    private String password;
    private String correo;
    private int matricula;
    private String domicilio;
    private String localidad;
    private String telefono;
    private int edad;
    private String rol;

    public User() {}

    public User(int id, String nombre, String apellido, String password, String correo, int matricula, String domicilio, String localidad, String telefono, int edad, String rol) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.password = password;
        this.correo = correo;
        this.matricula = matricula;
        this.domicilio = domicilio;
        this.localidad = localidad;
        this.telefono = telefono;
        this.edad = edad;
        this.rol = rol;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String nombre_completo (String nombre, String apellido){
        this.nombre = nombre;
        this.apellido = apellido;
        String nombreCompleto = (nombre + " " + apellido);
        return nombreCompleto;
    }
}
