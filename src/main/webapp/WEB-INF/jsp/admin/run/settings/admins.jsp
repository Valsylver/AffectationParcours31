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
					<li><a href="${pageContext.request.contextPath}/admin/run/settings/mail1">Mail 1 <c:choose><c:when test="${mail1Activated}">[actif]</c:when><c:otherwise>[non actif]</c:otherwise></c:choose></a></li>
					<li><a href="${pageContext.request.contextPath}/admin/run/settings/mail2">Mail 2 <c:choose><c:when test="${mail2Activated}">[actif]</c:when><c:otherwise>[non actif]</c:otherwise></c:choose></a></li>
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
					<div class="alert alert-info">${successMessage}</div>
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
</html>
