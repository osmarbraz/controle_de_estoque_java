<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/includes/header.jsp">
    <jsp:param name="title" value="Relatórios - Sistema de Controle de Estoque & Pedidos (SCEP)" />
</jsp:include>
<jsp:include page="/includes/navbar.jsp" />

<div class="container">
    <div class="page-header">
        <div>
            <h1 class="page-title">📊 Central de Relatórios</h1>
            <p class="page-subtitle">Selecione um relatório para visualizar na web ou exportar diretamente para planilha Excel (.xls)</p>
        </div>
    </div>

    <div class="form-grid" style="grid-template-columns: repeat(auto-fit, minmax(320px, 1fr));">
        <!-- 1. Cidades -->
        <div class="card">
            <h3 style="color: var(--primary); margin-bottom: 0.5rem;">🏙️ Relatório de Cidades</h3>
            <p style="color: var(--text-muted); font-size: 0.9rem; margin-bottom: 1.25rem;">Lista completa de cidades cadastradas e suas respectivas UFs.</p>
            <div style="display: flex; gap: 0.5rem;">
                <a href="${pageContext.request.contextPath}/servlet/relatorios?tipo=cidades" class="btn btn-outline btn-sm">👁️ Visualizar Web</a>
                <a href="${pageContext.request.contextPath}/servlet/relatorios?tipo=cidades&exportar=xls" class="btn btn-xls btn-sm">📥 Baixar XLS</a>
            </div>
        </div>

        <!-- 2. Categorias -->
        <div class="card">
            <h3 style="color: var(--primary); margin-bottom: 0.5rem;">🏷️ Relatório de Categorias</h3>
            <p style="color: var(--text-muted); font-size: 0.9rem; margin-bottom: 1.25rem;">Lista de todas as categorias de produtos.</p>
            <div style="display: flex; gap: 0.5rem;">
                <a href="${pageContext.request.contextPath}/servlet/relatorios?tipo=categorias" class="btn btn-outline btn-sm">👁️ Visualizar Web</a>
                <a href="${pageContext.request.contextPath}/servlet/relatorios?tipo=categorias&exportar=xls" class="btn btn-xls btn-sm">📥 Baixar XLS</a>
            </div>
        </div>

        <!-- 3. Clientes -->
        <div class="card">
            <h3 style="color: var(--primary); margin-bottom: 0.5rem;">👤 Relatório de Clientes</h3>
            <p style="color: var(--text-muted); font-size: 0.9rem; margin-bottom: 1.25rem;">Lista de clientes com CPF, endereço, cidade/UF e data de cadastro.</p>
            <div style="display: flex; gap: 0.5rem;">
                <a href="${pageContext.request.contextPath}/servlet/relatorios?tipo=clientes" class="btn btn-outline btn-sm">👁️ Visualizar Web</a>
                <a href="${pageContext.request.contextPath}/servlet/relatorios?tipo=clientes&exportar=xls" class="btn btn-xls btn-sm">📥 Baixar XLS</a>
            </div>
        </div>

        <!-- 4. Produtos -->
        <div class="card">
            <h3 style="color: var(--primary); margin-bottom: 0.5rem;">📦 Relatório de Produtos</h3>
            <p style="color: var(--text-muted); font-size: 0.9rem; margin-bottom: 1.25rem;">Lista de produtos com unidade, custos, preços de venda e saldos de estoque.</p>
            <div style="display: flex; gap: 0.5rem;">
                <a href="${pageContext.request.contextPath}/servlet/relatorios?tipo=produtos" class="btn btn-outline btn-sm">👁️ Visualizar Web</a>
                <a href="${pageContext.request.contextPath}/servlet/relatorios?tipo=produtos&exportar=xls" class="btn btn-xls btn-sm">📥 Baixar XLS</a>
            </div>
        </div>

        <!-- 5. Pedidos e Itens -->
        <div class="card" style="grid-column: span 1 / -1;">
            <h3 style="color: var(--primary); margin-bottom: 0.5rem;">📜 Relatório Geral de Pedidos e Itens</h3>
            <p style="color: var(--text-muted); font-size: 0.9rem; margin-bottom: 1.25rem;">Relatório analítico completo listando todos os pedidos de venda juntamente com o detalhamento dos itens de produto inseridos em cada pedido.</p>
            <div style="display: flex; gap: 0.5rem;">
                <a href="${pageContext.request.contextPath}/servlet/relatorios?tipo=pedidos" class="btn btn-primary btn-sm">👁️ Visualizar Relatório Analítico Web</a>
                <a href="${pageContext.request.contextPath}/servlet/relatorios?tipo=pedidos&exportar=xls" class="btn btn-xls btn-sm">📥 Baixar Planilha Completa XLS</a>
            </div>
        </div>

        <!-- 6. Pedidos Abertos -->
        <div class="card">
            <h3 style="color: var(--primary); margin-bottom: 0.5rem;">📂 Relatório de Pedidos Abertos</h3>
            <p style="color: var(--text-muted); font-size: 0.9rem; margin-bottom: 1.25rem;">Relatório detalhado listando exclusivamente pedidos em aberto e seus respectivos itens.</p>
            <div style="display: flex; gap: 0.5rem;">
                <a href="${pageContext.request.contextPath}/servlet/relatorios?tipo=pedidos_abertos" class="btn btn-outline btn-sm">👁️ Visualizar Web</a>
                <a href="${pageContext.request.contextPath}/servlet/relatorios?tipo=pedidos_abertos&exportar=xls" class="btn btn-xls btn-sm">📥 Baixar XLS</a>
            </div>
        </div>

        <!-- 7. Pedidos Fechados -->
        <div class="card">
            <h3 style="color: var(--primary); margin-bottom: 0.5rem;">🔒 Relatório de Pedidos Fechados</h3>
            <p style="color: var(--text-muted); font-size: 0.9rem; margin-bottom: 1.25rem;">Relatório detalhado listando exclusivamente pedidos concluídos/fechados e seus itens.</p>
            <div style="display: flex; gap: 0.5rem;">
                <a href="${pageContext.request.contextPath}/servlet/relatorios?tipo=pedidos_fechados" class="btn btn-outline btn-sm">👁️ Visualizar Web</a>
                <a href="${pageContext.request.contextPath}/servlet/relatorios?tipo=pedidos_fechados&exportar=xls" class="btn btn-xls btn-sm">📥 Baixar XLS</a>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/includes/footer.jsp" />
