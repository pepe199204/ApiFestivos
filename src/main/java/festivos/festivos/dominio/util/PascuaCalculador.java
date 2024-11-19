package festivos.festivos.dominio.util;

import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Component
public class PascuaCalculador {
    private static final int[][] TABLA_PASCUAL = {
        {1583, 1699, 22, 2},
        {1700, 1799, 23, 3},
        {1800, 1899, 23, 4},
        {1900, 2099, 24, 5},
        {2100, 2199, 24, 6},
        {2200, 2299, 25, 0}
    };

    public LocalDate calcularDomingoPascua(int year) {
        int[] parametros = obtenerParametrosPascua(year);
        if (parametros == null) {
            throw new IllegalArgumentException("Año fuera de rango para cálculo de pascua");
        }

        int M = parametros[0];
        int N = parametros[1];

        int A = year % 19;
        int B = year % 4;
        int C = year % 7;
        int D = ((19 * A) + M) % 30;
        int E = ((2 * B) + (4 * C) + (6 * D) + N) % 7;

        int dia = (D + E < 10) ? D + E + 22 : D + E - 9;
        int mes = (D + E < 10) ? 3 : 4;

        aplicarExcepcionesEspeciales(year, A, D, E, dia, mes);

        return LocalDate.of(year, mes, dia);
    }

    private int[] obtenerParametrosPascua(int year) {
        for (int[] rango : TABLA_PASCUAL) {
            if (year >= rango[0] && year <= rango[1]) {
                return new int[]{rango[2], rango[3]};
            }
        }
        return null;
    }

    private void aplicarExcepcionesEspeciales(int year, int A, int D, int E, int dia, int mes) {
        if (dia == 26 && mes == 4) dia = 19;
        if (dia == 25 && mes == 4 && D == 28 && E == 6 && A > 10) dia = 18;
    }
}