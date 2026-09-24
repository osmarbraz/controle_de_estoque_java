package com.estoque.servlet;

import com.estoque.dao.ItemPedidoDAO;
import com.estoque.dao.PedidoDAO;
import com.estoque.dao.ProdutoDAO;
import com.estoque.model.ItemPedido;
import com.estoque.model.Pedido;
import com.estoque.model.Produto;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/servlet/item-pedido")
public class ItemPedidoServlet extends HttpServlet {

    private final PedidoDAO pedidoDAO = new PedidoDAO();
    private final ItemPedidoDAO itemPedidoDAO = new ItemPedidoDAO();
    private final ProdutoDAO produtoDAO = new ProdutoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String pedidoIdStr = request.getParameter("pedidoId");
        String action = request.getParameter("action");

        if (pedidoIdStr == null || pedidoIdStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/servlet/pedidos?msg=pedido_invalido");
            return;
        }

        int pedidoId = Integer.parseInt(pedidoIdStr);

        try {
            if ("delete".equals(action)) {
                int produtoId = Integer.parseInt(request.getParameter("produtoId"));
                ItemPedido itemExistente = itemPedidoDAO.findByPedidoAndProduto(pedidoId, produtoId);
                if (itemExistente != null) {
                    // Restaura estoque do produto ao remover item do pedido
                    produtoDAO.updateEstoque(produtoId, itemExistente.getQuantidade());
                    itemPedidoDAO.delete(pedidoId, produtoId);
                    pedidoDAO.recalculateTotal(pedidoId);
                }
                response.sendRedirect(request.getContextPath() + "/servlet/item-pedido?pedidoId=" + pedidoId + "&msg=item_removido");
                return;
            }

            Pedido pedido = pedidoDAO.findById(pedidoId);
            if (pedido == null) {
                response.sendRedirect(request.getContextPath() + "/servlet/pedidos?msg=nao_encontrado");
                return;
            }

            List<Produto> produtos = produtoDAO.findAll();

            request.setAttribute("pedido", pedido);
            request.setAttribute("produtos", produtos);
            request.getRequestDispatcher("/WEB-INF/jsp/pedido/items.jsp").forward(request, response);

        } catch (Exception e) {
            throw new ServletException("Erro ao carregar itens do pedido", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String pedidoIdStr = request.getParameter("pedidoId");
        String produtoIdStr = request.getParameter("produtoId");
        String quantidadeStr = request.getParameter("quantidade");
        String precoVendaStr = request.getParameter("precoVenda");

        if (pedidoIdStr == null || pedidoIdStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/servlet/pedidos?msg=pedido_invalido");
            return;
        }

        int pedidoId = Integer.parseInt(pedidoIdStr);

        try {
            Pedido pedido = pedidoDAO.findById(pedidoId);
            if (pedido == null) {
                response.sendRedirect(request.getContextPath() + "/servlet/pedidos?msg=nao_encontrado");
                return;
            }

            if ("fechado".equalsIgnoreCase(pedido.getSituacao())) {
                response.sendRedirect(request.getContextPath() + "/servlet/item-pedido?pedidoId=" + pedidoId + "&msg=erro_pedido_fechado");
                return;
            }

            int produtoId = Integer.parseInt(produtoIdStr);
            double quantidade = Double.parseDouble(quantidadeStr.replace(",", "."));
            double precoVenda = Double.parseDouble(precoVendaStr.replace(",", "."));

            if (quantidade <= 0) {
                response.sendRedirect(request.getContextPath() + "/servlet/item-pedido?pedidoId=" + pedidoId + "&msg=erro_qtd_invalida");
                return;
            }

            // Verificar se o item ja existia para ajustar o estoque corretamente
            ItemPedido itemExistente = itemPedidoDAO.findByPedidoAndProduto(pedidoId, produtoId);
            double deltaEstoque;
            if (itemExistente != null) {
                deltaEstoque = itemExistente.getQuantidade() - quantidade; // se aumentou qtd, reduz mais estoque
            } else {
                deltaEstoque = -quantidade; // reduz estoque
            }

            ItemPedido item = new ItemPedido(pedidoId, produtoId, quantidade, precoVenda);
            itemPedidoDAO.insert(item);
            produtoDAO.updateEstoque(produtoId, deltaEstoque);
            pedidoDAO.recalculateTotal(pedidoId);

            response.sendRedirect(request.getContextPath() + "/servlet/item-pedido?pedidoId=" + pedidoId + "&msg=item_adicionado");

        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/servlet/item-pedido?pedidoId=" + pedidoId + "&msg=erro_adicionar_item");
        }
    }
}
