package com.bibliotech.dto;


import com.bibliotech.entity.Comentario;

record ComentarioResponse(String id, String calificacion, String comentario, String fechaAlta, String fechaBaja) {

    static ComentarioResponse comentarioToComentarioResponse(Comentario comentario) {
        return new ComentarioResponse(
                comentario.getId().toString(),
                comentario.getCalificacion().toString(),
                comentario.getComentario(),
                comentario.getFechaAlta().toString(),
                comentario.getFechaBaja().toString());
    }
}
