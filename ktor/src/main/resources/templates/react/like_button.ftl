<html>

<head>
    <#include "/common/cssStyles.ftl" />
    <title>React - Like button</title>
</head>

<body>
    <div id="like_button_container"></div>

    <#include "/common/reactScripts.ftl" />

    <!-- Load our React component. -->
    <script src="/static/js/react/like_button${javaScriptVariant}.js"></script>

</body>

</html>