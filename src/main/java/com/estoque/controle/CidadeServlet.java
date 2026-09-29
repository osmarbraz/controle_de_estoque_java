package com.estoque.controle;

import com.estoque.dao.CidadeDAO;
import com.estoque.modelo.Cidade;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/servlet/cidades")
/**
 * Classe responsável por gerenciar a lógica de CidadeServlet.
 */
public class CidadeServlet extends HttpServlet {

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
                    request.setAttribute("cidade", new Cidade());
                    request.getRequestDispatcher("/cidade/form.jsp").forward(request, response);
                    break;
                case "editar":
                    int idEdit = Integer.parseInt(request.getParameter("id"));
                    Cidade cidade = cidadeDAO.buscarPorId(idEdit);
                    if (cidade != null) {
                        request.setAttribute("cidade", cidade);
                        request.getRequestDispatcher("/cidade/form.jsp").forward(request, response);
                    } else {
                        response.sendRedirect(request.getContextPath() + "/servlet/cidades?msg=nao_encontrado");
                    }
                    break;
                case "excluir":
                    int idDel = Integer.parseInt(request.getParameter("id"));
                    try {
                        cidadeDAO.excluir(idDel);
                        response.sendRedirect(request.getContextPath() + "/servlet/cidades?msg=sucesso_excluir");
                    } catch (Exception e) {
                        response.sendRedirect(request.getContextPath() + "/servlet/cidades?msg=erro_excluir_vinculo");
                    }
                    break;
                case "listar":
                default:
                    List<Cidade> lista = cidadeDAO.buscarTodos();
                    request.setAttribute("cidades", lista);
                    request.getRequestDispatcher("/cidade/list.jsp").forward(request, response);
                    break;
            }
        } catch (Exception e) {
            throw new ServletException("Erro ao processar requisição em Cidades", e);
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
        String idStr = request.getParameter("cidadeId");
        String nome = request.getParameter("nome");
        String uf = request.getParameter("uf");

        try {
            Cidade cidade = new Cidade();
            cidade.setNome(nome != null ? nome.trim() : "");
            cidade.setUf(uf != null ? uf.trim().toUpperCase() : "");

            if (idStr != null && !idStr.trim().isEmpty()) {
                cidade.setCidadeId(Integer.parseInt(idStr));
                cidadeDAO.atualizar(cidade);
            } else {
                cidadeDAO.inserir(cidade);
            }
            response.sendRedirect(request.getContextPath() + "/servlet/cidades?msg=sucesso_salvar");
        } catch (Exception e) {
            request.setAttribute("erro", "Erro ao salvar cidade: " + e.getMessage());
            request.setAttribute("cidade", new Cidade(
                    idStr != null && !idStr.isEmpty() ? Integer.parseInt(idStr) : null,
                    nome, uf
            ));
            request.getRequestDispatcher("/cidade/form.jsp").forward(request, response);
        }
    }
}
