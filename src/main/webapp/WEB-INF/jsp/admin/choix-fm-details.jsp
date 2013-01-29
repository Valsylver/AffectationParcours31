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
		<tags:headerAdmin run="<%=true %>" />
		
		<div class="row">
			<div class="span2">
				<ul class="nav nav-list">
					<tags:leftColumnAdmin />

					<li class="nav-header">Parcours</li>
					<li><a href="/admin/parcours/synthese/choix1">Synthèse</a></li>
					<c:forEach var="spec" items="${allIc}">
						<li><a href="/admin/parcours/details/${spec.abbreviation}/choix1">${spec.abbreviation}</a></li>
					</c:forEach>
						
					<li class="nav-header">Filières</li>
					<li><a href="/admin/filieres/synthese/choix1">Synthèse</a></li>
					<c:forEach var="spec" items="${allJs}" varStatus="status">
						<c:choose>
							<c:when test="${spec.abbreviation == abbreviation}">
								<li class="active"><a href="/admin/filieres/details/${abbreviation}/choix1">${spec.abbreviation}</a></li>
							</c:when>
							<c:otherwise>
								<li><a href="/admin/filieres/details/${spec.abbreviation}/choix1">${spec.abbreviation}</a></li>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</ul>
			</div>
			<div class="span7">
				<ul class="nav nav-pills">
					<c:forEach var="i" begin="1" end="5" step="1">
						<c:choose>
							<c:when test="${i == order}">
								<li class="active">
									<a href="/admin/filieres/details/${abbreviation}/choix${i}">Choix ${i}</a>
								</li>
							</c:when>
							<c:otherwise>
								<li>
									<a href="/admin/filieres/details/${abbreviation}/choix${i}">Choix ${i}</a>
								</li>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</ul>	
				<hr>

				<span class="lead">
					<a href="/admin/config/modif/filieres/${abbreviation}" >
						${specialization.name} (${specialization.abbreviation})
					</a>
				</span>
				<br />
				<br />
				<c:choose>
					<c:when test="${fn:length(allStudents) > 0}">  
						<table id="student" class="table table-bordered table-striped tablesorter">
							<thead>
								<tr>
									<th>Nom</th>
									<th>Classement</th>
									<th>Appreciation</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="student" items="${allStudents}">
									<tr>
										<td><a href="/admin/student/${student.details.login}">${student.details.fullName}</a></td>
										<td>${student.results.ranking}</td>
										<td>${student.results.appreciation}</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</c:when>
					<c:otherwise>
						<p>Aucun elève n'a pour l'instant choisi cette filière métier en 
							<c:choose>
								<c:when test="${order == 1}">
									1er choix
								</c:when>
								<c:otherwise>
									${order}ème choix.
								</c:otherwise>
							</c:choose>
						</p>	
					</c:otherwise>
				</c:choose>
			</div>
			<tags:rightColumnAdmin />
		</div>
	</div>
	
	<script type="text/javascript" src="/js/jquery/jquery-latest.js"></script> 
	<script type="text/javascript" src="/js/jquery/jquery.tablesorter.min.js"></script> 
	<script>
		$(document).ready(function() { 
			$("#student").tablesorter(); 
		} 
		); 
	</script>

</body>
</html>
