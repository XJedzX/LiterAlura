package com.challenge.literAlura.model;

import jakarta.persistence.*;

@Entity
@Table(name="Libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(unique = true)
    private String titulo;
    private String idioma;
    private Integer numeroDescargas;
    @ManyToOne
    private Autor autor;



    public Libro() {
    }

    public Libro(DatosLibro datosLibro, Autor autor) {

        this.titulo = datosLibro.titulo();
        this.autor=autor;
        this.idioma =datosLibro.idioma().get(0);
        this.numeroDescargas = datosLibro.numeroDescargas();;
    }
    @Override
    public String toString() {
        return
         ", \nTitulo='" + titulo + '\'' +
         ", \nAutor=" + autor +
         ", \nIdioma=" + idioma +
         ", \nCantidadDescargas=" + numeroDescargas;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Integer getNumeroDescargas() {
        return numeroDescargas;
    }

    public void setNumeroDescargas(Integer numeroDescargas) {
        this.numeroDescargas = numeroDescargas;
    }
}
