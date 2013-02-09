<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags"%>

<!DOCTYPE html>
<html>
<head>
<title>Affectation parcours/filière 3ème année Centrale
	Marseille</title>
<link href="/css/bootstrap.min.css" rel="stylesheet">
<link href="/css/bootstrap-responsive.css" rel="stylesheet">
<script src="/js/jquery/jquery-1.8.3.js"></script>
<script src="/js/jquery/jquery-ui-1.9.2.custom.min.js"></script>

</head>
<body>
	<div class="container">
		<tags:headerAdmin run="<%=true%>" />

		<div class="row">
			<div class="span2">
				<ul class="nav nav-list">
					<li><a href="/admin/run/settings/students">Elèves</a></li>
					<li><a href="/admin/run/settings/export">Export</a></li>
					<li><a href="/admin/run/settings/mail1">Mail 1</a></li>
					<li><a href="/admin/run/settings/mail2">Mail 2</a></li>
					<li><a href="/admin/run/settings/spontaneous-mail">Mail Spontané</a></li>
					<li><a href="/admin/run/settings/process">Processus</a></li>
					<li class="active"><a
						href="/admin/run/settings/specializations">Spécialisations</a></li>
				</ul>
			</div>

			<div class="span5">
				<h2>Parcours</h2>
				<c:forEach var="pa" items="${paAvailable}" varStatus="status">
					<c:choose>
					<c:when test="${status.index != 0}">
						<br />
					</c:when>
					</c:choose>
					${pa.stringForForm}
					<br />
					<a href="/admin/common/edit/ic/${pa.abbreviation}"
						class="btn btn-primary btn-small pull-right">Modfier</a>
					<br />
				</c:forEach>
			
			</div>
	
			<div class="span5">
				<h2>Filières</h2>
				<c:forEach var="fm" items="${fmAvailable}" varStatus="status">
					<c:choose>
					<c:when test="${status.index != 0}">
						<br />
					</c:when>
					</c:choose>
					${fm.stringForForm}
					<br />
					<a href="/admin/common/edit/js/${fm.abbreviation}"
						class="btn btn-primary btn-small pull-right">Modfier</a>
					<br />
				</c:forEach>
			</div>
		</div>
	</div>

</body>
</html>
