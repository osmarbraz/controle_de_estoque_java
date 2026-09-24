package com.estoque.servlet;

import com.estoque.dao.ProdutoDAO;
import com.estoque.model.Produto;
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
public class ProdutoSearchServlet extends HttpServlet {

    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");

        String query = request.getParameter("q");
        try (PrintWriter out = response.getWriter()) {
            List<Produto> produtos = produtoDAO.searchByDescricao(query);
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
