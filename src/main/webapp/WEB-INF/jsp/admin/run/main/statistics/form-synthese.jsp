<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags"%>

<!DOCTYPE html>
<html>
<head>
<title>Affectation parcours/filière 3ème année Centrale Marseille</title>
<link href="/css/bootstrap.css" rel="stylesheet">
<link href="/css/bootstrap-responsive.css" rel="stylesheet">
<link href="/css/student-admin-page.css" rel="stylesheet">
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>
<script type="text/javascript" src="/js/admin/run/main/statistics/form/pie-chart.js"></script>
<script type="text/javascript" src="/js/jquery/jquery-latest.js"></script>
<script src="http://code.highcharts.com/highcharts.js"></script>
<script src="http://code.highcharts.com/modules/exporting.js"></script>
</head>
<body>
	<div class="container">
		<tags:headerAdmin run="<%=true%>" />

		<div class="row">
			<div class="span2">
				<ul class="nav nav-list">
					<li class="nav-header">Statistiques</li>
					<li><a href="/admin/run/main/statistics/choice1">Choix</a></li>
					<li class="active"><a href="/admin/run/main/statistics/form/synthese">Dossiers</a></li>
					<li class="dropdown-submenu"><a href="#">Répartition parcours</a>
					<ul class="dropdown-menu">
						<c:forEach var="js" items="${allJs}">
							<li><a href="/admin/run/main/statistics/inverse-repartition/js/${js.abbreviation}">${js.abbreviation}</a></li>
						</c:forEach>
					</ul>
					</li>
					<li class="dropdown-submenu"><a href="#">Répartition filières</a>
					<ul class="dropdown-menu">
						<c:forEach var="ic" items="${allIc}">
							<li><a href="/admin/run/main/statistics/inverse-repartition/ic/${ic.abbreviation}">${ic.abbreviation}</a></li>
						</c:forEach>
					</ul>
					</li>

					<li class="nav-header">Parcours</li>
					<li><a href="/admin/run/main/choices/improvement-course/synthese/choice1">Synthèse</a></li>
					<c:forEach var="spec" items="${allIc}">
						<li><a href="/admin/run/main/choices/improvement-course/details/${spec.abbreviation}/choice1">${spec.abbreviation}</a></li>
					</c:forEach>

					<li class="nav-header">Filières</li>
					<li><a href="/admin/run/main/choices/job-sector/synthese/choice1">Synthèse</a></li>
					<c:forEach var="spec" items="${allJs}" varStatus="status">
						<li><a href="/admin/run/main/choices/job-sector/details/${spec.abbreviation}/choice1">${spec.abbreviation}</a></li>
					</c:forEach>
				</ul>
			</div>

			<div class="span7">
				<ul class="nav nav-pills">
					<li class="active"><a href="/admin/run/main/statistics/form/synthese">Statistiques</a></li>
					<li><a href="/admin/run/main/statistics/form/details/all">Dossiers complets (${nbreAll})</a></li>
					<li><a href="/admin/run/main/statistics/form/details/partial">Dossiers incomplets (${nbrePartial})</a></li>
					<li><a href="/admin/run/main/statistics/form/details/no">Dossiers vides (${nbreNo})</a></li>
				</ul>

				<div id="container" style="min-width: 400px; height: 400px; margin: 0 auto"></div>

			</div>

			<input id="noSubmission" value="${nbreNo}" style="display: none"> <input id="partialSubmission" value="${nbrePartial}" style="display: none"> <input id="totalSubmission" value="${nbreAll}"
				style="display: none">

			<div class="span3">
				<tags:rightColumnAdmin />
			</div>
		</div>
	</div>
</body>
</html>
