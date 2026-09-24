<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/WEB-INF/jsp/includes/header.jsp">
    <jsp:param name="title" value="Detalhes do Pedido #${pedido.pedidoId}" />
</jsp:include>
<jsp:include page="/WEB-INF/jsp/includes/navbar.jsp" />

<div class="container">
    <div class="page-header">
        <div>
            <h1 class="page-title">📜 Pedido #${pedido.pedidoId}</h1>
            <p class="page-subtitle">Detalhes e itens do pedido</p>
        </div>
        <div style="display: flex; gap: 0.5rem;">
            <a href="${pageContext.request.contextPath}/servlet/item-pedido?pedidoId=${pedido.pedidoId}" class="btn btn-primary">🛒 Gerenciar Itens</a>
            <a href="${pageContext.request.contextPath}/servlet/pedidos" class="btn btn-outline">⬅️ Voltar</a>
        </div>
    </div>

    <div class="card" style="margin-bottom: 1.5rem;">
        <div class="form-grid" style="grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));">
            <div>
                <span style="font-size: 0.85rem; color: var(--text-muted);">Número do Pedido</span>
                <p style="font-size: 1.2rem; font-weight: 700; color: var(--primary);">#${pedido.pedidoId}</p>
            </div>
            <div>
                <span style="font-size: 0.85rem; color: var(--text-muted);">Cliente</span>
                <p style="font-size: 1.1rem; font-weight: 600;">${pedido.clienteNome}</p>
            </div>
            <div>
                <span style="font-size: 0.85rem; color: var(--text-muted);">Data da Emissão</span>
                <p style="font-size: 1rem;"><fmt:formatDate value="${pedido.data}" pattern="dd/MM/yyyy HH:mm" /></p>
            </div>
            <div>
                <span style="font-size: 0.85rem; color: var(--text-muted);">Situação</span>
                <p style="margin-top: 0.2rem;">
                    <span class="badge ${pedido.situacao == 'aberto' ? 'badge-open' : 'badge-closed'}">
                        ${pedido.situacao}
                    </span>
                </p>
            </div>
            <div>
                <span style="font-size: 0.85rem; color: var(--text-muted);">Valor Total</span>
                <p style="font-size: 1.3rem; font-weight: 700; color: var(--accent);">
                    <fmt:formatNumber value="${pedido.total}" type="currency" currencySymbol="R$" />
                </p>
            </div>
        </div>
    </div>

    <div class="card" style="padding: 0;">
        <div style="padding: 1rem 1.5rem; border-bottom: 1px solid var(--border-color);">
            <h3 style="font-size: 1.1rem; color: var(--primary);">Itens do Pedido</h3>
        </div>
        <div class="table-responsive">
            <table class="table">
                <thead>
                    <tr>
                        <th>Produto</th>
                        <th>Unidade</th>
                        <th class="text-right">Qtd</th>
                        <th class="text-right">Preço Un.</th>
                        <th class="text-right">Subtotal</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${empty pedido.itens}">
                            <tr>
                                <td colspan="5" class="text-center" style="padding: 2rem; color: var(--text-muted);">
                                    Nenhum item adicionado a este pedido.
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="item" items="${pedido.itens}">
                                <tr>
                                    <td><strong>${item.produtoDescricao}</strong></td>
                                    <td>${item.unidade}</td>
                                    <td class="text-right">${item.quantidade}</td>
                                    <td class="text-right"><fmt:formatNumber value="${item.precoVenda}" type="currency" currencySymbol="R$" /></td>
                                    <td class="text-right" style="font-weight: bold;"><fmt:formatNumber value="${item.subtotal}" type="currency" currencySymbol="R$" /></td>
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
