<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html>
<head>
<title>Affectation parcours/filière 3ème année Centrale
	Marseille</title>

<link href="${pageContext.request.contextPath}/css/bootstrap.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/css/bootstrap-responsive.css" rel="stylesheet">

</head>
<body>
	<div class="container">
		<tags:headerAdmin run="<%=false%>" />

		<div class="row">
			<div class="span2"></div>

			<div class="span7">
				<span id="letterJs2"> <form:form
						action="${pageContext.request.contextPath}/admin/config/process-students-exclusion" method="POST"
						class="well form-horizontal" enctype="multipart/form-data">
						<c:if test="${not empty alertMessage}">
							<div class="alert alert-block">${alertMessage}</div>
						</c:if>
						<c:if test="${not empty successMessage}">
							<div class="alert alert-success">${successMessage}</div>
						</c:if>
						<label for="exclusion">
							<h4>Fichier à partir duquel ajouter les élèves à exclure du
								processus</h4>
						</label>
						<input id="exclusion" name="exclusion" type="file"
							style="display: none">
						<div class="input-append">
							<input id="exclusionInput" type="text" style="min-width: 300px">
							<a class="btn" onclick="$('input[id=exclusion]').click();">Parcourir...</a>
						</div>
						<br />
						<button class="btn btn-primary pull-right" name="commit"
							type="submit">
							<i class="icon-white icon-ok"></i> Valider
						</button>
						<br />
					</form:form>
					
					<c:choose>
						<c:when test="${fn:length(studentsToExclude) > 0 }">
							<h5>Elèves exclus du processus</h5>
							<ul class="unstyled">
							<c:forEach var="student" items="${studentsToExclude}">
								<li>${student}</li>
							</c:forEach>
							</ul>
						</c:when>
						<c:otherwise>
							Aucun élève n'est pour l'instant exclu du processus.
						</c:otherwise>
					</c:choose>
				</span>
			</div>

		</div>

	</div>
</body>
<script
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"
	type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/bootstrap-typeahead.min.js" type="text/javascript"></script>
<script>
	$(document).ready(function() {
		$('input[id=exclusion]').change(function() {
			$('#exclusionInput').val($(this).val());
		});
	});
</script>
</html>
