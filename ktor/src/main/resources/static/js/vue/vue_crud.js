new Vue({
    el: '#vue-crud-app',
    data() {
        return {
            loading: true,
            loadingEntities: true,
            error: null,
            showForm: false,
            autoRefreshServerCount: true,
            timer: null,
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
            entities: [],
            entityCount: {
                count: 0
            }
        }
    },
    methods: {
        startRefreshTime: function() {
            this.timer = setInterval(this.getEntityCount, 500);
        },
        cancelRefreshTime: function() {
            clearInterval(this.timer);
        },
        refreshEntities: function(event) {
            if (event) {
                event.preventDefault();
            }
            this.getEntities();
        },
        getEntities: function() {
            this.loadingEntities = true;
            entityClient.getEntities()
                .then(response => {
                    this.entities = response.data;
                })
                .catch(error => {
                    this.error = error;
                })
                .finally(() => {
                    this.loading = false;
                    this.loadingEntities = false;
                });
        },
        getEntityCount: function() {
            entityClient.getTotalEntities()
                .then(response => {
                    this.entityCount = response.data;
                })
                .catch(error => {
                    this.error = error;
                })
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

            entityClient.createEntity(requestEntity)
                .then(response => {
                    this.entities.push(response.data);
                })
                .catch(error => {
                    this.error = error;
                })
                .finally(() => {
                    this.newEntity = deepCopy(this.defaultEntity);
                });
        },
        deleteEntity: function(id, event) {
            if (event) {
                event.preventDefault();
            }
            entityClient.deleteEntity(id)
                .then(response => {
                    this.getEntities();
                })
                .catch(error => {
                    this.error = error;
                });
        },
        clearError: function(error) {
            if (this.error.message === error.message) {
                this.error = null;
            }
        }
    },
    computed: {
        totalEntities: function() {
            return this.entities.length;
        },
        totalServerEntities: function() {
            return this.entityCount.count;
        },
        timeStampIso: function() {
            return this.convertToIsoUtcTimeStamp(Date.parse(this.newEntity.timeStampInEpochMillis));
        }
    },
    watch: {
        autoRefreshServerCount: function (newValue, oldValue) {
            if (newValue == true) {
                this.startRefreshTime();
            } else {
                this.cancelRefreshTime();
            }
        }
    },
    mounted() {
        this.defaultEntity = deepCopy(this.newEntity);
        this.getEntities();
        this.getEntityCount();
        this.startRefreshTime();
    },
    beforeDestroy() {
        this.cancelRefreshTime();
    }
});