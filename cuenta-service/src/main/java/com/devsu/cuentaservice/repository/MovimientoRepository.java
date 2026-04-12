package com.devsu.cuentaservice.repository;

import com.devsu.cuentaservice.entity.Cuenta;
import com.devsu.cuentaservice.entity.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {

    /**
     * Obtiene el último movimiento asociado a una cuenta,
     * ordenado por fecha descendente.
     *
     * @param cuenta la cuenta a consultar
     * @return un Optional con el último movimiento
     */
    Optional<Movimiento> findTopByCuentaOrderByFechaDesc(Cuenta cuenta);

    /**
     * Obtiene los movimientos de una cuenta dentro de un rango de fechas.
     *
     * @param cuenta      la cuenta a consultar
     * @param fechaInicio inicio del rango
     * @param fechaFin    fin del rango
     * @return lista de movimientos en el rango
     */
    List<Movimiento> findByCuentaAndFechaBetween(Cuenta cuenta,
                                                 LocalDateTime fechaInicio,
                                                 LocalDateTime fechaFin);

    /**
     * Obtiene todos los movimientos asociados a una cuenta.
     *
     * @param cuenta la cuenta a consultar
     * @return lista de movimientos
     */
    List<Movimiento> findByCuenta(Cuenta cuenta);

    /**
     * Obtiene los movimientos de una cuenta ordenados por fecha descendente.
     *
     * @param cuenta la cuenta a consultar
     * @return lista de movimientos ordenados
     */
    @Query("""
           SELECT m 
           FROM Movimiento m 
           WHERE m.cuenta = :cuenta 
           ORDER BY m.fecha DESC
           """)
    List<Movimiento> findMovimientosOrdenados(Cuenta cuenta);
}
