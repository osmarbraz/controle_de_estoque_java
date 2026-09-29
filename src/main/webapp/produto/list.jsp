<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/includes/header.jsp">
    <jsp:param name="title" value="Cadastro de Produtos - Sistema de Controle de Estoque & Pedidos (SCEP)" />
</jsp:include>
<jsp:include page="/includes/navbar.jsp" />

<div class="container">
    <div class="page-header">
        <div>
            <h1 class="page-title">📦 Cadastro de Produtos</h1>
            <p class="page-subtitle">Gestão de produtos e saldos de estoque</p>
        </div>
        <a href="${pageContext.request.contextPath}/servlet/produtos?acao=novo" class="btn btn-primary">➕ Novo Produto</a>
    </div>

    <c:if test="${param.msg == 'sucesso_salvar'}">
        <div class="alert alert-success">Produto salvo com sucesso!</div>
    </c:if>
    <c:if test="${param.msg == 'sucesso_excluir'}">
        <div class="alert alert-success">Produto excluído com sucesso!</div>
    </c:if>
    <c:if test="${param.msg == 'erro_excluir_vinculo'}">
        <div class="alert alert-danger">Não é possível excluir este produto pois ele já foi incluído em algum pedido.</div>
    </c:if>

    <div class="card" style="padding: 0;">
        <div class="table-responsive">
            <table class="table">
                <thead>
                    <tr>
                        <th style="width: 60px;">ID</th>
                        <th>Descrição</th>
                        <th>Categoria</th>
                        <th style="width: 80px;">Unidade</th>
                        <th class="text-right">Preço Custo</th>
                        <th class="text-right">Preço Venda</th>
                        <th class="text-right">Estoque</th>
                        <th style="width: 160px;" class="text-center">Ações</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${empty produtos}">
                            <tr>
                                <td colspan="8" class="text-center" style="padding: 2rem; color: var(--text-muted);">
                                    Nenhum produto cadastrado.
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="p" items="${produtos}">
                                <tr>
                                    <td>${p.produtoId}</td>
                                    <td><strong>${p.descricao}</strong></td>
                                    <td><span class="badge" style="background-color: #f1f5f9; color: var(--primary);">${p.categoriaNome}</span></td>
                                    <td>${p.unidade}</td>
                                    <td class="text-right"><fmt:formatNumber value="${p.precoCusto}" type="currency" currencySymbol="R$" /></td>
                                    <td class="text-right" style="font-weight: bold; color: var(--accent);"><fmt:formatNumber value="${p.precoVenda}" type="currency" currencySymbol="R$" /></td>
                                    <td class="text-right">
                                        <span class="badge ${p.quantidade > 0 ? 'badge-closed' : 'badge-open'}">
                                            ${p.quantidade}
                                        </span>
                                    </td>
                                    <td class="text-center">
                                        <a href="${pageContext.request.contextPath}/servlet/produtos?acao=editar&id=${p.produtoId}" class="btn btn-sm btn-outline">✏️ Editar</a>
                                        <a href="${pageContext.request.contextPath}/servlet/produtos?acao=excluir&id=${p.produtoId}" class="btn btn-sm btn-danger" onclick="return confirm('Deseja realmente excluir este produto?');">🗑️ Excluir</a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>
    </div>
</div>

<jsp:include page="/includes/footer.jsp" />
