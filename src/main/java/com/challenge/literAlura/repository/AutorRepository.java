package com.challenge.literAlura.repository;

import com.challenge.literAlura.model.Autor;

import com.challenge.literAlura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface AutorRepository extends JpaRepository<Autor,Long> {
    Autor findByNombreIgnoreCase(String nombre);

    @Query("SELECT a FROM Autor a WHERE a.fechaFallecimiento > :fecha")
    List<Autor> autoresPorFecha(Integer fecha);
}
