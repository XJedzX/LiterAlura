package com.challenge.literAlura.principal;

import com.challenge.literAlura.model.*;
import com.challenge.literAlura.repository.AutorRepository;
import com.challenge.literAlura.repository.LibroRepository;
import com.challenge.literAlura.service.ConsumoAPI;
import com.challenge.literAlura.service.ConvertirDatos;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private Scanner teclado=new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "https://gutendex.com/books/?search=";
    private ConvertirDatos conversor = new ConvertirDatos();
     private LibroRepository repositorio;
    private AutorRepository repositorioAutor;
    private List<Libro> libros = new ArrayList<Libro>();
    private List<Autor> autores = new ArrayList<>();

    public Principal(LibroRepository repository, AutorRepository autorRepository) {
        this.repositorio=repository;
        this.repositorioAutor=autorRepository;
    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    *********************************************************
                    Bienvenido Escoja una opcion
                    
                    1 - Buscar libro por titulo 
                    2 - Listar libros Registrados
                    3 - Listar autores Registrados
                    4 - Listar autores vivos en determinado año
                    5 - Listar libros por idioma
                    6 - Listar Top 10 Libros Descargados
                                       
                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarLibroPorTitulo();
                    break;
                case 2:
                    listarLibrosRegistrados();
                    break;
                case 3:
                    listarAutoresRegistrados();
                    break;
                case 4:
                    listarAutoresVivosAño();
                    break;
                case 5:
                    listarLibrosPorIdioma();
                    break;
                case 6:
                    top10LibrosMasDescargados();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }
    }

    private void getDatosLibros(){
        System.out.println("Escribe el nombre del libro que deseas buscar");
        var tituloLibro = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + tituloLibro.replace(" ", "+"));
        var datos = conversor.obtenerDatos(json, Datos.class);

        if (!datos.datosLibros().isEmpty()) {
            DatosLibro datosLibro = datos.datosLibros().get(0);
            DatosAutor datosAutor = datosLibro.autor().get(0);
            Libro libro = null;

            Libro libroDb = repositorio.findByTitulo(datosLibro.titulo());
            if (libroDb != null) {
                System.out.println("No se puede registrar el mismo libro dos veces" );
            } else {
                Autor autorDb = repositorioAutor.findByNombreIgnoreCase(datosLibro.autor().get(0).nombre());
                if (autorDb == null) {
                    Autor autor = new Autor(datosAutor);
                    autor = repositorioAutor.save(autor);
                    libro = new Libro(datosLibro, autor);
                    repositorio.save(libro);
                    System.out.println(libro);
                } else {
                    libro = new Libro(datosLibro, autorDb);
                    repositorio.save(libro);
                    System.out.println(libro);
                }
            }
        } else {
            System.out.println("Libro no encontrado");
        }
    }

    private void buscarLibroPorTitulo() {
       getDatosLibros();

    }
    private void listarLibrosRegistrados() {
        libros = repositorio.findAll();
        libros.forEach(System.out::println);
    }

    private void listarAutoresRegistrados() {
        autores = repositorioAutor.findAll();
        autores.forEach(l-> System.out.println(
                "Autor: " + l.getNombre() +
                        "\nFecha de nacimiento: " + l.getFechaNacimiento() +
                        "\nFecha de fallecimiento: " + l.getFechaFallecimiento() +
                        "\nLibros: " + l.getLibros().stream()
                        .map(t -> t.getTitulo()).collect(Collectors.toList()) +"\n"));
    }
    private void listarAutoresVivosAño() {
            System.out.println("Escriba el año para mostrar autor(es) vivos");
        try {
            Integer fecha = teclado.nextInt();

                List<Autor> autoresPorFecha = repositorioAutor.autoresPorFecha(fecha);
                if (autoresPorFecha.isEmpty()) {
                    System.out.println("no hay autores en esa fecha");
                } else {
                    autoresPorFecha.forEach(l-> System.out.println(
                            "Autor: " + l.getNombre() +
                             "\nFecha de nacimiento: " + l.getFechaNacimiento() +
                             "\nFecha de fallecimiento: " + l.getFechaFallecimiento() +
                             "\nLibros: " + l.getLibros().stream()
                                    .map(t -> t.getTitulo()).collect(Collectors.toList()) +"-\n"));
                }
            } catch (Exception e) {
                teclado.nextLine();
                System.out.println("opcion invalida");
            }
    }
    private void listarLibrosPorIdioma() {
        System.out.println("seleccione el idioma por el que desea filtrar");
        System.out.println("""   
                                        es - Español
                                        en - Ingles
                                        fr - Frances
                                        pt - Portugues
                """);
        var idioma = teclado.nextLine();
        try {
            libros = repositorio.librosPorIdioma(idioma);
            if (libros.isEmpty()) {
                System.out.println("no hay libros en ese idioma");
            } else {
                libros.forEach(System.out::println);
            }
        } catch (Exception e) {
            System.out.println("opcion invalida");
        }
    }

    private void top10LibrosMasDescargados() {
        System.out.println("Top 10 Libros Más Descargados\n");
        libros=repositorio.findTop10BynumeroDescargas();
        libros.forEach(System.out::println);
    }
}