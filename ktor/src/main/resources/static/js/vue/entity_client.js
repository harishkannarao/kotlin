var entityClient = new Object({
  getEntities: function() {
    return axiosInstance.get('/jdbi/json-entity');
  },
  getTotalEntities: function() {
    return axiosInstance.get('/jdbi/json-entity/count');
  },
  createEntity: function(entity) {
    return axiosInstance.post('/jdbi/json-entity', entity);
  },
  deleteEntity: function(id) {
    return axiosInstance.delete('/jdbi/json-entity/' + id);
  }
});