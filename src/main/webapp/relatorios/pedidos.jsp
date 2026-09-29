<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/includes/header.jsp">
    <jsp:param name="title" value="Relatório de Pedidos e Itens" />
</jsp:include>
<jsp:include page="/includes/navbar.jsp" />

<div class="container">
    <div class="page-header">
        <div>
            <h1 class="page-title">📜 Relatório de Pedidos e Itens Pedido</h1>
            <p class="page-subtitle">Listagem completa dos pedidos emitidos e o detalhamento de seus itens</p>
        </div>
        <div>
            <a href="${pageContext.request.contextPath}/relatorios?tipo=pedidos&exportar=xls" class="btn btn-xls">📥 Baixar em XLS</a>
            <a href="${pageContext.request.contextPath}/relatorios" class="btn btn-outline" style="margin-left: 0.5rem;">⬅️ Voltar</a>
        </div>
    </div>

    <c:choose>
        <c:when test="${empty pedidos}">
            <div class="card text-center" style="padding: 3rem; color: var(--text-muted);">
                Nenhum pedido registrado no sistema.
            </div>
        </c:when>
        <c:otherwise>
            <c:forEach var="ped" items="${pedidos}">
                <div class="card" style="margin-bottom: 2rem;">
                    <!-- Cabecalho do Pedido -->
                    <div style="display: flex; justify-content: space-between; align-items: center; padding-bottom: 1rem; border-bottom: 2px solid var(--border-color); margin-bottom: 1rem; flex-wrap: wrap; gap: 1rem;">
                        <div>
                            <span style="font-size: 1.25rem; font-weight: 700; color: var(--primary);">
                                Pedido #${ped.pedidoId}
                            </span>
                            <span style="margin-left: 1rem; color: var(--text-muted);">
                                Cliente: <strong>${ped.clienteNome}</strong>
                            </span>
                        </div>
                        <div style="display: flex; gap: 1rem; align-items: center;">
                            <span style="font-size: 0.9rem; color: var(--text-muted);">
                                📅 <fmt:formatDate value="${ped.data}" pattern="dd/MM/yyyy HH:mm" />
                            </span>
                            <span class="badge ${ped.situacao == 'aberto' ? 'badge-open' : 'badge-closed'}">
                                ${ped.situacao}
                            </span>
                            <span style="font-size: 1.15rem; font-weight: 700; color: var(--accent);">
                                Total: <fmt:formatNumber value="${ped.total}" type="currency" currencySymbol="R$" />
                            </span>
                        </div>
                    </div>

                    <!-- Tabela dos Itens do Pedido -->
                    <div class="table-responsive">
                        <table class="table">
                            <thead>
                                <tr style="background-color: #f8fafc;">
                                    <th>Produto (Descrição)</th>
                                    <th style="width: 90px;">Unidade</th>
                                    <th class="text-right" style="width: 100px;">Qtd</th>
                                    <th class="text-right">Preço Venda</th>
                                    <th class="text-right">Subtotal</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:choose>
                                    <c:when test="${empty ped.itens}">
                                        <tr>
                                            <td colspan="5" class="text-center" style="color: var(--text-muted);">
                                                Nenhum item adicionado a este pedido.
                                            </td>
                                        </tr>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach var="item" items="${ped.itens}">
                                            <tr>
                                                <td><strong>${item.produtoDescricao}</strong></td>
                                                <td>${item.unidade}</td>
                                                <td class="text-right">${item.quantidade}</td>
                                                <td class="text-right"><fmt:formatNumber value="${item.precoVenda}" type="currency" currencySymbol="R$" /></td>
                                                <td class="text-right" style="font-weight: 600;">
                                                    <fmt:formatNumber value="${item.subtotal}" type="currency" currencySymbol="R$" />
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                            </tbody>
                        </table>
                    </div>
                </div>
            </c:forEach>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="/includes/footer.jsp" />
