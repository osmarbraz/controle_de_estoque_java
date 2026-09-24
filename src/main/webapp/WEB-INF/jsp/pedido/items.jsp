<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/WEB-INF/jsp/includes/header.jsp">
    <jsp:param name="title" value="Itens do Pedido #${pedido.pedidoId}" />
</jsp:include>
<jsp:include page="/WEB-INF/jsp/includes/navbar.jsp" />

<div class="container">
    <div class="page-header">
        <div>
            <h1 class="page-title">🛒 Itens do Pedido #${pedido.pedidoId}</h1>
            <p class="page-subtitle">Cliente: <strong>${pedido.clienteNome}</strong> | Data: <fmt:formatDate value="${pedido.data}" pattern="dd/MM/yyyy HH:mm" /></p>
        </div>
        <div style="display: flex; gap: 0.5rem; align-items: center;">
            <a href="${pageContext.request.contextPath}/servlet/pedidos?action=view&id=${pedido.pedidoId}" class="btn btn-outline">👁️ Ver Pedido</a>
            <a href="${pageContext.request.contextPath}/servlet/pedidos" class="btn btn-secondary">📋 Voltar à Lista</a>
        </div>
    </div>

    <c:if test="${param.msg == 'pedido_criado'}">
        <div class="alert alert-success">Pedido criado com sucesso! Adicione os itens desejados abaixo.</div>
    </c:if>
    <c:if test="${param.msg == 'item_adicionado'}">
        <div class="alert alert-success">Item adicionado / atualizado no pedido com sucesso!</div>
    </c:if>
    <c:if test="${param.msg == 'item_removido'}">
        <div class="alert alert-success">Item removido do pedido com sucesso!</div>
    </c:if>
    <c:if test="${param.msg == 'erro_pedido_fechado'}">
        <div class="alert alert-danger">Este pedido está fechado e não permite alteração de itens.</div>
    </c:if>
    <c:if test="${param.msg == 'erro_qtd_invalida'}">
        <div class="alert alert-danger">A quantidade informada deve ser maior que zero.</div>
    </c:if>

    <!-- Resumo do Pedido -->
    <div class="summary-box">
        <div>
            <span style="font-size: 0.9rem; color: var(--text-muted);">Status do Pedido:</span>
            <span class="badge ${pedido.situacao == 'aberto' ? 'badge-open' : 'badge-closed'}" style="margin-left: 0.5rem;">
                ${pedido.situacao}
            </span>
        </div>
        <div>
            <span style="font-size: 1.1rem; font-weight: 600;">Total do Pedido:</span>
            <span style="font-size: 1.4rem; font-weight: 700; color: var(--accent); margin-left: 0.5rem;">
                <fmt:formatNumber value="${pedido.total}" type="currency" currencySymbol="R$" />
            </span>
        </div>
    </div>

    <!-- Form Inclusao de Item com Pesquisa Dinamica por Descricao -->
    <c:if test="${pedido.situacao == 'aberto'}">
        <div class="card" style="margin-bottom: 2rem;">
            <h3 style="margin-bottom: 1rem; color: var(--primary); font-size: 1.1rem;">
                🔍 Pesquisar & Adicionar Produto ao Pedido
            </h3>

            <form action="${pageContext.request.contextPath}/servlet/item-pedido" method="post" onsubmit="return validateItemPedidoForm(this);">
                <input type="hidden" name="pedidoId" value="${pedido.pedidoId}" />

                <div class="form-grid">
                    <!-- Campo de Pesquisa por Descrição com Dropdown AJAX -->
                    <div class="form-group" style="position: relative; grid-column: span 2;">
                        <label for="produtoSearchInput">Pesquisar Produto pela Descrição 🔎</label>
                        <input type="text" id="produtoSearchInput" placeholder="Digite o nome ou descrição do produto..." autocomplete="off" />
                        <div id="produtoSearchResults" class="search-results-dropdown"></div>
                        <small style="color: var(--text-muted);">Digite para buscar dinamicamente ou selecione abaixo.</small>
                    </div>

                    <!-- Select de Produtos (Populado automaticamente ao clicar na pesquisa ou escolher na lista) -->
                    <div class="form-group" style="grid-column: span 2;">
                        <label for="produtoId">Produto Selecionado *</label>
                        <select id="produtoId" name="produtoId" required>
                            <option value="">-- Selecione o Produto --</option>
                            <c:forEach var="prod" items="${produtos}">
                                <option value="${prod.produtoId}" data-preco="${prod.precoVenda}" data-estoque="${prod.quantidade}">
                                    ${prod.descricao} (${prod.unidade}) - R$ <fmt:formatNumber value="${prod.precoVenda}" pattern="#,##0.00" /> [Estoque: ${prod.quantidade}]
                                </option>
                            </c:forEach>
                        </select>
                        <small id="estoqueDisponivelInfo" style="color: var(--success); font-weight: 600;"></small>
                    </div>

                    <div class="form-group">
                        <label for="precoVenda">Preço Unitário (R$) *</label>
                        <input type="text" id="precoVenda" name="precoVenda" placeholder="0.00" required autocomplete="off" />
                    </div>

                    <div class="form-group">
                        <label for="quantidade">Quantidade *</label>
                        <input type="text" id="quantidade" name="quantidade" value="1" placeholder="1" required autocomplete="off" />
                    </div>
                </div>

                <div style="margin-top: 1.25rem;">
                    <button type="submit" class="btn btn-primary">➕ Adicionar Item ao Pedido</button>
                </div>
            </form>
        </div>
    </c:if>

    <!-- Tabela de Itens ja Incluidos -->
    <div class="card" style="padding: 0;">
        <div style="padding: 1rem 1.5rem; border-bottom: 1px solid var(--border-color); display: flex; justify-content: space-between; align-items: center;">
            <h3 style="font-size: 1.1rem; color: var(--primary);">Itens Incluídos no Pedido</h3>
            <span style="font-size: 0.9rem; color: var(--text-muted);">${empty pedido.itens ? 0 : pedido.itens.size()} item(ns)</span>
        </div>

        <div class="table-responsive">
            <table class="table">
                <thead>
                    <tr>
                        <th>Produto</th>
                        <th style="width: 80px;">Unidade</th>
                        <th class="text-right" style="width: 100px;">Qtd</th>
                        <th class="text-right">Preço Un.</th>
                        <th class="text-right">Subtotal</th>
                        <c:if test="${pedido.situacao == 'aberto'}">
                            <th style="width: 100px;" class="text-center">Ações</th>
                        </c:if>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${empty pedido.itens}">
                            <tr>
                                <td colspan="${pedido.situacao == 'aberto' ? 6 : 5}" class="text-center" style="padding: 2rem; color: var(--text-muted);">
                                    Nenhum item incluído neste pedido ainda. Utilize o formulário acima para adicionar produtos.
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
                                    <td class="text-right" style="font-weight: bold; color: var(--primary);">
                                        <fmt:formatNumber value="${item.subtotal}" type="currency" currencySymbol="R$" />
                                    </td>
                                    <c:if test="${pedido.situacao == 'aberto'}">
                                        <td class="text-center">
                                            <a href="${pageContext.request.contextPath}/servlet/item-pedido?action=delete&pedidoId=${pedido.pedidoId}&produtoId=${item.produtoId}" class="btn btn-sm btn-danger" onclick="return confirm('Deseja remover este item do pedido?');">🗑️ Remover</a>
                                        </td>
                                    </c:if>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>
    </div>

    <div style="margin-top: 1.5rem; display: flex; gap: 1rem; justify-content: flex-end;">
        <c:if test="${pedido.situacao == 'aberto'}">
            <a href="${pageContext.request.contextPath}/servlet/pedidos?action=status&id=${pedido.pedidoId}&situacao=fechado" class="btn btn-success" onclick="return confirm('Deseja realmente fechar este pedido?');">
                🔒 Fechar Pedido
            </a>
        </c:if>
        <a href="${pageContext.request.contextPath}/servlet/pedidos" class="btn btn-outline">
            📋 Voltar aos Pedidos
        </a>
    </div>
</div>

<jsp:include page="/WEB-INF/jsp/includes/footer.jsp" />
