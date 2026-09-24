<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/jsp/includes/header.jsp">
    <jsp:param name="title" value="${cidade.cidadeId != null ? 'Editar Cidade' : 'Nova Cidade'}" />
</jsp:include>
<jsp:include page="/WEB-INF/jsp/includes/navbar.jsp" />

<div class="container">
    <div class="page-header">
        <div>
            <h1 class="page-title">${cidade.cidadeId != null ? '✏️ Editar Cidade' : '➕ Nova Cidade'}</h1>
            <p class="page-subtitle">Preencha os dados da cidade abaixo</p>
        </div>
        <a href="${pageContext.request.contextPath}/servlet/cidades" class="btn btn-outline">⬅️ Voltar para a lista</a>
    </div>

    <c:if test="${not empty erro}">
        <div class="alert alert-danger">${erro}</div>
    </c:if>

    <div class="card">
        <form action="${pageContext.request.contextPath}/servlet/cidades" method="post" onsubmit="return validateCidadeForm(this);">
            <input type="hidden" name="cidadeId" value="${cidade.cidadeId}" />

            <div class="form-grid">
                <div class="form-group" style="grid-column: span 2;">
                    <label for="nome">Nome da Cidade *</label>
                    <input type="text" id="nome" name="nome" value="${cidade.nome}" placeholder="Ex: São Paulo" required autocomplete="off" />
                </div>

                <div class="form-group">
                    <label for="uf">UF (Estado) *</label>
                    <input type="text" id="uf" name="uf" value="${cidade.uf}" maxlength="2" placeholder="Ex: SP" style="text-transform: uppercase;" required autocomplete="off" />
                </div>
            </div>

            <div style="margin-top: 1.5rem; display: flex; gap: 1rem;">
                <button type="submit" class="btn btn-primary">💾 Salvar Cidade</button>
                <a href="${pageContext.request.contextPath}/servlet/cidades" class="btn btn-secondary">Cancelar</a>
            </div>
        </form>
    </div>
</div>

<jsp:include page="/WEB-INF/jsp/includes/footer.jsp" />
