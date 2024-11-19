package festivos.festivos.dominio.interfaces;

import java.time.LocalDate;
import java.util.List;

import festivos.festivos.dominio.DTO.FestivoDTO;
import festivos.festivos.dominio.entidades.Festivo;

public interface IFestivoServicio {
/**
     * Obtiene la lista de festivos para un año específico
     * @param year el año para el cual se quieren obtener los festivos
     * @return Lista de FestivoDTO con los festivos del año
     */
    List<FestivoDTO> obtenerFestivos(int year);

    /**
     * Verifica si una fecha específica es festiva
     * @param fecha la fecha a verificar
     * @return true si la fecha es festiva, false en caso contrario
     */
    boolean esFestivo(LocalDate fecha);
}
