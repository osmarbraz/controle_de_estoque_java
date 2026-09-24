<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/jsp/includes/header.jsp">
    <jsp:param name="title" value="${produto.produtoId != null ? 'Editar Produto' : 'Novo Produto'}" />
</jsp:include>
<jsp:include page="/WEB-INF/jsp/includes/navbar.jsp" />

<div class="container">
    <div class="page-header">
        <div>
            <h1 class="page-title">${produto.produtoId != null ? '✏️ Editar Produto' : '➕ Novo Produto'}</h1>
            <p class="page-subtitle">Preencha os dados do produto abaixo</p>
        </div>
        <a href="${pageContext.request.contextPath}/servlet/produtos" class="btn btn-outline">⬅️ Voltar para a lista</a>
    </div>

    <c:if test="${not empty erro}">
        <div class="alert alert-danger">${erro}</div>
    </c:if>

    <div class="card">
        <form action="${pageContext.request.contextPath}/servlet/produtos" method="post" onsubmit="return validateProdutoForm(this);">
            <input type="hidden" name="produtoId" value="${produto.produtoId}" />

            <div class="form-grid">
                <div class="form-group" style="grid-column: span 2;">
                    <label for="descricao">Descrição do Produto *</label>
                    <input type="text" id="descricao" name="descricao" value="${produto.descricao}" placeholder="Ex: Teclado USB Mecânico" required autocomplete="off" />
                </div>

                <div class="form-group">
                    <label for="unidade">Unidade *</label>
                    <select id="unidade" name="unidade" required>
                        <option value="UN" ${produto.unidade == 'UN' ? 'selected' : ''}>UN - Unidade</option>
                        <option value="CX" ${produto.unidade == 'CX' ? 'selected' : ''}>CX - Caixa</option>
                        <option value="DZ" ${produto.unidade == 'DZ' ? 'selected' : ''}>DZ - Dúzia</option>
                        <option value="KG" ${produto.unidade == 'KG' ? 'selected' : ''}>KG - Quilo</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="categoriaId">Categoria *</label>
                    <select id="categoriaId" name="categoriaId" required>
                        <option value="">-- Selecione a Categoria --</option>
                        <c:forEach var="cat" items="${categorias}">
                            <option value="${cat.categoriaId}" ${produto.categoriaId == cat.categoriaId ? 'selected' : ''}>
                                ${cat.nome}
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group">
                    <label for="precoCusto">Preço de Custo (R$) *</label>
                    <input type="text" id="precoCusto" name="precoCusto" value="${produto.precoCusto}" placeholder="0.00" required autocomplete="off" />
                </div>

                <div class="form-group">
                    <label for="precoVenda">Preço de Venda (R$) *</label>
                    <input type="text" id="precoVenda" name="precoVenda" value="${produto.precoVenda}" placeholder="0.00" required autocomplete="off" />
                </div>

                <div class="form-group">
                    <label for="quantidade">Quantidade em Estoque *</label>
                    <input type="text" id="quantidade" name="quantidade" value="${produto.quantidade}" placeholder="0" required autocomplete="off" />
                </div>
            </div>

            <div style="margin-top: 1.5rem; display: flex; gap: 1rem;">
                <button type="submit" class="btn btn-primary">💾 Salvar Produto</button>
                <a href="${pageContext.request.contextPath}/servlet/produtos" class="btn btn-secondary">Cancelar</a>
            </div>
        </form>
    </div>
</div>

<jsp:include page="/WEB-INF/jsp/includes/footer.jsp" />
