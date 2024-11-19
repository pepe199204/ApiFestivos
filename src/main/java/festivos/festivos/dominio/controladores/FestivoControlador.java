package festivos.festivos.dominio.controladores;

import java.time.LocalDate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import festivos.festivos.dominio.DTO.FestivoDTO;
import festivos.festivos.dominio.DTO.ResponseDTO;
import festivos.festivos.dominio.interfaces.IFestivoServicio;
import java.util.List;

@RestController
@RequestMapping("/festivos")
@CrossOrigin(origins = "*")
public class FestivoControlador {
    
    private final IFestivoServicio servicio;

    public FestivoControlador(IFestivoServicio servicio) {
        this.servicio = servicio;
    }

    @GetMapping("/verificar/{year}/{mes}/{dia}")
    public ResponseEntity<?> verificarFestivo(
            @PathVariable int year,
            @PathVariable int mes,
            @PathVariable int dia) {
        try {
            validarFecha(year, mes, dia);
            LocalDate fecha = LocalDate.of(year, mes, dia);
            
            boolean esFestivo = servicio.esFestivo(fecha);
            String mensaje = esFestivo ? "Es Festivo" : "No es festivo";
            
            return ResponseEntity.ok()
                .body(new ResponseDTO(mensaje));
                
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(new ResponseDTO(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(new ResponseDTO("Error en el procesamiento de la fecha"));
        }
    }

    @GetMapping("/obtener/{year}")
    public ResponseEntity<List<FestivoDTO>> obtenerFestivos(@PathVariable int year) {
        return ResponseEntity.ok(servicio.obtenerFestivos(year));
    }

    private void validarFecha(int year, int mes, int dia) {
        if (mes < 1 || mes > 12) {
            throw new IllegalArgumentException("Mes no válido");
        }
        
        int diasEnMes = LocalDate.of(year, mes, 1).lengthOfMonth();
        if (dia < 1 || dia > diasEnMes) {
            throw new IllegalArgumentException("Día no válido para el mes y año especificados");
        }
    }
}