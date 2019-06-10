<html>

<head>
    <link rel="stylesheet" href="/static/css/vue${cssVariant}.css" />
    <title>Vue CRUD</title>
</head>

<body>
    <div id="vue-crud-app">
        <input type="hidden" id="default-int-field" value="${defaultIntField}" />
        <span v-if="loading" class="qa-loading-message">Loading...</span>
        <div v-cloak>
            <p>Total entities: <span class="qa-total-entities">{{ totalEntities }}</span></p>
            <p>
                Total Server Entities: <span class="qa-total-server-entities">{{ totalServerEntities }}</span>
                <input type="checkbox" v-model="autoRefreshServerCount" class="qa-auto-refresh-server-count" />Auto Refresh
            </p>
            <button :class="[{'hidden' : showForm}, 'qa-add-new-btn']" @click="displayForm()">Add New</button>
            <form @submit="submitForm($event)" :class="[{'hidden' : !showForm}, 'qa-new-entity-form']">
                Username: <input type="text" v-model="newEntity.username" class="qa-username-field"/>
                <br />
                Date: <input type="text" v-model="newEntity.date" class="qa-date-field"/>
                <br />
                Epoch timestamp: <input type="text" v-model="newEntity.timeStampInEpochMillis" class="qa-epoch-timestamp-field"/><span class="qa-epoch-timestamp-iso">{{ timeStampIso }}</span>
                <br />
                Integer: <input type="number" v-model="newEntity.intField" class="qa-int-field"/>
                <br />
                Decimal: <input type="text" v-model="newEntity.decimalField" class="qa-decimal-field"/>
                <br />
                Boolean: <input type="checkbox" v-model="newEntity.booleanField" class="qa-boolean-field" /><span class="qa-boolean-field-display">{{ newEntity.booleanField }}</span>
                <br />
                Tags (comma separated): <textarea v-model="newEntity.tags" class="qa-tags-field"></textarea>
                <br />
                <button type="submit" class="qa-save-btn">Save</button>
                <button @click="hideForm($event)" class="qa-done-btn">Done</button>
            </form>
            <br />
            <br />
            <input type="image" src="/webjars/feather-icons/dist/icons/refresh-cw.svg" alt="Refresh" @click="refreshEntities($event)" class="qa-refresh-entities"/>
            <table style="width:100%">
                <tr>
                    <th>Number</th>
                    <th>UUID</th>
                    <th>Username</th>
                    <th>Date</th>
                    <th>Timestamp</th>
                    <th>Integer</th>
                    <th>Boolean</th>
                    <th>Decimal</th>
                    <th>Tags</th>
                    <th>Action</th>
                </tr>
                <tr v-for="(entity, index) in entities">
                    <td class="qa-number">{{ index + 1 }}</td>
                    <td class="qa-id">{{ entity.id }}</td>
                    <td class="qa-username">{{ entity.data.username }}</td>
                    <td class="qa-date">{{ entity.data.date }}</td>
                    <td class="qa-timestamp">{{ convertToIsoUtcTimeStamp(entity.data.timeStampInEpochMillis) }}</td>
                    <td class="qa-display-int-field">{{ entity.data.intField }}</td>
                    <td class="qa-display-boolean-field">{{ entity.data.booleanField }}</td>
                    <td class="qa-display-decimal-field">{{ entity.data.decimalField }}</td>
                    <td class="qa-tags">
                        <ul>
                            <li class="qa-tag" v-for="tag in entity.data.tags">
                                {{ tag }}
                            </li>
                        </ul>
                    </td>
                    <td>
                        <button class="qa-delete-entity-btn" @click="deleteEntity(entity.id, $event)">Delete</button>
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