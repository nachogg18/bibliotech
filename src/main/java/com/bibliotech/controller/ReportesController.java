package com.bibliotech.controller;

import com.bibliotech.dto.FechasPrestamoDTO;
import com.bibliotech.service.EdicionService;
import com.bibliotech.service.ReporteService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.sf.jasperreports.engine.JRException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.IOException;

@RestController
@RequestMapping(path = "/api/v1/reportes")
@SecurityRequirement(name = "bearer-key")
@RequiredArgsConstructor
public class ReportesController {

    private final ReporteService reporteService;


    @PostMapping("/informe-prestamo/export-pdf")
    public ResponseEntity<byte[]> pretamosInforme(@RequestBody FechasPrestamoDTO fechas) throws JRException, IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("informe_prestamo", "informe_prestamo.pdf");
        return ResponseEntity.ok().headers(headers).body(reporteService.prestamoInformeExportPdf(fechas));
    }

    @PostMapping("/informe-tiempo/export-pdf")
    public ResponseEntity<byte[]> tiemposInforme(@RequestBody FechasPrestamoDTO fechas) throws JRException, IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("informe_tiempo", "informe_tiempo.pdf");
        return ResponseEntity.ok().headers(headers).body(reporteService.tiemposInformeExportPdf(fechas));
    }


    @PostMapping("/informe-multa/export-pdf")
    public ResponseEntity<byte[]> multasInforme(@RequestBody FechasPrestamoDTO fechas) throws JRException, IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("informe_multa", "informe_multa.pdf");
        return ResponseEntity.ok().headers(headers).body(reporteService.multasInformeExportPdf(fechas));
    }

    @PostMapping("/informe-existencias/export-pdf")
    public ResponseEntity<byte[]> existenciasInforme() throws JRException, IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("informe_existencias", "informe_existencias.pdf");
        return ResponseEntity.ok().headers(headers).body(reporteService.existenciasInformeExportPdf());
    }
}
