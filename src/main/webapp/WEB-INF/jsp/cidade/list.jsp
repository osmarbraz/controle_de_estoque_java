<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/jsp/includes/header.jsp">
    <jsp:param name="title" value="Cadastro de Cidades - Controle de Estoque" />
</jsp:include>
<jsp:include page="/WEB-INF/jsp/includes/navbar.jsp" />

<div class="container">
    <div class="page-header">
        <div>
            <h1 class="page-title">🏙️ Cadastro de Cidades</h1>
            <p class="page-subtitle">Gestão das cidades cadastradas no sistema</p>
        </div>
        <a href="${pageContext.request.contextPath}/servlet/cidades?action=new" class="btn btn-primary">➕ Nova Cidade</a>
    </div>

    <c:if test="${param.msg == 'sucesso_salvar'}">
        <div class="alert alert-success">Cidade salva com sucesso!</div>
    </c:if>
    <c:if test="${param.msg == 'sucesso_excluir'}">
        <div class="alert alert-success">Cidade excluída com sucesso!</div>
    </c:if>
    <c:if test="${param.msg == 'erro_excluir_vinculo'}">
        <div class="alert alert-danger">Não é possível excluir esta cidade pois existem clientes vinculados a ela.</div>
    </c:if>

    <div class="card" style="padding: 0;">
        <div class="table-responsive">
            <table class="table">
                <thead>
                    <tr>
                        <th style="width: 80px;">ID</th>
                        <th>Nome da Cidade</th>
                        <th style="width: 100px;">UF</th>
                        <th style="width: 180px;" class="text-center">Ações</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${empty cidades}">
                            <tr>
                                <td colspan="4" class="text-center" style="padding: 2rem; color: var(--text-muted);">
                                    Nenhuma cidade cadastrada.
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="c" items="${cidades}">
                                <tr>
                                    <td>${c.cidadeId}</td>
                                    <td><strong>${c.nome}</strong></td>
                                    <td><span class="badge" style="background-color: #e2e8f0; color: #1e293b;">${c.uf}</span></td>
                                    <td class="text-center">
                                        <a href="${pageContext.request.contextPath}/servlet/cidades?action=edit&id=${c.cidadeId}" class="btn btn-sm btn-outline">✏️ Editar</a>
                                        <a href="${pageContext.request.contextPath}/servlet/cidades?action=delete&id=${c.cidadeId}" class="btn btn-sm btn-danger" onclick="return confirm('Deseja realmente excluir esta cidade?');">🗑️ Excluir</a>
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

<jsp:include page="/WEB-INF/jsp/includes/footer.jsp" />
