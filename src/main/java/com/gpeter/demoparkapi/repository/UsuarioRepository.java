package com.gpeter.demoparkapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gpeter.demoparkapi.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}