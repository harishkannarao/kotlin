<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <link rel="stylesheet" href="/static/css/vue${cssVariant}.css"/>
    <link rel="stylesheet" href="/webjars/bootstrap/css/bootstrap${cssVariant}.css"/>
    <title>Vue CRUD</title>
</head>

<body>
<div id="vue-crud-app" class="container-fluid">
    <div class="row">
        <input type="hidden" id="default-int-field" value="${defaultIntField}"/>
    </div>
    <div v-if="loading" class="row">
        <span class="qa-loading-message">Loading...</span>
    </div>
    <div v-cloak>
        <div class="row px-3 py-3">
            <p>Total entities: <span class="qa-total-entities">{{ totalEntities }}</span></p>
        </div>
        <div class="row px-3 py-3">
            <p>
                Total Server Entities: <span class="qa-total-server-entities">{{ totalServerEntities }}</span>
                <input type="checkbox" v-model="autoRefreshServerCount" class="qa-auto-refresh-server-count"/>Auto
                Refresh
            </p>
        </div>
        <div class="row px-3 py-3">
            <div class="col-md-auto">
                <input type="image" src="/webjars/feather-icons/dist/icons/refresh-cw.svg" alt="Refresh"
                       @click="refreshEntities($event)" class="qa-refresh-entities"/>
            </div>
            <div class="col-md-auto">
                <button :class="[{'hidden' : showForm}, 'qa-add-new-btn']" @click="displayForm()">Add New</button>
            </div>
            <div class="col-md-auto">
                <span v-if="loadingEntities" class="qa-refreshing-entities-message">Refreshing Entities...</span>
            </div>
        </div>
        <div class="row px-3 py-3">
            <form @submit="submitForm($event)" :class="[{'hidden' : !showForm}, 'qa-new-entity-form', 'px-3','py-3']">
                <div class="row">
                    <div class="col-4">
                        Username
                    </div>
                    <div class="col-8">
                        <input type="text" v-model="newEntity.username" class="qa-username-field"/>
                    </div>
                </div>
                <div class="row">
                    <div class="col-4">
                        Date
                    </div>
                    <div class="col-8">
                        <input type="text" v-model="newEntity.date" class="qa-date-field"/>
                    </div>
                </div>
                <div class="row">
                    <div class="col-4">
                        Epoch timestamp
                    </div>
                    <div class="col-8">
                        <div class="row">
                            <div class="col-md-auto">
                                <input type="text" v-model="newEntity.timeStampInEpochMillis"
                                       class="qa-epoch-timestamp-field"/>
                            </div>
                            <div class="col-md-auto">
                                <span class="qa-epoch-timestamp-iso">{{ timeStampIso }}</span>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-4">
                        Integer
                    </div>
                    <div class="col-8">
                        <input type="number" v-model="newEntity.intField" class="qa-int-field"/>
                    </div>
                </div>
                <div class="row">
                    <div class="col-4">
                        Decimal
                    </div>
                    <div class="col-8">
                        <input type="text" v-model="newEntity.decimalField" class="qa-decimal-field"/>
                    </div>
                </div>
                <div class="row">
                    <div class="col-4">
                        Boolean
                    </div>
                    <div class="col-8">
                        <input type="checkbox" v-model="newEntity.booleanField" class="qa-boolean-field"/><span
                            class="qa-boolean-field-display">{{ newEntity.booleanField }}</span>
                    </div>
                </div>
                <div class="row">
                    <div class="col-4">
                        Tags (comma separated)
                    </div>
                    <div class="col-8">
                        <textarea v-model="newEntity.tags" class="qa-tags-field"></textarea>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-auto">
                        <button type="submit" class="qa-save-btn">Save</button>
                    </div>
                    <div class="col-md-auto">
                        <button @click="hideForm($event)" class="qa-done-btn">Done</button>
                    </div>
                </div>
            </form>
            <table class="px-3 py-3 table table-bordered table-hover">
                <thead class="thead-dark">
                <tr>
                    <th scope="col">Number</th>
                    <th scope="col">UUID</th>
                    <th scope="col">Username</th>
                    <th scope="col">Date</th>
                    <th scope="col">Timestamp</th>
                    <th scope="col">Integer</th>
                    <th scope="col">Boolean</th>
                    <th scope="col">Decimal</th>
                    <th scope="col">Tags</th>
                    <th scope="col">Action</th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="(entity, index) in entities">
                    <th scope="row" class="qa-number">{{ index + 1 }}</th>
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
                </tbody>
            </table>
        </div>
    </div>
</div>

<#include "/common/vueScripts.ftl" />
<script src="/static/js/vue/axios${javaScriptVariant}.js"></script>
<script src="/static/js/vue/entity_client${javaScriptVariant}.js"></script>
<script src="/static/js/vue/vue_crud${javaScriptVariant}.js"></script>
</body>

</html>