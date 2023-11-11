package com.bibliotech.service;

import com.bibliotech.dto.FechasPrestamoDTO;
import com.bibliotech.entity.EstadoEjemplar;
import com.bibliotech.repository.EjemplarRepository;
import com.bibliotech.repository.MultaRepository;
import com.bibliotech.utils.dto_reporte.*;
import com.bibliotech.repository.PrestamosRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.server.ResponseStatusException;

import java.io.*;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class ReporteServiceImpl implements ReporteService {

    private final PrestamosRepository prestamosRepository;
    private final MultaRepository multaRepository;
    private final EjemplarRepository ejemplarRepository;

    public byte[] prestamoInformeExportPdf(FechasPrestamoDTO fechas) {

        List<Object[]> resultados = prestamosRepository.getInformePrestamo(fechas.getFechaDesde(), fechas.getFechaHasta());

        try {

            List<ItemInformePrestamo> ds = new ArrayList<>();
            List<ItemInformePublicacion> ds_publicacion = new ArrayList<>();

            for (Object[] resultado : resultados) {
                String titulo = (String) resultado[0]; // TITULO
                Integer anio = (Integer) resultado[1]; // ANIO
                Long ejemplares =  (Long) resultado[2]; // EJEMPLARES
                String edicion = (String) resultado[3];
                Long prestamos = (Long) resultado[4]; // PRESTAMOS
                ds.add(
                        ItemInformePrestamo.builder()
                                .titulo(titulo)
                                .anio(anio)
                                .ejemplares(Math.toIntExact(ejemplares))
                                .edicion(edicion)
                                .prestamos(Math.toIntExact(prestamos))
                                .build()
                );
                if (ds_publicacion.size() < 2) {
                    ds_publicacion.add(
                            ItemInformePublicacion.builder()
                                    .cantidad(Math.toIntExact(prestamos))
                                    .publicacion(titulo)
                                    .build()
                    );
                }
            }


            List<ItemInformeTipoPublicacion> ds_tipo = new ArrayList<>();

            resultados = prestamosRepository.obtenerCantidadPorTipo(fechas.getFechaDesde(), fechas.getFechaHasta());

            for (Object[] resultado : resultados) {
                String tipo = (String) resultado[0]; // TIPO
                Long cantidad = (Long) resultado[1]; // CANTIDAD
                ds_tipo.add(
                        ItemInformeTipoPublicacion.builder()
                                .tipo(tipo)
                                .cantidad(Math.toIntExact(cantidad))
                                .build()
                );
            }

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("ds", new JRBeanCollectionDataSource(ds));
            params.put("ds_pub", new JRBeanCollectionDataSource(ds_publicacion));
            params.put("ds_tipo", new JRBeanCollectionDataSource(ds_tipo));
            params.put("fecha_desde", fechas.getFechaDesde());
            params.put("fecha_hasta", fechas.getFechaHasta());

            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("jasperReports/img/logo.png");
            params.put("logo_empresa", inputStream);

            JasperPrint report  = JasperFillManager.fillReport(
                    JasperCompileManager.compileReport(ResourceUtils.getFile("classpath:jasperReports/InformePrestamo2.jrxml").getAbsolutePath()),
                    params,
                    new JREmptyDataSource()
            );

            inputStream.close();

            return JasperExportManager.exportReportToPdf(report);


        } catch (JRException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Ha ocurrido un error al generar el reporte -> "+e.getMessage());

        } catch (FileNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"No se ha encontrado el archivo de reporte");

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Ha ocurrido un error al cargar datos");
        }

    }

    public byte[] tiemposInformeExportPdf(FechasPrestamoDTO fechas) {

        try {

            Map<String, Long> resultadosRenovacion = prestamosRepository.obtenerCantidadPrestamosSiNoRenovados(fechas.getFechaDesde(), fechas.getFechaHasta());


            List<ItemInformeRenovacion> ds_renovacion = new ArrayList<>();
            List<ItemInformeTiempo> ds_tiempo = new ArrayList<>();
            int total_prestamos = Math.toIntExact(resultadosRenovacion.get("PRESTAMOS RENOVADOS")) + Math.toIntExact(resultadosRenovacion.get("PRESTAMOS NO RENOVADOS"));

            ds_renovacion.add(
                    ItemInformeRenovacion.builder()
                            .prestamos_renovados(Math.toIntExact(resultadosRenovacion.get("PRESTAMOS RENOVADOS")))
                            .prestanos_no_renovados(Math.toIntExact(resultadosRenovacion.get("PRESTAMOS NO RENOVADOS")))
                            .build()
            );


            List<Map<String, Object>> resultadosTiempo = prestamosRepository.obtenerDiasPrestamo(fechas.getFechaDesde(), fechas.getFechaHasta());

            for (Map<String, Object> resultado : resultadosTiempo) {
                Long dias = (Long) resultado.get("DÍAS");
                Long cantidad = (Long) resultado.get("CANTIDAD");

                ds_tiempo.add(
                        ItemInformeTiempo.builder()
                                .cantidad(Math.toIntExact(cantidad))
                                .dias(Math.toIntExact(dias))
                                .total_prestamos(total_prestamos)
                                .build()
                );
            }

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("ds_renovacion", new JRBeanCollectionDataSource(ds_renovacion));
            params.put("ds_tiempo", new JRBeanCollectionDataSource(ds_tiempo));
            params.put("fecha_desde", fechas.getFechaDesde());
            params.put("fecha_hasta", fechas.getFechaHasta());
            params.put("total_prestamos", total_prestamos);

            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("jasperReports/img/logo.png");
            params.put("logo", inputStream);


            JasperPrint report  = JasperFillManager.fillReport(
                    JasperCompileManager.compileReport(ResourceUtils.getFile("classpath:jasperReports/TiemposReporte.jrxml").getAbsolutePath()),
                    params,
                    new JREmptyDataSource()
            );

            inputStream.close();

            return JasperExportManager.exportReportToPdf(report);


        } catch (JRException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Ha ocurrido un error al generar el reporte -> "+e.getMessage());

        } catch (FileNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"No se ha encontrado el archivo de reporte");

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Ha ocurrido un error al cargar datos");
        }

    }

    public byte[] multasInformeExportPdf(FechasPrestamoDTO fechas) throws  IOException, JRException {

        try {

            List<Map<String, Object>> resultadosTiempo = multaRepository.obtenerReporteCantidadMultaPorTipo(fechas.getFechaDesde(), fechas.getFechaHasta());
            List<Map<String, Object>> resultadosMulta = multaRepository.obtenerReporteDetallesMulta(fechas.getFechaDesde(), fechas.getFechaDesde());
            List<ItemInformeTipoMulta> ds_tipo = new ArrayList<>();
            List<ItemInformeMultaDetalle> ds_detalle = new ArrayList<>();

            for (Map<String, Object> resultado : resultadosTiempo) {
                String tipos = (String) resultado.get("TIPO DE MULTA");
                Long cantidad = (Long) resultado.get("CANTIDAD");
                ds_tipo.add(
                        ItemInformeTipoMulta.builder()
                                .cantidad(Math.toIntExact(cantidad))
                                .tipo(tipos)
                                .build()
                );
            }

            for (Map<String, Object> resultado : resultadosMulta) {
                Long id = (Long) resultado.get("ID");
                Date fecha_desde = (Date) resultado.get("FECHA INICIO");
                Date fecha_hasta = (Date) resultado.get("FECHA FIN");
                Long legajo = (Long) resultado.get("LEGAJO MULTA");
                String multa = (String) resultado.get("TIPO MULTA");
                ds_detalle.add(
                        ItemInformeMultaDetalle.builder()
                                .id(Math.toIntExact(id))
                                .legajo(Math.toIntExact(legajo))
                                .tipo(multa)
                                .fecha_inicio(fecha_desde.getYear() + "-" + fecha_desde.getMonth() + "-" + fecha_desde.getDay())
                                .fecha_fin(fecha_hasta.getYear() + "-" + fecha_hasta.getMonth() + "-" + fecha_hasta.getDay())
                                .build()
                );
            }

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("ds_tipo_multa", new JRBeanCollectionDataSource(ds_tipo));
            params.put("ds_detalle_multa", new JRBeanCollectionDataSource(ds_detalle));
            params.put("fecha_desde", fechas.getFechaDesde());
            params.put("fecha_hasta", fechas.getFechaHasta());

            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("jasperReports/img/logo.png");
            params.put("logo", inputStream);


            JasperPrint report  = JasperFillManager.fillReport(
                    JasperCompileManager.compileReport(ResourceUtils.getFile("classpath:jasperReports/MultasReporte.jrxml").getAbsolutePath()),
                    params,
                    new JREmptyDataSource()
            );

            inputStream.close();

            return JasperExportManager.exportReportToPdf(report);


        } catch (JRException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Ha ocurrido un error al generar el reporte -> "+e.getMessage());

        } catch (FileNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"No se ha encontrado el archivo de reporte");

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Ha ocurrido un error al cargar datos");
        }
    }

    public byte[] existenciasInformeExportPdf() throws  IOException, JRException {

        try {

            List<Map<String, Object>> resultadosExistencias = ejemplarRepository.obtenerReporteExistencias();
            List<ItemInformeExistencia> ds_existencias = new ArrayList<>();

            for (Map<String, Object> resultado : resultadosExistencias) {
                Long id = (Long) resultado.get("ID EJEMPLAR");
                String nfcSerial = (String) resultado.get("NCF SERIAL");
                String publicacion = (String) resultado.get("PUBLICACION");
                Byte estado = (Byte) resultado.get("ESTADO");
                ds_existencias.add(
                        ItemInformeExistencia.builder()
                                .idEjemplar(Math.toIntExact(id))
                                .nfcSerial(nfcSerial)
                                .publicacion(publicacion)
                                .estado(EstadoEjemplar.values()[Math.toIntExact(estado)].name())
                                .build()
                );
            }

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("ds_e", new JRBeanCollectionDataSource(ds_existencias));

            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("jasperReports/img/logo.png");
            params.put("logo", inputStream);


            JasperPrint report  = JasperFillManager.fillReport(
                    JasperCompileManager.compileReport(ResourceUtils.getFile("classpath:jasperReports/ExistenciasReporte.jrxml").getAbsolutePath()),
                    params,
                    new JREmptyDataSource()
            );

            inputStream.close();

            return JasperExportManager.exportReportToPdf(report);


        } catch (JRException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Ha ocurrido un error al generar el reporte -> "+e.getMessage());

        } catch (FileNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"No se ha encontrado el archivo de reporte");

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Ha ocurrido un error al cargar datos");
        }
    }
}
