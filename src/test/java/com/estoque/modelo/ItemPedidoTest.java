package com.estoque.modelo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ItemPedidoTest {

    @Test
    void testConstructorsAndGettersSetters() {
        ItemPedido item1 = new ItemPedido();
        assertNull(item1.getPedidoId());
        assertNull(item1.getProdutoId());
        assertNull(item1.getProdutoDescricao());
        assertNull(item1.getUnidade());
        assertEquals(0.0, item1.getQuantidade(), 0.001);
        assertEquals(0.0, item1.getPrecoVenda(), 0.001);
        assertEquals(0.0, item1.getSubtotal(), 0.001);

        ItemPedido item2 = new ItemPedido(10, 5, 3.0, 50.0);
        assertEquals(10, item2.getPedidoId());
        assertEquals(5, item2.getProdutoId());
        assertEquals(3.0, item2.getQuantidade(), 0.001);
        assertEquals(50.0, item2.getPrecoVenda(), 0.001);
        assertEquals(150.0, item2.getSubtotal(), 0.001);

        item1.setPedidoId(20);
        item1.setProdutoId(8);
        item1.setProdutoDescricao("Teclado");
        item1.setUnidade("UN");
        item1.setQuantidade(2.5);
        item1.setPrecoVenda(100.0);

        assertEquals(20, item1.getPedidoId());
        assertEquals(8, item1.getProdutoId());
        assertEquals("Teclado", item1.getProdutoDescricao());
        assertEquals("UN", item1.getUnidade());
        assertEquals(2.5, item1.getQuantidade(), 0.001);
        assertEquals(100.0, item1.getPrecoVenda(), 0.001);
        assertEquals(250.0, item1.getSubtotal(), 0.001);
    }
}
