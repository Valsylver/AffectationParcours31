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
<script src="${pageContext.request.contextPath}/js/admin/common/students-exclusion.js" type="text/javascript"></script>
<script>
	function updateAgap(){
	$('#infoAgap').html("<div class='alert alert-block'>Traitement en cours ...</div>");
	document.getElementById("button").className += " disabled";
	var path = document.getElementById("path").innerHTML;
$.ajax({
		url: path + "/admin/common/update-agap",
		success: function(){
			$('#infoAgap').html("<div class='alert alert-success'>La base a bien été mise à jour depuis agap.</div>");
			document.getElementById("button").className = "btn btn-primary";
		}
	});
	}
</script>
</head>
<body>
	<div class="container">
		<tags:headerAdmin run="${run}" />
		<div id="path" style="display:none">${pageContext.request.contextPath}</div>

		<div class="row">
			<div class="span2">
				<c:if test="${run}">
					<ul class="nav nav-list">
						<li><a href="${pageContext.request.contextPath}/admin/run/settings/admins">Administrateurs</a></li>
						<li><a href="${pageContext.request.contextPath}/admin/run/settings/agap">Agap</a></li>
						<li class="active"><a href="${pageContext.request.contextPath}/admin/run/settings/students">Elèves</a></li>
						<li><a href="${pageContext.request.contextPath}/admin/run/settings/export">Export</a></li>
						<li><a href="${pageContext.request.contextPath}/admin/run/settings/mail1">Mail 1 <c:choose><c:when test="${mail1Activated}">[actif]</c:when><c:otherwise>[non actif]</c:otherwise></c:choose></li></a>
						<li><a href="${pageContext.request.contextPath}/admin/run/settings/mail2">Mail 2 <c:choose><c:when test="${mail2Activated}">[actif]</c:when><c:otherwise>[non actif]</c:otherwise></c:choose></li></a></li>
						<li><a href="${pageContext.request.contextPath}/admin/run/settings/spontaneous-mail">Mail Spontané</a></li>
						<li><a href="${pageContext.request.contextPath}/admin/run/settings/process">Processus</a></li>
						<li><a href="${pageContext.request.contextPath}/admin/run/settings/specializations">Spécialisations</a></li>
					</ul>
				</c:if>
			</div>

			<div class="span10">
				<c:if test="${not run}">
					<div id="infoAgap"></div>
					<div class="alert alert-info">
						Les noms et logins des élèves concernés sont automatiquement extraits d'agap et recopiés dans une autre base de données, plus facile d'accès, une fois par
						jour à heure fixe. Vous pouvez cependant mettre à jour manuellement ces données depuis agap en cliquant sur le bouton ci dessous.
					</div>
					<a onclick="updateAgap()" id="button" class="btn btn-primary"><i class="icon-white icon-bell"></i> Mise à jour agap</a>
					<br />
					<br />
				</c:if>
				<div id="info" class="alert alert-info">Cliquez sur les icônes pour exclure/ne plus exclure les élèves.</div>
				<div class="row">
					<div class="span3">
						<h4>Promo ${promo}</h4>
						<ul id="promo" class="unstyled">
							<c:forEach var="student" items="${studentsConcerned}" varStatus="status">
								<li><a id="promo${status.index}" title="${student.login}" href="javascript:void(0);" onclick="removePromoStudent(this.id);"><img src="${pageContext.request.contextPath}/img/minus-new.png"></img></a>
									${student.name}</li>
							</c:forEach>
						</ul>
					</div>
					<div class="span3">
						<h4>Elèves à exclure</h4>
						<ul id="exclusion" class="unstyled">
							<c:forEach var="student" items="${studentsToExclude}" varStatus="status">
								<c:choose>
									<c:when test="${student.origin == 'promo'}">
										<li><a id="exclusion${status.index}" title="${student.login}" href="javascript:void(0);" onclick="addPromoStudent(this.id);"><img src="${pageContext.request.contextPath}/img/plus-new.png"></img></a>
											${student.name}</li>
									</c:when>
									<c:otherwise>
										<li><a id="exclusion${status.index}" title="${student.login}" href="javascript:void(0);" onclick="addCesureStudent(this.id);"><img src="${pageContext.request.contextPath}/img/plus-new.png"></img></a>
											${student.name}</li>
									</c:otherwise>
								</c:choose>
							</c:forEach>
						</ul>
					</div>
					<div class="span3">
						<h4>Elèves en césure</h4>
						<ul id="cesure" class="unstyled">
							<c:forEach var="student" items="${studentsCesure}" varStatus="status">
								<li><a id="cesure${status.index}" title="${student.login}" href="javascript:void(0);" onclick="removeCesureStudent(this.id);"><img src="${pageContext.request.contextPath}/img/minus-new.png"></img></a>
									${student.name}</li>
							</c:forEach>
						</ul>
					</div>
				</div>
			</div>
		</div>
	</div>

</body>
</html>
