package com.example.appmusic.controllers;

public class Canciones {
    public class Cancion {

        private String nombre;
        private String artista;
        private String duracion;

        public Cancion(String nombre,
                       String artista,
                       String duracion) {

            this.nombre = nombre;
            this.artista = artista;
            this.duracion = duracion;
        }

        public String getNombre() {
            return nombre;
        }

        public String getArtista() {
            return artista;
        }

        public String getDuracion() {
            return duracion;
        }
    }
}
