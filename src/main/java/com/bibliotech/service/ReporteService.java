package com.bibliotech.service;

import com.bibliotech.dto.FechasPrestamoDTO;
import net.sf.jasperreports.engine.JRException;

import java.io.IOException;


public interface ReporteService{

    byte[] prestamoInformeExportPdf(FechasPrestamoDTO fechas) throws IOException, JRException;
    byte[] tiemposInformeExportPdf(FechasPrestamoDTO fechas) throws  IOException, JRException;
    byte[] multasInformeExportPdf(FechasPrestamoDTO fechas) throws  IOException, JRException;
    byte[] existenciasInformeExportPdf() throws  IOException, JRException;

}
