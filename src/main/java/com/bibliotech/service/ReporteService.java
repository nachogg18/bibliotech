package com.bibliotech.service;

import com.bibliotech.dto.FechasPrestamoDTO;
import net.sf.jasperreports.engine.JRException;

import java.io.IOException;


public interface ReporteService{

    public byte[] prestamoInformeExportPdf(FechasPrestamoDTO fechas) throws IOException, JRException;
    public byte[] tiemposInformeExportPdf(FechasPrestamoDTO fechas) throws  IOException, JRException;
    public byte[] multasInformeExportPdf(FechasPrestamoDTO fechas) throws  IOException, JRException;
    public byte[] existenciasInformeExportPdf() throws  IOException, JRException;

}
