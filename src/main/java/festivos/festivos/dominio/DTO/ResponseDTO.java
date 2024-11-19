package festivos.festivos.dominio.DTO;

public class ResponseDTO {
    private String mensaje;

    public ResponseDTO(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}