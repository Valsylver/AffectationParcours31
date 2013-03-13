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

</head>
<body>
	<div class="container">
		<tags:headerAdmin run="<%=true%>" />

		<div class="row">
			<div class="span2">
				<ul class="nav nav-list">
					<li class="active"><a href="${pageContext.request.contextPath}/admin/run/settings/admins">Administrateurs</a></li>
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
				<c:if test='${not empty alertMessage}'>
					<div class="alert alert-block">${alertMessage}</div>
				</c:if>
				<c:if test='${not empty errorMessage}'>
					<div class="alert alert-error">${errorMessage}</div>
				</c:if>
				<c:if test='${not empty successMessage}'>
					<div class="alert alert-success">${successMessage}</div>
				</c:if>
				<h4>Login des administrateurs actuels</h4>
				<c:forEach var="admin" items="${currentAdmins}">
							${admin}
						<a class="btn btn-danger" href="${pageContext.request.contextPath}/admin/run/settings/admins/delete/${admin}"><i class="icon-white icon-remove"></i> Supprimer</a>
						<br />
						<br />
				</c:forEach>
				<h4>Nouvel administrateur</h4>

				<form:form action="${pageContext.request.contextPath}/admin/run/settings/admins/new" method="post" commandName="newAdmin">
					<label for="login">Login</label>
					<form:input path="login" />
					<br />
					<button class="btn btn-success" name="commit" type="submit">
						<i class="icon-white icon-plus"></i> Ajouter
					</button>
				</form:form>
			</div>
		</div>
	</div>

</body>
<script src="${pageContext.request.contextPath}/js/students-exclusion.js" type="text/javascript"></script>
</html>
