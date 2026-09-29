<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/includes/header.jsp">
    <jsp:param name="title" value="Relatório de Clientes" />
</jsp:include>
<jsp:include page="/includes/navbar.jsp" />

<div class="container">
    <div class="page-header">
        <div>
            <h1 class="page-title">👤 Relatório de Clientes</h1>
            <p class="page-subtitle">Listagem oficial de clientes cadastrados</p>
        </div>
        <div>
            <a href="${pageContext.request.contextPath}/relatorios?tipo=clientes&exportar=xls" class="btn btn-xls">📥 Baixar em XLS</a>
            <a href="${pageContext.request.contextPath}/relatorios" class="btn btn-outline" style="margin-left: 0.5rem;">⬅️ Voltar</a>
        </div>
    </div>

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
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${empty clientes}">
                            <tr>
                                <td colspan="7" class="text-center" style="padding: 2rem; color: var(--text-muted);">
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
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>
    </div>
</div>

<jsp:include page="/includes/footer.jsp" />
