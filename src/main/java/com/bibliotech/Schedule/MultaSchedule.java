package com.bibliotech.Schedule;

import com.bibliotech.entity.*;
import com.bibliotech.repository.MultaRepository;
import com.bibliotech.security.entity.User;
import com.bibliotech.service.PrestamoService;
import com.bibliotech.service.TipoMultaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class MultaSchedule {

    @Autowired
    private PrestamoService prestamoService;

    @Autowired
    private TipoMultaService tipoMultaService;

    @Autowired
    private MultaRepository multaRepository;

//    @Scheduled(cron = "0 * * * * *")
//    @Transactional
//    public void multarPrestamosVencidos(){
//        List<Prestamo> prestamos = prestamoService.getPrestamosAMultar();
//        Optional<TipoMulta> tmVencido = tipoMultaService.findById(Long.parseLong("1"));
//        if (tmVencido.isEmpty()) return;
//        prestamos.forEach( prestamo -> {
//            Multa m = new Multa();
//            MultaEstado me = new MultaEstado();
//            me.setFechaInicio(Instant.now());
//            m.setPrestamo(prestamo);
//            m.setUser(prestamo.getUsuario());
//            m.setTipoMulta(tmVencido.get());
//            m.setFechaAlta(Instant.now());
//            if(multaRepository.obtenerMultaActivaByUser(prestamo.getUsuario()).isEmpty()){
//                m.setFechaInicio(Instant.now());
//                me.setEstadoMulta(EstadoMulta.ACTIVA);
//            } else {
//                me.setEstadoMulta(EstadoMulta.PENDIENTE);
//            }
//            List<MultaEstado> meList = m.getMultaEstados();
//            meList.add(me);
//            m.setMultaEstados(meList);
//            multaRepository.save(m);
//        });
//    }

//    private Optional<Multa> controlarMultasPendientesUsuario(User user){
//
//        List<Multa> multas = this.multaRepository.obtenerMultasByUserId(user.getId());
//        multas.stream().findFirst(multa -> {
//
//        }).
//        multas = multas.stream().filter(multa -> {
//            return !Objects.nonNull(multa.getFechaBaja());
//        }).toList();
//
//
//
//
//        return null;
//    }
}
