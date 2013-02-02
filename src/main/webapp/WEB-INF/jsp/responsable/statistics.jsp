<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags"%>

<!DOCTYPE html>
<html>
<head>
<title>Affectation parcours/filière 3ème année Centrale Marseille</title>
<!-- Bootstrap -->
<link href="/css/bootstrap.css" rel="stylesheet">
<link href="/css/bootstrap-responsive.css" rel="stylesheet">
</head>
<body>
	<div class="container">

		<tags:headerResponsible title="${specialization.name} (${specialization.abbreviation})" />

		<div class="row">
			<div class="span2">
				<ul class="nav nav-list">

					<li class="nav-header">${specialization.abbreviation}</li>
					<c:forEach var="i" begin="1" end="5" step="1">
						<li><a href="/responsable/${i}">Choix ${i}</a></li>
					</c:forEach>

					<li class="nav-header">Synthese</li>
					<li class="active"><a href="/responsable/statistics">Statistiques</a></li>
				</ul>
			</div>

			<div class="span8">
				<legend>
					<c:choose>
						<c:when test="${type == 1}">
							Parcours d'approfondissement
						</c:when>
						<c:otherwise>
							Filières métier
						</c:otherwise>
					</c:choose>
				</legend>
				
				<h4>Répartion</h4>
				
				<table class="table table-bordered table-striped table-condensed">
					<thead>
						<tr>
							<th><c:choose>
									<c:when test="${type == 1}">
										Parcours
									</c:when>
									<c:otherwise>
										Filières
									</c:otherwise>
								</c:choose></th>
							<th>Nombre d'élèves</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="spec" items="${specForStats}">
							<tr>
								<td>${spec.name} (${spec.abbreviation})</td>
								<td>${spec.number}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<br />
				
				<h4>Représentations Graphiques</h4>
				<c:choose>
					<c:when test="${type == 1}">
						<img src="/img/jspchart/piechartPa.png">
						<br />
						<br />
						<img src="/img/jspchart/barchartPa.png">

					</c:when>
					<c:otherwise>
						<img src="/img/jspchart/piechartFm.png">
						<br />
						<br />
						<img src="/img/jspchart/barchartFm.png">
					</c:otherwise>
				</c:choose>
			</div>
		</div>
	</div>
</body>
</html>