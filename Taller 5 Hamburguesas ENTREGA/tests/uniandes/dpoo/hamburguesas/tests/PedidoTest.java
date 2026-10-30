package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.Combo;
import uniandes.dpoo.hamburguesas.mundo.Pedido;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

public class PedidoTest
{
    private Pedido pedido;

    @BeforeEach
    void setUp( )
    {
        pedido = new Pedido( "Mariana", "Calle 1 # 2-3" );
    }

    @Test
    void testDatosBasicosEIdentificadorIncremental( )
    {
        Pedido siguiente = new Pedido( "Laura", "Calle 2" );
        assertEquals( "Mariana", pedido.getNombreCliente( ) );
        assertEquals( pedido.getIdPedido( ) + 1, siguiente.getIdPedido( ) );
    }

    @Test
    void testPrecioPedidoVacio( )
    {
        assertEquals( 0, pedido.getPrecioTotalPedido( ) );
    }

    @Test
    void testAgregarProductoYCalcularTotalConIVA( )
    {
        pedido.agregarProducto( new ProductoMenu( "hamburguesa", 10000 ) );
        pedido.agregarProducto( new ProductoMenu( "papas", 5000 ) );

        // Neto 15000, IVA truncado 2850, total 17850.
        assertEquals( 17850, pedido.getPrecioTotalPedido( ) );
    }

    @Test
    void testGenerarTextoFactura( )
    {
        ProductoMenu hamburguesa = new ProductoMenu( "hamburguesa", 10000 );
        ArrayList<ProductoMenu> items = new ArrayList<>( );
        items.add( hamburguesa );
        Combo combo = new Combo( "combo", 0.10, items );

        pedido.agregarProducto( hamburguesa );
        pedido.agregarProducto( combo );

        String factura = pedido.generarTextoFactura( );
        int neto = 19000;
        int iva = ( int ) ( neto * 0.19 );

        assertTrue( factura.contains( "Cliente: Mariana" ) );
        assertTrue( factura.contains( "Dirección: Calle 1 # 2-3" ) );
        assertTrue( factura.contains( "hamburguesa" ) );
        assertTrue( factura.contains( "Combo combo" ) );
        assertTrue( factura.contains( "Precio Neto:  " + neto ) );
        assertTrue( factura.contains( "IVA:          " + iva ) );
        assertTrue( factura.contains( "Precio Total: " + ( neto + iva ) ) );
    }

    @Test
    void testGuardarFactura( ) throws Exception
    {
        pedido.agregarProducto( new ProductoMenu( "hamburguesa", 10000 ) );
        File archivo = File.createTempFile( "factura-pedido-", ".txt" );
        try
        {
            pedido.guardarFactura( archivo );
            String contenido = Files.readString( archivo.toPath( ) );
            assertEquals( pedido.generarTextoFactura( ), contenido );
        }
        finally
        {
            archivo.delete( );
        }
    }

    @Test
    void testGuardarFacturaEnRutaInvalida( )
    {
        File archivo = new File( "carpeta-que-no-existe/subcarpeta/factura.txt" );
        assertThrows( FileNotFoundException.class, ( ) -> pedido.guardarFactura( archivo ) );
    }
}
