package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.Combo;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

public class ComboTest
{
    private Combo combo;

    @BeforeEach
    void setUp( )
    {
        ArrayList<ProductoMenu> items = new ArrayList<>( );
        items.add( new ProductoMenu( "hamburguesa", 10000 ) );
        items.add( new ProductoMenu( "papas", 5000 ) );
        combo = new Combo( "combo prueba", 0.10, items );
    }

    @Test
    void testGetNombre( )
    {
        assertEquals( "combo prueba", combo.getNombre( ) );
    }

    @Test
    void testGetPrecioAplicaDescuento( )
    {
        assertEquals( 13500, combo.getPrecio( ), "El descuento debe restarse del precio normal." );
    }

    @Test
    void testGenerarTextoFactura( )
    {
        String esperado = "Combo combo prueba\n Descuento: 0.1\n            13500\n";
        assertEquals( esperado, combo.generarTextoFactura( ) );
    }

    @Test
    void testComboSinProductos( )
    {
        Combo vacio = new Combo( "vacio", 0.20, new ArrayList<ProductoMenu>( ) );
        assertEquals( 0, vacio.getPrecio( ) );
    }
}
