package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.Ingrediente;
import uniandes.dpoo.hamburguesas.mundo.ProductoAjustado;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

public class ProductoAjustadoTest
{
    private ProductoAjustado ajustado;

    @BeforeEach
    void setUp( )
    {
        ajustado = new ProductoAjustado( new ProductoMenu( "corral", 14000 ) );
    }

    @Test
    void testGetNombre( )
    {
        assertEquals( "corral", ajustado.getNombre( ) );
    }

    @Test
    void testPrecioSinAjustes( )
    {
        assertEquals( 14000, ajustado.getPrecio( ) );
    }

    @Test
    void testPrecioConIngredientesAgregados( ) throws Exception
    {
        agregarAListaPrivada( "agregados", new Ingrediente( "queso", 2500 ) );
        agregarAListaPrivada( "agregados", new Ingrediente( "tocineta", 3000 ) );
        agregarAListaPrivada( "eliminados", new Ingrediente( "tomate", 1000 ) );

        assertEquals( 19500, ajustado.getPrecio( ),
                "Los agregados aumentan el precio, pero los eliminados no lo reducen." );
    }

    @Test
    void testGenerarTextoFacturaConAjustes( ) throws Exception
    {
        agregarAListaPrivada( "agregados", new Ingrediente( "queso", 2500 ) );
        agregarAListaPrivada( "eliminados", new Ingrediente( "tomate", 1000 ) );

        String factura = ajustado.generarTextoFactura( );
        assertTrue( factura.startsWith( "corral\n" ) );
        assertTrue( factura.contains( "+queso" ) );
        assertTrue( factura.contains( "2500" ) );
        assertTrue( factura.contains( "-tomate" ) );
        assertTrue( factura.endsWith( "16500\n" ) );
    }

    @SuppressWarnings("unchecked")
    private void agregarAListaPrivada( String nombreCampo, Ingrediente ingrediente ) throws Exception
    {
        Field campo = ProductoAjustado.class.getDeclaredField( nombreCampo );
        campo.setAccessible( true );
        ArrayList<Ingrediente> lista = ( ArrayList<Ingrediente> ) campo.get( ajustado );
        lista.add( ingrediente );
    }
}
