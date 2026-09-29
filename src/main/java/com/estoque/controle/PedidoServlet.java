package com.estoque.controle;

import com.estoque.dao.ClienteDAO;
import com.estoque.dao.PedidoDAO;
import com.estoque.dao.ProdutoDAO;
import com.estoque.modelo.ItemPedido;
import com.estoque.modelo.Pedido;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@WebServlet("/servlet/pedidos")
/**
 * Classe responsável por gerenciar a lógica de PedidoServlet.
 */
public class PedidoServlet extends HttpServlet {

    private final PedidoDAO pedidoDAO = new PedidoDAO();
    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final ProdutoDAO produtoDAO = new ProdutoDAO();

    @Override
    /**
     * Processa as requisições HTTP GET.
     * @param request Parâmetro necessário para a operação
     * @param response Parâmetro necessário para a operação
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String acao = request.getParameter("acao");
        if (acao == null) acao = "listar";

        try {
            switch (acao) {
                case "novo":
                    Pedido novo = new Pedido();
                    novo.setData(new Date());
                    novo.setSituacao("aberto");
                    request.setAttribute("pedido", novo);
                    request.setAttribute("clientes", clienteDAO.buscarTodos());
                    request.getRequestDispatcher("/pedido/form.jsp").forward(request, response);
                    break;
                case "visualizar":
                    int idView = Integer.parseInt(request.getParameter("id"));
                    Pedido pedView = pedidoDAO.buscarPorId(idView);
                    if (pedView != null) {
                        request.setAttribute("pedido", pedView);
                        request.getRequestDispatcher("/pedido/view.jsp").forward(request, response);
                    } else {
                        response.sendRedirect(request.getContextPath() + "/servlet/pedidos?msg=nao_encontrado");
                    }
                    break;
                case "status":
                    int idStatus = Integer.parseInt(request.getParameter("id"));
                    String status = request.getParameter("situacao");
                    if (status != null) {
                        Pedido pedidoExistente = pedidoDAO.buscarPorId(idStatus);
                        if (pedidoExistente != null && !status.equals(pedidoExistente.getSituacao())) {
                            List<ItemPedido> itens = pedidoExistente.getItens();
                            if (itens != null) {
                                for (ItemPedido item : itens) {
                                    if ("fechado".equals(status)) {
                                        // Aberto para Fechado: reduz o estoque
                                        produtoDAO.atualizarEstoque(item.getProdutoId(), -item.getQuantidade());
                                    } else if ("aberto".equals(status)) {
                                        // Fechado para Aberto: volta o estoque
                                        produtoDAO.atualizarEstoque(item.getProdutoId(), item.getQuantidade());
                                    }
                                }
                            }
                            pedidoDAO.atualizarSituacao(idStatus, status);
                        }
                    }
                    response.sendRedirect(request.getContextPath() + "/servlet/pedidos?msg=situacao_atualizada");
                    break;
                case "excluir":
                    int idDel = Integer.parseInt(request.getParameter("id"));
                    try {
                        pedidoDAO.excluir(idDel);
                        response.sendRedirect(request.getContextPath() + "/servlet/pedidos?msg=sucesso_excluir");
                    } catch (Exception e) {
                        response.sendRedirect(request.getContextPath() + "/servlet/pedidos?msg=erro_excluir");
                    }
                    break;
                case "listar":
                default:
                    List<Pedido> lista = pedidoDAO.buscarTodos();
                    request.setAttribute("pedidos", lista);
                    request.getRequestDispatcher("/pedido/list.jsp").forward(request, response);
                    break;
            }
        } catch (Exception e) {
            throw new ServletException("Erro ao processar requisição em Pedidos", e);
        }
    }

    @Override
    /**
     * Processa as requisições HTTP POST.
     * @param request Parâmetro necessário para a operação
     * @param response Parâmetro necessário para a operação
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String idStr = request.getParameter("pedidoId");
        String clienteIdStr = request.getParameter("clienteId");
        String dataStr = request.getParameter("data");
        String situacao = request.getParameter("situacao");

        try {
            Pedido pedido = new Pedido();
            if (clienteIdStr != null && !clienteIdStr.isEmpty()) {
                pedido.setClienteId(Integer.parseInt(clienteIdStr));
            }
            pedido.setSituacao(situacao != null && !situacao.isEmpty() ? situacao : "aberto");

            if (dataStr != null && !dataStr.isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                pedido.setData(sdf.parse(dataStr));
            } else {
                pedido.setData(new Date());
            }

            if (idStr != null && !idStr.trim().isEmpty()) {
                pedido.setPedidoId(Integer.parseInt(idStr));
                pedidoDAO.atualizarCabecalho(pedido);
                // Redireciona para gerenciar itens
                response.sendRedirect(request.getContextPath() + "/servlet/item-pedido?pedidoId=" + pedido.getPedidoId() + "&msg=pedido_atualizado");
            } else {
                pedido.setTotal(0.0);
                pedidoDAO.inserir(pedido);
                // Logo apos inserir o pedido, redireciona para a inclusao de itens de pedido
                response.sendRedirect(request.getContextPath() + "/servlet/item-pedido?pedidoId=" + pedido.getPedidoId() + "&msg=pedido_criado");
            }
        } catch (Exception e) {
            try {
                request.setAttribute("clientes", clienteDAO.buscarTodos());
            } catch (Exception ex) {
                // ignore
            }
            request.setAttribute("erro", "Erro ao salvar cabeçalho do pedido: " + e.getMessage());
            Pedido p = new Pedido();
            if (idStr != null && !idStr.isEmpty()) p.setPedidoId(Integer.parseInt(idStr));
            if (clienteIdStr != null && !clienteIdStr.isEmpty()) p.setClienteId(Integer.parseInt(clienteIdStr));
            p.setSituacao(situacao);
            p.setData(new Date());
            request.setAttribute("pedido", p);
            request.getRequestDispatcher("/pedido/form.jsp").forward(request, response);
        }
    }
}
