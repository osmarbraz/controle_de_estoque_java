package com.estoque.controle;

import com.estoque.dao.ProdutoDAO;
import com.estoque.modelo.Produto;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/servlet/produtos-search")
/**
 * Classe responsável por gerenciar a lógica de ProdutoSearchServlet.
 */
public class ProdutoProcuraServlet extends HttpServlet {

    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    private final Gson gson = new Gson();

    @Override
    /**
     * Processa as requisições HTTP GET.
     * 
     * @param request  Parâmetro necessário para a operação
     * @param response Parâmetro necessário para a operação
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");

        String consulta = request.getParameter("q");
        try (PrintWriter out = response.getWriter()) {
            List<Produto> produtos = produtoDAO.buscarPorDescricao(consulta);
            out.print(gson.toJson(produtos));
            out.flush();
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try (PrintWriter out = response.getWriter()) {
                out.print("{\"error\": \"" + e.getMessage() + "\"}");
            }
        }
    }
}
