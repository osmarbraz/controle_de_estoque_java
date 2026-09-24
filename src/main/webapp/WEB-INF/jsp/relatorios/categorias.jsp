<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/jsp/includes/header.jsp">
    <jsp:param name="title" value="Relatório de Categorias" />
</jsp:include>
<jsp:include page="/WEB-INF/jsp/includes/navbar.jsp" />

<div class="container">
    <div class="page-header">
        <div>
            <h1 class="page-title">🏷️ Relatório de Categorias</h1>
            <p class="page-subtitle">Listagem de categorias de produtos</p>
        </div>
        <div>
            <a href="${pageContext.request.contextPath}/servlet/relatorios?tipo=categorias&export=xls" class="btn btn-xls">📥 Baixar em XLS</a>
            <a href="${pageContext.request.contextPath}/servlet/relatorios" class="btn btn-outline" style="margin-left: 0.5rem;">⬅️ Voltar</a>
        </div>
    </div>

    <div class="card" style="padding: 0;">
        <div class="table-responsive">
            <table class="table">
                <thead>
                    <tr>
                        <th style="width: 80px;">ID</th>
                        <th>Nome da Categoria</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${empty categorias}">
                            <tr>
                                <td colspan="2" class="text-center" style="padding: 2rem; color: var(--text-muted);">
                                    Nenhuma categoria cadastrada.
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="cat" items="${categorias}">
                                <tr>
                                    <td>${cat.categoriaId}</td>
                                    <td><strong>${cat.nome}</strong></td>
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
