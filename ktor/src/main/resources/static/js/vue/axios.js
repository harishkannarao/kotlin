var axiosInstance = axios.create({
    baseURL: config().baseURL,
    timeout: 1000
});

axiosInstance.interceptors.response.use(
    function (response) {
        return response;
    },
    function (error) {
        console.log(error)
        if (error.response.status === 401) {
            var currentUrl = window.location.href;
            window.location.href = "/vue/vue_login?redirectTo="+currentUrl;
        }
        return Promise.reject(error);
    }
)