package com.estoque.controle;

import com.estoque.dao.CategoriaDAO;
import com.estoque.dao.ProdutoDAO;
import com.estoque.modelo.Produto;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/servlet/produtos")
/**
 * Classe responsável por gerenciar a lógica de ProdutoServlet.
 */
public class ProdutoServlet extends HttpServlet {

    private final ProdutoDAO produtoDAO = new ProdutoDAO();
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
                    request.setAttribute("produto", new Produto());
                    request.setAttribute("categorias", categoriaDAO.buscarTodos());
                    request.getRequestDispatcher("/produto/form.jsp").forward(request, response);
                    break;
                case "editar":
                    int idEdit = Integer.parseInt(request.getParameter("id"));
                    Produto produto = produtoDAO.buscarPorId(idEdit);
                    if (produto != null) {
                        request.setAttribute("produto", produto);
                        request.setAttribute("categorias", categoriaDAO.buscarTodos());
                        request.getRequestDispatcher("/produto/form.jsp").forward(request, response);
                    } else {
                        response.sendRedirect(request.getContextPath() + "/servlet/produtos?msg=nao_encontrado");
                    }
                    break;
                case "excluir":
                    int idDel = Integer.parseInt(request.getParameter("id"));
                    try {
                        produtoDAO.excluir(idDel);
                        response.sendRedirect(request.getContextPath() + "/servlet/produtos?msg=sucesso_excluir");
                    } catch (Exception e) {
                        response.sendRedirect(request.getContextPath() + "/servlet/produtos?msg=erro_excluir_vinculo");
                    }
                    break;
                case "listar":
                default:
                    request.setAttribute("produtos", produtoDAO.buscarTodos());
                    request.getRequestDispatcher("/produto/list.jsp").forward(request, response);
                    break;
            }
        } catch (Exception e) {
            throw new ServletException("Erro ao processar requisição em Produtos", e);
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
                produtoDAO.atualizar(produto);
            } else {
                produtoDAO.inserir(produto);
            }
            response.sendRedirect(request.getContextPath() + "/servlet/produtos?msg=sucesso_salvar");
        } catch (Exception e) {
            try {
                request.setAttribute("categorias", categoriaDAO.buscarTodos());
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
            request.getRequestDispatcher("/produto/form.jsp").forward(request, response);
        }
    }
}
