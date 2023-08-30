package com.bibliotech.security.service;

import com.bibliotech.entity.Usuario;
import com.bibliotech.repository.UsuarioRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String userName, password;
        List<GrantedAuthority> authorities;
        List<Usuario> usuarios = usuarioRepository.findByEmail(username);
        if (usuarios.size() == 0) {
            throw new UsernameNotFoundException("User details not found for the user : " + username);
        } else{
            userName = usuarios.get(0).getEmail();
            password = usuarios.get(0).getPwd();
            authorities = new ArrayList<>();
            authorities.addAll(usuarios.get(0).getRoles().stream().map(
                    rol -> new SimpleGrantedAuthority(rol.getName())
            ).collect(Collectors.toList()));
        }
        return new User(userName,password,authorities);
    }

}
