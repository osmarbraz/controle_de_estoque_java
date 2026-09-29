<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/includes/header.jsp">
    <jsp:param name="title" value="Relatório de Cidades" />
</jsp:include>
<jsp:include page="/includes/navbar.jsp" />

<div class="container">
    <div class="page-header">
        <div>
            <h1 class="page-title">🏙️ Relatório de Cidades</h1>
            <p class="page-subtitle">Listagem oficial de cidades</p>
        </div>
        <div>
            <a href="${pageContext.request.contextPath}/servlet/relatorios?tipo=cidades&exportar=xls" class="btn btn-xls">📥 Baixar em XLS</a>
            <a href="${pageContext.request.contextPath}/servlet/relatorios" class="btn btn-outline" style="margin-left: 0.5rem;">⬅️ Voltar</a>
        </div>
    </div>

    <div class="card" style="padding: 0;">
        <div class="table-responsive">
            <table class="table">
                <thead>
                    <tr>
                        <th style="width: 80px;">ID</th>
                        <th>Nome da Cidade</th>
                        <th>UF</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${empty cidades}">
                            <tr>
                                <td colspan="3" class="text-center" style="padding: 2rem; color: var(--text-muted);">
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
