<html>

<head>
    <#include "/common/cssStyles.ftl" />
    <link rel="stylesheet" href="/static/css/vue${cssVariant}.css" />
    <title>Vue Examples</title>
</head>

<body>
    <div id="app">
        <span v-if="loading">Loading...</span>
        <span v-cloak class="qa-vue-message">{{ message }}</span>
    </div>

    <#include "/common/vueScripts.ftl" />
    <script src="/static/js/vue/vue_examples${javaScriptVariant}.js"></script>
</body>

</html>