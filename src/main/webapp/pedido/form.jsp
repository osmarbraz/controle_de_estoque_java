<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/includes/header.jsp">
    <jsp:param name="title" value="${pedido.pedidoId != null ? 'Editar Cabeçalho do Pedido' : 'Novo Pedido'}" />
</jsp:include>
<jsp:include page="/includes/navbar.jsp" />

<div class="container">
    <div class="page-header">
        <div>
            <h1 class="page-title">${pedido.pedidoId != null ? '✏️ Editar Cabeçalho do Pedido' : '➕ Abrir Novo Pedido'}</h1>
            <p class="page-subtitle">Selecione o cliente e a data do pedido</p>
        </div>
        <a href="${pageContext.request.contextPath}/servlet/pedidos" class="btn btn-outline">⬅️ Voltar para a lista</a>
    </div>

    <c:if test="${not empty erro}">
        <div class="alert alert-danger">${erro}</div>
    </c:if>

    <div class="card">
        <form acao="${pageContext.request.contextPath}/servlet/pedidos" method="post" onsubmit="return validatePedidoForm(this);">
            <input type="hidden" name="pedidoId" value="${pedido.pedidoId}" />

            <div class="form-grid">
                <div class="form-group" style="grid-column: span 2;">
                    <label for="clienteId">Cliente *</label>
                    <select id="clienteId" name="clienteId" required>
                        <option value="">-- Selecione o Cliente --</option>
                        <c:forEach var="cli" items="${clientes}">
                            <option value="${cli.clienteId}" ${pedido.clienteId == cli.clienteId ? 'selected' : ''}>
                                ${cli.nome} (CPF: ${cli.cpf}) - ${cli.cidadeNome}/${cli.cidadeUf}
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group">
                    <label for="data">Data do Pedido</label>
                    <input type="date" id="data" name="data" value="<fmt:formatDate value="${pedido.data}" pattern="yyyy-MM-dd" />" />
                </div>

                <div class="form-group">
                    <label for="situacao">Situação</label>
                    <select id="situacao" name="situacao">
                        <option value="aberto" ${pedido.situacao == 'aberto' ? 'selected' : ''}>Aberto</option>
                        <option value="fechado" ${pedido.situacao == 'fechado' ? 'selected' : ''}>Fechado</option>
                    </select>
                </div>
            </div>

            <div style="margin-top: 1.5rem; display: flex; gap: 1rem; align-items: center;">
                <button type="submit" class="btn btn-primary">
                    💾 Continuar para Inclusão de Itens ➡️
                </button>
                <a href="${pageContext.request.contextPath}/servlet/pedidos" class="btn btn-secondary">Cancelar</a>
            </div>
        </form>
    </div>
</div>

<jsp:include page="/includes/footer.jsp" />
