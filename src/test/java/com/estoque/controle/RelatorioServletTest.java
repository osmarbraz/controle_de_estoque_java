package com.estoque.controle;

import com.estoque.TestUtils;
import com.estoque.dao.*;
import com.estoque.modelo.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class RelatorioServletTest {

    private RelatorioServlet servlet;
    private CidadeDAO mockCidadeDAO;
    private CategoriaDAO mockCategoriaDAO;
    private ClienteDAO mockClienteDAO;
    private ProdutoDAO mockProdutoDAO;
    private PedidoDAO mockPedidoDAO;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private RequestDispatcher dispatcher;
    private StringWriter stringWriter;
    private PrintWriter printWriter;

    @BeforeEach
    void setUp() throws IOException {
        servlet = new RelatorioServlet();
        mockCidadeDAO = mock(CidadeDAO.class);
        mockCategoriaDAO = mock(CategoriaDAO.class);
        mockClienteDAO = mock(ClienteDAO.class);
        mockProdutoDAO = mock(ProdutoDAO.class);
        mockPedidoDAO = mock(PedidoDAO.class);

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        dispatcher = mock(RequestDispatcher.class);

        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        when(request.getContextPath()).thenReturn("/controle-estoque");

        TestUtils.setField(servlet, "cidadeDAO", mockCidadeDAO);
        TestUtils.setField(servlet, "categoriaDAO", mockCategoriaDAO);
        TestUtils.setField(servlet, "clienteDAO", mockClienteDAO);
        TestUtils.setField(servlet, "produtoDAO", mockProdutoDAO);
        TestUtils.setField(servlet, "pedidoDAO", mockPedidoDAO);
    }

    @Test
    void testDoGetTipoNullOuVazio() throws ServletException, IOException {
        when(request.getParameter("tipo")).thenReturn(null);

        servlet.doGet(request, response);

        verify(request).getRequestDispatcher("/relatorios/index.jsp");
        verify(dispatcher).forward(request, response);

        when(request.getParameter("tipo")).thenReturn("   ");
        servlet.doGet(request, response);
        verify(dispatcher, times(2)).forward(request, response);
    }

    @Test
    void testDoGetHtmlCidades() throws ServletException, IOException, SQLException {
        when(request.getParameter("tipo")).thenReturn("cidades");
        when(mockCidadeDAO.buscarTodos()).thenReturn(Collections.emptyList());

        servlet.doGet(request, response);

        verify(request).setAttribute(eq("cidades"), any());
        verify(request).getRequestDispatcher("/relatorios/cidades.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoGetHtmlCategorias() throws ServletException, IOException, SQLException {
        when(request.getParameter("tipo")).thenReturn("categorias");
        when(mockCategoriaDAO.buscarTodos()).thenReturn(Collections.emptyList());

        servlet.doGet(request, response);

        verify(request).setAttribute(eq("categorias"), any());
        verify(request).getRequestDispatcher("/relatorios/categorias.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoGetHtmlClientes() throws ServletException, IOException, SQLException {
        when(request.getParameter("tipo")).thenReturn("clientes");
        when(mockClienteDAO.buscarTodos()).thenReturn(Collections.emptyList());

        servlet.doGet(request, response);

        verify(request).setAttribute(eq("clientes"), any());
        verify(request).getRequestDispatcher("/relatorios/clientes.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoGetHtmlProdutos() throws ServletException, IOException, SQLException {
        when(request.getParameter("tipo")).thenReturn("produtos");
        when(mockProdutoDAO.buscarTodos()).thenReturn(Collections.emptyList());

        servlet.doGet(request, response);

        verify(request).setAttribute(eq("produtos"), any());
        verify(request).getRequestDispatcher("/relatorios/produtos.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoGetHtmlPedidos() throws ServletException, IOException, SQLException {
        when(request.getParameter("tipo")).thenReturn("pedidos");
        when(mockPedidoDAO.buscarTodosComItens()).thenReturn(Collections.emptyList());

        servlet.doGet(request, response);

        verify(request).setAttribute(eq("pedidos"), any());
        verify(request).getRequestDispatcher("/relatorios/pedidos.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoGetHtmlDefaultRedirect() throws ServletException, IOException {
        when(request.getParameter("tipo")).thenReturn("desconhecido");

        servlet.doGet(request, response);

        verify(response).sendRedirect("/controle-estoque/servlet/relatorios");
    }

    @Test
    void testDoGetExceptionLancaServletException() throws SQLException {
        when(request.getParameter("tipo")).thenReturn("cidades");
        when(mockCidadeDAO.buscarTodos()).thenThrow(new SQLException("DB error"));

        ServletException ex = assertThrows(ServletException.class, () -> servlet.doGet(request, response));
        assertTrue(ex.getMessage().contains("Erro ao gerar relatório: cidades"));
    }

    @Test
    void testExportXlsCidades() throws ServletException, IOException, SQLException {
        when(request.getParameter("tipo")).thenReturn("cidades");
        when(request.getParameter("exportar")).thenReturn("XLS");
        Cidade c = new Cidade(1, "São Paulo & Santos", "SP");
        when(mockCidadeDAO.buscarTodos()).thenReturn(Collections.singletonList(c));

        servlet.doGet(request, response);

        verify(response).setContentType("application/vnd.ms-excel; charset=UTF-8");
        verify(response).setHeader("Content-Disposition", "attachment; filename=\"relatorio_cidades.xls\"");
        printWriter.flush();
        String out = stringWriter.toString();
        assertTrue(out.contains("Relatório - Lista de Cidades"));
        assertTrue(out.contains("São Paulo &amp; Santos"));
    }

    @Test
    void testExportXlsCategorias() throws ServletException, IOException, SQLException {
        when(request.getParameter("tipo")).thenReturn("categorias");
        when(request.getParameter("exportar")).thenReturn("xls");
        Categoria cat = new Categoria(2, "<Livros & Mídias \"Digitais\">");
        when(mockCategoriaDAO.buscarTodos()).thenReturn(Collections.singletonList(cat));

        servlet.doGet(request, response);

        printWriter.flush();
        String out = stringWriter.toString();
        assertTrue(out.contains("Relatório - Lista de Categorias"));
        assertTrue(out.contains("&lt;Livros &amp; Mídias &quot;Digitais&quot;&gt;"));
    }

    @Test
    void testExportXlsClientesComESemDataCadastro() throws ServletException, IOException, SQLException {
        when(request.getParameter("tipo")).thenReturn("clientes");
        when(request.getParameter("exportar")).thenReturn("xls");

        Cliente c1 = new Cliente(1, "José", "123", "Rua A", "10", "Centro", 1, new Date());
        c1.setCidadeNome("Curitiba");
        c1.setCidadeUf("PR");

        Cliente c2 = new Cliente(2, "Maria", "456", null, null, null, 2, null);

        when(mockClienteDAO.buscarTodos()).thenReturn(Arrays.asList(c1, c2));

        servlet.doGet(request, response);

        printWriter.flush();
        String out = stringWriter.toString();
        assertTrue(out.contains("Relatório - Lista de Clientes"));
        assertTrue(out.contains("José"));
        assertTrue(out.contains("Maria"));
        assertTrue(out.contains("-"));
    }

    @Test
    void testExportXlsProdutosComESemDataCadastro() throws ServletException, IOException, SQLException {
        when(request.getParameter("tipo")).thenReturn("produtos");
        when(request.getParameter("exportar")).thenReturn("xls");

        Produto p1 = new Produto(1, "Monitor", "UN", 500.0, 800.0, 10.0, 1, new Date());
        p1.setCategoriaNome("Informática");

        Produto p2 = new Produto(2, null, null, 10.0, 20.0, 5.0, 2, null);

        when(mockProdutoDAO.buscarTodos()).thenReturn(Arrays.asList(p1, p2));

        servlet.doGet(request, response);

        printWriter.flush();
        String out = stringWriter.toString();
        assertTrue(out.contains("Relatório - Lista de Produtos"));
        assertTrue(out.contains("Monitor"));
    }

    @Test
    void testExportXlsPedidosComItensESemItensEComNull() throws ServletException, IOException, SQLException {
        when(request.getParameter("tipo")).thenReturn("pedidos");
        when(request.getParameter("exportar")).thenReturn("xls");

        Pedido p1 = new Pedido(1, 10, new Date(), 250.0, "aberto");
        p1.setClienteNome("Cliente 1");
        ItemPedido item = new ItemPedido(1, 5, 2.0, 125.0);
        item.setProdutoDescricao("Mouse");
        item.setUnidade("UN");
        p1.setItens(Collections.singletonList(item));

        Pedido p2 = new Pedido(2, 20, null, 0.0, "fechado");
        p2.setClienteNome(null);
        p2.setItens(Collections.emptyList());

        Pedido p3 = new Pedido(3, 30, null, 0.0, "fechado");
        p3.setItens(null);

        when(mockPedidoDAO.buscarTodosComItens()).thenReturn(Arrays.asList(p1, p2, p3));

        servlet.doGet(request, response);

        printWriter.flush();
        String out = stringWriter.toString();
        assertTrue(out.contains("Relatório - Lista de Pedidos e Itens"));
        assertTrue(out.contains("Pedido #1"));
        assertTrue(out.contains("Mouse"));
        assertTrue(out.contains("Nenhum item cadastrado"));
    }
}
