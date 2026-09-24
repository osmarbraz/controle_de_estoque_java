package com.estoque.servlet;

import com.estoque.dao.CidadeDAO;
import com.estoque.dao.ClienteDAO;
import com.estoque.model.Cliente;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/servlet/clientes")
public class ClienteServlet extends HttpServlet {

    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final CidadeDAO cidadeDAO = new CidadeDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if (action == null) action = "list";

        try {
            switch (action) {
                case "new":
                    request.setAttribute("cliente", new Cliente());
                    request.setAttribute("cidades", cidadeDAO.findAll());
                    request.getRequestDispatcher("/WEB-INF/jsp/cliente/form.jsp").forward(request, response);
                    break;
                case "edit":
                    int idEdit = Integer.parseInt(request.getParameter("id"));
                    Cliente cliente = clienteDAO.findById(idEdit);
                    if (cliente != null) {
                        request.setAttribute("cliente", cliente);
                        request.setAttribute("cidades", cidadeDAO.findAll());
                        request.getRequestDispatcher("/WEB-INF/jsp/cliente/form.jsp").forward(request, response);
                    } else {
                        response.sendRedirect(request.getContextPath() + "/servlet/clientes?msg=nao_encontrado");
                    }
                    break;
                case "delete":
                    int idDel = Integer.parseInt(request.getParameter("id"));
                    try {
                        clienteDAO.delete(idDel);
                        response.sendRedirect(request.getContextPath() + "/servlet/clientes?msg=sucesso_excluir");
                    } catch (Exception e) {
                        response.sendRedirect(request.getContextPath() + "/servlet/clientes?msg=erro_excluir_vinculo");
                    }
                    break;
                case "list":
                default:
                    request.setAttribute("clientes", clienteDAO.findAll());
                    request.getRequestDispatcher("/WEB-INF/jsp/cliente/list.jsp").forward(request, response);
                    break;
            }
        } catch (Exception e) {
            throw new ServletException("Erro ao processar requisição em Clientes", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String idStr = request.getParameter("clienteId");
        String nome = request.getParameter("nome");
        String cpf = request.getParameter("cpf");
        String rua = request.getParameter("rua");
        String numero = request.getParameter("numero");
        String bairro = request.getParameter("bairro");
        String cidadeIdStr = request.getParameter("cidadeId");

        try {
            Cliente cliente = new Cliente();
            cliente.setNome(nome != null ? nome.trim() : "");
            cliente.setCpf(cpf != null ? cpf.trim() : "");
            cliente.setRua(rua != null ? rua.trim() : "");
            cliente.setNumero(numero != null ? numero.trim() : "");
            cliente.setBairro(bairro != null ? bairro.trim() : "");
            if (cidadeIdStr != null && !cidadeIdStr.isEmpty()) {
                cliente.setCidadeId(Integer.parseInt(cidadeIdStr));
            }

            if (idStr != null && !idStr.trim().isEmpty()) {
                cliente.setClienteId(Integer.parseInt(idStr));
                clienteDAO.update(cliente);
            } else {
                clienteDAO.insert(cliente);
            }
            response.sendRedirect(request.getContextPath() + "/servlet/clientes?msg=sucesso_salvar");
        } catch (Exception e) {
            try {
                request.setAttribute("cidades", cidadeDAO.findAll());
            } catch (Exception ex) {
                // ignore
            }
            request.setAttribute("erro", "Erro ao salvar cliente: " + e.getMessage());
            Cliente c = new Cliente();
            c.setNome(nome);
            c.setCpf(cpf);
            c.setRua(rua);
            c.setNumero(numero);
            c.setBairro(bairro);
            if (idStr != null && !idStr.isEmpty()) c.setClienteId(Integer.parseInt(idStr));
            if (cidadeIdStr != null && !cidadeIdStr.isEmpty()) c.setCidadeId(Integer.parseInt(cidadeIdStr));
            request.setAttribute("cliente", c);
            request.getRequestDispatcher("/WEB-INF/jsp/cliente/form.jsp").forward(request, response);
        }
    }
}
