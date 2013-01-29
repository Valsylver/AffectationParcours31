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
		
		<tags:headerResponsible title="${specialization.name} (${specialization.abbreviation})" />
		
		<div class="row">
			<div class="span2">
				<ul class="nav nav-list">
					
					<li class="nav-header">${specialization.abbreviation}</li>
					<c:forEach var="i" begin="1" end="5" step="1">
						<c:choose>
							<c:when test="${i == order}">
								<li class="active">
									<a href="/responsable/${i}">Choix ${i}</a>
								</li>
							</c:when>
							<c:otherwise>
								<li>
									<a href="/responsable/${i}">Choix ${i}</a>
								</li>
							</c:otherwise>
						</c:choose>
					</c:forEach>
					
					<li class="nav-header">Statistiques</li>
					<li><a>Dates</a></li>
					<li><a>Autres parcours</a></li>
				</ul>
			</div>
			
			<div class="span7">
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
										<td>${student.details.fullName}</td>
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