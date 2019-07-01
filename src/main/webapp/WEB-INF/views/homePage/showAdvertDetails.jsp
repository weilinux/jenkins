<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../inc/inc.jsp" %>
<meta name="viewport"content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
<style>
	@charset "utf-8";
/* reset */
body,p,h1,h2,h3,h4,h5,h6,ul,ol,li,dl,dt,dd,table,th,td,form,fieldset,legend,input,textarea,button,select{margin:0;padding:0;}
body,input,textarea,select,button,
body{min-width:320px;font-size:1em;font-family:'microsoft yahei',Verdana,Arial,Helvetica,sans-serif;color:#000;-webkit-text-size-adjust:none;}
img,fieldset{border:0;}
ul,ol{list-style:none;}
em,address{font-style:normal;}
a{color:#000;text-decoration:none;}
table{width:100% !important;border-collapse:collapse;}
html{
	font-size: 100px;
}
#common{
	position: relative;
    font-size: 0.32rem;
    line-height: 1.5;
    margin: 0 0.6rem;
	color:#333;
}

#common .title{
	font-size: 0.5rem;
	font-weight: bold;
    line-height: 1.6;
    padding: 0.3rem 0 0.1rem;
}
#common .time{
	font-size: 0.3rem;
    line-height: 0.4rem;
    text-align: left;
    color: #888;
    margin-bottom: 0.2rem;
}

#common img{
	max-width: 100%;
	height: auto;
	display: inline-block;	
}

p{
	margin-bottom: 0.16rem;
}
p:nth-child(1){
	margin-top: 0.16rem;	
}
img:nth-child(1){
	margin-top: 0.16rem;
}
</style>
<script>   
		(function (doc, win) {
		        var docEl = doc.documentElement,
		            resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize',
		            recalc = function () {
		                var clientWidth = docEl.clientWidth;
		                if (!clientWidth) return;
		                if(clientWidth>=750){
		                    docEl.style.fontSize = '100px';
		                }else{
		                    docEl.style.fontSize = 100 * (clientWidth / 750) + 'px';
		                }
		            };
		        
		        if (!doc.addEventListener) return;
		        win.addEventListener(resizeEvt, recalc, false);
		        doc.addEventListener('DOMContentLoaded', recalc, false);
		    })(document, window);
</script> 

<div id="common">
	
	<div class="title">
		<c:if test="${advertDetail != null }">${advertDetail.name}</c:if>
	</div>
	
	<div class="time">
		<c:if test="${advertDetail != null }">
			<fmt:formatDate value="${advertDetail.createTime}" pattern="yyyy-MM-dd"/>
		</c:if>
	</div>
	
	<div class="content">
		<c:if test="${advertDetail != null }">${advertDetail.imgPath}</c:if>
	</div>
	<c:if test="${errorMsg !=null && errorMsg !='' }">${errorMsg}</c:if>

</div>


<div style="display: none;">${hideMsg}</div>