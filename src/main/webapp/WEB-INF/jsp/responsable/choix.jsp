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
					<li><a href="/responsable/run/statistics/choice1">Parcours/filière</a></li>
					<li><a href="/responsable/run/statistics/repartition-other-choice2">Répartition autres choix</a></li>
				</ul>
			</div>
			
			<div class="span7">
				<c:choose>
					<c:when test="${fn:length(allStudents) > 0}">  
						<table id="student" class="table table-bordered table-striped tablesorter">
							<thead>
								<tr>
									<th>Nom</th>
								</tr>
							</thead>
							<c:choose>
								<c:when test="${state == 'after'}">
									<tbody>
										<c:forEach var="student" items="${allStudents}">
											<c:choose>
												<c:when test="${student.validated}">
													<tr class="success">
														<td><a href="/responsable/student/${student.login}">${student.name}</a></td>
													</tr>
												</c:when>
												<c:otherwise>
													<tr class="error">
														<td><a href="/responsable/student/${student.login}">${student.name}</a></td>
													</tr>
												</c:otherwise>
											</c:choose>
										</c:forEach>
									</tbody>
								</c:when>
								<c:otherwise>
									<tbody>
										<c:forEach var="student" items="${allStudents}">
											<tr>
												<td><a href="/responsable/student/${student.login}">${student.name}</a></td>
											</tr>
										</c:forEach>
									</tbody>
								</c:otherwise>
							</c:choose>
						</table>
					</c:when>
					<c:otherwise>
						<p>
							<c:choose>
								<c:when test="${specialization.type == 'ic'}">
									Aucun elève n'a pour l'instant choisi ce parcours d'approfondissement en 
								</c:when>
								<c:otherwise>
									Aucun elève n'a pour l'instant choisi cette filière métier en 
								</c:otherwise>
							</c:choose>
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
</body>
</html>