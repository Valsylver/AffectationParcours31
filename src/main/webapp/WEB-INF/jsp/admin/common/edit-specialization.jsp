<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html>
<head>
    <title>Affectation parcours/filière 3ème année Centrale Marseille</title>
    <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/bootstrap-responsive.min.css" rel="stylesheet">
</head>
<body>
	<div class="container">
		<c:choose>
			<c:when test="${state == 'run'}">
				<tags:headerAdmin run="<%=true%>" />
			</c:when>
			<c:otherwise>
				<tags:headerAdmin run="<%=false%>" />
			</c:otherwise>
		</c:choose>
		
		<div class="row">
			<c:choose>
				<c:when test="${specialization.type == specialization.JOB_SECTOR}">
					<c:set var="action" value="${pageContext.request.contextPath}/admin/common/process-edition/js" />
				</c:when>
				<c:otherwise>
					<c:set var="action" value="${pageContext.request.contextPath}/admin/common/process-edition/ic" />
				</c:otherwise>
			</c:choose>
			<div class="span2">
			</div>
			<div class="span5">
				<form:form action="${action}" method="post" commandName="specialization">
					<label for="name">
					Nom
					<form:errors path="name" cssStyle="color: red">
						<br /> <font color="red">Le nom ne peut pas être vide.</font>
					</form:errors>
					</label>
					<form:input id="name" path="name" class="span5" />
					
					<c:if test="${specialization.type == specialization.IMPROVEMENT_COURSE}">
						<label for="superIc">
							Macro parcours
							<form:errors path="superIc" >
								<br /> <font color="red">La macro abréviation ne peut pas être vide.</font>
							</form:errors>
							<form:input id="superIc" path="superIc" class="span5" />
						</label>
					</c:if>
					
					<label for="abbreviation">
					Abréviation
					<form:errors path="abbreviation" >
						<br /> <font color="red">L'abréviation ne peut pas être vide.</font>
					</form:errors>
					</label>
					
					<c:choose>
						<c:when test="${state == 'run'}">
							<form:input id="abbreviation" path="abbreviation" class="span2" readonly="true"></form:input>
						</c:when>
						<c:otherwise>
							<form:input id="abbreviation" path="abbreviation" class="span2" />
						</c:otherwise>
					</c:choose>
					
					<label for="responsibleLogin">
					Login du responsable
					</label>
					<form:input id="responsibleLogin" path="responsibleLogin" class="span2" />
					
					<br />
					<br />
					<div class="btn-group pull-right">
						<c:if test="${(alreadyExists) && (not (state == 'run'))}">
							<c:choose>
								<c:when test="${specialization.type == specialization.JOB_SECTOR}">
									<a name="delete" class="btn btn-danger" href="${pageContext.request.contextPath}/admin/config/delete/job-sector/${abbreviation}">
										Supprimer
									</a>
								</c:when>
								<c:otherwise>
									<a name="delete" class="btn btn-danger" href="${pageContext.request.contextPath}/admin/config/delete/improvement-course/${abbreviation}">
										Supprimer
									</a>
								</c:otherwise>
							</c:choose>
						</c:if>
						<button name="commit" type="submit" class="btn btn-primary">
							${alreadyExists ? 'Mettre à jour' : 'Sauvegarder'}
						</button>
					</div>
				</form:form>
			</div>
			<div class="span2">
			</div>
		</div>
	</div>
</body>
</html>