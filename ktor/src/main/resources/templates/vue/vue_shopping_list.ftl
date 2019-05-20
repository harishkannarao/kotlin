<!DOCTYPE html>
<html lang="en">

<head>
    <#include "/common/cssStyles.ftl" />
    <link rel="stylesheet" href="/static/css/vue${cssVariant}.css" />
    <link rel="stylesheet" href="/static/css/vue_shopping_list${cssVariant}.css" />
    <meta charset="UTF-8">
    <title>Shopping List App</title>
</head>

<body>
    <div id="shopping-list" v-cloak>
        <div class="header">
            <h1>{{ header.toLocaleUpperCase() }}</h1>
            <button v-if="state === 'default'" class="btn btn-primary" @click="changeState('edit')">Add Item</button>
            <button v-else class="btn btn-cancel" @click="changeState('default')">Cancel Adding Item</button>
        </div>
        <div v-if="state === 'edit'" class="add-item-form">
            <input v-model="newItem" type="text" placeholder="Add an item" @keyup.enter="saveItem">
            <button class="btn btn-primary" :disabled="newItem.length === 0" @click="saveItem">Save Item</button>
        </div>
        <ul>
            <li v-for="item in reversedItems" :class="{strikeout: item.purchased}" @click="togglePurchased(item)">{{ item.label }}</li>
        </ul>
        <p v-if="items.length === 0">Nice job! You've bought all your items.</p>
    </div>
    <#include "/common/vueScripts.ftl" />
    <script src="/static/js/vue/vue_shopping_list${javaScriptVariant}.js"></script>
</body>

</html>