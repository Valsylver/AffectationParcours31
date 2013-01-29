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
		<tags:headerAdmin />
		
		<div class="row">
			<div class="span2">
				<ul class="nav nav-list">
					<li class="nav-header">Statistiques</li>
					<li><a href="#">Synthèse</a></li>
					<li><a href="#">Parcours</a></li>
					<li><a href="#">Filières</a></li>
					<li></li>
					<c:if test="${type == 2}">
						<li class="nav-header">Parcours</li>
						<li><a href="#">Synthèse</a></li>
						<c:forEach var="specialization" items="${allSpec}" varStatus="status">
							<li><a href="/admin/parcours/synthese/choix${status.index + 1}">${specialization.abbreviation}</a></li>
						</c:forEach>
						
						<li class="nav-header">Filières</li>
						<li><a href="#">Synthèse</a></li>
						<c:forEach var="specialization" items="${otherSpec}" varStatus="status">
							<li><a href="#">${specialization.abbreviation}</a></li>
						</c:forEach>
					</c:if>
					
					<c:if test="${type == 1}">
						<li class="nav-header">Parcours</li>
						<li><a href="#">Synthèse</a></li>
						<c:forEach var="specialization" items="${otherSpec}" varStatus="status">
							<li><a href="#">${specialization.abbreviation}</a></li>
						</c:forEach>
						
						<li class="nav-header">Filières</li>
						<li><a href="#">Synthèse</a></li>
						<c:forEach var="specialization" items="${allSpec}" varStatus="status">
							<li><a href="#">${specialization.abbreviation}</a></li>
						</c:forEach>
					</c:if>
				</ul>
			</div>
			<div class="span7">
				<legend>
					<c:if test="${type == 1}">
						Filières métier
					</c:if>
					<c:if test="${type == 2}">
						Parcours d'approfondissement
					</c:if>
				</legend>
			
				<c:forEach var="specialization" items="${allSpec}" varStatus="status">
					<span class="lead">
						<c:if test="${type == 2}">
							<a href="/admin/parcours/${specialization.abbreviation}" >
						</c:if>
						<c:if test="${type == 1}">
							<a href="/admin/filieres/${specialization.abbreviation}" >
						</c:if>
						${specialization.name} (${specialization.abbreviation})
						</a>
					</span>
					<br />
					<br />
					<c:choose>
						<c:when test="${fn:length(allStudents[status.index]) > 0}">  
							<table id="student" class="table table-bordered table-striped tablesorter">
								<thead>
									<tr>
										<th>Nom</th>
										<th>Classement</th>
										<th>Appreciation</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="student" items="${allStudents[status.index]}">
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
							<p>Aucun elève n'a pour l'instant choisi
								<c:if test="${type == 2}">
									ce parcours d'approfondissement
								</c:if>
								<c:if test="${type == 1}">
									cette filière métier
								</c:if>
							</p>	
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</div>
			<div class="span3">
				<div class="pull right">
					<a href="/logout">Deconnexion<i class="icon-off"></i></a>
				</div>
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
