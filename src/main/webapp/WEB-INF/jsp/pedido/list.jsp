<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/WEB-INF/jsp/includes/header.jsp">
    <jsp:param name="title" value="Controle de Pedidos - Controle de Estoque" />
</jsp:include>
<jsp:include page="/WEB-INF/jsp/includes/navbar.jsp" />

<div class="container">
    <div class="page-header">
        <div>
            <h1 class="page-title">🛒 Controle de Pedidos de Venda</h1>
            <p class="page-subtitle">Emissão de novos pedidos e gerenciamento de itens</p>
        </div>
        <a href="${pageContext.request.contextPath}/servlet/pedidos?action=new" class="btn btn-primary">➕ Novo Pedido</a>
    </div>

    <c:if test="${param.msg == 'sucesso_salvar'}">
        <div class="alert alert-success">Pedido salvo com sucesso!</div>
    </c:if>
    <c:if test="${param.msg == 'sucesso_excluir'}">
        <div class="alert alert-success">Pedido excluído com sucesso!</div>
    </c:if>
    <c:if test="${param.msg == 'situacao_atualizada'}">
        <div class="alert alert-success">Situação do pedido atualizada com sucesso!</div>
    </c:if>

    <div class="card" style="padding: 0;">
        <div class="table-responsive">
            <table class="table">
                <thead>
                    <tr>
                        <th style="width: 80px;">Nº Pedido</th>
                        <th>Cliente</th>
                        <th>Data</th>
                        <th class="text-right">Valor Total</th>
                        <th class="text-center">Situação</th>
                        <th style="width: 260px;" class="text-center">Ações</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${empty pedidos}">
                            <tr>
                                <td colspan="6" class="text-center" style="padding: 2rem; color: var(--text-muted);">
                                    Nenhum pedido registrado até o momento.
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="p" items="${pedidos}">
                                <tr>
                                    <td><strong>#${p.pedidoId}</strong></td>
                                    <td>${p.clienteNome}</td>
                                    <td><fmt:formatDate value="${p.data}" pattern="dd/MM/yyyy HH:mm" /></td>
                                    <td class="text-right" style="font-weight: bold; color: var(--accent);">
                                        <fmt:formatNumber value="${p.total}" type="currency" currencySymbol="R$" />
                                    </td>
                                    <td class="text-center">
                                        <span class="badge ${p.situacao == 'aberto' ? 'badge-open' : 'badge-closed'}">
                                            ${p.situacao}
                                        </span>
                                    </td>
                                    <td class="text-center">
                                        <a href="${pageContext.request.contextPath}/servlet/item-pedido?pedidoId=${p.pedidoId}" class="btn btn-sm btn-primary" title="Gerenciar Itens do Pedido">🛒 Itens</a>
                                        <a href="${pageContext.request.contextPath}/servlet/pedidos?action=view&id=${p.pedidoId}" class="btn btn-sm btn-outline" title="Ver Detalhes">👁️ Detalhes</a>
                                        <c:if test="${p.situacao == 'aberto'}">
                                            <a href="${pageContext.request.contextPath}/servlet/pedidos?action=status&id=${p.pedidoId}&situacao=fechado" class="btn btn-sm btn-success" title="Fechar Pedido" onclick="return confirm('Deseja fechar este pedido?');">🔒 Fechar</a>
                                        </c:if>
                                        <c:if test="${p.situacao == 'fechado'}">
                                            <a href="${pageContext.request.contextPath}/servlet/pedidos?action=status&id=${p.pedidoId}&situacao=aberto" class="btn btn-sm btn-outline" title="Reabrir Pedido">🔓 Reabrir</a>
                                        </c:if>
                                        <a href="${pageContext.request.contextPath}/servlet/pedidos?action=delete&id=${p.pedidoId}" class="btn btn-sm btn-danger" onclick="return confirm('Deseja realmente excluir este pedido?');">🗑️</a>
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
