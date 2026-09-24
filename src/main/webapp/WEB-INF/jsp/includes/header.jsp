<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${param.title != null ? param.title : 'Controle de Estoque'}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <script>
        window.contextPath = "${pageContext.request.contextPath}";
    </script>
</head>
<body>
