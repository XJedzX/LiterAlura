package com.challenge.literAlura.repository;

import com.challenge.literAlura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LibroRepository  extends JpaRepository<Libro,Long> {
    Libro findByTitulo(String titulo);

    @Query("SELECT l FROM Libro l WHERE l.idioma =:idioma")
    List<Libro> librosPorIdioma(String idioma);

    @Query("SELECT l FROM Libro l ORDER BY l.numeroDescargas DESC LIMIT 10")
    List<Libro> findTop10BynumeroDescargas();

}
