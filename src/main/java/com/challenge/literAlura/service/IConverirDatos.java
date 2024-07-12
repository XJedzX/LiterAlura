package com.challenge.literAlura.service;

import java.util.List;

public interface IConverirDatos {
    <T> T obtenerDatos(String json, Class<T> clase);

}
