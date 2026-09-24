package com.estoque.servlet;

import com.estoque.dao.CategoriaDAO;
import com.estoque.dao.ProdutoDAO;
import com.estoque.model.Produto;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/servlet/produtos")
public class ProdutoServlet extends HttpServlet {

    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if (action == null) action = "list";

        try {
            switch (action) {
                case "new":
                    request.setAttribute("produto", new Produto());
                    request.setAttribute("categorias", categoriaDAO.findAll());
                    request.getRequestDispatcher("/WEB-INF/jsp/produto/form.jsp").forward(request, response);
                    break;
                case "edit":
                    int idEdit = Integer.parseInt(request.getParameter("id"));
                    Produto produto = produtoDAO.findById(idEdit);
                    if (produto != null) {
                        request.setAttribute("produto", produto);
                        request.setAttribute("categorias", categoriaDAO.findAll());
                        request.getRequestDispatcher("/WEB-INF/jsp/produto/form.jsp").forward(request, response);
                    } else {
                        response.sendRedirect(request.getContextPath() + "/servlet/produtos?msg=nao_encontrado");
                    }
                    break;
                case "delete":
                    int idDel = Integer.parseInt(request.getParameter("id"));
                    try {
                        produtoDAO.delete(idDel);
                        response.sendRedirect(request.getContextPath() + "/servlet/produtos?msg=sucesso_excluir");
                    } catch (Exception e) {
                        response.sendRedirect(request.getContextPath() + "/servlet/produtos?msg=erro_excluir_vinculo");
                    }
                    break;
                case "list":
                default:
                    request.setAttribute("produtos", produtoDAO.findAll());
                    request.getRequestDispatcher("/WEB-INF/jsp/produto/list.jsp").forward(request, response);
                    break;
            }
        } catch (Exception e) {
            throw new ServletException("Erro ao processar requisição em Produtos", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String idStr = request.getParameter("produtoId");
        String descricao = request.getParameter("descricao");
        String unidade = request.getParameter("unidade");
        String precoCustoStr = request.getParameter("precoCusto");
        String precoVendaStr = request.getParameter("precoVenda");
        String quantidadeStr = request.getParameter("quantidade");
        String categoriaIdStr = request.getParameter("categoriaId");

        try {
            Produto produto = new Produto();
            produto.setDescricao(descricao != null ? descricao.trim() : "");
            produto.setUnidade(unidade != null ? unidade.trim() : "UN");

            double precoCusto = (precoCustoStr != null && !precoCustoStr.isEmpty())
                    ? Double.parseDouble(precoCustoStr.replace(",", ".")) : 0.0;
            double precoVenda = (precoVendaStr != null && !precoVendaStr.isEmpty())
                    ? Double.parseDouble(precoVendaStr.replace(",", ".")) : 0.0;
            double quantidade = (quantidadeStr != null && !quantidadeStr.isEmpty())
                    ? Double.parseDouble(quantidadeStr.replace(",", ".")) : 0.0;

            produto.setPrecoCusto(precoCusto);
            produto.setPrecoVenda(precoVenda);
            produto.setQuantidade(quantidade);

            if (categoriaIdStr != null && !categoriaIdStr.isEmpty()) {
                produto.setCategoriaId(Integer.parseInt(categoriaIdStr));
            }

            if (idStr != null && !idStr.trim().isEmpty()) {
                produto.setProdutoId(Integer.parseInt(idStr));
                produtoDAO.update(produto);
            } else {
                produtoDAO.insert(produto);
            }
            response.sendRedirect(request.getContextPath() + "/servlet/produtos?msg=sucesso_salvar");
        } catch (Exception e) {
            try {
                request.setAttribute("categorias", categoriaDAO.findAll());
            } catch (Exception ex) {
                // ignore
            }
            request.setAttribute("erro", "Erro ao salvar produto: " + e.getMessage());
            Produto p = new Produto();
            p.setDescricao(descricao);
            p.setUnidade(unidade);
            if (idStr != null && !idStr.isEmpty()) p.setProdutoId(Integer.parseInt(idStr));
            if (categoriaIdStr != null && !categoriaIdStr.isEmpty()) p.setCategoriaId(Integer.parseInt(categoriaIdStr));
            request.setAttribute("produto", p);
            request.getRequestDispatcher("/WEB-INF/jsp/produto/form.jsp").forward(request, response);
        }
    }
}
