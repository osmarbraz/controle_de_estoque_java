<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/includes/header.jsp">
    <jsp:param name="title" value="Cadastro de Categorias - Sistema de Controle de Estoque & Pedidos (SCEP)" />
</jsp:include>
<jsp:include page="/includes/navbar.jsp" />

<div class="container">
    <div class="page-header">
        <div>
            <h1 class="page-title">🏷️ Cadastro de Categorias</h1>
            <p class="page-subtitle">Gestão das categorias de produtos do sistema</p>
        </div>
        <a href="${pageContext.request.contextPath}/servlet/categorias?acao=novo" class="btn btn-primary">➕ Nova Categoria</a>
    </div>

    <c:if test="${param.msg == 'sucesso_salvar'}">
        <div class="alert alert-success">Categoria salva com sucesso!</div>
    </c:if>
    <c:if test="${param.msg == 'sucesso_excluir'}">
        <div class="alert alert-success">Categoria excluída com sucesso!</div>
    </c:if>
    <c:if test="${param.msg == 'erro_excluir_vinculo'}">
        <div class="alert alert-danger">Não é possível excluir esta categoria pois existem produtos vinculados a ela.</div>
    </c:if>

    <div class="card" style="padding: 0;">
        <div class="table-responsive">
            <table class="table">
                <thead>
                    <tr>
                        <th style="width: 80px;">ID</th>
                        <th>Nome da Categoria</th>
                        <th style="width: 180px;" class="text-center">Ações</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${empty categorias}">
                            <tr>
                                <td colspan="3" class="text-center" style="padding: 2rem; color: var(--text-muted);">
                                    Nenhuma categoria cadastrada.
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="cat" items="${categorias}">
                                <tr>
                                    <td>${cat.categoriaId}</td>
                                    <td><strong>${cat.nome}</strong></td>
                                    <td class="text-center">
                                        <a href="${pageContext.request.contextPath}/servlet/categorias?acao=editar&id=${cat.categoriaId}" class="btn btn-sm btn-outline">✏️ Editar</a>
                                        <a href="${pageContext.request.contextPath}/servlet/categorias?acao=excluir&id=${cat.categoriaId}" class="btn btn-sm btn-danger" onclick="return confirm('Deseja realmente excluir esta categoria?');">🗑️ Excluir</a>
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
