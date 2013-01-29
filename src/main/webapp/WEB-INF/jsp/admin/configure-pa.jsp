<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html>
<head>
    <title>Affectation parcours/filière 3ème année Centrale Marseille</title>
    <!-- Bootstrap -->
    <link href="/css/bootstrap.css" rel="stylesheet">
    <link href="/css/bootstrap-responsive.css" rel="stylesheet">
    <link href="/css/select2.css" rel="stylesheet">
</head>
<body>
	<div class="container">
		<c:choose>
			<c:when test="${state == 'run'}">
				<tags:headerAdmin run="<%=true%>" />
			</c:when>
			<c:otherwise>
				<tags:headerAdmin run="<%=false%>" />
			</c:otherwise>
		</c:choose>
		
		<div class="row">
			<div class="span3">
			</div>
			<div class="span6">
				<c:choose>
					<c:when test="${state == 'run'}">
						<h2>Parcours</h2>
					</c:when>
					<c:otherwise>
						<h2>Parcours <a href="/admin/config/modif/new/parcours" class="btn btn-info"><i class="icon-white icon-plus"></i></a></h2>
					</c:otherwise>
				</c:choose>
				<br />
				
				<c:forEach var="pa" items="${paAvailable}">
					${pa.stringForForm}
					<br />
					<a href="/admin/config/modif/parcours/${pa.abbreviation}" class="btn btn-primary btn-small pull-right">Modfier</a>
					<br />
					<br />
				</c:forEach>
			</div>
		</div>
	</div>
	
	<script type="text/javascript" src="/js/jquery/jquery-latest.js"></script> 
	<script type="text/javascript" src="/js/jquery/jquery.tablesorter.min.js"></script> 
	<script src="/js/select2.min.js" type="text/javascript"></script>
</body>
</html>
