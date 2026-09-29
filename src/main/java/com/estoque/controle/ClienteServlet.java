package com.estoque.controle;

import com.estoque.dao.CidadeDAO;
import com.estoque.dao.ClienteDAO;
import com.estoque.modelo.Cliente;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/servlet/clientes")
/**
 * Classe responsável por gerenciar a lógica de ClienteServlet.
 */
public class ClienteServlet extends HttpServlet {

    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final CidadeDAO cidadeDAO = new CidadeDAO();

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
                    request.setAttribute("cliente", new Cliente());
                    request.setAttribute("cidades", cidadeDAO.buscarTodos());
                    request.getRequestDispatcher("/cliente/form.jsp").forward(request, response);
                    break;
                case "editar":
                    int idEdit = Integer.parseInt(request.getParameter("id"));
                    Cliente cliente = clienteDAO.buscarPorId(idEdit);
                    if (cliente != null) {
                        request.setAttribute("cliente", cliente);
                        request.setAttribute("cidades", cidadeDAO.buscarTodos());
                        request.getRequestDispatcher("/cliente/form.jsp").forward(request, response);
                    } else {
                        response.sendRedirect(request.getContextPath() + "/servlet/clientes?msg=nao_encontrado");
                    }
                    break;
                case "excluir":
                    int idDel = Integer.parseInt(request.getParameter("id"));
                    try {
                        clienteDAO.excluir(idDel);
                        response.sendRedirect(request.getContextPath() + "/servlet/clientes?msg=sucesso_excluir");
                    } catch (Exception e) {
                        response.sendRedirect(request.getContextPath() + "/servlet/clientes?msg=erro_excluir_vinculo");
                    }
                    break;
                case "listar":
                default:
                    request.setAttribute("clientes", clienteDAO.buscarTodos());
                    request.getRequestDispatcher("/cliente/list.jsp").forward(request, response);
                    break;
            }
        } catch (Exception e) {
            throw new ServletException("Erro ao processar requisição em Clientes", e);
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
                clienteDAO.atualizar(cliente);
            } else {
                clienteDAO.inserir(cliente);
            }
            response.sendRedirect(request.getContextPath() + "/servlet/clientes?msg=sucesso_salvar");
        } catch (Exception e) {
            try {
                request.setAttribute("cidades", cidadeDAO.buscarTodos());
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
            request.getRequestDispatcher("/cliente/form.jsp").forward(request, response);
        }
    }
}
