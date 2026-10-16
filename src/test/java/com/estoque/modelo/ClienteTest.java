package com.estoque.modelo;

import org.junit.jupiter.api.Test;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

public class ClienteTest {

    @Test
    void testConstructorsAndGettersSetters() {
        Cliente c1 = new Cliente();
        assertNull(c1.getClienteId());
        assertNull(c1.getNome());
        assertNull(c1.getCpf());
        assertNull(c1.getRua());
        assertNull(c1.getNumero());
        assertNull(c1.getBairro());
        assertNull(c1.getCidadeId());
        assertNull(c1.getCidadeNome());
        assertNull(c1.getCidadeUf());
        assertNull(c1.getDataCadastro());

        Date data = new Date();
        Cliente c2 = new Cliente(10, "Maria", "111.222.333-44", "Rua das Flores", "100", "Centro", 5, data);
        assertEquals(10, c2.getClienteId());
        assertEquals("Maria", c2.getNome());
        assertEquals("111.222.333-44", c2.getCpf());
        assertEquals("Rua das Flores", c2.getRua());
        assertEquals("100", c2.getNumero());
        assertEquals("Centro", c2.getBairro());
        assertEquals(5, c2.getCidadeId());
        assertEquals(data, c2.getDataCadastro());

        c1.setClienteId(20);
        c1.setNome("José");
        c1.setCpf("999.888.777-66");
        c1.setRua("Av Principal");
        c1.setNumero("500");
        c1.setBairro("Jardins");
        c1.setCidadeId(2);
        c1.setCidadeNome("São Paulo");
        c1.setCidadeUf("SP");
        Date novaData = new Date(data.getTime() + 10000);
        c1.setDataCadastro(novaData);

        assertEquals(20, c1.getClienteId());
        assertEquals("José", c1.getNome());
        assertEquals("999.888.777-66", c1.getCpf());
        assertEquals("Av Principal", c1.getRua());
        assertEquals("500", c1.getNumero());
        assertEquals("Jardins", c1.getBairro());
        assertEquals(2, c1.getCidadeId());
        assertEquals("São Paulo", c1.getCidadeNome());
        assertEquals("SP", c1.getCidadeUf());
        assertEquals(novaData, c1.getDataCadastro());
    }
}
