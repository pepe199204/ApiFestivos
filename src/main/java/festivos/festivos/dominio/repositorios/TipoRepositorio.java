package festivos.festivos.dominio.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import festivos.festivos.dominio.entidades.Tipo;

public interface TipoRepositorio extends JpaRepository<Tipo, Long> {
    
}
