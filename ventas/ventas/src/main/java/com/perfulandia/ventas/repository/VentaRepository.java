package com.perfulandia.ventas.repository;

import com.perfulandia.ventas.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VentaRepository extends JpaRepository<Venta, Long> {
}
