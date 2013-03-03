<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags"%>

<!DOCTYPE html>
<html>
<head>
<title>Affectation parcours/filière 3ème année Centrale
	Marseille</title>
<link href="/css/bootstrap.css" rel="stylesheet">
<link href="/css/bootstrap-responsive.css" rel="stylesheet">
<script type="text/javascript"
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>
<script type="text/javascript"
	src="/js/responsible/repartition-other-choice.js"></script>
<script type="text/javascript" src="/js/jquery/jquery-latest.js"></script>
<script src="http://code.highcharts.com/highcharts.js"></script>
<script src="http://code.highcharts.com/modules/exporting.js"></script>
</head>
<body>
	<div class="container">

		<tags:headerResponsible
			title="${specialization.name} (${specialization.abbreviation})" />

		<div class="row">
			<div class="span2">
				<ul class="nav nav-list">

					<li class="nav-header">${specialization.abbreviation}</li>
					<c:forEach var="i" begin="1" end="5" step="1">
						<li><a href="/responsable/${i}">Choix ${i}</a></li>
					</c:forEach>

					<li class="nav-header">Statistiques</li>
					<li><a href="/responsable/run/statistics/choice1">Parcours/filière</a></li>
					<c:choose>
						<c:when test="${specialization.type == 'JobSector' }">
							<li class="active"><a
								href="/responsable/run/statistics/repartition-other-choice2">Répartition
									filières</a></li>
							<li><a
								href="/responsable/run/statistics/inverse-repartition">Répartition
									parcours</a></li>
						</c:when>
						<c:otherwise>
							<li class="active"><a
								href="/responsable/run/statistics/repartition-other-choice2">Répartition
									parcours</a></li>
							<li><a
								href="/responsable/run/statistics/inverse-repartition">Répartition
									filières</a></li>
						</c:otherwise>
					</c:choose>
				</ul>
			</div>

			<div class="span7">
				<ul class="nav nav-pills">
					<c:forEach var="i" begin="2" end="5" step="1">
						<c:choose>
							<c:when test="${i == choiceNumber}">
								<li class="active"><a
									href="/responsable/run/statistics/repartition-other-choice${i}">Choix
										${i}</a></li>
							</c:when>
							<c:otherwise>
								<li><a
									href="/responsable/run/statistics/repartition-other-choice${i}">Choix
										${i}</a></li>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</ul>

				<div id="container"
					style="min-width: 400px; height: 400px; margin: 0 auto"></div>

				<div id="results">
					<c:forEach var="spec" items="${specializations}" varStatus="status">
						<div>
							<input value="${spec.name};${spec.abbreviation}"
								style="display: none">
							<c:forEach var="student" items="${spec.students}">
								<input value="${student}" style="display: none">
							</c:forEach>
						</div>
					</c:forEach>
				</div>
				<input id="spec" value="${specialization.abbreviation}"
					style="display: none"> <input id="number"
					value="${choiceNumber}" style="display: none">

			</div>
			
			<div class="span3">
				<tags:rightColumnResponsible specialization="${specialization}"></tags:rightColumnResponsible>
			</div>
		</div>
	</div>
</body>
</html>