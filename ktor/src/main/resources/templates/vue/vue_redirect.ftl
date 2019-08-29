<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <link rel="stylesheet" href="/static/css/vue${cssVariant}.css"/>
    <link rel="stylesheet" href="/webjars/bootstrap/css/bootstrap${cssVariant}.css"/>
    <title>Vue Axios Interceptor Redirect</title>
</head>

<body>
<div id="vue-redirect-app" class="container-fluid">
    <button :class="['qa-generate-401-btn']" @click="callApi()">Generate 401 from api</button>
</div>

<#include "/common/vueScripts.ftl" />
<script src="/static/js/vue/vue_redirect${javaScriptVariant}.js"></script>
</body>

</html>