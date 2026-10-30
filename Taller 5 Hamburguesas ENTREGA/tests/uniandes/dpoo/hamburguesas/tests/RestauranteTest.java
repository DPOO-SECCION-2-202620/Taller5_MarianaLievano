package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.excepciones.IngredienteRepetidoException;
import uniandes.dpoo.hamburguesas.excepciones.NoHayPedidoEnCursoException;
import uniandes.dpoo.hamburguesas.excepciones.ProductoFaltanteException;
import uniandes.dpoo.hamburguesas.excepciones.ProductoRepetidoException;
import uniandes.dpoo.hamburguesas.excepciones.YaHayUnPedidoEnCursoException;
import uniandes.dpoo.hamburguesas.mundo.Pedido;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;
import uniandes.dpoo.hamburguesas.mundo.Restaurante;

public class RestauranteTest
{
    private Restaurante restaurante;

    private final File ingredientesOk = new File( "data/pruebas/ingredientes_ok.txt" );
    private final File menuOk = new File( "data/pruebas/menu_ok.txt" );
    private final File combosOk = new File( "data/pruebas/combos_ok.txt" );

    @BeforeEach
    void setUp( )
    {
        restaurante = new Restaurante( );
    }

    @Test
    void testConstructorCreaColeccionesVacias( )
    {
        assertNotNull( restaurante.getPedidos( ) );
        assertNotNull( restaurante.getIngredientes( ) );
        assertNotNull( restaurante.getMenuBase( ) );
        assertNotNull( restaurante.getMenuCombos( ) );
        assertTrue( restaurante.getPedidos( ).isEmpty( ) );
        assertNull( restaurante.getPedidoEnCurso( ) );
    }

    @Test
    void testIniciarPedido( ) throws Exception
    {
        restaurante.iniciarPedido( "Mariana", "Calle 1" );
        assertNotNull( restaurante.getPedidoEnCurso( ) );
        assertEquals( "Mariana", restaurante.getPedidoEnCurso( ).getNombreCliente( ) );
    }

    @Test
    void testNoPermiteDosPedidosEnCurso( ) throws Exception
    {
        restaurante.iniciarPedido( "Mariana", "Calle 1" );
        YaHayUnPedidoEnCursoException ex = assertThrows( YaHayUnPedidoEnCursoException.class,
                ( ) -> restaurante.iniciarPedido( "Laura", "Calle 2" ) );
        assertTrue( ex.getMessage( ).contains( "Mariana" ) );
        assertTrue( ex.getMessage( ).contains( "Laura" ) );
    }

    @Test
    void testCerrarSinPedidoLanzaExcepcion( )
    {
        assertThrows( NoHayPedidoEnCursoException.class, ( ) -> restaurante.cerrarYGuardarPedido( ) );
    }

    @Test
    void testCargarInformacionValida( ) throws Exception
    {
        restaurante.cargarInformacionRestaurante( ingredientesOk, menuOk, combosOk );

        assertEquals( 2, restaurante.getIngredientes( ).size( ) );
        assertEquals( 3, restaurante.getMenuBase( ).size( ) );
        assertEquals( 1, restaurante.getMenuCombos( ).size( ) );
        assertEquals( "tomate", restaurante.getIngredientes( ).get( 0 ).getNombre( ) );
        assertEquals( "hamburguesa", restaurante.getMenuBase( ).get( 0 ).getNombre( ) );
        assertEquals( "combo prueba", restaurante.getMenuCombos( ).get( 0 ).getNombre( ) );
        assertEquals( 13500, restaurante.getMenuCombos( ).get( 0 ).getPrecio( ) );
    }

    @Test
    void testCerrarPedidoGuardaFacturaYLoPasaAlHistorico( ) throws Exception
    {
        restaurante.iniciarPedido( "Mariana", "Calle 1" );
        Pedido pedido = restaurante.getPedidoEnCurso( );
        pedido.agregarProducto( new ProductoMenu( "hamburguesa", 10000 ) );

        int id = pedido.getIdPedido( );
        File factura = new File( "facturas/factura_" + id + ".txt" );
        factura.delete( );

        restaurante.cerrarYGuardarPedido( );

        assertNull( restaurante.getPedidoEnCurso( ) );
        assertEquals( 1, restaurante.getPedidos( ).size( ) );
        assertSame( pedido, restaurante.getPedidos( ).get( 0 ) );
        assertTrue( factura.exists( ) );
        factura.delete( );
    }

    @Test
    void testIngredienteRepetido( )
    {
        File repetidos = new File( "data/pruebas/ingredientes_repetidos.txt" );
        assertThrows( IngredienteRepetidoException.class,
                ( ) -> restaurante.cargarInformacionRestaurante( repetidos, menuOk, combosOk ) );
    }

    @Test
    void testProductoMenuRepetido( )
    {
        File repetido = new File( "data/pruebas/menu_repetido.txt" );
        assertThrows( ProductoRepetidoException.class,
                ( ) -> restaurante.cargarInformacionRestaurante( ingredientesOk, repetido, combosOk ) );
    }

    @Test
    void testComboRepetido( )
    {
        File repetido = new File( "data/pruebas/combos_repetidos.txt" );
        assertThrows( ProductoRepetidoException.class,
                ( ) -> restaurante.cargarInformacionRestaurante( ingredientesOk, menuOk, repetido ) );
    }

    @Test
    void testProductoFaltanteEnCombo( )
    {
        File faltante = new File( "data/pruebas/combo_faltante.txt" );
        assertThrows( ProductoFaltanteException.class,
                ( ) -> restaurante.cargarInformacionRestaurante( ingredientesOk, menuOk, faltante ) );
    }

    @Test
    void testPrecioIngredienteInvalido( )
    {
        File invalido = new File( "data/pruebas/ingrediente_precio_invalido.txt" );
        assertThrows( NumberFormatException.class,
                ( ) -> restaurante.cargarInformacionRestaurante( invalido, menuOk, combosOk ) );
    }

    @Test
    void testArchivoInexistente( )
    {
        File inexistente = new File( "data/pruebas/NO_EXISTE.txt" );
        assertThrows( FileNotFoundException.class,
                ( ) -> restaurante.cargarInformacionRestaurante( inexistente, menuOk, combosOk ) );
    }
}
