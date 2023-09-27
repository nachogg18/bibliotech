package com.bibliotech.entity;

import jakarta.persistence.*;
import lombok.*;

public enum TipoEvento {
    CREACION_EJEMPLAR,
    PERDIDA_EJEMPLAR,
    DESTRUCCION_EJEMPLAR,
    DESACTIVACION_EJEMPLAR,
    DAÑO_EJEMPLAR,
    OBSERVACION_EJEMPLAR

}
