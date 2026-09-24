<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/jsp/includes/header.jsp">
    <jsp:param name="title" value="Início - Controle de Estoque" />
</jsp:include>
<jsp:include page="/WEB-INF/jsp/includes/navbar.jsp" />

<div class="container">
    <div class="page-header">
        <div>
            <h1 class="page-title">📦 Sistema de Controle de Estoque & Pedidos</h1>
            <p class="page-subtitle">Gerencie cadastros, movimentação de estoque, emissão de pedidos e relatórios.</p>
        </div>
    </div>

    <!-- Cards Grid -->
    <div class="form-grid" style="grid-template-columns: repeat(auto-fit, minmax(240px, 1fr)); margin-bottom: 2rem;">
        <div class="card" style="text-align: center;">
            <div style="font-size: 2.5rem; margin-bottom: 0.5rem;">👤</div>
            <h3 style="color: var(--primary);">Cadastros Base</h3>
            <p style="color: var(--text-muted); font-size: 0.9rem; margin-bottom: 1rem;">Clientes, Cidades, Produtos e Categorias</p>
            <a href="${pageContext.request.contextPath}/servlet/clientes" class="btn btn-primary btn-sm">Acessar Cadastros</a>
        </div>

        <div class="card" style="text-align: center;">
            <div style="font-size: 2.5rem; margin-bottom: 0.5rem;">🛒</div>
            <h3 style="color: var(--primary);">Controle de Estoque</h3>
            <p style="color: var(--text-muted); font-size: 0.9rem; margin-bottom: 1rem;">Emissão de Pedidos e Itens de Pedido</p>
            <a href="${pageContext.request.contextPath}/servlet/pedidos" class="btn btn-primary btn-sm">Gerenciar Pedidos</a>
        </div>

        <div class="card" style="text-align: center;">
            <div style="font-size: 2.5rem; margin-bottom: 0.5rem;">📊</div>
            <h3 style="color: var(--primary);">Relatórios</h3>
            <p style="color: var(--text-muted); font-size: 0.9rem; margin-bottom: 1rem;">Visualização web e exportação para XLS</p>
            <a href="${pageContext.request.contextPath}/servlet/relatorios" class="btn btn-success btn-sm">Ver Relatórios</a>
        </div>
    </div>

    <div class="card">
        <h3 style="margin-bottom: 1rem; color: var(--primary);">Ações Rápidas</h3>
        <div style="display: flex; gap: 1rem; flex-wrap: wrap;">
            <a href="${pageContext.request.contextPath}/servlet/pedidos?action=new" class="btn btn-primary">➕ Novo Pedido de Venda</a>
            <a href="${pageContext.request.contextPath}/servlet/clientes?action=new" class="btn btn-outline">👤 Novo Cliente</a>
            <a href="${pageContext.request.contextPath}/servlet/produtos?action=new" class="btn btn-outline">📦 Novo Produto</a>
            <a href="${pageContext.request.contextPath}/servlet/cidades?action=new" class="btn btn-outline">🏙️ Nova Cidade</a>
            <a href="${pageContext.request.contextPath}/servlet/categorias?action=new" class="btn btn-outline">🏷️ Nova Categoria</a>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/jsp/includes/footer.jsp" />
