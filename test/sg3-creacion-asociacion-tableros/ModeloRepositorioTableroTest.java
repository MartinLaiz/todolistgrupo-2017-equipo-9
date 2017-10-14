import org.junit.*;
import static org.junit.Assert.*;

import play.inject.guice.GuiceApplicationBuilder;
import play.inject.Injector;
import play.inject.guice.GuiceInjectorBuilder;
import play.Environment;

import play.db.jpa.*;
import play.db.Database;
import play.db.Databases;

import java.sql.*;

import models.Usuario;
import models.UsuarioRepository;
import models.Tablero;
import models.TableroRepository;

public class ModeloRepositorioTableroTest {
  static Database db;
  static private Injector injector;

  @BeforeClass
  static public void initApplication() {
    GuiceApplicationBuilder guiceApplicationBuilder =
         new GuiceApplicationBuilder().in(Environment.simple());
    injector = guiceApplicationBuilder.injector();
    db = injector.instanceOf(Database.class);
    injector.instanceOf(JPAApi.class);
  }

  @Test
  public void testCrearTablero() {
    Usuario usuario = new Usuario("juangutierrez", "juangutierrez@gmail.com");
    Tablero tablero = new Tablero(usuario, "Tablero 1");

    assertEquals("juangutierrez", tablero.getAdministrador().getLogin());
    assertEquals("juangutierrez@gmail.com", tablero.getAdministrador().getEmail());
    assertEquals("Tablero 1", tablero.getNombre());
  }

  @Test
  public void testObtenerTableroRepository() {
    TableroRepository tableroRepository = injector.instanceOf(TableroRepository.class);
    assertNotNull(tableroRepository);
  }

  @Test
  public void testCrearTablaTableroEnBD() throws Exception {
    Connection connection = db.getConnection();
    DatabaseMetaData meta = connection.getMetaData();
    ResultSet res = meta.getTables(null, null, "TABLERO", new String[] {"TABLE"});
    assertTrue(res.next());
  }

  @Test
  public void testAddTableroInsertsDatabase() {
    UsuarioRepository usuarioRepository = injector.instanceOf(UsuarioRepository.class);
    TableroRepository tableroRepository = injector.instanceOf(TableroRepository.class);
    Usuario administrador = new Usuario("juangutierrez", "juangutierrez@gmail.com");
    administrador = usuarioRepository.add(administrador);
    Tablero tablero = new Tablero(administrador, "Tablero 1");
    tablero = tableroRepository.add(tablero);
    assertNotNull(tablero.getId());
    assertEquals("Tablero 1", getNombreFromTableroDB(tablero.getId()));
  }

  private String getNombreFromTableroDB(Long tableroId) {
    String nombre = db.withConnection(connection -> {
       String selectStatement = "SELECT Nombre FROM Tablero WHERE ID = ? ";
       PreparedStatement prepStmt = connection.prepareStatement(selectStatement);
       prepStmt.setLong(1, tableroId);
       ResultSet rs = prepStmt.executeQuery();
       rs.next();
       return rs.getString("Nombre");
    });
    return nombre;
  }
}
