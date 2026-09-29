package com.estoque.controle;

import com.estoque.dao.CategoriaDAO;
import com.estoque.modelo.Categoria;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/servlet/categorias")
/**
 * Classe responsável por gerenciar a lógica de CategoriaServlet.
 */
public class CategoriaServlet extends HttpServlet {

    private final CategoriaDAO categoriaDAO = new CategoriaDAO();

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
                    request.setAttribute("categoria", new Categoria());
                    request.getRequestDispatcher("/categoria/form.jsp").forward(request, response);
                    break;
                case "editar":
                    int idEdit = Integer.parseInt(request.getParameter("id"));
                    Categoria cat = categoriaDAO.buscarPorId(idEdit);
                    if (cat != null) {
                        request.setAttribute("categoria", cat);
                        request.getRequestDispatcher("/categoria/form.jsp").forward(request, response);
                    } else {
                        response.sendRedirect(request.getContextPath() + "/servlet/categorias?msg=nao_encontrado");
                    }
                    break;
                case "excluir":
                    int idDel = Integer.parseInt(request.getParameter("id"));
                    try {
                        categoriaDAO.excluir(idDel);
                        response.sendRedirect(request.getContextPath() + "/servlet/categorias?msg=sucesso_excluir");
                    } catch (Exception e) {
                        response.sendRedirect(request.getContextPath() + "/servlet/categorias?msg=erro_excluir_vinculo");
                    }
                    break;
                case "listar":
                default:
                    List<Categoria> lista = categoriaDAO.buscarTodos();
                    request.setAttribute("categorias", lista);
                    request.getRequestDispatcher("/categoria/list.jsp").forward(request, response);
                    break;
            }
        } catch (Exception e) {
            throw new ServletException("Erro ao processar requisição em Categorias", e);
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
        String idStr = request.getParameter("categoriaId");
        String nome = request.getParameter("nome");

        try {
            Categoria categoria = new Categoria();
            categoria.setNome(nome != null ? nome.trim() : "");

            if (idStr != null && !idStr.trim().isEmpty()) {
                categoria.setCategoriaId(Integer.parseInt(idStr));
                categoriaDAO.atualizar(categoria);
            } else {
                categoriaDAO.inserir(categoria);
            }
            response.sendRedirect(request.getContextPath() + "/servlet/categorias?msg=sucesso_salvar");
        } catch (Exception e) {
            request.setAttribute("erro", "Erro ao salvar categoria: " + e.getMessage());
            request.setAttribute("categoria", new Categoria(
                    idStr != null && !idStr.isEmpty() ? Integer.parseInt(idStr) : null,
                    nome
            ));
            request.getRequestDispatcher("/categoria/form.jsp").forward(request, response);
        }
    }
}
