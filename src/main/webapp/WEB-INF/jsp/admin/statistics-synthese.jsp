<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags"%>

<!DOCTYPE html>
<html>
<head>
<title>Affectation parcours/filière 3ème année Centrale
	Marseille</title>
<!-- Bootstrap -->
<link href="/css/bootstrap.css" rel="stylesheet">
<link href="/css/bootstrap-responsive.css" rel="stylesheet">
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>
		<script type="text/javascript" src="/js/high-chart.js">
		</script>
</head>
<body>
<script src="http://code.highcharts.com/highcharts.js"></script>
<script src="http://code.highcharts.com/modules/exporting.js"></script>
	<div class="container">
		<tags:headerAdmin run="<%=true%>" />


		<div class="row">
			<div class="span2">
				<ul class="nav nav-list">
					<li class="nav-header">Statistiques</li>
					<li class="active"><a href="/admin/statistics/synthese">Synthèse</a></li>
					<li><a href="/admin/parcours/statistics">Parcours</a></li>
					<li><a href="/admin/filieres/statistics">Filières</a></li>
					<li><a href="/admin/statistics/eleves/all">Elèves</a></li>

					<li class="nav-header">Parcours</li>
					<li><a href="/admin/parcours/synthese/choix1">Synthèse</a></li>
					<c:forEach var="spec" items="${allIc}">
						<li><a
							href="/admin/parcours/details/${spec.abbreviation}/choix1">${spec.abbreviation}</a></li>
					</c:forEach>

					<li class="nav-header">Filières</li>
					<li><a href="/admin/filieres/synthese/choix1">Synthèse</a></li>
					<c:forEach var="spec" items="${allJs}" varStatus="status">
						<li><a
							href="/admin/filieres/details/${spec.abbreviation}/choix1">${spec.abbreviation}</a></li>
					</c:forEach>
				</ul>
			</div>
			<div class="span7">
				<div id="container" style="min-width: 400px; height: 400px; margin: 0 auto"></div>
				<legend> Filières métier </legend>
				<img src="/img/jspchart/piechartFm.png"> <br /> <img
					src="/img/jspchart/barchartFm.png">
			</div>
			<div class="span2">
				<tags:rightColumnAdmin />
				<div id="hiddenBloc">haha</div>
			</div>
			<div id="inputs">
				<c:forEach var="parcours" items="${theList}" varStatus="status">
					<input id="input${status.index}" value="${parcours.abbreviation};${parcours.number};${parcours.name}"
						style="display: none">
				</c:forEach>
			</div>

		</div>
	</div>

	<script type="text/javascript" src="/js/jquery/jquery-latest.js"></script>
	<script src="/js/high-chart.js" type="text/javascript"></script>

</body>
</html>
