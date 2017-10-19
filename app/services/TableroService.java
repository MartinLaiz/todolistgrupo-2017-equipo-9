package services;

import javax.inject.*;

import models.Usuario;
import models.UsuarioRepository;
import models.Tablero;
import models.TableroRepository;

public class TableroService {
  UsuarioRepository usuarioRepository;
  TableroRepository tableroRepository;

  @Inject
  public TableroService(UsuarioRepository usuarioRepository, TableroRepository tableroRepository) {
     this.usuarioRepository = usuarioRepository;
     this.tableroRepository = tableroRepository;
  }

  public Tablero crearTableroUsuario(String nombre, Long idUsuario) {
    System.out.println(idUsuario);
    Usuario usuario = usuarioRepository.findById(idUsuario);
    if (usuario == null) {
      throw new TableroServiceException("No existe el usuario");
    }
    Tablero tablero = new Tablero(usuario, nombre);
    return tableroRepository.add(tablero);
  }
}
