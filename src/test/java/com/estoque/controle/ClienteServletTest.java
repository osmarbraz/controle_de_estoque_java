package com.estoque.controle;

import com.estoque.TestUtils;
import com.estoque.dao.CidadeDAO;
import com.estoque.dao.ClienteDAO;
import com.estoque.modelo.Cidade;
import com.estoque.modelo.Cliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class ClienteServletTest {

    private ClienteServlet servlet;
    private ClienteDAO mockClienteDAO;
    private CidadeDAO mockCidadeDAO;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private RequestDispatcher dispatcher;

    @BeforeEach
    void setUp() {
        servlet = new ClienteServlet();
        mockClienteDAO = mock(ClienteDAO.class);
        mockCidadeDAO = mock(CidadeDAO.class);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        dispatcher = mock(RequestDispatcher.class);

        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        when(request.getContextPath()).thenReturn("/controle-estoque");

        TestUtils.setField(servlet, "clienteDAO", mockClienteDAO);
        TestUtils.setField(servlet, "cidadeDAO", mockCidadeDAO);
    }

    @Test
    void testDoGetListarPadrao() throws ServletException, IOException, SQLException {
        when(request.getParameter("acao")).thenReturn(null);
        when(mockClienteDAO.buscarTodos()).thenReturn(Collections.emptyList());

        servlet.doGet(request, response);

        verify(request).setCharacterEncoding("UTF-8");
        verify(mockClienteDAO).buscarTodos();
        verify(request).setAttribute(eq("clientes"), any());
        verify(request).getRequestDispatcher("/cliente/list.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoGetNovo() throws ServletException, IOException, SQLException {
        when(request.getParameter("acao")).thenReturn("novo");
        when(mockCidadeDAO.buscarTodos()).thenReturn(Collections.singletonList(new Cidade(1, "SP", "SP")));

        servlet.doGet(request, response);

        verify(request).setAttribute(eq("cliente"), any(Cliente.class));
        verify(request).setAttribute(eq("cidades"), any());
        verify(request).getRequestDispatcher("/cliente/form.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoGetEditarEncontrado() throws ServletException, IOException, SQLException {
        when(request.getParameter("acao")).thenReturn("editar");
        when(request.getParameter("id")).thenReturn("5");
        Cliente c = new Cliente();
        c.setClienteId(5);
        when(mockClienteDAO.buscarPorId(5)).thenReturn(c);
        when(mockCidadeDAO.buscarTodos()).thenReturn(Collections.emptyList());

        servlet.doGet(request, response);

        verify(request).setAttribute("cliente", c);
        verify(request).setAttribute(eq("cidades"), any());
        verify(request).getRequestDispatcher("/cliente/form.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoGetEditarNaoEncontrado() throws ServletException, IOException, SQLException {
        when(request.getParameter("acao")).thenReturn("editar");
        when(request.getParameter("id")).thenReturn("99");
        when(mockClienteDAO.buscarPorId(99)).thenReturn(null);

        servlet.doGet(request, response);

        verify(response).sendRedirect("/controle-estoque/servlet/clientes?msg=nao_encontrado");
    }

    @Test
    void testDoGetExcluirSucesso() throws ServletException, IOException, SQLException {
        when(request.getParameter("acao")).thenReturn("excluir");
        when(request.getParameter("id")).thenReturn("3");
        when(mockClienteDAO.excluir(3)).thenReturn(true);

        servlet.doGet(request, response);

        verify(mockClienteDAO).excluir(3);
        verify(response).sendRedirect("/controle-estoque/servlet/clientes?msg=sucesso_excluir");
    }

    @Test
    void testDoGetExcluirComErroVinculo() throws ServletException, IOException, SQLException {
        when(request.getParameter("acao")).thenReturn("excluir");
        when(request.getParameter("id")).thenReturn("3");
        doThrow(new RuntimeException("FK Error")).when(mockClienteDAO).excluir(3);

        servlet.doGet(request, response);

        verify(mockClienteDAO).excluir(3);
        verify(response).sendRedirect("/controle-estoque/servlet/clientes?msg=erro_excluir_vinculo");
    }

    @Test
    void testDoGetExceptionLancaServletException() throws SQLException {
        when(request.getParameter("acao")).thenReturn("listar");
        when(mockClienteDAO.buscarTodos()).thenThrow(new SQLException("DB error"));

        ServletException ex = assertThrows(ServletException.class, () -> servlet.doGet(request, response));
        assertTrue(ex.getMessage().contains("Erro ao processar requisição em Clientes"));
    }

    @Test
    void testDoPostInserirSucesso() throws ServletException, IOException, SQLException {
        when(request.getParameter("clienteId")).thenReturn(null);
        when(request.getParameter("nome")).thenReturn(" Ana ");
        when(request.getParameter("cpf")).thenReturn(" 123 ");
        when(request.getParameter("rua")).thenReturn(" Rua X ");
        when(request.getParameter("numero")).thenReturn(" 10 ");
        when(request.getParameter("bairro")).thenReturn(" Centro ");
        when(request.getParameter("cidadeId")).thenReturn("2");

        servlet.doPost(request, response);

        verify(mockClienteDAO).inserir(argThat(c -> "Ana".equals(c.getNome()) && Integer.valueOf(2).equals(c.getCidadeId())));
        verify(response).sendRedirect("/controle-estoque/servlet/clientes?msg=sucesso_salvar");
    }

    @Test
    void testDoPostInserirCamposNull() throws ServletException, IOException, SQLException {
        when(request.getParameter("clienteId")).thenReturn("");
        when(request.getParameter("nome")).thenReturn(null);
        when(request.getParameter("cpf")).thenReturn(null);
        when(request.getParameter("rua")).thenReturn(null);
        when(request.getParameter("numero")).thenReturn(null);
        when(request.getParameter("bairro")).thenReturn(null);
        when(request.getParameter("cidadeId")).thenReturn("");

        servlet.doPost(request, response);

        verify(mockClienteDAO).inserir(argThat(c -> "".equals(c.getNome()) && c.getCidadeId() == null));
        verify(response).sendRedirect("/controle-estoque/servlet/clientes?msg=sucesso_salvar");
    }

    @Test
    void testDoPostAtualizarSucesso() throws ServletException, IOException, SQLException {
        when(request.getParameter("clienteId")).thenReturn("5");
        when(request.getParameter("nome")).thenReturn("Carlos");
        when(request.getParameter("cpf")).thenReturn("456");
        when(request.getParameter("rua")).thenReturn("Rua Y");
        when(request.getParameter("numero")).thenReturn("20");
        when(request.getParameter("bairro")).thenReturn("Norte");
        when(request.getParameter("cidadeId")).thenReturn("3");

        servlet.doPost(request, response);

        verify(mockClienteDAO).atualizar(argThat(c -> Integer.valueOf(5).equals(c.getClienteId())));
        verify(response).sendRedirect("/controle-estoque/servlet/clientes?msg=sucesso_salvar");
    }

    @Test
    void testDoPostComErroComIdECidadeId() throws ServletException, IOException, SQLException {
        when(request.getParameter("clienteId")).thenReturn("5");
        when(request.getParameter("nome")).thenReturn("Erro");
        when(request.getParameter("cpf")).thenReturn("000");
        when(request.getParameter("rua")).thenReturn("Rua");
        when(request.getParameter("numero")).thenReturn("1");
        when(request.getParameter("bairro")).thenReturn("B");
        when(request.getParameter("cidadeId")).thenReturn("2");
        doThrow(new SQLException("DB error")).when(mockClienteDAO).atualizar(any());
        when(mockCidadeDAO.buscarTodos()).thenReturn(Collections.emptyList());

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("erro"), contains("Erro ao salvar cliente"));
        verify(request).setAttribute(eq("cliente"), argThat(c -> ((Cliente) c).getClienteId() == 5 && ((Cliente) c).getCidadeId() == 2));
        verify(request).getRequestDispatcher("/cliente/form.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoPostComErroSemIdESemCidadeIdComFalhaEmBuscarCidades() throws ServletException, IOException, SQLException {
        when(request.getParameter("clienteId")).thenReturn("");
        when(request.getParameter("nome")).thenReturn("Erro2");
        when(request.getParameter("cpf")).thenReturn("000");
        when(request.getParameter("rua")).thenReturn("Rua");
        when(request.getParameter("numero")).thenReturn("1");
        when(request.getParameter("bairro")).thenReturn("B");
        when(request.getParameter("cidadeId")).thenReturn(null);
        doThrow(new RuntimeException("DB error")).when(mockClienteDAO).inserir(any());
        when(mockCidadeDAO.buscarTodos()).thenThrow(new SQLException("Falha ao buscar cidades"));

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("erro"), contains("Erro ao salvar cliente"));
        verify(request).setAttribute(eq("cliente"), argThat(c -> ((Cliente) c).getClienteId() == null && ((Cliente) c).getCidadeId() == null));
        verify(request).getRequestDispatcher("/cliente/form.jsp");
        verify(dispatcher).forward(request, response);
    }
}
