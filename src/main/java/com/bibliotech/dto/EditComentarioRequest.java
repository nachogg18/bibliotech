package com.bibliotech.dto;

import java.time.Instant;

public record EditComentarioRequest(
    Integer calificacion, String comentario, Instant fechaAlta, Instant fechaBaja) {}
