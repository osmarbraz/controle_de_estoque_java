package com.estoque.controle;

import com.estoque.dao.*;
import com.estoque.modelo.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

@WebServlet({"/servlet/relatorios", "/relatorios"})
/**
 * Classe responsável por gerenciar a lógica de RelatorioServlet.
 */
public class RelatorioServlet extends HttpServlet {

    private final CidadeDAO cidadeDAO = new CidadeDAO();
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();
    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    private final PedidoDAO pedidoDAO = new PedidoDAO();

    @Override
    /**
     * Processa as requisições HTTP GET.
     * @param request Parâmetro necessário para a operação
     * @param response Parâmetro necessário para a operação
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String tipo = request.getParameter("tipo");
        String export = request.getParameter("exportar");

        if (tipo == null || tipo.trim().isEmpty()) {
            request.getRequestDispatcher("/relatorios/index.jsp").forward(request, response);
            return;
        }

        try {
            if ("xls".equalsIgnoreCase(export)) {
                exportXls(tipo, response);
                return;
            }

            switch (tipo) {
                case "cidades":
                    request.setAttribute("cidades", cidadeDAO.buscarTodos());
                    request.getRequestDispatcher("/relatorios/cidades.jsp").forward(request, response);
                    break;
                case "categorias":
                    request.setAttribute("categorias", categoriaDAO.buscarTodos());
                    request.getRequestDispatcher("/relatorios/categorias.jsp").forward(request, response);
                    break;
                case "clientes":
                    request.setAttribute("clientes", clienteDAO.buscarTodos());
                    request.getRequestDispatcher("/relatorios/clientes.jsp").forward(request, response);
                    break;
                case "produtos":
                    request.setAttribute("produtos", produtoDAO.buscarTodos());
                    request.getRequestDispatcher("/relatorios/produtos.jsp").forward(request, response);
                    break;
                case "pedidos":
                    request.setAttribute("pedidos", pedidoDAO.buscarTodosComItens());
                    request.getRequestDispatcher("/relatorios/pedidos.jsp").forward(request, response);
                    break;
                case "pedidos_abertos":
                case "pedidos-abertos":
                    request.setAttribute("pedidos", pedidoDAO.buscarPorSituacaoComItens("aberto"));
                    request.getRequestDispatcher("/relatorios/pedidos_abertos.jsp").forward(request, response);
                    break;
                case "pedidos_fechados":
                case "pedidos-fechados":
                    request.setAttribute("pedidos", pedidoDAO.buscarPorSituacaoComItens("fechado"));
                    request.getRequestDispatcher("/relatorios/pedidos_fechados.jsp").forward(request, response);
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/servlet/relatorios");
                    break;
            }
        } catch (Exception e) {
            throw new ServletException("Erro ao gerar relatório: " + tipo, e);
        }
    }

    private void exportXls(String tipo, HttpServletResponse response) throws Exception {
        response.setContentType("application/vnd.ms-excel; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"relatorio_" + tipo + ".xls\"");

        PrintWriter out = response.getWriter();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

        out.println("<!DOCTYPE html><html><head><meta charset='UTF-8'><style>");
        out.println("table { border-collapse: collapse; width: 100%; }");
        out.println("th, td { border: 1px solid #cccccc; padding: 6px; text-align: left; }");
        out.println("th { background-color: #1e293b; color: #ffffff; font-weight: bold; }");
        out.println(".sub-header { background-color: #e2e8f0; font-weight: bold; }");
        out.println(".text-right { text-align: right; }");
        out.println("</style></head><body>");

        switch (tipo) {
            case "cidades":
                out.println("<h2>Relatório - Lista de Cidades</h2>");
                out.println("<table><thead><tr><th>ID</th><th>Nome da Cidade</th><th>UF</th></tr></thead><tbody>");
                for (Cidade c : cidadeDAO.buscarTodos()) {
                    out.println("<tr><td>" + c.getCidadeId() + "</td><td>" + escapeHtml(c.getNome()) + "</td><td>" + escapeHtml(c.getUf()) + "</td></tr>");
                }
                out.println("</tbody></table>");
                break;

            case "categorias":
                out.println("<h2>Relatório - Lista de Categorias</h2>");
                out.println("<table><thead><tr><th>ID</th><th>Nome da Categoria</th></tr></thead><tbody>");
                for (Categoria cat : categoriaDAO.buscarTodos()) {
                    out.println("<tr><td>" + cat.getCategoriaId() + "</td><td>" + escapeHtml(cat.getNome()) + "</td></tr>");
                }
                out.println("</tbody></table>");
                break;

            case "clientes":
                out.println("<h2>Relatório - Lista de Clientes</h2>");
                out.println("<table><thead><tr><th>ID</th><th>Nome</th><th>CPF</th><th>Endereço</th><th>Bairro</th><th>Cidade/UF</th><th>Data Cadastro</th></tr></thead><tbody>");
                for (Cliente cl : clienteDAO.buscarTodos()) {
                    String endereco = escapeHtml(cl.getRua()) + ", " + escapeHtml(cl.getNumero());
                    String cidUf = escapeHtml(cl.getCidadeNome()) + " / " + escapeHtml(cl.getCidadeUf());
                    String dt = cl.getDataCadastro() != null ? sdf.format(cl.getDataCadastro()) : "-";
                    out.println("<tr><td>" + cl.getClienteId() + "</td><td>" + escapeHtml(cl.getNome()) + "</td><td>" + escapeHtml(cl.getCpf()) + "</td><td>" + endereco + "</td><td>" + escapeHtml(cl.getBairro()) + "</td><td>" + cidUf + "</td><td>" + dt + "</td></tr>");
                }
                out.println("</tbody></table>");
                break;

            case "produtos":
                out.println("<h2>Relatório - Lista de Produtos</h2>");
                out.println("<table><thead><tr><th>ID</th><th>Descrição</th><th>Categoria</th><th>Unidade</th><th>Preço Custo</th><th>Preço Venda</th><th>Estoque</th><th>Data Cadastro</th></tr></thead><tbody>");
                for (Produto p : produtoDAO.buscarTodos()) {
                    String dt = p.getDataCadastro() != null ? sdf.format(p.getDataCadastro()) : "-";
                    out.println("<tr><td>" + p.getProdutoId() + "</td><td>" + escapeHtml(p.getDescricao()) + "</td><td>" + escapeHtml(p.getCategoriaNome()) + "</td><td>" + escapeHtml(p.getUnidade()) + "</td><td class='text-right'>" + nf.format(p.getPrecoCusto()) + "</td><td class='text-right'>" + nf.format(p.getPrecoVenda()) + "</td><td>" + p.getQuantidade() + "</td><td>" + dt + "</td></tr>");
                }
                out.println("</tbody></table>");
                break;

            case "pedidos":
                out.println("<h2>Relatório - Lista de Pedidos e Itens</h2>");
                exportPedidosTabela(out, pedidoDAO.buscarTodosComItens(), sdf, nf);
                break;

            case "pedidos_abertos":
            case "pedidos-abertos":
                out.println("<h2>Relatório - Lista de Pedidos Abertos e Itens</h2>");
                exportPedidosTabela(out, pedidoDAO.buscarPorSituacaoComItens("aberto"), sdf, nf);
                break;

            case "pedidos_fechados":
            case "pedidos-fechados":
                out.println("<h2>Relatório - Lista de Pedidos Fechados e Itens</h2>");
                exportPedidosTabela(out, pedidoDAO.buscarPorSituacaoComItens("fechado"), sdf, nf);
                break;
        }

        out.println("</body></html>");
        out.flush();
    }

    private void exportPedidosTabela(PrintWriter out, java.util.List<Pedido> pedidos, SimpleDateFormat sdf, NumberFormat nf) {
        for (Pedido ped : pedidos) {
            String dt = ped.getData() != null ? sdf.format(ped.getData()) : "-";
            String situacao = ped.getSituacao() != null ? ped.getSituacao().toUpperCase() : "-";
            out.println("<br><table><thead>");
            out.println("<tr class='sub-header'><th colspan='5'>Pedido #" + ped.getPedidoId() + " | Cliente: " + escapeHtml(ped.getClienteNome()) + " | Data: " + dt + " | Situação: " + situacao + " | Total: " + nf.format(ped.getTotal()) + "</th></tr>");
            out.println("<tr><th>Produto</th><th>Unidade</th><th>Qtd</th><th>Preço Un.</th><th>Subtotal</th></tr>");
            out.println("</thead><tbody>");
            if (ped.getItens() == null || ped.getItens().isEmpty()) {
                out.println("<tr><td colspan='5'><i>Nenhum item cadastrado neste pedido.</i></td></tr>");
            } else {
                for (ItemPedido item : ped.getItens()) {
                    out.println("<tr><td>" + escapeHtml(item.getProdutoDescricao()) + "</td><td>" + escapeHtml(item.getUnidade()) + "</td><td>" + item.getQuantidade() + "</td><td class='text-right'>" + nf.format(item.getPrecoVenda()) + "</td><td class='text-right'>" + nf.format(item.getSubtotal()) + "</td></tr>");
                }
            }
            out.println("</tbody></table>");
        }
    }

    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
}
