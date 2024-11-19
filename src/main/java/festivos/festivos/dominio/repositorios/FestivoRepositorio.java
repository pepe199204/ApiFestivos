package festivos.festivos.dominio.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import festivos.festivos.dominio.entidades.Festivo;

@Repository
public interface FestivoRepositorio extends JpaRepository<Festivo, Long> {
    
}
