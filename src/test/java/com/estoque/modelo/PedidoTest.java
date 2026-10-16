package com.estoque.modelo;

import org.junit.jupiter.api.Test;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class PedidoTest {

    @Test
    void testConstructorsAndGettersSetters() {
        Pedido p1 = new Pedido();
        assertNull(p1.getPedidoId());
        assertNull(p1.getClienteId());
        assertNull(p1.getClienteNome());
        assertNull(p1.getData());
        assertEquals(0.0, p1.getTotal(), 0.001);
        assertNull(p1.getSituacao());
        assertNotNull(p1.getItens());
        assertTrue(p1.getItens().isEmpty());

        Date data = new Date();
        Pedido p2 = new Pedido(100, 2, data, 450.50, "aberto");
        assertEquals(100, p2.getPedidoId());
        assertEquals(2, p2.getClienteId());
        assertEquals(data, p2.getData());
        assertEquals(450.50, p2.getTotal(), 0.001);
        assertEquals("aberto", p2.getSituacao());

        p1.setPedidoId(200);
        p1.setClienteId(3);
        p1.setClienteNome("Cliente Teste");
        Date novaData = new Date(data.getTime() + 1000);
        p1.setData(novaData);
        p1.setTotal(890.00);
        p1.setSituacao("fechado");

        ItemPedido item = new ItemPedido(200, 1, 2.0, 445.00);
        List<ItemPedido> itens = Collections.singletonList(item);
        p1.setItens(itens);

        assertEquals(200, p1.getPedidoId());
        assertEquals(3, p1.getClienteId());
        assertEquals("Cliente Teste", p1.getClienteNome());
        assertEquals(novaData, p1.getData());
        assertEquals(890.00, p1.getTotal(), 0.001);
        assertEquals("fechado", p1.getSituacao());
        assertEquals(1, p1.getItens().size());
        assertEquals(item, p1.getItens().get(0));
    }
}
