package com.estoque.servlet;

import com.estoque.dao.CategoriaDAO;
import com.estoque.model.Categoria;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/servlet/categorias")
public class CategoriaServlet extends HttpServlet {

    private final CategoriaDAO categoriaDAO = new CategoriaDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if (action == null) action = "list";

        try {
            switch (action) {
                case "new":
                    request.setAttribute("categoria", new Categoria());
                    request.getRequestDispatcher("/WEB-INF/jsp/categoria/form.jsp").forward(request, response);
                    break;
                case "edit":
                    int idEdit = Integer.parseInt(request.getParameter("id"));
                    Categoria cat = categoriaDAO.findById(idEdit);
                    if (cat != null) {
                        request.setAttribute("categoria", cat);
                        request.getRequestDispatcher("/WEB-INF/jsp/categoria/form.jsp").forward(request, response);
                    } else {
                        response.sendRedirect(request.getContextPath() + "/servlet/categorias?msg=nao_encontrado");
                    }
                    break;
                case "delete":
                    int idDel = Integer.parseInt(request.getParameter("id"));
                    try {
                        categoriaDAO.delete(idDel);
                        response.sendRedirect(request.getContextPath() + "/servlet/categorias?msg=sucesso_excluir");
                    } catch (Exception e) {
                        response.sendRedirect(request.getContextPath() + "/servlet/categorias?msg=erro_excluir_vinculo");
                    }
                    break;
                case "list":
                default:
                    List<Categoria> lista = categoriaDAO.findAll();
                    request.setAttribute("categorias", lista);
                    request.getRequestDispatcher("/WEB-INF/jsp/categoria/list.jsp").forward(request, response);
                    break;
            }
        } catch (Exception e) {
            throw new ServletException("Erro ao processar requisição em Categorias", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String idStr = request.getParameter("categoriaId");
        String nome = request.getParameter("nome");

        try {
            Categoria categoria = new Categoria();
            categoria.setNome(nome != null ? nome.trim() : "");

            if (idStr != null && !idStr.trim().isEmpty()) {
                categoria.setCategoriaId(Integer.parseInt(idStr));
                categoriaDAO.update(categoria);
            } else {
                categoriaDAO.insert(categoria);
            }
            response.sendRedirect(request.getContextPath() + "/servlet/categorias?msg=sucesso_salvar");
        } catch (Exception e) {
            request.setAttribute("erro", "Erro ao salvar categoria: " + e.getMessage());
            request.setAttribute("categoria", new Categoria(
                    idStr != null && !idStr.isEmpty() ? Integer.parseInt(idStr) : null,
                    nome
            ));
            request.getRequestDispatcher("/WEB-INF/jsp/categoria/form.jsp").forward(request, response);
        }
    }
}
