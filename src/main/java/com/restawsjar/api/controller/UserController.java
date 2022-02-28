package com.restawsjar.api.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.restawsjar.api.entity.User;
import com.restawsjar.api.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
// @RequestMapping(path = "/user")
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE,
        RequestMethod.PUT })
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/agregar")
    public ResponseEntity<?> agregar(@RequestBody User val) {
        Map<String, Object> response = new HashMap<>();
        User user = null;
        try {
            // return ResponseEntity.ok(repo.save(val));
            user = userService.guardar(val);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("mensaje", "El usuario ha sido creado con éxito");
        response.put("referencia", user);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @GetMapping("/listar")
    public ResponseEntity<?> listar() {
        // return repo.findAll();
        try {
            List<User> lista = new ArrayList<User>();
            lista = userService.listar();
            return new ResponseEntity<>(lista, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("No se puede conectar al servidor.", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/listar/pagina/{page}")
    public Page<User> paginar(@PathVariable Integer page) {
        return userService.paginar(PageRequest.of(page, 3));
    }

    @GetMapping("/ver/{val}")
    public ResponseEntity<?> ver(@PathVariable Integer val) {
        // return repo.findById(val);
        User user = null;
        Map<String, Object> response = new HashMap<>();
        try {
            user = userService.buscar(val);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }
        if (user == null) {
            response.put("mensaje", "El usuario Id: ".concat(val.toString().concat(" no existe en la base de datos")));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<User>(user, HttpStatus.OK);

    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @RequestBody User val) {
        // return repo.save(val);
        try {
            return ResponseEntity.ok(userService.modificarUsuario(id, val));
        } catch (Exception e) {
            return new ResponseEntity<>("NO SE ENCONTRÓ EL USUARIO", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/eliminar/{val}")
    public ResponseEntity<?> delete(@PathVariable Integer val) {
        // repo.deleteById(val);
        // return "Id : " + val + " delete";
        Map<String, Object> response = new HashMap<>();
        try {
            userService.eliminarUsuario(val);

        } catch (DataAccessException e) {
            response.put("mensaje", "Error al eliminar el usuario de la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("mensaje", "El usuario ha sido eliminado con éxito");
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NO_CONTENT);
    }

}
