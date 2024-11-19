package festivos.festivos.dominio.servicios;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import festivos.festivos.dominio.DTO.FestivoDTO;
import festivos.festivos.dominio.entidades.Festivo;
import festivos.festivos.dominio.interfaces.IFestivoServicio;
import festivos.festivos.dominio.repositorios.FestivoRepositorio;
import festivos.festivos.dominio.util.PascuaCalculador;

@Service
public class FestivoServicio implements IFestivoServicio {
    
    private final FestivoRepositorio repositorio;
    private final PascuaCalculador pascuaCalculador;

    public FestivoServicio(FestivoRepositorio repositorio, PascuaCalculador pascuaCalculador) {
        this.repositorio = repositorio;
        this.pascuaCalculador = pascuaCalculador;
    }

    @Override
    public List<FestivoDTO> obtenerFestivos(int year) {
        List<Festivo> festivos = repositorio.findAll();
        calcularFestivos(festivos, year);
        return convertirADTO(festivos);
    }

    @Override
    public boolean esFestivo(LocalDate fecha) {
        List<Festivo> festivos = repositorio.findAll();
        calcularFestivos(festivos, fecha.getYear());
        
        return festivos.stream()
                .anyMatch(festivo -> festivo.getFecha().equals(fecha));
    }

    protected void calcularFestivos(List<Festivo> festivos, int year) {
        if (festivos == null) return;
        
        LocalDate pascua = pascuaCalculador.calcularDomingoPascua(year);
        festivos.forEach(festivo -> calcularFechaFestivo(festivo, pascua));
    }

    public enum TipoFestivo {
        FIJO(1L),
        PUENTE(2L),
        PASCUA(3L),
        PASCUA_PUENTE(4L);
    
        private final Long id;
    
        TipoFestivo(Long id) {
            this.id = id;
        }
    
        public Long getId() {
            return id;
        }
    
        public static TipoFestivo fromId(Long id) {
            for (TipoFestivo tipo : values()) {
                if (tipo.getId().equals(id)) {
                    return tipo;
                }
            }
            throw new IllegalArgumentException("Tipo de festivo no válido: " + id);
        }
    }
    
    protected void calcularFechaFestivo(Festivo festivo, LocalDate pascua) {
        TipoFestivo tipo = TipoFestivo.fromId(festivo.getTipo().getId());
        
        switch (tipo) {
            case FIJO -> calcularFestivoFijo(festivo, pascua.getYear());
            case PUENTE -> calcularFestivoPuente(festivo, pascua.getYear());
            case PASCUA -> calcularFestivoPascua(festivo, pascua);
            case PASCUA_PUENTE -> calcularFestivoPascuaPuente(festivo, pascua);
        }
    }

    private void calcularFestivoFijo(Festivo festivo, int year) {
        festivo.setFecha(LocalDate.of(year, festivo.getMes(), festivo.getDia()));
    }

    private void calcularFestivoPuente(Festivo festivo, int year) {
        LocalDate fecha = LocalDate.of(year, festivo.getMes(), festivo.getDia());
        festivo.setFecha(ajustarALunes(fecha));
    }

    private void calcularFestivoPascua(Festivo festivo, LocalDate pascua) {
        festivo.setFecha(pascua.plusDays(festivo.getDiasPascua()));
    }

    private void calcularFestivoPascuaPuente(Festivo festivo, LocalDate pascua) {
        LocalDate fecha = pascua.plusDays(festivo.getDiasPascua());
        festivo.setFecha(ajustarALunes(fecha));
    }

    private LocalDate ajustarALunes(LocalDate fecha) {
        if (fecha.getDayOfWeek() != DayOfWeek.MONDAY) {
            return fecha.with(DayOfWeek.MONDAY);
        }
        return fecha;
    }

    private List<FestivoDTO> convertirADTO(List<Festivo> festivos) {
        List<FestivoDTO> fechas = new ArrayList<>();
        festivos.forEach(festivo -> 
            fechas.add(new FestivoDTO(festivo.getNombre(), festivo.getFecha()))
        );
        return fechas;
    }
}