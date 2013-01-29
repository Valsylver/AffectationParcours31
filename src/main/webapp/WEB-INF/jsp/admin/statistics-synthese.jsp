<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
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
						<li><a href="/admin/parcours/details/${spec.abbreviation}/choix1">${spec.abbreviation}</a></li>
					</c:forEach>
						
					<li class="nav-header">Filières</li>
					<li><a href="/admin/filieres/synthese/choix1">Synthèse</a></li>
					<c:forEach var="spec" items="${allJs}" varStatus="status">
						<li><a href="/admin/filieres/details/${spec.abbreviation}/choix1">${spec.abbreviation}</a></li>
					</c:forEach>
				</ul>
			</div>
			<div class="span7">
				<legend>
					Parcours d'approfondissement
				</legend>
				<img src="/img/jspchart/piechartPa.png">
				
				<br />
				
				<legend>
					Filières métier
				</legend>
				<img src="/img/jspchart/piechartFm.png">
			</div>
			<div class="span3">
				<tags:rightColumnAdmin />
			</div>
		</div>
	</div>
	
	<script type="text/javascript" src="/js/jquery/jquery-latest.js"></script> 

</body>
</html>
