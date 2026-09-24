package com.estoque.servlet;

import com.estoque.dao.CidadeDAO;
import com.estoque.model.Cidade;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/servlet/cidades")
public class CidadeServlet extends HttpServlet {

    private final CidadeDAO cidadeDAO = new CidadeDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if (action == null) action = "list";

        try {
            switch (action) {
                case "new":
                    request.setAttribute("cidade", new Cidade());
                    request.getRequestDispatcher("/WEB-INF/jsp/cidade/form.jsp").forward(request, response);
                    break;
                case "edit":
                    int idEdit = Integer.parseInt(request.getParameter("id"));
                    Cidade cidade = cidadeDAO.findById(idEdit);
                    if (cidade != null) {
                        request.setAttribute("cidade", cidade);
                        request.getRequestDispatcher("/WEB-INF/jsp/cidade/form.jsp").forward(request, response);
                    } else {
                        response.sendRedirect(request.getContextPath() + "/servlet/cidades?msg=nao_encontrado");
                    }
                    break;
                case "delete":
                    int idDel = Integer.parseInt(request.getParameter("id"));
                    try {
                        cidadeDAO.delete(idDel);
                        response.sendRedirect(request.getContextPath() + "/servlet/cidades?msg=sucesso_excluir");
                    } catch (Exception e) {
                        response.sendRedirect(request.getContextPath() + "/servlet/cidades?msg=erro_excluir_vinculo");
                    }
                    break;
                case "list":
                default:
                    List<Cidade> lista = cidadeDAO.findAll();
                    request.setAttribute("cidades", lista);
                    request.getRequestDispatcher("/WEB-INF/jsp/cidade/list.jsp").forward(request, response);
                    break;
            }
        } catch (Exception e) {
            throw new ServletException("Erro ao processar requisição em Cidades", e);
        }
    }

    @Override
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
                cidadeDAO.update(cidade);
            } else {
                cidadeDAO.insert(cidade);
            }
            response.sendRedirect(request.getContextPath() + "/servlet/cidades?msg=sucesso_salvar");
        } catch (Exception e) {
            request.setAttribute("erro", "Erro ao salvar cidade: " + e.getMessage());
            request.setAttribute("cidade", new Cidade(
                    idStr != null && !idStr.isEmpty() ? Integer.parseInt(idStr) : null,
                    nome, uf
            ));
            request.getRequestDispatcher("/WEB-INF/jsp/cidade/form.jsp").forward(request, response);
        }
    }
}
