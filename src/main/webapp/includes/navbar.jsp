<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<nav class="navbar">
    <a href="${pageContext.request.contextPath}/" class="navbar-brand">
        📦 Estoque<span>DB</span>
    </a>
    <ul class="navbar-menu">
        <!-- Menu 1: Cadastros (Separado do controle de estoque) -->
        <li class="nav-item">
            <a href="#" class="nav-link">
                ⚙️ Cadastros ▾
            </a>
            <div class="nav-dropdown">
                <a href="${pageContext.request.contextPath}/servlet/clientes" class="dropdown-link">👤 Clientes (CRUD)</a>
                <a href="${pageContext.request.contextPath}/servlet/cidades" class="dropdown-link">🏙️ Cidades (CRUD)</a>
                <div class="dropdown-divider"></div>
                <a href="${pageContext.request.contextPath}/servlet/produtos" class="dropdown-link">📦 Produtos (CRUD)</a>
                <a href="${pageContext.request.contextPath}/servlet/categorias" class="dropdown-link">🏷️ Categorias (CRUD)</a>
            </div>
        </li>

        <!-- Menu 2: Sistema de Controle de Estoque & Pedidos (SCEP) & Pedidos -->
        <li class="nav-item">
            <a href="#" class="nav-link">
                🛒 Cadastro de Pedidos ▾
            </a>
            <div class="nav-dropdown">
                <a href="${pageContext.request.contextPath}/servlet/pedidos" class="dropdown-link">📋 Cadastro de Pedidos</a>
                <a href="${pageContext.request.contextPath}/servlet/pedidos?acao=novo" class="dropdown-link">➕ Novo Pedido</a>
            </div>
        </li>

        <!-- Menu 3: Relatorios -->
        <li class="nav-item">
            <a href="#" class="nav-link">
                📊 Relatórios ▾
            </a>
            <div class="nav-dropdown">
                <a href="${pageContext.request.contextPath}/servlet/relatorios" class="dropdown-link">📁 Painel de Relatórios</a>
                <div class="dropdown-divider"></div>
                <a href="${pageContext.request.contextPath}/servlet/relatorios?tipo=cidades" class="dropdown-link">🏙️ Lista de Cidades</a>
                <a href="${pageContext.request.contextPath}/servlet/relatorios?tipo=categorias" class="dropdown-link">🏷️ Lista de Categorias</a>
                <a href="${pageContext.request.contextPath}/servlet/relatorios?tipo=clientes" class="dropdown-link">👤 Lista de Clientes</a>
                <a href="${pageContext.request.contextPath}/servlet/relatorios?tipo=produtos" class="dropdown-link">📦 Lista de Produtos</a>
                <a href="${pageContext.request.contextPath}/servlet/relatorios?tipo=pedidos" class="dropdown-link">📜 Pedidos e Itens Pedido</a>
            </div>
        </li>
    </ul>
</nav>
