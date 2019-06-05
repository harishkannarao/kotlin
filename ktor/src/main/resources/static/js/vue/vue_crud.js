new Vue({
    el: '#vue-crud-app',
    data() {
        return {
            loading: true,
            errored: false,
            showForm: false,
            newEntity: {
                username: '',
                date: '',
                timeStampInEpochMillis: new Date().toISOString(),
                intField: document.getElementById('default-int-field').value,
                booleanField: true,
                decimalField: '0.00',
                tags: ''
            },
            defaultEntity: {},
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
        },
        convertToIsoUtcTimeStamp: function(epochMillis) {
            return toIsoUtcTimeStamp(epochMillis);
        },
        displayForm: function() {
            this.showForm = true;
        },
        hideForm: function(event) {
            if (event) {
                event.preventDefault();
            }
            this.showForm = false;
        },
        submitForm: function(event) {
            if (event) {
                event.preventDefault();
            }
            var requestEntity = {};
            requestEntity.username = this.newEntity.username;
            requestEntity.date = this.newEntity.date;
            requestEntity.timeStampInEpochMillis = Date.parse(this.newEntity.timeStampInEpochMillis);
            requestEntity.intField = Number(this.newEntity.intField);
            requestEntity.booleanField = this.newEntity.booleanField;
            requestEntity.decimalField = Number(this.newEntity.decimalField);
            if (this.newEntity.tags.trim() === "") {
                requestEntity.tags = [];
            } else {
                requestEntity.tags = this.newEntity.tags.split(",");
            }
            requestEntity.nestedData = [];

            axiosInstance.post('/jdbi/json-entity', requestEntity)
                .then(response => {
                    console.log(response);
                    this.entities.push(response.data);
                })
                .catch(error => {
                    console.log(error);
                    this.errored = true;
                })
                .finally(() => {
                    this.newEntity = deepCopy(this.defaultEntity);
                });

            console.log('Submitted form');
        },
        deleteEntity: function(id, event) {
            if (event) {
                event.preventDefault();
            }
            axiosInstance.delete('/jdbi/json-entity/' + id)
                .then(response => {
                    console.log(response);
                    this.getEntities();
                })
                .catch(error => {
                    console.log(error);
                    this.errored = true;
                });
        }
    },
    computed: {
        totalEntities: function() {
            return this.entities.length;
        },
        timeStampIso: function() {
            return this.convertToIsoUtcTimeStamp(Date.parse(this.newEntity.timeStampInEpochMillis));
        }
    },
    mounted() {
        this.defaultEntity = deepCopy(this.newEntity);
        this.getEntities();
    }
});