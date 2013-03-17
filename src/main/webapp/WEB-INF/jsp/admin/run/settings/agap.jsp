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
<link href="${pageContext.request.contextPath}/css/bootstrap-responsive.css" rel="stylesheet">
<script src="${pageContext.request.contextPath}/js/jquery/jquery-1.8.3.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery/jquery-ui-1.9.2.custom.min.js"></script>
<script>
	function updateAgap(){
	$('#info').html("<div class='alert alert-block'>Traitement en cours ...</div>");
	document.getElementById("button").className += " disabled";
	var path = document.getElementById("path").innerHTML;
$.ajax({
		url: path + "/admin/common/update-agap",
		success: function(){
			$('#info').html("<div class='alert alert-success'>La base a bien été mise à jour depuis agap.</div>");
			document.getElementById("button").className = "btn btn-primary";
		}
	});
	}
</script>
</head>
<body>
	<div class="container">
		<tags:headerAdmin run="<%=true%>" />
		<div id="path" style="display:none">${pageContext.request.contextPath}</div>

		<div class="row">
			<div class="span2">
				<ul class="nav nav-list">
					<li><a href="${pageContext.request.contextPath}/admin/run/settings/admins">Administrateurs</a></li>
					<li class="active"><a href="${pageContext.request.contextPath}/admin/run/settings/agap">Agap</a></li>
					<li><a href="${pageContext.request.contextPath}/admin/run/settings/students">Elèves</a></li>
					<li><a href="${pageContext.request.contextPath}/admin/run/settings/export">Export</a></li>
					<li><a href="${pageContext.request.contextPath}/admin/run/settings/mail1">Mail 1 <c:choose><c:when test="${mail1Activated}">[actif]</c:when><c:otherwise>[non actif]</c:otherwise></c:choose></li></a>
					<li><a href="${pageContext.request.contextPath}/admin/run/settings/mail2">Mail 2 <c:choose><c:when test="${mail2Activated}">[actif]</c:when><c:otherwise>[non actif]</c:otherwise></c:choose></li></a></li>
					<li><a href="${pageContext.request.contextPath}/admin/run/settings/spontaneous-mail">Mail Spontané</a></li>
					<li><a href="${pageContext.request.contextPath}/admin/run/settings/process">Processus</a></li>
					<li><a href="${pageContext.request.contextPath}/admin/run/settings/specializations">Spécialisations</a></li>
				</ul>
			</div>

			<div class="span10">
				<c:if test="${not empty infoMessage}">
					<div class="alert alert-info">
						${infoMessage}
					</div>
				</c:if>
				<div id="info">
				<div class="alert alert-info">
						Les noms et logins des élèves concernés sont automatiquement extraits d'agap et recopiés dans une autre base de données, plus facile d'accès, une fois par
						jour à heure fixe. Vous pouvez cependant mettre à jour manuellement ces données depuis agap en cliquant sur le bouton ci dessous.
					</div>
				</div>
				<a onclick="updateAgap()" id="button" class="btn btn-primary"><i class="icon-white icon-bell"></i> Mise à jour agap</a>
			</div>
		</div>
	</div>

</body>
<script src="${pageContext.request.contextPath}/js/students-exclusion.js" type="text/javascript"></script>
</html>
