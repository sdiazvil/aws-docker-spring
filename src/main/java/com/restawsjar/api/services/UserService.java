package com.restawsjar.api.services;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import com.restawsjar.api.entity.User;
import com.restawsjar.api.interfaces.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    @Autowired
    private UserRepository repo;

    @Transactional(readOnly = true)
    public User buscar(int id) {
        return repo.findById(id).orElse(null);
    }

    @Transactional
    public User guardar(User user) {
        return repo.save(user);
    }

    @Transactional
    public User modificarUsuario(int id, User user) {
        User response = repo.findById(id).orElseThrow(() -> new EntityNotFoundException("NO SE ENCONTRÓ EL USUARIO"));
        response.setName(user.getName());
        response.setLastname(user.getLastname());
        response.setCorreo(user.getCorreo());
        return repo.save(response);
    }

    @Transactional
    public void eliminarUsuario(int id) {
        repo.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<User> listar() {
        return repo.findAll();
    }

    public Page<User> paginar(Pageable pageable) {
        return repo.findAll(pageable);
    }
}
