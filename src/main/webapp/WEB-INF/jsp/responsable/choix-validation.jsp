<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html>
<head>
<title>Affectation parcours/filière 3ème année Centrale Marseille</title>
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/css/bootstrap-responsive.min.css" rel="stylesheet">
<script src="${pageContext.request.contextPath}/js/jquery-final/jquery-1.8.3.min.js"></script>
<script>
function inverseValidation(id){
	var loginName = id.split(";;;");
	var login = loginName[0];
	var name = loginName[1];
	var path = document.getElementById("path").innerHTML;
	$.ajax({
		url: path + "/responsable/inverse-validation",
		data: "login="+login,
		success: function(validated){
			if (validated == "true"){
				$('#info').html("<b>" + name + "</b> est désormais <b>accepté(e).</b>");				
			}
			else{
				$('#info').html("<b>" + name + "</b> n'est désormais <b>plus accepté(e).</b>");
			}
		}
	});
}
</script>
</head>
<body>
	<div class="container">
		<div id="path" style="display:none">${pageContext.request.contextPath}</div>

		<tags:headerResponsible title="${specialization.name} (${specialization.abbreviation})" />

		<div class="row">
			<div class="span2">
				<ul class="nav nav-list">

					<li class="nav-header">${specialization.abbreviation}</li>
					<c:forEach var="i" begin="1" end="5" step="1">
						<c:choose>
							<c:when test="${i == order}">
								<li class="active"><a href="${pageContext.request.contextPath}/responsable/${i}">Choix ${i}</a></li>
							</c:when>
							<c:otherwise>
								<li><a href="${pageContext.request.contextPath}/responsable/${i}">Choix ${i}</a></li>
							</c:otherwise>
						</c:choose>
					</c:forEach>

					<li class="nav-header">Statistiques</li>
					<li><a href="${pageContext.request.contextPath}/responsable/run/statistics/choice1">Parcours/filières</a></li>
					<c:choose>
						<c:when test="${specialization.type == 'JobSector' }">
							<li><a href="${pageContext.request.contextPath}/responsable/run/statistics/repartition-other-choice2">Répartition filières</a></li>
							<li><a href="${pageContext.request.contextPath}/responsable/run/statistics/inverse-repartition">Répartition parcours</a></li>
						</c:when>
						<c:otherwise>
							<li><a href="${pageContext.request.contextPath}/responsable/run/statistics/repartition-other-choice2">Répartition parcours</a></li>
							<li><a href="${pageContext.request.contextPath}/responsable/run/statistics/inverse-repartition">Répartition filières</a></li>
						</c:otherwise>
					</c:choose>
				</ul>
			</div>

			<div class="span7">
				<c:choose>
					<c:when test="${fn:length(allStudents) > 0}">
						<div id="info" class="alert alert-info">Cochez/décochez pour accepter/refuser les élèves. Par défaut, ils sont tous acceptés.</div>
						<table id="student" class="table table-bordered table-striped">
							<tbody>
								<c:forEach var="student" items="${allStudents}" varStatus="status">
									<tr>
										<td><a href="${pageContext.request.contextPath}/responsable/student/${student.login}">${student.name}</a></td>
										<c:choose>
											<c:when test="${student.validated}">
												<td><input id ="${student.login};;;${student.name}" type="checkbox" checked="checked" onchange="inverseValidation(this.id)"></td>
											</c:when>
											<c:otherwise>
												<td><input id ="${student.login};;;${student.name}" type="checkbox" onchange="inverseValidation(this.id)"></td>
											</c:otherwise>
										</c:choose>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</c:when>
					<c:otherwise>
						<p>
							<c:choose>
								<c:when test="${specialization.type == specialization.IMPROVEMENT_COURSE}">
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

			<div class="span3">
				<tags:rightColumnResponsible specialization="${specialization}"></tags:rightColumnResponsible>
			</div>
		</div>
	</div>
</body>
</html>