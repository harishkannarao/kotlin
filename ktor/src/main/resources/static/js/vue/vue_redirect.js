new Vue({
    el: '#vue-redirect-app',
    data() {
        return {

        }
    },
    methods: {
        callApi: function() {
            axiosInstance.get('/generate-401');
        }
    }
});