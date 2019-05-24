new Vue({
    el: '#vue-crud-app',
    data() {
        return {
            loading: true,
            errored: false,
            entities: []
        }
    },
    methods: {
        getEntities: function() {
            axiosInstance.get('/jdbi/json-entity')
                .then(response => {
                    console.log(response);
                    this.entities = response.data;
                })
                .catch(error => {
                    console.log(error);
                    this.errored = true;
                })
                .finally(() => {
                    this.loading = false;
                });
        }
    },
    computed: {
        totalEntities: function() {
            return this.entities.length
        }
    },
    mounted() {
        this.getEntities();
    }
});