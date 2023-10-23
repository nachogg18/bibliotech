package com.bibliotech.utils;


import lombok.Data;

@Data
public class Tripla<A, B, C> {

    private A primero;
    private B segundo;
    private C tercero;

    public Tripla(A primero, B segundo, C tercero) {
        this.primero = primero;
        this.segundo = segundo;
        this.tercero = tercero;
    }
}