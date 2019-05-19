var app = new Vue({
    el: '#app',
    data: {
        'loading': true,
        'message': 'Hello Vue!'
    },
    mounted: function () {
        this.loading = false
    }
});