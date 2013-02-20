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
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>
<script type="text/javascript" src="/js/admin/run/main/statistics/choice/pie-chart.js"></script>
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
					<li class="active"><a href="/admin/run/main/statistics/choice1">Choix</a></li>
					<li><a href="/admin/run/main/statistics/form/synthese">Dossiers</a></li>

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
					<c:forEach var="i" begin="1" end="5" step="1">
						<c:choose>
							<c:when test="${i == choiceNumber}">
								<li class="active"><a href="/admin/run/main/statistics/choice${i}">Choix ${i}</a></li>
							</c:when>
							<c:otherwise>
								<li><a href="/admin/run/main/statistics/choice${i}">Choix ${i}</a></li>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</ul>

				<div id="containerPieChartIc" style="min-width: 400px; height: 400px; margin: 0 auto"></div>
				<legend></legend>
				<div id="containerPieChartJs" style="min-width: 400px; height: 400px; margin: 0 auto"></div>
			</div>
			<div class="span3">
				<tags:rightColumnAdmin />
			</div>
			<div id="inputsIc">
				<c:forEach var="improvementCourse" items="${simpleImprovementCourses}" varStatus="status">
					<input id="ic${status.index}" value="${improvementCourse.abbreviation};${improvementCourse.number};${improvementCourse.name}" style="display: none">
				</c:forEach>
			</div>

			<div id="inputsJs">
				<c:forEach var="jobSector" items="${simpleJobSectors}" varStatus="status">
					<input id="js${status.index}" value="${jobSector.abbreviation};${jobSector.number};${jobSector.name}" style="display: none">
				</c:forEach>
			</div>

		</div>
	</div>

</body>
</html>
