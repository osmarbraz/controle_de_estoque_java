package com.estoque.modelo;

import org.junit.jupiter.api.Test;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

public class ProdutoTest {

    @Test
    void testConstructorsAndGettersSetters() {
        Produto p1 = new Produto();
        assertNull(p1.getProdutoId());
        assertNull(p1.getDescricao());
        assertNull(p1.getUnidade());
        assertEquals(0.0, p1.getPrecoCusto(), 0.001);
        assertEquals(0.0, p1.getPrecoVenda(), 0.001);
        assertEquals(0.0, p1.getQuantidade(), 0.001);
        assertNull(p1.getCategoriaId());
        assertNull(p1.getCategoriaNome());
        assertNull(p1.getDataCadastro());

        Date data = new Date();
        Produto p2 = new Produto(15, "Monitor 24", "UN", 400.0, 650.0, 10.0, 3, data);
        assertEquals(15, p2.getProdutoId());
        assertEquals("Monitor 24", p2.getDescricao());
        assertEquals("UN", p2.getUnidade());
        assertEquals(400.0, p2.getPrecoCusto(), 0.001);
        assertEquals(650.0, p2.getPrecoVenda(), 0.001);
        assertEquals(10.0, p2.getQuantidade(), 0.001);
        assertEquals(3, p2.getCategoriaId());
        assertEquals(data, p2.getDataCadastro());

        p1.setProdutoId(25);
        p1.setDescricao("Mousepad");
        p1.setUnidade("PC");
        p1.setPrecoCusto(15.0);
        p1.setPrecoVenda(35.0);
        p1.setQuantidade(50.0);
        p1.setCategoriaId(4);
        p1.setCategoriaNome("Acessórios");
        Date novaData = new Date(data.getTime() + 5000);
        p1.setDataCadastro(novaData);

        assertEquals(25, p1.getProdutoId());
        assertEquals("Mousepad", p1.getDescricao());
        assertEquals("PC", p1.getUnidade());
        assertEquals(15.0, p1.getPrecoCusto(), 0.001);
        assertEquals(35.0, p1.getPrecoVenda(), 0.001);
        assertEquals(50.0, p1.getQuantidade(), 0.001);
        assertEquals(4, p1.getCategoriaId());
        assertEquals("Acessórios", p1.getCategoriaNome());
        assertEquals(novaData, p1.getDataCadastro());
    }
}
