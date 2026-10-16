package com.estoque.modelo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CategoriaTest {

    @Test
    void testCategoriaConstructorsAndGettersSetters() {
        Categoria c1 = new Categoria();
        assertNull(c1.getCategoriaId());
        assertNull(c1.getNome());

        Categoria c2 = new Categoria("Informática");
        assertEquals("Informática", c2.getNome());
        assertNull(c2.getCategoriaId());

        Categoria c3 = new Categoria(10, "Eletrônicos");
        assertEquals(10, c3.getCategoriaId());
        assertEquals("Eletrônicos", c3.getNome());

        c1.setCategoriaId(20);
        c1.setNome("Móveis");
        assertEquals(20, c1.getCategoriaId());
        assertEquals("Móveis", c1.getNome());
    }
}
