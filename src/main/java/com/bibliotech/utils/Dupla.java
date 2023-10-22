package com.bibliotech.utils;


import lombok.Data;

@Data
public class Dupla<A, B> {

    private A primero;
    private B segundo;

  public Dupla(A primero, B segundo) {
    this.primero = primero;
    this.segundo = segundo;
  }
}