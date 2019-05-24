<html>

<head>
    <link rel="stylesheet" href="/static/css/vue${cssVariant}.css" />
    <title>Vue CRUD</title>
</head>

<body>
    <div id="vue-crud-app">
        <span v-if="loading">Loading...</span>
        <div v-cloak>
            <h1>Total entities: <span class="qa-total-entities">{{ totalEntities }}</span></h1>
            <table style="width:100%">
                <tr>
                    <th>Number</th>
                    <th>UUID</th>
                    <th>Username</th>
                    <th>Tags</th>
                </tr>
                <tr v-for="(entity, index) in entities">
                    <td class="qa-number">{{ index + 1 }}</td>
                    <td class="qa-id">{{ entity.id }}</td>
                    <td class="qa-username">{{ entity.data.username }}</td>
                    <td class="qa-tags">
                        <ul>
                            <li class="qa-tag" v-for="tag in entity.data.tags">
                                {{ tag }}
                            </li>
                        </ul>
                    </td>
                </tr>
            </table>
        </div>
    </div>

    <#include "/common/vueScripts.ftl" />
    <script src="/static/js/vue/axios${javaScriptVariant}.js"></script>
    <script src="/static/js/vue/vue_crud${javaScriptVariant}.js"></script>
</body>

</html>