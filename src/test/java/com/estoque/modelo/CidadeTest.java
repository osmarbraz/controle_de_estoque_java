package com.estoque.modelo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CidadeTest {

    @Test
    void testConstructorsAndGettersSetters() {
        Cidade c1 = new Cidade();
        assertNull(c1.getCidadeId());
        assertNull(c1.getNome());
        assertNull(c1.getUf());

        Cidade c2 = new Cidade("Curitiba", "PR");
        assertNull(c2.getCidadeId());
        assertEquals("Curitiba", c2.getNome());
        assertEquals("PR", c2.getUf());

        Cidade c3 = new Cidade(1, "São Paulo", "SP");
        assertEquals(1, c3.getCidadeId());
        assertEquals("São Paulo", c3.getNome());
        assertEquals("SP", c3.getUf());

        c1.setCidadeId(2);
        c1.setNome("Rio de Janeiro");
        c1.setUf("RJ");

        assertEquals(2, c1.getCidadeId());
        assertEquals("Rio de Janeiro", c1.getNome());
        assertEquals("RJ", c1.getUf());
    }
}
