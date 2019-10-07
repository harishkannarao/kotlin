<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <link rel="stylesheet" href="/static/css/vue${cssVariant}.css"/>
    <link rel="stylesheet" href="/webjars/bootstrap/css/bootstrap${cssVariant}.css"/>
    <title>Vue Reference - Error Component</title>
</head>

<body>
<div id="vue-error-container-reference-app">
    <button :class="['qa-display-error-btn']" @click="displayError()">Display Error</button>
    <error-container v-if="error" v-bind:error="error" v-on:clear-error="clearError"></error-container>
</div>

<#include "/common/vueScripts.ftl" />
<script src="/static/js/vue/vue_error_component${javaScriptVariant}.js"></script>
<script>
    new Vue({
        el: '#vue-error-container-reference-app',
        data() {
            return {
                error: null
            }
        },
        methods: {
            displayError: function() {
                this.error = {
                    message: "My sample error message"
                }
            },
            clearError: function(error) {
                if (this.error.message === error.message) {
                    this.error = null;
                }
            }
        }
    })
</script>
</body>

</html>