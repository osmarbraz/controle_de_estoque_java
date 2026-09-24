<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/WEB-INF/jsp/includes/header.jsp">
    <jsp:param name="title" value="Cadastro de Clientes - Controle de Estoque" />
</jsp:include>
<jsp:include page="/WEB-INF/jsp/includes/navbar.jsp" />

<div class="container">
    <div class="page-header">
        <div>
            <h1 class="page-title">👤 Cadastro de Clientes</h1>
            <p class="page-subtitle">Gestão dos clientes da empresa</p>
        </div>
        <a href="${pageContext.request.contextPath}/servlet/clientes?action=new" class="btn btn-primary">➕ Novo Cliente</a>
    </div>

    <c:if test="${param.msg == 'sucesso_salvar'}">
        <div class="alert alert-success">Cliente salvo com sucesso!</div>
    </c:if>
    <c:if test="${param.msg == 'sucesso_excluir'}">
        <div class="alert alert-success">Cliente excluído com sucesso!</div>
    </c:if>
    <c:if test="${param.msg == 'erro_excluir_vinculo'}">
        <div class="alert alert-danger">Não é possível excluir este cliente pois existem pedidos vinculados a ele.</div>
    </c:if>

    <div class="card" style="padding: 0;">
        <div class="table-responsive">
            <table class="table">
                <thead>
                    <tr>
                        <th style="width: 60px;">ID</th>
                        <th>Nome</th>
                        <th>CPF</th>
                        <th>Endereço</th>
                        <th>Bairro</th>
                        <th>Cidade / UF</th>
                        <th>Data Cadastro</th>
                        <th style="width: 160px;" class="text-center">Ações</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${empty clientes}">
                            <tr>
                                <td colspan="8" class="text-center" style="padding: 2rem; color: var(--text-muted);">
                                    Nenhum cliente cadastrado.
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="cl" items="${clientes}">
                                <tr>
                                    <td>${cl.clienteId}</td>
                                    <td><strong>${cl.nome}</strong></td>
                                    <td>${cl.cpf}</td>
                                    <td>${cl.rua}, ${cl.numero}</td>
                                    <td>${cl.bairro}</td>
                                    <td>${cl.cidadeNome} / ${cl.cidadeUf}</td>
                                    <td><fmt:formatDate value="${cl.dataCadastro}" pattern="dd/MM/yyyy HH:mm" /></td>
                                    <td class="text-center">
                                        <a href="${pageContext.request.contextPath}/servlet/clientes?action=edit&id=${cl.clienteId}" class="btn btn-sm btn-outline">✏️ Editar</a>
                                        <a href="${pageContext.request.contextPath}/servlet/clientes?action=delete&id=${cl.clienteId}" class="btn btn-sm btn-danger" onclick="return confirm('Deseja realmente excluir este cliente?');">🗑️ Excluir</a>
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
