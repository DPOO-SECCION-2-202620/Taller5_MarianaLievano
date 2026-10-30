package uniandes.dpoo.hamburguesas.mundo;

import java.util.ArrayList;

/**
 * Un producto ajustado es un producto para el cual el cliente solicitó alguna modificación.
 */
public class ProductoAjustado implements Producto
{
    private ProductoMenu productoBase;
    private ArrayList<Ingrediente> agregados;
    private ArrayList<Ingrediente> eliminados;

    public ProductoAjustado( ProductoMenu productoBase )
    {
        this.productoBase = productoBase;
        agregados = new ArrayList<Ingrediente>( );
        eliminados = new ArrayList<Ingrediente>( );
    }

    @Override
    public String getNombre( )
    {
        return productoBase.getNombre( );
    }

    @Override
    public int getPrecio( )
    {
        int precio = productoBase.getPrecio( );
        for( Ingrediente ingrediente : agregados )
        {
            precio += ingrediente.getCostoAdicional( );
        }
        return precio;
    }

    @Override
    public String generarTextoFactura( )
    {
        StringBuffer sb = new StringBuffer( );
        sb.append( productoBase.getNombre( ) + "\n" );

        for( Ingrediente ing : agregados )
        {
            sb.append( "    +" + ing.getNombre( ) );
            sb.append( "                " + ing.getCostoAdicional( ) + "\n" );
        }

        for( Ingrediente ing : eliminados )
        {
            sb.append( "    -" + ing.getNombre( ) + "\n" );
        }

        sb.append( "            " + getPrecio( ) + "\n" );
        return sb.toString( );
    }
}
