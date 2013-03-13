<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ attribute name="run" required="true" type="java.lang.Boolean"%>

<nav class="navbar navbar-inverse">
	<div class="navbar-inner">
		<a href="${pageContext.request.contextPath}/admin/" class="brand">Affectation 3A </a>
		<div class="pull-right">
			<ul class="nav">
				<c:choose>
					<c:when test="${run}">
						<li><a href="${pageContext.request.contextPath}/admin/run/settings/admins">Administration</a></li>
					</c:when>
					<c:otherwise>
						<li><a href="${pageContext.request.contextPath}/admin/config/exclude">Elèves (liste)</a></li>
						<li><a href="${pageContext.request.contextPath}/admin/config/exclude-from-file">Elèves (fichier xls)</a></li>
					</c:otherwise>
				</c:choose>
				<li><a href="${pageContext.request.contextPath}/logout">Deconnexion</a></li>
			</ul>
		</div>
	</div>
</nav>

<%-- 
<div class="navbar navbar-inverse navbar-fixed-top">
      <div class="navbar-inner">
        <div class="container">
          <button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="brand" href="./index.html">Bootstrap</a>
          <div class="nav-collapse collapse">
            <ul class="nav">
              <li class="">
                <a href="./index.html">Home</a>
              </li>
              <li class="">
                <a href="./getting-started.html">Get started</a>
              </li>
              <li class="">
                <a href="./scaffolding.html">Scaffolding</a>
              </li>
              <li class="">
                <a href="./base-css.html">Base CSS</a>
              </li>
              <li class="active">
                <a href="./components.html">Components</a>
              </li>
              <li class="">
                <a href="./javascript.html">JavaScript</a>
              </li>
              <li class="">
                <a href="./customize.html">Customize</a>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>

--%>