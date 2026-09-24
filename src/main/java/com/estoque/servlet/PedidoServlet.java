package com.estoque.servlet;

import com.estoque.dao.ClienteDAO;
import com.estoque.dao.PedidoDAO;
import com.estoque.model.Pedido;

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
public class PedidoServlet extends HttpServlet {

    private final PedidoDAO pedidoDAO = new PedidoDAO();
    private final ClienteDAO clienteDAO = new ClienteDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if (action == null) action = "list";

        try {
            switch (action) {
                case "new":
                    Pedido novo = new Pedido();
                    novo.setData(new Date());
                    novo.setSituacao("aberto");
                    request.setAttribute("pedido", novo);
                    request.setAttribute("clientes", clienteDAO.findAll());
                    request.getRequestDispatcher("/WEB-INF/jsp/pedido/form.jsp").forward(request, response);
                    break;
                case "view":
                    int idView = Integer.parseInt(request.getParameter("id"));
                    Pedido pedView = pedidoDAO.findById(idView);
                    if (pedView != null) {
                        request.setAttribute("pedido", pedView);
                        request.getRequestDispatcher("/WEB-INF/jsp/pedido/view.jsp").forward(request, response);
                    } else {
                        response.sendRedirect(request.getContextPath() + "/servlet/pedidos?msg=nao_encontrado");
                    }
                    break;
                case "status":
                    int idStatus = Integer.parseInt(request.getParameter("id"));
                    String status = request.getParameter("situacao");
                    if (status != null) {
                        pedidoDAO.updateSituacao(idStatus, status);
                    }
                    response.sendRedirect(request.getContextPath() + "/servlet/pedidos?msg=situacao_atualizada");
                    break;
                case "delete":
                    int idDel = Integer.parseInt(request.getParameter("id"));
                    try {
                        pedidoDAO.delete(idDel);
                        response.sendRedirect(request.getContextPath() + "/servlet/pedidos?msg=sucesso_excluir");
                    } catch (Exception e) {
                        response.sendRedirect(request.getContextPath() + "/servlet/pedidos?msg=erro_excluir");
                    }
                    break;
                case "list":
                default:
                    List<Pedido> lista = pedidoDAO.findAll();
                    request.setAttribute("pedidos", lista);
                    request.getRequestDispatcher("/WEB-INF/jsp/pedido/list.jsp").forward(request, response);
                    break;
            }
        } catch (Exception e) {
            throw new ServletException("Erro ao processar requisição em Pedidos", e);
        }
    }

    @Override
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
                pedidoDAO.updateHeader(pedido);
                // Redireciona para gerenciar itens
                response.sendRedirect(request.getContextPath() + "/servlet/item-pedido?pedidoId=" + pedido.getPedidoId() + "&msg=pedido_atualizado");
            } else {
                pedido.setTotal(0.0);
                pedidoDAO.insert(pedido);
                // Logo apos inserir o pedido, redireciona para a inclusao de itens de pedido
                response.sendRedirect(request.getContextPath() + "/servlet/item-pedido?pedidoId=" + pedido.getPedidoId() + "&msg=pedido_criado");
            }
        } catch (Exception e) {
            try {
                request.setAttribute("clientes", clienteDAO.findAll());
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
            request.getRequestDispatcher("/WEB-INF/jsp/pedido/form.jsp").forward(request, response);
        }
    }
}
