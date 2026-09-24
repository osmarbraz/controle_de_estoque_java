<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/jsp/includes/header.jsp">
    <jsp:param name="title" value="${categoria.categoriaId != null ? 'Editar Categoria' : 'Nova Categoria'}" />
</jsp:include>
<jsp:include page="/WEB-INF/jsp/includes/navbar.jsp" />

<div class="container">
    <div class="page-header">
        <div>
            <h1 class="page-title">${categoria.categoriaId != null ? '✏️ Editar Categoria' : '➕ Nova Categoria'}</h1>
            <p class="page-subtitle">Preencha o nome da categoria abaixo</p>
        </div>
        <a href="${pageContext.request.contextPath}/servlet/categorias" class="btn btn-outline">⬅️ Voltar para a lista</a>
    </div>

    <c:if test="${not empty erro}">
        <div class="alert alert-danger">${erro}</div>
    </c:if>

    <div class="card">
        <form action="${pageContext.request.contextPath}/servlet/categorias" method="post" onsubmit="return validateCategoriaForm(this);">
            <input type="hidden" name="categoriaId" value="${categoria.categoriaId}" />

            <div class="form-grid">
                <div class="form-group full-width">
                    <label for="nome">Nome da Categoria *</label>
                    <input type="text" id="nome" name="nome" value="${categoria.nome}" placeholder="Ex: Eletrônicos, Informática" required autocomplete="off" />
                </div>
            </div>

            <div style="margin-top: 1.5rem; display: flex; gap: 1rem;">
                <button type="submit" class="btn btn-primary">💾 Salvar Categoria</button>
                <a href="${pageContext.request.contextPath}/servlet/categorias" class="btn btn-secondary">Cancelar</a>
            </div>
        </form>
    </div>
</div>

<jsp:include page="/WEB-INF/jsp/includes/footer.jsp" />
