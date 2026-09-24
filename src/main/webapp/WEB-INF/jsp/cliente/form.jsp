<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/jsp/includes/header.jsp">
    <jsp:param name="title" value="${cliente.clienteId != null ? 'Editar Cliente' : 'Novo Cliente'}" />
</jsp:include>
<jsp:include page="/WEB-INF/jsp/includes/navbar.jsp" />

<div class="container">
    <div class="page-header">
        <div>
            <h1 class="page-title">${cliente.clienteId != null ? '✏️ Editar Cliente' : '➕ Novo Cliente'}</h1>
            <p class="page-subtitle">Preencha os dados do cliente abaixo</p>
        </div>
        <a href="${pageContext.request.contextPath}/servlet/clientes" class="btn btn-outline">⬅️ Voltar para a lista</a>
    </div>

    <c:if test="${not empty erro}">
        <div class="alert alert-danger">${erro}</div>
    </c:if>

    <div class="card">
        <form action="${pageContext.request.contextPath}/servlet/clientes" method="post" onsubmit="return validateClienteForm(this);">
            <input type="hidden" name="clienteId" value="${cliente.clienteId}" />

            <div class="form-grid">
                <div class="form-group" style="grid-column: span 2;">
                    <label for="nome">Nome Completo *</label>
                    <input type="text" id="nome" name="nome" value="${cliente.nome}" placeholder="Ex: João da Silva" required autocomplete="off" />
                </div>

                <div class="form-group">
                    <label for="cpf">CPF *</label>
                    <input type="text" id="cpf" name="cpf" value="${cliente.cpf}" class="mask-cpf" placeholder="000.000.000-00" required autocomplete="off" />
                </div>

                <div class="form-group" style="grid-column: span 2;">
                    <label for="rua">Logradouro / Rua</label>
                    <input type="text" id="rua" name="rua" value="${cliente.rua}" placeholder="Ex: Av. Paulista" autocomplete="off" />
                </div>

                <div class="form-group">
                    <label for="numero">Número</label>
                    <input type="text" id="numero" name="numero" value="${cliente.numero}" placeholder="Ex: 1000" autocomplete="off" />
                </div>

                <div class="form-group">
                    <label for="bairro">Bairro</label>
                    <input type="text" id="bairro" name="bairro" value="${cliente.bairro}" placeholder="Ex: Centro" autocomplete="off" />
                </div>

                <div class="form-group" style="grid-column: span 2;">
                    <label for="cidadeId">Cidade / Estado *</label>
                    <select id="cidadeId" name="cidadeId" required>
                        <option value="">-- Selecione uma Cidade --</option>
                        <c:forEach var="cid" items="${cidades}">
                            <option value="${cid.cidadeId}" ${cliente.cidadeId == cid.cidadeId ? 'selected' : ''}>
                                ${cid.nome} - ${cid.uf}
                            </option>
                        </c:forEach>
                    </select>
                </div>
            </div>

            <div style="margin-top: 1.5rem; display: flex; gap: 1rem;">
                <button type="submit" class="btn btn-primary">💾 Salvar Cliente</button>
                <a href="${pageContext.request.contextPath}/servlet/clientes" class="btn btn-secondary">Cancelar</a>
            </div>
        </form>
    </div>
</div>

<jsp:include page="/WEB-INF/jsp/includes/footer.jsp" />
