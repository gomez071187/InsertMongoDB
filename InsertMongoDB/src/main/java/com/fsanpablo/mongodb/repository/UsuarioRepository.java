package com.fsanpablo.mongodb.repository;

import java.io.Serializable;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.fsanpablo.mongodb.document.Usuario;

@Repository
public interface UsuarioRepository extends MongoRepository<Usuario, Serializable> {

}
